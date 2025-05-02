package com.dailyreader.daily_reader.dto;

import jakarta.validation.constraints.NotNull;

public record SentContentRequest(
        @NotNull
        Long userId,
        @NotNull
        Long contentId
) {
}
