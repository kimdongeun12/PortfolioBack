package com.portfolio.auth;

import com.portfolio.global.exception.ForbiddenException;
import com.portfolio.global.exception.UnauthorizedException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AdminAuthInterceptor implements HandlerInterceptor {

    public static final String CSRF_HEADER = "X-Admin-Request";

    private final AdminCookieManager adminCookieManager;
    private final AdminAuthService adminAuthService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        boolean adminOnly = handlerMethod.hasMethodAnnotation(AdminOnly.class)
                || handlerMethod.getBeanType().isAnnotationPresent(AdminOnly.class);
        if (!adminOnly) {
            return true;
        }

        String token = adminCookieManager.resolveToken(request);
        if (!adminAuthService.isAuthenticated(token)) {
            throw new UnauthorizedException("관리자 로그인이 필요합니다.");
        }
        if (!"GET".equalsIgnoreCase(request.getMethod()) && !"1".equals(request.getHeader(CSRF_HEADER))) {
            throw new ForbiddenException(CSRF_HEADER + " 헤더가 필요합니다.");
        }
        return true;
    }
}
