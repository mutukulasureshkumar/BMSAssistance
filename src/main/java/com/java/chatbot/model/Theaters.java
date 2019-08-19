package com.java.chatbot.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class Theaters {
    @Value("${prasads}")
    private String prasads;

    @Value("${prasadsBigScreen}")
    private String prasadsBigScreen;

    @Value("${central}")
    private String central;

    @Value("${forum}")
    private String forum;

    @Value("${inorbit}")
    private String inorbit;

    @Value("${ambSharatMall}")
    private String ambSharatMall;

    @Value("${platinumSLNTerminus}")
    private String platinumSLNTerminus;

    @Value("${cinipolisMajeeraMall}")
    private String cinipolisMajeeraMall;

    @Value("${geliriaHitech}")
    private String geliriaHitech;

    @Value("${bigCinemasAmeerpet}")
    private String bigCinemasAmeerpet;

    @Value("${asianLaxmikala}")
    private String asianLaxmikala;

    @Value("${sriramuluMoosapet}")
    private String sriramuluMoosapet;

     @Value("${inoxGSMMall}")
    private String inoxGSMMall;

    public HashMap<String, String> getTheraters(){
        HashMap<String, String> theraters = new HashMap<String, String>();
        theraters.put("PRASADS", prasads);
        theraters.put("PRASADS_BIGSCREEN", prasadsBigScreen);
        theraters.put("CENTRAL", central);
        theraters.put("PVR_FORUM", forum);
        theraters.put("PVR_INORBIT", inorbit);
        theraters.put("AMB_SHARATCITYMALL", ambSharatMall);
        theraters.put("PLATINUM_SLN", platinumSLNTerminus);
        theraters.put("PVR_GALIRIA_HITECH", geliriaHitech);
        theraters.put("CINIPOLIS_MANJEERA", cinipolisMajeeraMall);
        theraters.put("BIGCINIMAS_AMEERPET", bigCinemasAmeerpet);
        theraters.put("ASIAN_LAXMIKALA", asianLaxmikala);
        theraters.put("SRIRAMULU_MOOSAPET", sriramuluMoosapet);
        theraters.put("INOX_GSMMALL", inoxGSMMall);
        return theraters;
    }



}
