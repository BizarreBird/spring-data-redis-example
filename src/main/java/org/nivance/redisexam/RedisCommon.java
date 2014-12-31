package org.nivance.redisexam;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import redis.clients.jedis.JedisPoolConfig;

@PropertySource("classpath:application.properties")
@EnableAutoConfiguration
@Slf4j
public class RedisCommon {
	
	@Bean
	JedisPoolConfig jedisPoolConfig(
			@Value("${redis.pool.maxactive}") int maxActive,
			@Value("${redis.pool.maxidle}") int maxIdle,
			@Value("${redis.pool.minidle}") int minIdle) {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(maxActive);
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMinIdle(minIdle);
		return jedisPoolConfig;
	}

	@Bean
	RedisConnectionFactory connectionFactory(JedisPoolConfig poolConfig,
			@Value("${redis.host}") String host,
			@Value("${redis.port}") int port,
			@Value("${redis.timout}") int timeout) {
		JedisConnectionFactory connectionFactory = new JedisConnectionFactory(
				poolConfig);
		log.info("--------------redis.host:" + host + ", port:" + port
				+ "--------------");
		connectionFactory.setHostName(host);
		connectionFactory.setPort(port);
		connectionFactory.setTimeout(timeout);
		connectionFactory.setUsePool(true);
		return connectionFactory;
	}
	
	@Bean
	StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}

}
