package com.java.chatbot.service;

import com.java.chatbot.model.Chat;
import com.java.chatbot.model.Movies;
import com.java.chatbot.model.Relations;
import com.java.chatbot.model.Theaters;
import com.java.chatbot.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author ${Suresh M Kumar}
 * @date Jun 18, 2018
 */
@Service
public class ChatService {

	@Value("${chatbot.name}")
	private String chatBotName;

	@Value("${admin}")
	private String admin;

	@Value("${admin.key}")
	private String adminKey;

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private Repository repository;

	@Autowired
	private Theaters theaters;

	public synchronized String getchatId() {
		return String.valueOf(new Date().getTime());
	}

	public void joinUser(Chat chat) {
		ArrayList<Relations> relationsList = null;
		try {
			chat.setContent(message(Phase.START, chat.getSender()));
		} catch (NullPointerException e) {
			chat.setContent(message(null, null));
		}
		chat.setType(Chat.MessageType.CHAT);
		chat.setSender(chatBotName);
		template.convertAndSend("/topic/private/" + chat.getChatId(), chat);
	}

	public void sendMessage(Chat chat) {
		if (chat.getContent() == null || chat.getContent().trim().length() == 0) {
			sendReply(chat, message(Phase.NULL_ERROR, null));
		} else if (adminKey.equalsIgnoreCase(chat.getContent().trim()) && admin.equalsIgnoreCase(chat.getSender().trim())) {
			sendReply(chat, repository.getAll());
		} else {
			Movies movies = repository.get(chat.getChatId());
			if(movies.getMovieName() == null){
				setMovieName(chat, movies);
			}else if(movies.getDate() == null){
				setMovieDate(chat, movies);
			}else if(movies.getTheaterName() == null){
				setMovieTheater(chat, movies);
			}else if(movies.getMobile() == null){
				setMobile(chat, movies);
			}else if(movies.getEmailId() == null){
				setEmailId(chat, movies);
			}else{
				concludeChat(chat, movies);
			}
		}
	}

	private void setMobile(Chat chat, Movies movies) {
		try{
			if(chat.getContent().length() == 10){
				movies.setMobile(chat.getContent());
				String response = message(Phase.MOBILE, null);
				repository.set(chat.getChatId(), movies);
				sendReply(chat, response);
			}else{
				String response = "Please enter valid 10 digits mobile number!!";
				sendReply(chat, response);
			}
		}catch (Exception e){
			String response = "Please enter valid 10 digits mobile number!!";
			sendReply(chat, response);
		}
	}

	public static String getDate(String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		return sdf.format(new Date());
	}

	private boolean validateDate(Chat chat) throws ParseException {
		String[] fulDate = chat.getContent().split("/");
		int date = Integer.parseInt(fulDate[0]);
		int month = Integer.parseInt(fulDate[1]);
		int year = Integer.parseInt(fulDate[2]);
		if((date > 0 && date <= 31) && (month > 0 && month <= 12) && year >= 2019) {
			Date movieDate = new SimpleDateFormat("dd/MM/yyyy").parse(chat.getContent().trim());
			Date currentDate = new SimpleDateFormat("dd/MM/yyyy").parse(getDate("dd/M/yyyy"));
			if (movieDate.before(currentDate)) {
				return false;
			}
		}else{
			return false;
		}
		return true;
	}
	private String message(Phase phase, String input){
		String message=null;
		switch (phase){
			case MOVIE_NAME:
				message = "Ohhh "+input+ " movie!!! great selection. Please tell me the date you want to watch the movie in dd/mm/yyyy format.";
				break;
			case DATE:
				message = "Date locked. Please select the theater you want to watch.";
				break;
			case DATE_ERROR:
				message = "Invalid date selection!! <"+input+">. I'll only accept today or a future date. Please try again.";
				break;
			case THEATER:
				message = "Nice Theater!! I have saved your data. Please enter your 10 digits mobile number to notify.";
				break;
			case MOBILE:
				message = "Please enter your EMAIL to notify";
				break;
			case EMAIL_ID:
				message = "Please type 'YES' to confirm the details are correct.";
				break;
			case END:
				message = "Thanks "+input+" for giving me a opportunity to notify you. Have a nice day :)";
				break;
			case START:
				message = "Hi "+input+"!! Hope you are doing well. Please enter the movie name you want to watch without spelling mistakes.";
				break;
			case NULL_ERROR:
				message = "looking like you are not entering valid question, Please type your question my friend.";
				break;
			default:
				message = "Looks like Admin is not ready for any suggessions!! Our team will look into this. Thanks for using me.";
		}
		return message;
	}

	private void sendReply(Chat chat, String response){
		chat.setContent(response);
		chat.setSender(chatBotName);
		template.convertAndSend("/topic/private/" + chat.getChatId(), chat);
	}

	private String buildURL(Chat chat, Movies movies){
		SimpleDateFormat inSDF = new SimpleDateFormat("dd/mm/yyyy");
		SimpleDateFormat outSDF = new SimpleDateFormat("yyyymmdd");
		Date date = null;
		String outDate = null;
		try {
			date = inSDF.parse(movies.getDate());
			outDate = outSDF.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return theaters.getTheraters().get(chat.getContent())+outDate;
	}

	private void setMovieName(Chat chat, Movies movies){
		movies.setTransactionDate(new Date().toString());
		movies.setRequestedPerson(chat.getSender());
		movies.setMovieName(chat.getContent());
		String response = message(Phase.MOVIE_NAME, chat.getContent());
		repository.set(chat.getChatId(), movies);
		sendReply(chat, response);
	}

	private void setMovieDate(Chat chat, Movies movies){
		String response = null;
		try{
			if(validateDate(chat)){
				movies.setDate(chat.getContent());
				ArrayList<String> keywords = new ArrayList<>();
				theaters.getTheraters().forEach((k,v) -> keywords.add(k));
				chat.setKeywords(keywords);
				response = message(Phase.DATE, null);
			}else{
				response = message(Phase.DATE_ERROR, chat.getContent());
				movies.setError(response);
			}
		}catch (Exception e){
			response = message(Phase.DATE_ERROR, chat.getContent());
			movies.setError(e.getMessage());
		}
		repository.set(chat.getChatId(), movies);
		sendReply(chat, response);

	}

	private void setMovieTheater(Chat chat, Movies movies){
		movies.setTheaterName(chat.getContent());
		movies.setTheaterURL(buildURL(chat, movies));
		String response = message(Phase.THEATER, null);
		repository.set(chat.getChatId(), movies);
		sendReply(chat, response);
	}

	private void setEmailId(Chat chat, Movies movies){
		movies.setEmailId(chat.getContent());
		ArrayList<String> keywords = new ArrayList<>();
		keywords.add(Phase.MOVIE_NAME+" : "+movies.getMovieName());
		keywords.add(Phase.DATE+" : "+movies.getDate());
		keywords.add(Phase.THEATER+" : "+movies.getTheaterName());
		keywords.add(Phase.MOBILE+" : "+movies.getMobile());
		keywords.add(Phase.EMAIL_ID+" : "+movies.getEmailId());

		String response = message(Phase.EMAIL_ID, null);
		repository.set(chat.getChatId(), movies);

		chat.setEndCoversation(true);
		chat.setKeywords(keywords);
		sendReply(chat, response);
	}

	private void concludeChat(Chat chat, Movies movies){
		String response = null;
		if("YES".equalsIgnoreCase(chat.getContent().trim())) {
			movies.setEligibleForSearch(true);
			repository.addMovie(movies);
			response = message(Phase.END, chat.getSender());
		}else {
			response = message(null, null);
		}
		repository.set(chat.getChatId(), movies);
		sendReply(chat, response);
	}

	private enum Phase{
		START, MOVIE_NAME, DATE, DATE_ERROR, THEATER, EMAIL_ID, MOBILE, NULL_ERROR, END
	}
}
