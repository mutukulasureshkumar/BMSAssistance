package com.java.chatbot.service;

import com.java.chatbot.model.Movies;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class SMSService {

	public void sendSms(Movies movies) {
		try {
			// Construct data
			System.out.println("***************************************** From SMS Service");
			String textMessage = "BMS-ASSIST: "+movies.getMovieName().toUpperCase()+", Bookings opened in "+ movies.getTheaterName().toUpperCase();
			String apiKey = "apikey=" + "*******************";
			String message = "&message=" + textMessage;
			String sender = "&sender=" + "TXTLCL";
			String numbers = "&numbers=" + movies.getMobile();

			// Send data
			URL url = new URL("https://api.textlocal.in/send/?");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			String data = apiKey + numbers + message + sender;
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();
			System.out.println(stringBuffer.toString());
		} catch (Exception e) {
			System.out.println("Error SMS "+e);
		}
	}
}


