package com.example.demo.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.config.interceptor.LoggingInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
	
    @Value("${file.upload-dir}")
	private String uploadDir;
	
    @Autowired 
    private LoggingInterceptor loggingInterceptor;
    
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
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

	    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // http://localhost:8080/uploads/filename.png 접속 시 실제 디렉토리 파일 제공
    	String absolutePath = new File(uploadDir).getAbsolutePath();
    	String resourceLocation = "file:///" + absolutePath.replace("\\", "/") + "/";
    	
        registry.addResourceHandler("/files/**")
                .addResourceLocations(resourceLocation);
    }
}
