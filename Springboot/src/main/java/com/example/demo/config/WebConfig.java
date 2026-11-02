package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
	// SSO 확인
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ssoInterceptor)
                .addPathPatterns("/api/**")  // /api/로 시작하는 모든 요청에 인터셉터 적용
                .excludePathPatterns(
                    "/api/login",            // 로그인 API는 인증 체크 제외
                    "/api/public/**",        // 공용 API 제외
                    "/api/user/getEmpId"     // 최초 토큰만 있고 사번을 모들 때
                );
        registry.addInterceptor(loggingInterceptor)
		        .addPathPatterns("/api/**")  // /api/로 시작하는 모든 요청에 인터셉터 적용
		        .excludePathPatterns(
		        );
    }
	
    // CORS 허용
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
