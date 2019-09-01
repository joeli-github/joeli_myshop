package com.mmall.common;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class TonkenCache {
	private static Logger logger = LoggerFactory.getLogger(TonkenCache.class);
	public static final String TOKEN_PREFIX = "token_";
//	本地缓存，
	private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder()
			.initialCapacity(1000).maximumSize(10000).expireAfterAccess(12,TimeUnit.HOURS)
			.build(new CacheLoader<String,String>() {
				@Override
				public String load(String s)throws Exception {
					return "null";
				}
			});
	public static void setKey(String key,String value) {
		localCache.put(key, value);
	}
	public static String getKey(String key) {
		String value = null;
		try {
			value=localCache.get(key);
			if ("null".equals(value)) {
				return null;
			}
			return value;
		} catch (Exception e) {
			logger.error("localCache get error",e);
		}
		return null;
		
		
	}
}
