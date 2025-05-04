package com.dailyreader.daily_reader.scheduler;

import com.dailyreader.daily_reader.entity.Content;
import com.dailyreader.daily_reader.entity.User;
import com.dailyreader.daily_reader.messaging.message.SentContentMessage;
import com.dailyreader.daily_reader.repository.ContentRepository;
import com.dailyreader.daily_reader.repository.SentContentRepository;
import com.dailyreader.daily_reader.repository.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MorningContentScheduler {


    private final RabbitTemplate rabbitTemplate;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final SentContentRepository sentContentRepository;


    public MorningContentScheduler(RabbitTemplate rabbitTemplate, UserRepository userRepository, ContentRepository contentRepository, SentContentRepository sentContentRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.userRepository = userRepository;
        this.contentRepository = contentRepository;
        this.sentContentRepository = sentContentRepository;
    }


    @Scheduled(cron = "*/15 * * * * *")
    public void sendContentToDailyReader() {

        List<User> users = userRepository.findAll();
        List<Content> contents = contentRepository.findAll();

        for (User user : users) {
            for (Content content : contents) {
                boolean alreadySent = sentContentRepository
                        .existsByUserIdAndContentId(user.getId(), content.getId());

                if (!alreadySent) {
                    SentContentMessage message = new SentContentMessage(user.getId(), content.getId());
                    rabbitTemplate.convertAndSend("", "sent-content-queue", message);
                    System.out.println("RabbitMQ → Kuyruğa mesaj gönderildi: " + message);
                    break; // o kullanıcıya sadece 1 içerik gönderiyoruz
                }
            }
        }
    }
}
