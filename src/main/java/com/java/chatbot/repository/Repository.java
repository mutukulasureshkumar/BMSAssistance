package com.java.chatbot.repository;

import com.java.chatbot.model.Movies;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;

@Configuration
public class Repository {

private static HashMap<String, Movies> database = new HashMap<String, Movies>();
private static ArrayList<Movies> moviesList = new ArrayList<>();

public void set(String chatId, Movies movies){
    database.put(chatId, movies);
}

public Movies get(String chatId){
    if(database.get(chatId) == null)
        return new Movies();
    return database.get(chatId);
}

public String getAll(){
    StringBuffer stringBuffer = new StringBuffer();
    database.forEach((k,v) -> stringBuffer.append(k +" --> "+ v.toString()+".\n"));
    return stringBuffer.toString();
}

public void addMovie(Movies movie){
    moviesList.add(movie);
}

public void removeMovie(Movies movie){
    moviesList.remove(movie);
}

public ArrayList<Movies> getMovies(){
    return moviesList;
}

}