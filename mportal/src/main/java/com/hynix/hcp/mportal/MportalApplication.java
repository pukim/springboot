package com.hynix.hcp.mportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication
public class MportalApplication {

	public static void main(String[] args) {
		SpringApplication.run(MportalApplication.class, args);
	}
	
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("내 프로젝트 API 명세서")
                        .description("스프링 부트 Swagger 테스트 API 문서입니다.")
                        .version("v1.0.0"));
    }

}
