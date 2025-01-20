package com.clone.twitter.post_service;

import com.clone.twitter.post_service.config.redis.RedisCacheConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
@EnableKafka
@EnableCaching
@EnableRedisRepositories(
		enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP,
		keyspaceConfiguration = RedisCacheConfig.RedisKeyspaceConfiguration.class,
		shadowCopy = RedisKeyValueAdapter.ShadowCopy.OFF)
@EnableFeignClients(basePackages = "com.clone.twitter.post_service.client")
@ConfigurationPropertiesScan(basePackages = "com.clone.twitter.post_service.property")
@EnableRetry
public class PostServiceApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(PostServiceApplication.class)
				.bannerMode(Banner.Mode.OFF)
				.run(args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper;
	}
}
