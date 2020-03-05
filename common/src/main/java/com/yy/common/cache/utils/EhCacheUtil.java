package com.yy.common.cache.utils;

import com.alibaba.fastjson.JSON;
import com.yy.common.cache.constant.DataConstants;
import org.ehcache.Cache;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.util.List;

/**
 * 本地缓存工具类
 * @author Jiny
 *
 */
@Component
public class EhCacheUtil {
	
	@Value("${ehcache.dbfile}")
	private String diskCacheDir;
	
	private static PersistentCacheManager cacheManager;
	
	@PostConstruct
	private void init() {
		DataConstants.DISK_CACHE_DIR = diskCacheDir;
		cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
				.with(CacheManagerBuilder
						.persistence(new File(DataConstants.DISK_CACHE_DIR, DataConstants.EHCACHE_CACHE_NAME)))
				.withCache(DataConstants.EHCACHE_CACHE_NAME,
						CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
								ResourcePoolsBuilder.newResourcePoolsBuilder()
								 		// 堆内缓存大小
										.heap(DataConstants.HEAP_CACHE_SIZE, MemoryUnit.MB)
										// 堆外缓存大小
										.offheap(DataConstants.OFF_HEAP_CACHE_SIZE, MemoryUnit.MB) 
										// 文件缓存大小
										.disk(DataConstants.DISK_CACHE_SIZE, MemoryUnit.MB,true) 
						)
								// 缓存超时时间
								// .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(DataConstants.EHCACHE_TTL))) 
						 		// 统计对象大小时对象图遍历深度
								.withSizeOfMaxObjectGraph(DataConstants.HEAP_MAX_OBJECT_GRAPH)
								 // 可缓存的最大对象大小
								.withSizeOfMaxObjectSize(DataConstants.HEAP_MAX_OBJECT_SIZE, MemoryUnit.KB)
								.build())
				.build(true);
	}

	/**
	 * 获取缓存
	 * @return
	 */
	private static Cache<String, String> getCache() {
		return cacheManager.getCache(DataConstants.EHCACHE_CACHE_NAME, String.class, String.class);
	}
	
	
	/**
	 * 键值存入相应的缓存
	 * @param key
	 * @param value
	 */
	public static void put(String key, Object value) {
		String json = JSON.toJSONString(value);
		if (getCache() == null ){
			return;
		}
		getCache().put(key, json);
	}

	/**
	 * 获取相应相应缓存值
	 * @param <T>
	 * @param key
	 * @param clazz
	 * @return
	 */
	public static <T> T get( String key, Class<T> clazz) {
		if (getCache() == null){
			return null;
		}
		String json = getCache().get(key);
		if (!ObjectUtils.isEmpty(json)) {
			return JSON.parseObject(json, clazz);
		}
		return null;
	}

	/**
	 * 获取相应相应缓存值
	 * @param <T>
	 * @param key
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> getList(String key, Class<T> clazz) {
		String json = getCache().get(key);
		if (!ObjectUtils.isEmpty(json)) {
			return JSON.parseArray(json, clazz);
		}
		return null;
	}

	// 清除缓存值
	public static void remove(String key) {
		getCache().remove(key);
	}

	/**
	 * 清楚缓存池
	 */
	public static void removeCache() {
		cacheManager.removeCache(DataConstants.EHCACHE_CACHE_NAME);
	}

	/**
	 * 关闭缓存
	 */
	@PreDestroy
	public static void close() {
		cacheManager.close();
	}
}
