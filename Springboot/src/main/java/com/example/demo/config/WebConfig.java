package com.example.demo.config;

@Configuration
@RequiredArgsConstructor
public class WebConfig {
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
