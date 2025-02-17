package com.fuxuras.patisoru.configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@EnableCaching
@Configuration
public class RedisConfig {




    @Bean
    public RedisConnectionFactory redisConnectionFactory() {

        RedisStandaloneConfiguration redisConfiguration =
                new RedisStandaloneConfiguration();

        redisConfiguration.setHostName("redis");
        redisConfiguration.setPort(6379);
        return new LettuceConnectionFactory(redisConfiguration);

    }
}