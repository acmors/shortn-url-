package dev.shortn.web.controller;

import dev.shortn.service.UrlService;
import dev.shortn.web.dto.UrlRequesDto;
import dev.shortn.web.dto.UrlResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class UrlController {

    private final UrlService urlService;

    @PostMapping
    public ResponseEntity<UrlResponseDto> createShorCode(@RequestBody UrlRequesDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(urlService.createShortUrl(request));
    }

    @GetMapping("information/{shortCode}")
    public ResponseEntity<UrlResponseDto> findByShortCode(@PathVariable String shortCode){
        return ResponseEntity.ok().body(urlService.findByShortCodeDto(shortCode));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode){
        var redirect = urlService.redirect(shortCode);
        return ResponseEntity
                .status(302)
                .header("Location", redirect.url())
                .build();

    }
}
