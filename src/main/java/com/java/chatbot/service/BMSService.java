package com.java.chatbot.service;

import com.java.chatbot.model.Movies;
import com.java.chatbot.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ListIterator;

@Configuration
@EnableAsync
public class BMSService {

    @Autowired
    private Repository repository;

    @Autowired
    private EmailService emailService;

   @Autowired
    private SMSService smsService;

    private void readByTherater(){
        BufferedReader br = null;
        try {
            ListIterator<Movies> movies = repository.getMovies().listIterator();
            while(movies.hasNext()){
                if(validate(movies.next())){
                    movies.remove();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        finally {

            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private synchronized boolean validate(Movies movies) throws Exception {
        URLConnection url = new URL(movies.getTheaterURL()).openConnection();
        url.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        url.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(url.getInputStream(), Charset.forName("UTF-8")));
        String line;
        while ((line = br.readLine()) != null) {
            if(line.toUpperCase().contains(movies.getMovieName().toUpperCase())){
                smsService.sendSms(movies);
                emailService.sendMail(movies);
                System.out.println(movies.getMovieName()+ " Movie Placed in BMS......Email sent to "+movies.getEmailId());
                return true;
            }
        }
        return false;
    }

    @Scheduled(fixedRate = 180000)
    public void execute(){
        System.out.println("Executing started....");
        if(!repository.getMovies().isEmpty()){
            System.out.println("Found movies to notify......");
            readByTherater();
        }
    }
}
