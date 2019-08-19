package com.java.chatbot.model;

public class Movies {
    private String movieName;
    private String date;
    private String emailId;
    private String error;
    private String transactionDate;
    private String requestedPerson;
    private String theaterName;
    private String theaterURL;
    private boolean eligibleForSearch=false;
    private String mobile;

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getTheaterName() {
        return theaterName;
    }

    public void setTheaterName(String theaterName) {
        this.theaterName = theaterName;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getRequestedPerson() {
        return requestedPerson;
    }

    public void setRequestedPerson(String requestedPerson) {
        this.requestedPerson = requestedPerson;
    }

    public String getTheaterURL() {
        return theaterURL;
    }

    public void setTheaterURL(String theaterURL) {
        this.theaterURL = theaterURL;
    }

    @Override
    public String toString() {
        return "{Movie : "+movieName+", Date : "+date+", Theater : "+theaterName+", Email Id : "+emailId+", ERRORS : "+error+", Transaction Date : "+transactionDate+", Requested Person : "+requestedPerson+", URL : "+theaterURL+" }";
    }

    public boolean isEligibleForSearch() {
        return eligibleForSearch;
    }

    public void setEligibleForSearch(boolean eligibleForSearch) {
        this.eligibleForSearch = eligibleForSearch;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
