
package com.url.shortener.service;


/**
 * Service Interface to declare URL shortening and expanding operations
 * at the service layer
 * 
 * @author Shubham
 *
 */
public interface URLShortenerService {
	
	public String getShortenedURL(String URL) throws Exception;
	public String getExpandedURL(String shortURL) throws Exception;
	

}
