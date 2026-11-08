package com.portfolio.global.revalidate;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

/**
 * 쓰기 요청(POST/PUT/DELETE)이 성공하면 공개 사이트 캐시 갱신을 요청한다.
 */
@Component
@RequiredArgsConstructor
public class RevalidateInterceptor implements HandlerInterceptor {

    private static final Set<String> READ_METHODS = Set.of("GET", "HEAD", "OPTIONS");

    private final SiteRevalidator siteRevalidator;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (ex != null || READ_METHODS.contains(request.getMethod()) || response.getStatus() >= 400) {
            return;
        }
        siteRevalidator.request();
    }
}
