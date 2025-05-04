package com.dailyreader.daily_reader;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@EnableRabbit
@EnableRetry
@SpringBootApplication
public class DailyReaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DailyReaderApplication.class, args);
	}

}
