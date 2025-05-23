package com.dailyreader.daily_reader.repository;

import com.dailyreader.daily_reader.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    boolean existsByTitle(String title);
    //Aynı başlıkla ama bu ID’ye ait olmayan başka içerik var mı?
    boolean existsByTitleAndIdNot(String title, Long id);
}
