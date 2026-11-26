package com.thegoldenbook.rest.api.utils;

import java.lang.reflect.Type;
import redis.clients.jedis.Jedis;

public class RedisCache {
	
	private static final String HOST = "localhost";
	private static final int PORT = 6379;
	
	private static Jedis jedis = new Jedis(HOST, PORT);
	

	public static <T> void setObject(String key, T value, int ttlSeconds) {
	    String json = JsonUtils.getGson().toJson(value);
	    jedis.setex(key, ttlSeconds, json);
	}

	public static <T> T getObject(String key, Type type) {
	    String json = jedis.get(key);
	    return json != null ? JsonUtils.getGson().fromJson(json, type) : null;
	}
	
	public static void close() {
		jedis.close();
	}
}
