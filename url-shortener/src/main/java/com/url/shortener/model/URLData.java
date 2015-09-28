package com.url.shortener.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
 * This is the model class that is being used
 * to hold URL related information or data.
 * 
 * @author Shubham
 *
 */
public class URLData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3633301276970742893L;



	private String originalUrl;
	private String shortUrl;
	private Timestamp createdDate;
	private Timestamp lastAccessedDate;

	public URLData(){}
	
	public URLData (String originalUrl, String shortUrl, Timestamp createdDate, Timestamp lastAccessedDate){

		this.originalUrl=originalUrl;
		this.shortUrl=shortUrl;
		this.createdDate=createdDate;
		this.lastAccessedDate=lastAccessedDate;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getLastAccessedDate() {
		return lastAccessedDate;
	}

	public void setLastAccessedDate(Timestamp lastAccessedDate) {
		this.lastAccessedDate = lastAccessedDate;
	}




}
