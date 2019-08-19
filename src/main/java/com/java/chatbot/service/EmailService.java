package com.java.chatbot.service;

import com.java.chatbot.model.Movies;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EmailService {

	@Autowired
	public JavaMailSender emailSender;

	public void sendMail(Movies movies){
		try{
			/*SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(movies.getEmailId());
			message.setSubject("BMS-ASSIST: "+movies.getMovieName().toUpperCase()+" Bookings opened in "+ movies.getTheaterName().toUpperCase());
			message.setText("Automatic mail, please don't replay");
			emailSender.send(message);*/
			System.out.println("***************************************** From Email Service");
			Email from = new Email("admin@bmsassist.com");
			String subject = "BMS-ASSIST: "+movies.getMovieName().toUpperCase()+" Bookings opened in "+ movies.getTheaterName().toUpperCase();
			Email to = new Email(movies.getEmailId());
			Content content = new Content("text/plain", "Automatic mail, please don't replay");
			Mail mail = new Mail(from, subject, to, content);
			SendGrid sg = new SendGrid("***************************");
			Request request = new Request();
			try {
				request.setMethod(Method.POST);
				request.setEndpoint("mail/send");
				request.setBody(mail.build());
				Response response = sg.api(request);
				System.out.println(response.getStatusCode());
				System.out.println(response.getBody());
				System.out.println(response.getHeaders());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}catch (Exception e){
			System.out.println("Exception casued in Email Service :: "+e.getMessage());
					e.printStackTrace();
		}
	}
}


