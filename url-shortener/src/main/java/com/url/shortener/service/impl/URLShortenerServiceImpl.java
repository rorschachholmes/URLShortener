package com.url.shortener.service.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.url.shortener.cache.impl.URLDataCache;
import com.url.shortener.dao.URLShortenerDAO;
import com.url.shortener.model.URLData;
import com.url.shortener.service.URLShortenerService;
import com.url.shortener.util.URLShortenerUtil;

/**
 * This class is the implementation class that defines url 
 * shortening and expanding operations at the service layer
 * 
 * @author Shubham
 *
 */
public class URLShortenerServiceImpl implements URLShortenerService {


	@Autowired
	@Qualifier("urlShortenerDAO")
	private URLShortenerDAO urlShortenerDAO;

	@Autowired
	private URLDataCache urlDataCache;

	private String shortUrlFormat;

	public String getShortUrlFormat() {
		return shortUrlFormat;
	}

	public void setShortUrlFormat(String shortUrlFormat) {
		this.shortUrlFormat = shortUrlFormat;
	}

	/**
	 * This method is responsible for returning the shortened
	 * version of the original long url argument.
	 * 
	 * @param url the original long url as the input
	 * @return the shortened url of the form http://lnk.url/aX1CFb
	 * @throws Exception
	 * 
	 */
	@Transactional(rollbackFor=Exception.class, readOnly=false)
	@Override
	public String getShortenedURL(String url) throws Exception {

		StringBuilder shortenedUrl = new StringBuilder();

		/* First check if the input url data is in the cache 
		 * 
		 * If yes return the short url directly from cache
		 * 
		 */
		if(urlDataCache.isURLDataInCache(url)){

			URLData urlData = urlDataCache.getURLDataFromCache(url);
			return (urlData!=null ? urlData.getShortUrl():null);
		}

		/*
		 * If input url data is not in cache
		 */
		else {

			/* Check whether there is any url same as input url
			 * already in the database
			 * 
			 * If yes then return the shortened url directly
			 * 
			 */
			if(getUrlIfAlreadyExist(url)!=null){
				String shortUrl = getUrlIfAlreadyExist(url);
				URLData urlData = getUrlData(url, shortUrl);
				urlDataCache.putURLDataToCache(url, urlData);
				return shortUrl;
			}
			
			/*
			 * If no, then :
			 * 
			 * 1. Insert new url in database
			 * 2. Get the generated url_id and get it converted to base 62 format
			 * 3. Append the base62 generated format to the short url prefix to create the short url
			 * 4. Encapsulate this data and put it in to tha cache
			 * 5. Return the short url.
			 */
			else {
				String keyHolder = urlShortenerDAO.insertNewOriginalUrl(url);
				String shortURLId = URLShortenerUtil.convertNumberToBase62(keyHolder);
				int updatedRows = urlShortenerDAO.updateDBWithShortUrl(keyHolder, (shortenedUrl.append(getShortUrlFormat()).append(shortURLId).toString()));
				assert(updatedRows==1);
				URLData urlData = getUrlData(url, shortenedUrl.toString());
				urlDataCache.putURLDataToCache(url, urlData);
				return (shortenedUrl.toString());
			}

		}

	}

	/**
	 * This method is responsible for returning the original expanded url
	 * from the short url suffix input argument.
	 * 
	 * @param shortURL the short url suffix like aZ1Cfb from http://lnk.url/aZ1Cfb
	 * @return the original expanded url that is http://www.youtube.com
	 * @throws Exception
	 * 
	 */
	
	@Override
	public String getExpandedURL(String shortURL) throws Exception {

		/*
		 * Firstly check whether the shortUrl suffix related info
		 * is in the cache.
		 * 
		 * If yes, return the original url directly from the cache
		 * 
		 */
		if(urlDataCache.isURLDataInCache(shortURL)){

			URLData urlData = urlDataCache.getURLDataFromCache(shortURL);
			return (urlData!=null ? urlData.getOriginalUrl():null);
		}

		/*
		 * If input short url suffix data is not in cache then
		 * 
		 * 1. Convert the short URL prefix (which is in base62) to decimal format
		 * 2. Fetch the original url with the generated decimal format number in Step 1 acting
		 *    as the input.
		 * 3. Encapsulate this entire data
		 * 4. Put this data into the cache
		 * 5. Update the last accessed time in the database as it is hitting the database here
		 * 6. Return the original Url
		 */
		else {
			String originalUrlId = URLShortenerUtil.convertNumberToDecimal(shortURL);
			String originalUrl = urlShortenerDAO.selectOriginalURLWithId(originalUrlId);
			URLData urlData = getUrlData(originalUrl, shortURL);
			urlDataCache.putURLDataToCache(shortURL, urlData);
			int updatedRows = urlShortenerDAO.updateLastAccessedTime(originalUrl);
			assert(updatedRows==1);
			return originalUrl;
		}
	}


	@Transactional(rollbackFor=Exception.class, readOnly=false)
	private String getUrlIfAlreadyExist(String url) throws Exception{

		String shortUrl = urlShortenerDAO.selectAlreadyExistingUrl(url);

		if(shortUrl!=null && !shortUrl.isEmpty()){
			int updatedRows = urlShortenerDAO.updateLastAccessedTime(url);
			assert(updatedRows==1);
		}
		return shortUrl;

	}

	private URLData getUrlData(String originalUrl, String shortUrl){

		URLData urlData = new URLData();
		urlData.setOriginalUrl(originalUrl);
		urlData.setShortUrl(shortUrl);
		urlData.setLastAccessedDate(new Timestamp(System.currentTimeMillis()));
		return urlData;
	}

}
