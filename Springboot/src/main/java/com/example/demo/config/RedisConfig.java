


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.Pool;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Slf4j
public class RedisConfig {

     private String host;
	 private int port;
	 private String password;
	 private String activeMode;
	 private String clusterNodes;
	 
	 @Bean
	 public RedisConnectionFactory redisConnectionFactory() {

   		if("prd".equals(activeMode)) {  // redis cluster 연결
			RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration(clusterNodes);
			clusterConfiguration.setPassword(password);
			
			return new LettuceConnectionFactory(clusterConfiguration);
		} else {                       // standalone redis 연결
			RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
			redisStandaloneConfiguration.setPassword(password);
			
			return  new LettuceConnectionFactory(redisStandaloneConfiguration);
		}

	@Bean(name = "hcpRedisTemplate")
	public RedisTemplate<String, Object> redisTemplate(@Qualifier("hcpRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		return redisTemplate;
	
	}

} 
  
