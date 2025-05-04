package com.dailyreader.daily_reader.controller;

import com.dailyreader.daily_reader.messaging.message.SentContentMessage;
import com.dailyreader.daily_reader.messaging.producer.SentContentProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/test")
public class TestMessageController {

    private final SentContentProducer producer;


    public TestMessageController(SentContentProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendTestMessage(@RequestParam Long userId, @RequestParam Long contentId) {
        SentContentMessage message = new SentContentMessage(userId, contentId);
        producer.sendMessage(message);
        return ResponseEntity.ok("Mesaj kuyruğa gönderildi");
    }
}
