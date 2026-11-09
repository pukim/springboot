


import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.Pool;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.skhynix.hcp.mportal.home.controller.EtcController;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.util.Pool;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Slf4j
 public class RedisConfig {

     private String host;
	   private int port;
	   private String password;

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
  
