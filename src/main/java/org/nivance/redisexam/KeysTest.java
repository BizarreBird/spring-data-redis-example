package org.nivance.redisexam;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KeysTest extends RedisCommon {
	private static StringRedisTemplate template;
	private static String key = "key1";

	public static void main(String[] args) {
		log.info("-----------Starting Redis keys testing-----------");
		ApplicationContext ctx = SpringApplication.run(KeysTest.class, args);
		template = ctx.getBean(StringRedisTemplate.class);
		KeysTest keysTest = ctx.getBean(KeysTest.class);
		keysTest.initValue();

		log.info("KeysTest @##@ randomKey: " + template.randomKey());

		keysTest.expireKey(key, 2);
		// keysTest.persistKey(key, 2);

		String newkey = "newKey";
		// template.rename(key, newkey);
		template.renameIfAbsent(key, newkey);

		Set<String> keys = template.keys("*");
		log.info("KeysTest @##@ keys:" + keys);
		for (String key : keys) {
			log.info("KeysTest @##@ " + key + " expire:"
					+ template.getExpire(key));
			// template.getExpire(key, TimeUnit.SECONDS);
		}

		int dbIndex = 1;// ref:http://redisdoc.com/key/move.html
		log.info("KeysTest @##@ move " + key + " to db" + dbIndex + ": "
				+ template.move(key, 1));

		// template.delete(key);
		template.delete(keys);
		log.info("KeysTest @##@ delete keys: " + keys);

		// template.exec();
		// template.multi();
		// template.discard();

		// template.slaveOf(host, port);
		// template.slaveOfNoOne();

		// template.watch(key);
		// template.watch(keys);
		// template.unwatch();
		log.info("-----------End Redis keys testing-----------");
	}

	public void initValue() {
		String value = "hello,redis";
		template.opsForValue().set(key, value);
		Set<String> keys = new HashSet<String>() {
			private static final long serialVersionUID = -4402948387930279259L;
			{
				this.add("key2");
				this.add("key3");
				this.add("key4");
			}
		};
		try {
			byte[] bytes = template.dump(key);
			log.info("KeysTest # key dump:" + new String(bytes));
			for (String k : keys) {
				template.restore(k, bytes, 0, TimeUnit.SECONDS);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean expireKey(String key, long expiretime) {
		log.info("KeysTest @##@ has " + key + " : " + template.hasKey(key));
		log.info("KeysTest @##@ expire " + key + " for " + expiretime
				+ " seconds : "
				+ template.expire(key, expiretime, TimeUnit.SECONDS));
		// template.expireAt(key, new Date());
		try {
			Thread.sleep((expiretime + 1) * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		boolean result = template.hasKey(key);
		log.info("KeysTest @##@ has " + key + " : " + result);
		return result;
	}

	public boolean persistKey(String key, long expiretime) {
		log.info("KeysTest @##@ has " + key + " : " + template.hasKey(key));
		log.info("KeysTest @##@ expire " + key + " for " + expiretime
				+ " seconds : "
				+ template.expire(key, expiretime, TimeUnit.SECONDS));
		log.info("KeysTest @##@ persist " + key + " : " + template.persist(key));
		try {
			Thread.sleep((expiretime + 1) * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return template.hasKey(key);
	}

}
