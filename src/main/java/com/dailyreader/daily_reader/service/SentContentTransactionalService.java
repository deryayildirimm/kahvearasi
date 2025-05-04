package com.dailyreader.daily_reader.service;

import com.dailyreader.daily_reader.entity.SentContent;
import com.dailyreader.daily_reader.repository.SentContentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class SentContentTransactionalService {

    private final SentContentRepository sentContentRepository;

    public SentContentTransactionalService(SentContentRepository sentContentRepository) {
        this.sentContentRepository = sentContentRepository;
    }

    @Transactional
    public void persist(SentContent sentContent) {
        System.out.println("Persist işlemine girildi.");
        sentContentRepository.save(sentContent);
    }
}
