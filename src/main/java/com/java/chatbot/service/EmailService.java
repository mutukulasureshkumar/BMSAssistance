package com.java.chatbot.service;

import com.java.chatbot.model.Movies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

	@Autowired
	public JavaMailSender emailSender;

	public void sendMail(Movies movies){
		try{
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(movies.getEmailId());
			message.setSubject("BMS-ASSIST: "+movies.getMovieName().toUpperCase()+" Bookings opened in "+ movies.getTheaterName().toUpperCase());
			message.setText("Automatic mail, please don't replay");
			emailSender.send(message);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
