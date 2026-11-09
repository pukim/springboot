import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@EnableCaching
@Configuration
public class CacheConfig {
  	// 캐시 이름  정의 
	public static final String CACHE_MYINFO    = "myInfoCache";     // 사용자 정보 cache
	public static final String CACHE_MYCNTINFO = "myCntInfoCache";  // 사용자 메일, 일정, 할일, 결재 건수 
	
	
    @Bean
    public RedisCacheManager  cacheManager(RedisConnectionFactory connectionFactory) {
        
        // 기본 캐시 설정 (String Key, JSON Value 직렬화)
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)) // 기본 TTL 10분
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues(); // null 값 캐싱 방지

        // 도메인/기능별 각기 다른 TTL 설정 맵
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // 1.1 나의 정보
        cacheConfigurations.put(CACHE_MYINFO  , defaultConfig.entryTtl(Duration.ofSeconds(myInfoCache)));
        // 1.2 나의 메일, 일정 건수정보
        // cacheConfigurations.put(CACHE_MYCNTINFO, defaultConfig.entryTtl(Duration.ofSeconds(myInfoCntCache)));
        
      )));
        
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
