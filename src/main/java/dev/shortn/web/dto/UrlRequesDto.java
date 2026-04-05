package dev.shortn.web.dto;

import jakarta.validation.constraints.NotBlank;

public record UrlRequesDto(
        @NotBlank
        String url
) {
}
