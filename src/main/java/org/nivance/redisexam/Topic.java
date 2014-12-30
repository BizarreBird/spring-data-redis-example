package org.nivance.redisexam;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import redis.clients.jedis.JedisPoolConfig;

@PropertySource("classpath:application.properties")
@EnableAutoConfiguration
public class Topic {
	private static final Logger LOGGER = LoggerFactory.getLogger(Topic.class);

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
		LOGGER.info("--------------redis.host:" + host + ", port:" + port
				+ "--------------");
		connectionFactory.setHostName(host);
		connectionFactory.setPort(port);
		connectionFactory.setTimeout(timeout);
		connectionFactory.setUsePool(true);
		return connectionFactory;
	}

	@Bean
	RedisMessageListenerContainer container(
			RedisConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapter, new PatternTopic("chat"));
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	@Bean
	Receiver receiver(CountDownLatch latch) {
		return new Receiver(latch);
	}

	@Bean
	CountDownLatch latch() {
		return new CountDownLatch(1);
	}

	@Bean
	StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}

	public static void main(String[] args) throws InterruptedException {
		ApplicationContext ctx = SpringApplication.run(Topic.class, args);
		StringRedisTemplate template = ctx.getBean(StringRedisTemplate.class);
		CountDownLatch latch = ctx.getBean(CountDownLatch.class);
		LOGGER.info("Sending message...");
		template.convertAndSend("chat", "Hello from Redis!");
		latch.await();
		System.exit(0);
	}

	class Receiver {
		private CountDownLatch latch;

		@Autowired
		public Receiver(CountDownLatch latch) {
			this.latch = latch;
		}

		public void receiveMessage(String message) {
			LOGGER.info("Received <" + message + ">");
			latch.countDown();
		}
	}

}