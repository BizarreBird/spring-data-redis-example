package org.nivance.redisexam;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SetTest extends RedisCommon {

	public static void main(String[] args) throws InterruptedException {
		log.info("-----------Starting Redis Set testing-----------");
		ApplicationContext ctx = SpringApplication.run(SetTest.class, args);
		StringRedisTemplate st = ctx.getBean(StringRedisTemplate.class);
		String key = "SetKey";
		String destKey = "DestKey";
		String[] values = new String[]{"value1","value2","value3","value4","value5","value6","value7"};
		log.info("SetKey add [" + st.opsForSet().add(key, values ) + "] values ");
		log.info("SetKey's member " + st.opsForSet().members(key));
		String value5 = "value5";
		log.info(value5 + " is member of SetKey's : " + st.opsForSet().isMember(key, value5));
		log.info("SetKey's randomMember [" + st.opsForSet().randomMember(key) + "]");
		log.info("SetKey's size: " + st.opsForSet().size(key));
		
		String[] subValues = new String[]{"value1","value2","value3"};
		log.info("SetKey remove " + st.opsForSet().remove(key, subValues) + " members");
		log.info("SetKey's size: " + st.opsForSet().size(key));
		
		log.info("SetKey move to DestKey: " + st.opsForSet().move(key, value5, destKey));
		log.info("SetKey's size: " + st.opsForSet().size(key));
		log.info("DestKey size: " + st.opsForSet().size(destKey));
		
		String popValue = st.opsForSet().pop(key);
		log.info("SetKey move to DestKey: " + st.opsForSet().move(key, popValue, destKey));
		log.info("SetKey's size: " + st.opsForSet().size(key));
		log.info("DestKey size: " + st.opsForSet().size(destKey));
		
		//st.opsForSet().difference(key, destKey);
		//st.opsForSet().differenceAndStore(key, otherKeys, destKey);
		
		//st.opsForSet().intersect(key, destKey);
		//st.opsForSet().intersectAndStore(key, otherKey, destKey);
	}

}

