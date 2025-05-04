package com.dailyreader.daily_reader.messaging.message;


public record SentContentMessage (
        Long userId,
        Long contentId
) {
}
