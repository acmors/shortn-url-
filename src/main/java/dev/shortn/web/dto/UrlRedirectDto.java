package dev.shortn.web.dto;

import java.io.Serializable;

public record UrlRedirectDto(
        String url
) implements Serializable {
}
