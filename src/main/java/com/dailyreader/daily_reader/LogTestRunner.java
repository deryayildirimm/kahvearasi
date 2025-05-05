package com.dailyreader.daily_reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class LogTestRunner implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void run(String... args) {
        log.info("✅ Uygulama başlatıldı. Bu bir test logudur - ELK kontrol ediliyor.");
    }
}
