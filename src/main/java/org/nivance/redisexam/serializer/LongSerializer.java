package org.nivance.redisexam.serializer;

import java.nio.ByteBuffer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class LongSerializer implements RedisSerializer<Long> {
	private static final byte[] EMPTY_BYTE_ARRAY = ByteBuffer.wrap(new byte[0])
			.array();

	@Override
	public byte[] serialize(Long l) throws SerializationException {
		return l == null ? EMPTY_BYTE_ARRAY : ByteBuffer.allocate(8)
				.putLong(0, l).array();
	}

	@Override
	public Long deserialize(byte[] bytes) throws SerializationException {
		return ByteBuffer.wrap(bytes).asLongBuffer().get(0);
	}
	
}
