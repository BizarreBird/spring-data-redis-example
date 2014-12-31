package org.nivance.redisexam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import org.nivance.redisexam.bean.UserInfo;
import org.nivance.redisexam.serializer.LongSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HashTest extends RedisCommon {
	private static RedisTemplate<String, Map<String, UserInfo>> userTemplate;
	private static RedisTemplate<String, Map<String, Double>> doubleTemplate;
	private static RedisTemplate<String, Map<String, Long>> longTemplate;
	private static String key = "UserInfo";

	public static void main(String[] args) throws InterruptedException {
		log.info("-----------Starting Redis hash testing-----------");
		ApplicationContext ctx = SpringApplication.run(HashTest.class, args);
		RedisConnectionFactory connectionFactory = ctx
				.getBean(RedisConnectionFactory.class);
		userTemplate = new RedisTemplate<>();
		userTemplate.setConnectionFactory(connectionFactory);
		userTemplate.setKeySerializer(userTemplate.getStringSerializer());
		userTemplate.setHashKeySerializer(userTemplate.getStringSerializer());
		userTemplate
				.setHashValueSerializer(new JacksonJsonRedisSerializer<UserInfo>(
						UserInfo.class));
		userTemplate.afterPropertiesSet();

		doubleTemplate = new RedisTemplate<>();
		doubleTemplate.setConnectionFactory(connectionFactory);
		doubleTemplate.setKeySerializer(doubleTemplate.getStringSerializer());
		doubleTemplate.setHashKeySerializer(doubleTemplate.getStringSerializer());
		doubleTemplate.setHashValueSerializer(doubleTemplate.getDefaultSerializer());
		doubleTemplate.afterPropertiesSet();

		longTemplate = new RedisTemplate<>();
		longTemplate.setConnectionFactory(connectionFactory);
		longTemplate.setKeySerializer(longTemplate.getStringSerializer());
		longTemplate.setHashKeySerializer(longTemplate.getStringSerializer());
		longTemplate.setHashValueSerializer(new LongSerializer());
		longTemplate.afterPropertiesSet();

		HashTest hashTest = ctx.getBean(HashTest.class);
		// hashTest.insert();
		// hashTest.batchInsert();
		// hashTest.insertIfAbsent();
		// hashTest.findAll();
		// hashTest.findOne();
		// hashTest.findAllKeys();
//		hashTest.incrementDouble();
		hashTest.incrementLong();
	}

	public void insert() {
		UserInfo info = new UserInfo();
		info.setName("Tomy");
		info.setAge(20);
		info.setBirthday(new Date());
		info.setId(UUID.randomUUID().toString());
		userTemplate.opsForHash().put(key, info.getId(), info);
		log.info("insert User[" + info + "] success!");
		log.info("User Hash size is : " + userTemplate.opsForHash().size(key));
	}

	public void batchInsert() {
		Map<String, UserInfo> users = new HashMap<>();
		for (int i = 1; i <= 3; i++) {
			UserInfo info = new UserInfo();
			info.setName("Tomy" + i);
			info.setAge(20 + i);
			info.setBirthday(new Date());
			info.setId(UUID.randomUUID().toString());
			users.put(info.getId(), info);
		}
		userTemplate.opsForHash().putAll(key, users);
		log.info("batchInsert Users[" + users + "] success!");
		log.info("User Hash size is : " + userTemplate.opsForHash().size(key));
	}

	public void insertIfAbsent() {
		UserInfo info = new UserInfo();
		info.setName("Tomy4");
		info.setAge(20);
		info.setBirthday(new Date());
		info.setId(UUID.randomUUID().toString());
		userTemplate.opsForHash().putIfAbsent(key, info.getId(), info);
		log.info("insertIfAbsent User[" + info + "] success!");
		log.info("User Hash size is : " + userTemplate.opsForHash().size(key));
	}

	public void findAll() {
		Map<Object, Object> users = userTemplate.opsForHash().entries(key);
		log.info("All User[" + users + "]");
		log.info("findAll User size is : " + users.size());
	}

	public UserInfo findOne() {
		String hashKey = "2ca66275-88ab-49e5-8651-b987e55d9347";
		Object userInfo = userTemplate.opsForHash().get(key, hashKey);
		// boolean have = userTemplate.opsForHash().hasKey(hashKey, hashKey);
		log.info("find one : " + userInfo);
		return userInfo != null ? (UserInfo) userInfo : null;
	}

	public Set<Object> findAllKeys() {
		Set<Object> users = userTemplate.opsForHash().keys(key);
		log.info("find : " + users.size() + " users :" + users);
		return users;
	}

	public void scan() {
		userTemplate.opsForHash().scan(key, ScanOptions.NONE);
	}

	public void incrementDouble() {
		String hashKey = UUID.randomUUID().toString();
		Double value1 = doubleTemplate.opsForHash().increment(key, hashKey,
				Double.valueOf("30"));
		log.info(key + ":" + hashKey + " has value :" + value1);
		Double delta = Double.valueOf("30.3");
		Double value2 = doubleTemplate.opsForHash().increment(key, hashKey, delta);
		log.info(key + ":" + hashKey + " has value: " + value2 + ", after increment " + delta);
	}

	public void incrementLong() {
		String hashKey = UUID.randomUUID().toString();
		long value1 = doubleTemplate.opsForHash().increment(key, hashKey, 30l);
		log.info(key + ":" + hashKey + " has value :" + value1);
		long delta = 20l;
		long value2 = doubleTemplate.opsForHash().increment(key, hashKey, delta);
		log.info(key + ":" + hashKey + " has value: " + value2 + ", after increment " + delta);
	}

}
