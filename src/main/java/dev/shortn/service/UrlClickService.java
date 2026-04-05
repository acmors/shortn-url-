package dev.shortn.service;

import dev.shortn.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlClickService {


    private static final String CLICK_KEY_PREFIX = "click:";
    private final RedisTemplate<String, Long> longRedisTemplate;
    private final UrlRepository urlRepository;

    public void incrementClick(String shortCode){
        String key = CLICK_KEY_PREFIX + shortCode;
        longRedisTemplate.opsForValue().increment(key);
    }

    @Transactional
    public void syncClicksDB(){
        Set<String> keys = longRedisTemplate.keys(CLICK_KEY_PREFIX + "*");

        if (keys == null || keys.isEmpty()) return;

        for(String key : keys){
            String shortCode = key.replace(CLICK_KEY_PREFIX, "");
            Long clicks = longRedisTemplate.opsForValue().getAndDelete(key);

            if (clicks == null || clicks == 0) continue;

            urlRepository.incrementClicks(shortCode, clicks);

            log.info("Sincronizado: {} → +{} clicks", shortCode, clicks);
            System.out.println();
        }
    }
}
