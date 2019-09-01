package com.mmall.util;

import com.mmall.common.RedisPool;
import com.mmall.common.RedisShardedPool;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
/**
 * 封装jedis api
 * @author 94420
 *
 */
@Slf4j
public class RedisPoolUtil {
	//exTime时间单位是秒
			public static String setEx(String key,int exTime,String value) {
				Jedis jedis = null;
				String result = null;
				
				try {
					jedis = RedisPool.getJedis();
					result = jedis.setex(key,exTime,value);
				} catch (Exception e) {
					log.error("setex key:{} value:{} error",key,value,e);
					RedisPool.returnBrokenResource(jedis);
					return result;
				}
				RedisPool.returnResource(jedis);
				return result;
			}
			
			/**
			 * 设置key的过期时间，单位是秒
			 * @param key
			 * @param exTime
			 * @return
			 */
			public static Long expire(String key,int exTime) {
				Jedis jedis = null;
				Long result = null;
				
				try {
					jedis = RedisPool.getJedis();
					result = jedis.expire(key,exTime);
				} catch (Exception e) {
					log.error("setex key:{} error",key,e);
					RedisPool.returnBrokenResource(jedis);
					return result;
				}
				RedisPool.returnResource(jedis);
				return result;
			}
			
			public static String set(String key,String value) {
				Jedis jedis = null;
				String result = null;
				
				try {
					jedis = RedisPool.getJedis();
					result = jedis.set(key, value);
				} catch (Exception e) {
					log.error("set key:{} value:{} error",key,value,e);
					RedisPool.returnBrokenResource(jedis);
					return result;
				}
				RedisPool.returnResource(jedis);
				return result;
			}
			
			public static String get(String key) {
				Jedis jedis = null;
				String result = null;
				
				try {
					jedis = RedisPool.getJedis();
					result = jedis.get(key);
				} catch (Exception e) {
					log.error("get key:{} error",key,e);
					RedisPool.returnBrokenResource(jedis);
					return result;
				}
				RedisPool.returnResource(jedis);
				return result;
			}
			
			public static Long del(String key) {
				Jedis jedis = null;
				Long result = null;
				
				try {
					jedis = RedisPool.getJedis();
					result = jedis.del(key);
				} catch (Exception e) {
					log.error("del key:{} error",key,e);
					RedisPool.returnBrokenResource(jedis);
					return result;
				}
				RedisPool.returnResource(jedis);
				return result;
			}
			
			public static void main(String[] args) {
				RedisPoolUtil.set("testkey", "testvaule");
				String value = RedisShardedPoolUtil.get("testkey");
				RedisPoolUtil.setEx("keyex", 60*20, "valueex");
				RedisPoolUtil.expire("testkey", 60*10);
				RedisPoolUtil.del("testkey");
				System.out.println("end");
				
			}
			
	
	
}
