package com.dailyreader.daily_reader.controller;

import com.dailyreader.daily_reader.dto.ContentRequest;
import com.dailyreader.daily_reader.dto.ContentResponse;
import com.dailyreader.daily_reader.service.ContentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/contents")
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }


    @PostMapping()
    public ResponseEntity<ContentResponse> createContent(@Valid @RequestBody ContentRequest contentRequest){

       return ResponseEntity.status(HttpStatus.CREATED).body(contentService.createContent(contentRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentResponse> getContentById(@PathVariable Long id){

        return ResponseEntity.status(HttpStatus.OK).body(contentService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContentResponse> updateContent(@PathVariable Long id, @Valid @RequestBody ContentRequest contentRequest){

        return ResponseEntity.status(HttpStatus.OK).body(contentService.updateContent(id, contentRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id){
        contentService.deleteContent(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
