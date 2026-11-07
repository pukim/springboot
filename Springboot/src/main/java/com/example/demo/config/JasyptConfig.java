<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:key.properties") // src/main/resources/key.properties 참조

  public class JasyptConfig {

    @Value("${jasypt.key}")
=======
@Configuration
@PropertySource("classpath:key.properties") // src/main/resources/key.properties 참조

  public class JasyptConfig {

    	@Value("${jasypt.key}")
>>>>>>> branch 'master' of https://github.com/pukim/springboot.git
	private String key;
	
	@Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(key); //암호화에 사용할 키 -> 중요
        config.setAlgorithm("PBEWithMD5AndDES"); //사용할 알고리즘
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
  }

  
