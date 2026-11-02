package com.example.demo.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Configuration
@EnableAsync
public class AsyncConfig {
  @Bean(name = "taskExecutor")

  public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(100);                  // 기본적으로 유지할 스레드 수
        executor.setMaxPoolSize(300);                   // 최대 생성 가능한 스레드 수
        executor.setQueueCapacity(1000);                // 스레드 풀이 가득 찼을 때 대기할 큐 용량
        executor.setThreadNamePrefix("AsyncThread-");  // 스레드 이름 접두사 (로깅용)
        executor.setTaskDecorator(runnable -> {
        	RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        	return()->{
        		try {
        			if(requestAttributes != null) {
        				//비동기 스레드에 Context 설정(inheritable:true) 
        				RequestContextHolder.setRequestAttributes(requestAttributes, true);
        			}
        			runnable.run();
        		} finally {
        			// 메모리 누수 방지 Context 정리 
        			RequestContextHolder.resetRequestAttributes();
        		}
        	};
        }); // 비동기 스레드 생성 시 기존 스레드의 RequestContext를 복사하도록 설정
        executor.initialize();
        return executor;
    }
}
