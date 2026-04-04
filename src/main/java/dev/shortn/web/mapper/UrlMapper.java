package dev.shortn.web.mapper;

import dev.shortn.domain.Url;
import dev.shortn.web.dto.UrlRedirectDto;
import dev.shortn.web.dto.UrlResponseDto;

public class UrlMapper {
    public static UrlResponseDto toDto(Url url){
        return new UrlResponseDto(
                url.getOriginalUrl(),
                url.getShortCode(),
                url.getCreatedAt()
        );
    }

    public static UrlRedirectDto redirect(Url url){
        return new UrlRedirectDto(
                url.getShortCode()
        );
    }
}
