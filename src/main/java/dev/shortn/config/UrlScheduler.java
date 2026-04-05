package dev.shortn.config;

import dev.shortn.service.UrlClickService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UrlScheduler {
    private final UrlClickService clickService;

    @Scheduled(fixedDelay = 30000)
    public void syncClicks(){
        log.info("Start clicks sync.....");
        clickService.syncClicksDB();
    }
}
