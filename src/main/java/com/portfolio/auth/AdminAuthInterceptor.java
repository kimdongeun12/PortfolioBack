package com.portfolio.auth;

import com.portfolio.exception.ForbiddenException;
import com.portfolio.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @AdminOnly 가 붙은 API 요청을 검사합니다.
 *
 * 1) admin_token 쿠키가 유효한지 (없거나 만료 → 401)
 * 2) GET 이 아닌 요청은 X-Admin-Request: 1 헤더가 있는지 (없으면 403)
 *    → 다른 사이트의 form 전송은 커스텀 헤더를 붙일 수 없어서 CSRF 공격을 막을 수 있습니다.
 */
@Component
@RequiredArgsConstructor
public class AdminAuthInterceptor implements HandlerInterceptor {

    public static final String CSRF_HEADER = "X-Admin-Request";

    private final AdminCookieManager adminCookieManager;
    private final AdminAuthService adminAuthService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 정적 리소스, CORS preflight 등은 검사하지 않음
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
