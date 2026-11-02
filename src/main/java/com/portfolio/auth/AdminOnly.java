package com.portfolio.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 관리자만 호출할 수 있는 API 에 붙입니다. (메서드 또는 컨트롤러 클래스)
 * AdminAuthInterceptor 가 로그인 쿠키를 검사합니다.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminOnly {
}
