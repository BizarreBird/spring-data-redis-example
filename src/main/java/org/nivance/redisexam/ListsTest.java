package org.nivance.redisexam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

import org.nivance.redisexam.bean.UserInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ListsTest extends RedisCommon {
	private static RedisTemplate<String, UserInfo> userTemplate;

	public static void main(String[] args) throws InterruptedException {
		log.info("-----------Starting Redis Lists testing-----------");
		ApplicationContext ctx = SpringApplication.run(ListsTest.class, args);
		RedisConnectionFactory connectionFactory = ctx
				.getBean(RedisConnectionFactory.class);
		userTemplate = new RedisTemplate<>();
		userTemplate.setConnectionFactory(connectionFactory);
		userTemplate.setKeySerializer(userTemplate.getStringSerializer());
		userTemplate.setValueSerializer(new JacksonJsonRedisSerializer<UserInfo>(
				UserInfo.class));
		userTemplate.afterPropertiesSet();
		
		String key = "UserInfo";
		
		ListsTest listsTest = ctx.getBean(ListsTest.class);
		listsTest.leftpush(key);
		listsTest.leftpushBatch(key);
		listsTest.leftpop(key);
		listsTest.rightPopAndLeftPush(key);
	}

	public void leftpush(String key) {
		int size = 10;
		for(int i = 0; i < size; i++){
			UserInfo info = new UserInfo();
			info.setName("Tomy" + i);
			info.setAge(20 + i);
			info.setBirthday(new Date());
			info.setId(UUID.randomUUID().toString());
			userTemplate.opsForList().leftPush(key, info);
			//userTemplate.opsForList().leftPush(key, pivot, value)
			//userTemplate.opsForList().leftPushIfPresent(key, value)
			//userTemplate.opsForList().rightPush(key, pivot, value)
			//userTemplate.opsForList().rightPushIfPresent(key, value)
		}
		log.info("insert [" + size + "] User success! " + key + "'s size is:" + userTemplate.opsForList().size(key));
	}
	
	public void leftpushBatch(String key){
		int size = 20;
		List<UserInfo> users = new ArrayList<>();
		for(int i = 10; i < size; i++){
			UserInfo info = new UserInfo();
			info.setName("Tomy" + i);
			info.setAge(20 + i);
			info.setBirthday(new Date());
			info.setId(UUID.randomUUID().toString());
			users.add(info);
		}
		userTemplate.opsForList().leftPushAll(key, users.toArray(new UserInfo[users.size()]));
		//userTemplate.opsForList().rightPushAll(key, (UserInfo[])users.toArray());
		log.info("batchinsert [" + users.size() + "] User success! " + key + "'s size is:" + userTemplate.opsForList().size(key));
	}

	public void leftpop(String key){
		UserInfo userInfo = userTemplate.opsForList().leftPop(key, 2, TimeUnit.SECONDS);
		//userTemplate.opsForList().leftPop(key);
		AtomicInteger ai = new AtomicInteger(0);
		while(userInfo != null){
			ai.incrementAndGet();
			userInfo = userTemplate.opsForList().leftPop(key, 2, TimeUnit.SECONDS);
		}
		log.info("pop [" + ai.get() + "] Users from " + key);
	}
	
	public void rightPopAndLeftPush(String srcKey){
		String destinationKey = "destinationKey";
		log.info("srcKey [" + srcKey + "]'s size : " + userTemplate.opsForList().size(srcKey));
		log.info("destKey [" + destinationKey + "]'s size : " + userTemplate.opsForList().size(destinationKey));
		UserInfo userInfo = userTemplate.opsForList().rightPopAndLeftPush(srcKey, destinationKey);
		while(userInfo != null){
			userInfo = userTemplate.opsForList().rightPopAndLeftPush(srcKey, destinationKey, 2, TimeUnit.SECONDS);
		}
		log.info("After rightPopAndLeftPush destKey [" + destinationKey + "]'s size : " + userTemplate.opsForList().size(destinationKey));
	}
	
	
	
}

