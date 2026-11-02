package com.portfolio.controller;

import com.portfolio.auth.AdminAuthService;
import com.portfolio.auth.AdminCookieManager;
import com.portfolio.auth.AdminOnly;
import com.portfolio.auth.IssuedToken;
import com.portfolio.dto.AdminLoginRequestDto;
import com.portfolio.dto.AdminSessionResponseDto;
import com.portfolio.util.ClientInfoResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 관리자 인증 API
 *
 * POST /api/admin/login    로그인 (성공 시 admin_token 쿠키 발급)
 * POST /api/admin/logout   로그아웃 (쿠키 삭제)
 * GET  /api/admin/me       로그인 상태 확인 (로그인 안 됨 → 401) — 어드민 화면 진입 시 체크용
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;
    private final AdminCookieManager adminCookieManager;
    private final ClientInfoResolver clientInfoResolver;

    @PostMapping("/login")
    public ResponseEntity<AdminSessionResponseDto> login(@Valid @RequestBody AdminLoginRequestDto requestDto,
                                                         HttpServletRequest request) {
        IssuedToken issued = adminAuthService.login(requestDto.getPassword(), clientInfoResolver.getClientIp(request));
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,
                        adminCookieManager.createLoginCookie(issued.token(), adminAuthService.getTokenValidity()).toString())
                .body(new AdminSessionResponseDto(true, issued.expiresAt()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, adminCookieManager.createLogoutCookie().toString())
                .build();
    }

    @AdminOnly
    @GetMapping("/me")
    public ResponseEntity<AdminSessionResponseDto> me() {
        return ResponseEntity.ok(new AdminSessionResponseDto(true, null));
    }
}
