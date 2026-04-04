package dev.shortn.web.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record UrlResponseDto(
        String url,
        String shortCode,
        LocalDateTime createdAt
) implements Serializable {
}
