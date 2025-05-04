package com.dailyreader.daily_reader.messaging.producer;

import com.dailyreader.daily_reader.entity.SentContent;
import com.dailyreader.daily_reader.messaging.config.RabbitMQConfig;
import com.dailyreader.daily_reader.messaging.message.SentContentMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class SentContentProducer {

    private final RabbitTemplate rabbitTemplate;

    private static final String QUEUE_NAME = RabbitMQConfig.SENT_CONTENT_QUEUE;

    public SentContentProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(SentContentMessage sentContentMessage) {
        rabbitTemplate.convertAndSend(QUEUE_NAME, sentContentMessage);
        System.out.println("RabbitMQ → Kuyruğa mesaj gönderildi: " + sentContentMessage);
    }

}
