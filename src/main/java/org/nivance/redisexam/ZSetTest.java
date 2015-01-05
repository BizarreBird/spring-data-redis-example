package org.nivance.redisexam;

import java.util.HashSet;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ZSetTest extends RedisCommon {

	public static void main(String[] args) throws InterruptedException {
		log.info("-----------Starting Redis ZSet testing-----------");
		ApplicationContext ctx = SpringApplication.run(ZSetTest.class, args);
		StringRedisTemplate st = ctx.getBean(StringRedisTemplate.class);
		String key = "ZSetKey";
		Set<TypedTuple<String>> values = new HashSet<>();
		for (int i = 0; i < 10; i++) {
			TypedTuple<String> tuple = new DefaultTypedTuple<String>("value-"
					+ i, 12d + i);
			values.add(tuple);
		}
//		log.info("SetKey add [" + st.opsForZSet().add(key, values) + "] values");
		//st.opsForZSet().add(key, value, score)
		//st.opsForZSet().incrementScore(key, value, delta)
		log.info("SetKey has [" + st.opsForZSet().size(key) + "] values");
		
		double start = 15d;
		double end = 18d;
		log.info("SetKey between " + start + " and " + end + " have " + st.opsForZSet().count(key, start, end));
		
		long s = 1;
		long e = 5;
		log.info("SetKey range from " + s + " to " + e + " have " + st.opsForZSet().range(key, s, e));
		//st.opsForZSet().rangeByScore(key, min, max, offset, count)
		//st.opsForZSet().rangeByScoreWithScores(key, min, max)
		//st.opsForZSet().rangeByScoreWithScores(key, min, max, offset, count)
		//st.opsForZSet()
		
		String member = "value-5";
		log.info(member + "'s rank is " + st.opsForZSet().rank(key, member) + " in SetKey.");
		 
		log.info("Remove " + member + " from SetKey : " + st.opsForZSet().remove(key, member));
		
//		st.opsForZSet().removeRange(key, start, end)
//		st.opsForZSet().removeRangeByScore(key, min, max)
//		st.opsForZSet().reverseRange(key, start, end)
//		st.opsForZSet().reverseRangeByScore(key, min, max)
//		st.opsForZSet().reverseRangeByScoreWithScores(key, min, max)
//		st.opsForZSet().reverseRank(key, o)
//		st.opsForZSet().unionAndStore(key, otherKeys, destKey)
//		st.opsForZSet().unionAndStore(key, otherKey, destKey)
	}

}
