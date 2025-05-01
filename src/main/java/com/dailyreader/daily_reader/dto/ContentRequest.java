package com.dailyreader.daily_reader.dto;

import jakarta.validation.constraints.NotBlank;

public record ContentRequest(
        @NotBlank
        String title,
        @NotBlank
        String body
) {
}
