package org.nivance.redisexam;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * 可以启动多个RedisMessageListener监听同一个Topic，每个listener都会收到相同的消息。
 * <p>
 * 
 */
@EnableAutoConfiguration
@Slf4j
public class TopicTest extends RedisCommon {
	private static String topicName = "Topic:chat";

	@Bean
	RedisMessageListenerContainer container(
			RedisConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapter, new PatternTopic(topicName));
		//container.addMessageListener(listenerAdapter, new ChannelTopic(topicName));
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	@Bean
	Receiver receiver(@Value("Receiver-1") String name) {
		return new Receiver(name);
	}

	public static void main(String[] args) throws InterruptedException {
		log.info("-----------Starting Redis Topic testing-----------");
		ApplicationContext ctx = SpringApplication.run(TopicTest.class, args);
		StringRedisTemplate template = ctx.getBean(StringRedisTemplate.class);
		template.convertAndSend(topicName, "Hello from Redis!");
	}

	static class Receiver {
		private String name;

		@Autowired
		public Receiver(String name) {
			this.name = name;
		}

		public void receiveMessage(String message) {
			log.info(name + " received <" + message + ">");
		}
	}

}