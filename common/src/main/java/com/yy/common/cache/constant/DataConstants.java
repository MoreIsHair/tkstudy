package com.yy.common.cache.constant;

/**
 * 常量
 */
public class DataConstants {

	
	/**
	 * 堆缓存大小 单位M
	 */
	public static final int HEAP_CACHE_SIZE = 10;
	
	
	/**
	 * 堆外缓存大小 单位MB
	 */
	public static final int OFF_HEAP_CACHE_SIZE = 20;
	
	
	/**
	 * 磁盘缓存大小 单位MB
	 */
	public static final int DISK_CACHE_SIZE = 100;
	
	
	/**
	 * 堆可缓存的最大对象大小 单位KB
	 */
	public static final int HEAP_MAX_OBJECT_SIZE = 100;
	
	
	/**
	 * 统计对象大小时对象图遍历深度
	 */
	public static final long HEAP_MAX_OBJECT_GRAPH = 1000L;
	
	
	/**
	 * 磁盘文件路径
	 */
	public static String DISK_CACHE_DIR;
	
	
	/**
	 *ehcache缓存超时时间 单位秒
	 */
	public static final int EHCACHE_TTL = 120;
	
	
	/**
	 *ehcache缓存超时时间 单位秒
	 */
	public static final String EHCACHE_CACHE_NAME = "default";
	
	

}
