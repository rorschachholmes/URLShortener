package com.url.shortener.cache;

import java.util.concurrent.ConcurrentHashMap;


/**
 * 
 * This class is an abstraction to the caching layer which provides a
 * template for a Thread Safe LRU(Least Recently Used) Cache using a
 * ConcurrentHashMap in order to store most accessed data in it. 
 * 
 * This layer is responsible for performance and high
 * throughput for the application through caching and can be used for
 * scaling purposes.
 * 
 * @author Shubham
 *
 * @param <K>
 * @param <E>
 * 
 * 
 */
public abstract class DataCache<K,E> {

	protected Long timeToLive;
	protected ConcurrentHashMap<K, E> cacheMap;
		
	public E getDataFromCache(K key){
		E value = get(key);
		return value;
	}

	public void putDataToCache(K key, E value){
		put(key, value);
	}

	public boolean isDataInCache(K key){
		return isContains(key);
	}
	
	protected abstract void put(K key, E value);

	protected abstract E get(K key);

	protected abstract void removeFromCache(K key);
		
	protected abstract boolean isContains(K key);

	protected int size() {
		return cacheMap.size();
	}

	public Long getTimeToLive() {
		return this.timeToLive;
	}

	public void setTimeToLive(Long timeToLive) {
		this.timeToLive = timeToLive;
	}

	public ConcurrentHashMap<K, E> getCacheMap() {
		return this.cacheMap;
	}

	public void setCacheMap(ConcurrentHashMap<K, E> cacheMap) {
		this.cacheMap = cacheMap;
	}
	
	
}
