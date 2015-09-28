package com.url.shortener.cache.impl;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.scheduling.annotation.Scheduled;

import com.url.shortener.cache.DataCache;
import com.url.shortener.model.URLData;


/**
 * 
 * This class is the concrete class to the caching layer which provides a
 * template for a Thread Safe LRU(Least Recently Used) Cache to store most accessed
 * URL data in it. 
 * 
 * 
 * @author Shubham
 *
 */
public class URLDataCache extends DataCache<String, URLData> {

	/**
	 * 
	 * This method initializes the timeToLive and cacheMap 
	 * after construction
	 * 
	 */
	@PostConstruct
	public void init(){
		this.timeToLive = this.timeToLive * 2000;
		this.cacheMap = new ConcurrentHashMap<String, URLData>();
	}


	/**
	 * 
	 * Returns URLData from the cache based on the input key
	 * @param key the short url suffix/ the original urL
	 * @return the value associated for the key
	 * 
	 */
	@Override
	public URLData getURLDataFromCache(String key){
		return get(key);
	}

	
	/**
	 * 
	 * Puts URLData into the cache using the input key and value
	 * @param key the short url suffix/ the original urL
	 * @param value the URL data object
	 * 
	 */
	@Override
	public void putURLDataToCache(String key, URLData value){
		put(key, value);
	}

	/**
	 * 
	 * Checks if a particular key is present in the cache
	 * 
	 * @param key the input key which is checked to be present in the cache
	 * @return true/false based on the presence of the key in the cache
	 * 
	 */
	@Override
	public boolean isURLDataInCache(String key){
		return isContains(key);
	}

	/**
	 * 
	 * Get the url data from the cache based on the input key
	 * 
	 * Update its last accessed time
	 * 
	 * Put the updated url data in the cache
	 * 
	 * @param key the input key
	 * @return the urlData
	 * 
	 */
	
	@Override
	protected URLData get(String key) {
		URLData urlData = this.cacheMap.get(key);
		if (urlData == null)
			return null;
		else {
			urlData.setLastAccessedDate(new Timestamp(System.currentTimeMillis()));
			cacheMap.put(key, urlData);
			return urlData;
		}
	}

	@Override
	protected void put(String key, URLData value) {
		this.cacheMap.put(key, value);
	}

	@Override
	protected void removeFromCache(String key) {
		this.cacheMap.remove(key);
	}


	@Override
	protected boolean isContains(String key) {
		return this.cacheMap.containsKey(key);
	}

	
	/**
	 * 
	 * This method is a asynchronous scheduled method which runs after a fixed
	 * delay of 100 milliseconds to clear up the cache by removing stale and
	 * least recently used URL data from it. 
	 * 
	 * This frees up the memory for storage of most recently used URLData
	 * 
	 * 
	 */
	@Scheduled(fixedDelay=100000)
	private void clearUpCache() {

		Timestamp currTimestamp = new Timestamp(System.currentTimeMillis());
		Iterator<Entry<String,URLData>> itr = (this.cacheMap.entrySet()!=null && !this.cacheMap.entrySet().isEmpty())? this.cacheMap.entrySet().iterator() : null;
		URLData urlData = null;
		while (itr!=null && itr.hasNext()) {
			Entry<String,URLData> entry = itr.next();
			urlData = (URLData) entry.getValue();
			if (urlData != null && (currTimestamp.getTime() > (timeToLive.longValue() + ((urlData!=null && urlData.getLastAccessedDate()!=null)?urlData.getLastAccessedDate().getTime():0)))) {
				removeFromCache(entry.getKey());
			}
		}
	}



}
