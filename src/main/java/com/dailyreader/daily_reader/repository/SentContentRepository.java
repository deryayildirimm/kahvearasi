package com.dailyreader.daily_reader.repository;

import com.dailyreader.daily_reader.entity.SentContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SentContentRepository extends JpaRepository<SentContent, Long> {


    boolean existsByUserIdAndContentId(Long userId, Long contentId);
}
