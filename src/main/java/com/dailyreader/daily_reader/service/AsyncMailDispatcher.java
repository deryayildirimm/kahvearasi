package com.dailyreader.daily_reader.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncMailDispatcher {


    private final MailService mailService;

    public AsyncMailDispatcher(MailService mailService) {
        this.mailService = mailService;
    }

    @Async
    public void asyncSend(String to, String userName, String contentTitle, String contentBody){
        try{

            mailService.sendEmail(to, userName, contentTitle, contentBody);

        } catch (Exception e) {

            System.err.println("Async işlemi içinde hata: " + e.getMessage());
        }
    }


}
