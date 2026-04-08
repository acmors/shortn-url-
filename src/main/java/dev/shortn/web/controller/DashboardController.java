package dev.shortn.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.shortn.service.UrlClickService;
import dev.shortn.service.UrlService;
import dev.shortn.web.dto.UrlRequesDto;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UrlService urlService;
    private final UrlClickService clickService;
    private final RateLimiterRegistry rateLimiterRegistry;

    @Value("${app.base-url}")
    private String baseUrl;

    private RateLimiter rateLimiter;

    @PostConstruct
    public void init() {
        this.rateLimiter = rateLimiterRegistry.rateLimiter("createShortUrl");
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/create")
    public String create(UrlRequesDto dto, RedirectAttributes redirect) {

        if (!rateLimiter.acquirePermission()) {
            redirect.addFlashAttribute("error", "Rate limit exceeded! Maximum 3 URLs per minute. Please wait 60 seconds.");
            return "redirect:/error-page";
        }
        
        try {
            var result = urlService.createShortUrl(dto);
            redirect.addAttribute("code", result.shortCode());
            return "redirect:/list";
        } catch (Exception e) {
            log.error("Error creating short URL", e);
            redirect.addFlashAttribute("error", "Error creating short URL. Please try again.");
            return "redirect:/error-page";
        }
    }

    @GetMapping("/list")
    public ModelAndView list(@RequestParam(required = false) String code) {
        ModelAndView mv = new ModelAndView("list");
        try {
            var url = urlService.findByShortCodeDto(code);
            mv.addObject("url", url);
            mv.addObject("shortUrl", baseUrl + "/" + url.shortCode());
        } catch (Exception e) {
            mv.addObject("error", "URL not found");
        }
        return mv;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        var redirect = urlService.redirect(shortCode);
        clickService.incrementClick(shortCode);
        return ResponseEntity
                .status(302)
                .header("Location", redirect.url())
                .build();
    }

    @GetMapping("/error-page")
    public ModelAndView errorPage() {
        ModelAndView mv = new ModelAndView("error-limit");
        return mv;
    }
}   