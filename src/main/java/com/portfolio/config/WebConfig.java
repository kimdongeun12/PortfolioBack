package com.portfolio.config;

import com.portfolio.auth.AdminAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * - CORS: 프론트 도메인에서 /api/** 호출 허용 (DELETE 메서드는 사용하지 않음, 쿠키 포함 허용)
 * - 관리자 인증: @AdminOnly 가 붙은 API 를 AdminAuthInterceptor 가 검사
 * - 업로드 이미지: /uploads/** 요청을 업로드 폴더의 파일로 연결
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AdminAuthInterceptor adminAuthInterceptor;

    @Value("${app.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.upload.url-prefix}")
    private String uploadUrlPrefix;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns("/api/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        String location = uploadPath.toUri().toString();
        if (!location.endsWith("/")) {
            location += "/";
        }
        registry.addResourceHandler(uploadUrlPrefix + "/**")
                .addResourceLocations(location);
    }
}
