package com.dailyreader.daily_reader.service;

import com.dailyreader.daily_reader.dto.ContentRequest;
import com.dailyreader.daily_reader.dto.ContentResponse;
import com.dailyreader.daily_reader.entity.Content;
import com.dailyreader.daily_reader.exception.BadRequestException;
import com.dailyreader.daily_reader.exception.ErrorMessages;
import static com.dailyreader.daily_reader.exception.ExceptionHandler.throwIf;
import com.dailyreader.daily_reader.exception.ResourceNotFoundException;
import com.dailyreader.daily_reader.repository.ContentRepository;
import org.springframework.stereotype.Service;

@Service
public class ContentService {

    private final ContentRepository contentRepository;


    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }


    // create , findById,

    public ContentResponse createContent(ContentRequest contentRequest) {

        throwIf(contentRepository.existsByTitle(contentRequest.title()) ,
                () -> new BadRequestException(ErrorMessages.CONTENT_TITLE_ALREADY_EXISTS));

        Content content = Content.builder()
                .title(contentRequest.title())
                .body(contentRequest.body())
                .build();

        Content saved = contentRepository.save(content);

        return new ContentResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getBody()
        );

    }

    public ContentResponse getById(Long id) {

        Content content = contentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(ErrorMessages.CONTENT_NOT_FOUND)
        );

        return new ContentResponse(
                content.getId(),
                content.getTitle(),
                content.getBody()
        );
    }

    public ContentResponse updateContent(Long id, ContentRequest contentRequest) {

        Content content = contentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(ErrorMessages.CONTENT_NOT_FOUND) );

        throwIf(
                contentRepository.existsByTitleAndIdNot(contentRequest.title(), id),
                () -> new BadRequestException(ErrorMessages.CONTENT_TITLE_ALREADY_EXISTS)
        );

        content.setTitle(contentRequest.title());
        content.setBody(contentRequest.body());

        Content saved = contentRepository.save(content);

        return new ContentResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getBody()
        );

    }

    public void deleteContent(Long id) {
        Content content = contentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(ErrorMessages.CONTENT_NOT_FOUND)
        );

        contentRepository.delete(content);
    }





}
