package com.dvaults.recipecatalogue.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.data.redis.autoconfigure.DataRedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfigs {

  public static final String JWT_REFRESH_TOKEN_KEY_TEMPLATE = "auth:refresh:%s:%s";
  public static final String JWT_RESET_ACCESS_TOKEN_KEY_TEMPLATE = "auth:reset:%s";

  @Bean
  @Primary
  @ConfigurationProperties(prefix = "redis")
  public DataRedisProperties dataRedisProperties() {
    return new DataRedisProperties();
  }

  @Bean
  @Primary
  public LettuceConnectionFactory redisConnectionFactory(
      DataRedisProperties dataRedisProperties
  ) {

    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(
        dataRedisProperties.getHost(),
        dataRedisProperties.getPort()
    );
    config.setPassword(dataRedisProperties.getPassword());

    LettuceConnectionFactory factory = new LettuceConnectionFactory(config);
    SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor("redis-");
		executor.setVirtualThreads(true);
		factory.setExecutor(executor);

    return factory;

  }

  @Bean
  @Primary
  public StringRedisTemplate jwtStringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {

    StringRedisTemplate template = new StringRedisTemplate();
    template.setConnectionFactory(redisConnectionFactory);
    template.setDefaultSerializer(new StringRedisSerializer());
    template.afterPropertiesSet();

    return template;

  }

}
