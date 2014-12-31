package org.nivance.redisexam.serializer;

import java.nio.ByteBuffer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class DoubleSerializer implements RedisSerializer<Double> {
	private static final byte[] EMPTY_BYTE_ARRAY = ByteBuffer.wrap(new byte[0])
			.array();

	@Override
	public byte[] serialize(Double d) throws SerializationException {
		return d == null ? EMPTY_BYTE_ARRAY : ByteBuffer.allocate(8)
				.putDouble(0, d).array();
	}

	@Override
	public Double deserialize(byte[] bytes) throws SerializationException {
		return ByteBuffer.wrap(bytes).asDoubleBuffer().get(0);
	}
	
}
