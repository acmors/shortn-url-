package dev.shortn.service;

import dev.shortn.domain.Url;
import dev.shortn.exceptions.UrlNotFoundException;
import dev.shortn.repository.UrlRepository;
import dev.shortn.web.dto.UrlRedirectDto;
import dev.shortn.web.dto.UrlRequesDto;
import dev.shortn.web.dto.UrlResponseDto;
import dev.shortn.web.mapper.UrlMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository repository;

    @Transactional
    public UrlResponseDto createShortUrl(UrlRequesDto request){
        String code = generateCode();
        while (repository.findByShortCode(code).isPresent()){
            code = generateCode();
        }

        Url url = new Url();
        url.setOriginalUrl(request.url());
        url.setShortCode(code);
        url.setCreatedAt(LocalDateTime.now());
        url.setClicks(0L);

        var saved = repository.save(url);
        return UrlMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public Url findByShortCode(String shortCode){
        return repository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("Short code not found"));
    }

    @Transactional(readOnly = true)
    public UrlResponseDto findByShortCodeDto(String shortCode){
        Url url = findByShortCode(shortCode);
        return UrlMapper.toDto(url);
    }

    @Transactional(readOnly = true)
    public UrlRedirectDto redirect(String shortCode){
        Url url = findByShortCode(shortCode);
        return new UrlRedirectDto(url.getOriginalUrl());
    }



    //code generator method
    private String generateCode(){
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 6);
    }
}
