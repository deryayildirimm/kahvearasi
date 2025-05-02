package com.dailyreader.daily_reader.dto;

import java.time.LocalDateTime;

public record SentContentResponse(
        Long id,
        Long userId,
        Long contentId,
        LocalDateTime sentAt
) {
}
