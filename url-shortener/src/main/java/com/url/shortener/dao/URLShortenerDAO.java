package com.url.shortener.dao;

import java.sql.SQLException;

import org.springframework.dao.DataAccessException;

/**
 * DAO Interface to declare URL shortening and expanding operations
 * at the Database Access Layer
 * @author Shubham
 *
 */
public interface URLShortenerDAO {

	public String insertNewOriginalUrl(String url) throws SQLException, DataAccessException;
	public int updateDBWithShortUrl(String urlId, String shortUrl) throws SQLException, DataAccessException;
	public String selectOriginalURLWithId(String urlId) throws SQLException, DataAccessException;
	public String selectAlreadyExistingUrl(String url) throws SQLException, DataAccessException;
	public int updateLastAccessedTime(String url) throws SQLException, DataAccessException;


}
