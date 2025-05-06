package com.dailyreader.daily_reader.messaging.consumer;

import com.dailyreader.daily_reader.entity.Content;
import com.dailyreader.daily_reader.entity.SentContent;
import com.dailyreader.daily_reader.entity.User;
import com.dailyreader.daily_reader.exception.BadRequestException;
import com.dailyreader.daily_reader.service.AsyncMailDispatcher;
import com.dailyreader.daily_reader.service.RedisService;
import com.dailyreader.daily_reader.service.SentContentTransactionalService;
import com.dailyreader.daily_reader.messaging.config.RabbitMQConfig;
import com.dailyreader.daily_reader.messaging.message.SentContentMessage;
import com.dailyreader.daily_reader.repository.ContentRepository;
import com.dailyreader.daily_reader.repository.SentContentRepository;
import com.dailyreader.daily_reader.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class SentContentConsumer {

    private static final Logger log = LoggerFactory.getLogger(SentContentConsumer.class);
    private final SentContentRepository sentContentRepository;
    private final SentContentTransactionalService transactionalService;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final AsyncMailDispatcher asyncMailDispatcher;
    private final RedisService redisService;
    private final StringRedisTemplate redisTemplate;

    public SentContentConsumer(
            SentContentRepository sentContentRepository,
            SentContentTransactionalService transactionalService,
            UserRepository userRepository,
            ContentRepository contentRepository,
            AsyncMailDispatcher asyncMailDispatcher, RedisService redisService, StringRedisTemplate redisTemplate) {
        this.sentContentRepository = sentContentRepository;
        this.transactionalService = transactionalService;
        this.userRepository = userRepository;
        this.contentRepository = contentRepository;
        this.asyncMailDispatcher = asyncMailDispatcher;
        this.redisService = redisService;
        this.redisTemplate = redisTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.SENT_CONTENT_QUEUE)
    public void receiveMessage(SentContentMessage message) {
        System.out.println("RabbitMQ → Kuyruktan mesaj alındı: " + message);
        System.out.println("Dinlenen Kuyruk : " + RabbitMQConfig.SENT_CONTENT_QUEUE);

        String key = "sent:user:" + message.userId() + ":content:" + message.contentId();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            System.out.println("Redis: Bu içerik zaten gönderilmiş.");
            return;
        }

        try {
            User user = userRepository.findById(message.userId())
                    .orElseThrow(() -> new BadRequestException("User not found"));
            Content content = contentRepository.findById(message.contentId())
                    .orElseThrow(() -> new BadRequestException("Content not found"));

            boolean alreadySent = sentContentRepository.
                    existsByUserIdAndContentId(user.getId(), content.getId());
            if (alreadySent) {
                System.out.println("Bu içerik zaten gönderilmişti.");
                redisTemplate.opsForValue().set(key, "true", Duration.ofDays(7)); // Redis'te eksikti → ekle
                return;
            }

            SentContent sentContent = SentContent.builder()
                    .user(user)
                    .content(content)
                    .sentAt(LocalDateTime.now())
                    .build();


            transactionalService.persist(sentContent); // 🔥 KAYIT BURADA GERÇEKLEŞİR

            redisTemplate.opsForValue().set(key, "true", Duration.ofDays(7)); // Yeni gönderim → Redis'e ekle
            System.out.println("Kayıt edildi ve mail gönderiliyor → " + key);
            log.info("Kayıt edildi ve mail gönderiliyor → " + key);
            System.out.println("Mesaj başarıyla işlendi. ID: " + sentContent.getId());

            asyncMailDispatcher.asyncSend(user.getEmail(), user.getUserName(), content.getTitle(),  content.getBody());


        } catch (BadRequestException e) {
            // Kullanıcıya daha önce içerik gönderilmişse logla ve işleme devam et
            System.out.println("Bu içerik zaten gönderilmişti: " + e.getMessage());

        } catch (Exception e) {
            // Diğer istisnalar hala önemlidir → logla veya sistemsel işlem başlat
            System.err.println("RabbitMQ mesajı işlenirken beklenmedik hata oluştu: " + e.getMessage());
            throw e; // Bu satırı istersen kaldırabilirsin, retry yapmak istiyorsan bırak.
        }
    }
}
