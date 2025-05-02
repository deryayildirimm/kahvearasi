package com.dailyreader.daily_reader.service;

import com.dailyreader.daily_reader.dto.SentContentRequest;
import com.dailyreader.daily_reader.dto.SentContentResponse;
import static com.dailyreader.daily_reader.exception.ExceptionHandler.throwIf;

import com.dailyreader.daily_reader.entity.Content;
import com.dailyreader.daily_reader.entity.SentContent;
import com.dailyreader.daily_reader.entity.User;
import com.dailyreader.daily_reader.exception.BadRequestException;
import com.dailyreader.daily_reader.exception.ErrorMessages;
import com.dailyreader.daily_reader.repository.ContentRepository;
import com.dailyreader.daily_reader.repository.SentContentRepository;
import com.dailyreader.daily_reader.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SentContentService {

    private final SentContentRepository sentContentRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;

    public SentContentService(SentContentRepository sentContentRepository,
                              UserRepository userRepository,
                              ContentRepository contentRepository) {
        this.sentContentRepository = sentContentRepository;
        this.userRepository = userRepository;
        this.contentRepository = contentRepository;
    }


    public SentContentResponse sendContent(SentContentRequest sentContentRequest) {

        User user = userRepository.findById(sentContentRequest.userId()).orElseThrow(
                () -> new BadRequestException(ErrorMessages.USER_NOT_FOUND)
        );

        Content content = contentRepository.findById(sentContentRequest.contentId()).orElseThrow(
                () -> new BadRequestException(ErrorMessages.CONTENT_NOT_FOUND)
        );

        throwIf(sentContentRepository.existsByUserIdAndContentId(user.getId(), content.getId()) ,
                () -> new BadRequestException(ErrorMessages.CONTENT_ALREADY_SENT_TO_USER));

        SentContent sentContent = SentContent.builder()
                .content(content)
                .user(user)
                .sentAt(LocalDateTime.now())
                .build();

       SentContent saved =  sentContentRepository.save(sentContent);

        return new SentContentResponse(
                saved.getId(),
                saved.getUser().getId(),
                saved.getContent().getId(),
                saved.getSentAt()
        );
    }


    public void deleteById(Long id) {

        SentContent sentContent = sentContentRepository.findById(id).orElseThrow(
                () -> new BadRequestException(ErrorMessages.SENT_CONTENT_NOT_FOUND)
        );
        sentContentRepository.delete(sentContent);
    }
}
