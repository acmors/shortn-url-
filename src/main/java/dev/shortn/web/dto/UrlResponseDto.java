package dev.shortn.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public record UrlResponseDto(
        String url,
        String shortCode,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "GMT-3")
        LocalDateTime createdAt,
        Long clicks
) implements Serializable {
}
