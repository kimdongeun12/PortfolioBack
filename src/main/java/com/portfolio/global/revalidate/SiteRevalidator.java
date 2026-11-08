package com.portfolio.global.revalidate;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 콘텐츠가 바뀌면 공개 사이트(Next.js)의 캐시를 갱신하도록 요청한다.
 * 짧은 시간에 여러 번 바뀌어도 한 번만 요청하도록 묶어서 보낸다.
 */
@Slf4j
@Component
public class SiteRevalidator {

    private static final long DELAY_SECONDS = 2;

    private final String url;
    private final String secret;
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
        Thread thread = new Thread(runnable, "site-revalidator");
        thread.setDaemon(true);
        return thread;
    });
    private final AtomicBoolean pending = new AtomicBoolean(false);

    public SiteRevalidator(@Value("${app.revalidate.url:}") String url,
                           @Value("${app.revalidate.secret:}") String secret) {
        this.url = url == null ? "" : url.trim();
        this.secret = secret == null ? "" : secret.trim();
    }

    public void request() {
        if (url.isEmpty() || secret.isEmpty()) {
            return;
        }
        if (pending.compareAndSet(false, true)) {
            scheduler.schedule(this::send, DELAY_SECONDS, TimeUnit.SECONDS);
        }
    }

    private void send() {
        pending.set(false);
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .header("x-revalidate-secret", secret)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            if (response.statusCode() >= 400) {
                log.warn("사이트 캐시 갱신 요청 실패: status={}", response.statusCode());
            }
        } catch (Exception e) {
            log.warn("사이트 캐시 갱신 요청 실패: {}", e.getMessage());
        }
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdownNow();
    }
}
