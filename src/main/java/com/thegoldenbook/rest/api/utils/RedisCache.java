package com.thegoldenbook.rest.api.utils;

import redis.clients.jedis.Jedis;

public class RedisCache {
	
	private static final String HOST = "localhost";
	private static final int PORT = 6379;
	
	private static Jedis jedis = new Jedis(HOST, PORT);
	
	public static String get(String key) {
		return jedis.get(key);
	}
	
	public static void set(String key, String value, int ttlSeconds) {
		jedis.setex(key, ttlSeconds, value);
	}
	
	public static void close() {
		jedis.close();
	}
}
