package com.dailyreader.daily_reader.controller;

import com.dailyreader.daily_reader.dto.SentContentRequest;
import com.dailyreader.daily_reader.dto.SentContentResponse;
import com.dailyreader.daily_reader.entity.SentContent;
import com.dailyreader.daily_reader.service.SentContentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/sentcontents")
public class SentContentController {

    private final SentContentService sentContentService;


    public SentContentController(SentContentService sentContentService) {
        this.sentContentService = sentContentService;
    }


    @PostMapping
    public ResponseEntity<SentContentResponse> saveSentContent(@Valid  @RequestBody SentContentRequest sentContent) {

        SentContentResponse sentContentResponse = sentContentService.sendContent(sentContent);

        return ResponseEntity.status(HttpStatus.CREATED).body(sentContentResponse);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSentContent(@PathVariable Long id) {
        sentContentService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
