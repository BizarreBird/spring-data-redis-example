package org.nivance.redisexam;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StringsTest extends RedisCommon {
	private static StringRedisTemplate template;
	private static String key = "strKey";
	
	public static void main(String[] args) throws InterruptedException {
		log.info("-----------Starting Redis Strings testing-----------");
		ApplicationContext ctx = SpringApplication.run(StringsTest.class, args);
		template = ctx.getBean(StringRedisTemplate.class);
		StringsTest stringsTest = ctx.getBean(StringsTest.class);
		
		String value = "hello, redis";
		template.opsForValue().set(key, value);
		log.info("StringsTest @##@ " + key + "'s value: " + template.opsForValue().get(key));
		log.info("StringsTest @##@ " + key + "'s size: " + template.opsForValue().size(key));
		
		stringsTest.getRange(key, 0, 5);
		
		template.opsForValue().getAndSet(key, "hello, redis world");
		log.info("StringsTest @##@ " + key + "'s value after getAndSet: " + template.opsForValue().get(key));
		
		stringsTest.multiOperation();
		
		stringsTest.incrementDouble();
		
		stringsTest.incrementLong();
	}
	
	public void getRange(String key, int start, int end){
		log.info("StringsTest @##@ " + key + "'s value: " + template.opsForValue().get(key));
		log.info("StringsTest @##@ " + key + " range from "+start +" to " + end +" value is: " + template.opsForValue().get(key, start, end));
	}
	
	public void multiOperation(){
		Map<String, String> m = new HashMap<>();
		Set<String> keys = new HashSet<>();
		for(int i=0;i< 4;i++){
			m.put("key" + i, "value" + i);
			keys.add("key" + i);
		}
		template.opsForValue().multiSet(m);
		log.info("StringsTest @##@ multiSet : done.");
		log.info("StringsTest @##@ multiGet : " + template.opsForValue().multiGet(keys));
	}

	public void incrementDouble() {
		String hashKey = UUID.randomUUID().toString();
		Double value1 = template.opsForValue().increment(hashKey, 30.5d);
		log.info(hashKey + " has value :" + value1);
		double d = 30.2d;
		Double value2 = template.opsForValue().increment(hashKey, d);
		log.info(hashKey + " has value: " + value2 + ", after increment " + d);
	}

	public void incrementLong() {
		String hashKey = UUID.randomUUID().toString();
		long value1 = template.opsForValue().increment(hashKey, 30l);
		log.info(hashKey + " has value :" + value1);
		long l = 25l;
		long value2 = template.opsForValue().increment(hashKey, l);
		log.info(hashKey + " has value: " + value2 + ", after increment " + l);
	}
}
