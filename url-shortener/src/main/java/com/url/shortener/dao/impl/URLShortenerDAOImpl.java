package com.url.shortener.dao.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.url.shortener.dao.URLShortenerDAO;
import com.url.shortener.dao.rowmapper.URLRowMapper;
import com.url.shortener.model.URLData;

/**
 * 
 * This class is the implementation class of the DAO Interface 
 * to define URL shortening and expanding operations at the Database Access Layer
 * This class makes physical interactions with the Database to fetch and update
 * URL Data records from and into the database.
 * 
 * @author Shubham
 *
 */
public class URLShortenerDAOImpl implements URLShortenerDAO {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private String insertSql;

	private String updateSql;

	private String selectSql;

	private String selectUrlSql;

	private String updateLastAccessedSql;

	
	/**
	 * This method inserts a new URL in to the database if and only if that
	 * was not present in the database earlier.
	 * 
	 * @param url
	 * @return the generated url_id after insertion, the auto increment sequence value of the table
	 * @throws SQLException
	 * @throws DataAccessException
	 * @author Shubham
	 */
	@Transactional(propagation=Propagation.MANDATORY)
	public String insertNewOriginalUrl(String url) throws SQLException, DataAccessException{

		final String sql = getInsertSql();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		Date currentDate = new Date();
		Timestamp currentTimeStamp = new Timestamp(currentDate.getTime());
		URLData urlData = new URLData(url,null,currentTimeStamp,null);
		SqlParameterSource urlInsertParameters = new BeanPropertySqlParameterSource(urlData);
		jdbcTemplate.update(sql, urlInsertParameters, keyHolder, new String[]{"URL_ID"});
		return(String.valueOf(keyHolder.getKey()));


	}

	
	/**
	 * This method updates the row in to the database that contains the original url
	 * data with the short url that is generated for that original long url 
	 * 
	 * @param urlId
	 * @param shortUrl
	 * @return the number of rows affected/updated after the update operation, should be 1
	 * @throws SQLException
	 * @throws DataAccessException
	 * @author Shubham
	 */
	@Transactional(propagation=Propagation.MANDATORY)
	public int updateDBWithShortUrl(String urlId, String shortUrl) throws SQLException, DataAccessException{

		final String sql = getUpdateSql();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("urlId", urlId);
		paramMap.put("shortUrl", shortUrl);

		return jdbcTemplate.update(sql, paramMap);

	}

	
	/**
	 * This method fetches the original url corresponding to the input urlId.
	 * 
	 * @param urlId
	 * @return the original url for the urlId
	 * @throws SQLException
	 * @throws DataAccessException
	 * @author Shubham
	 */
	@Override
	public String selectOriginalURLWithId(String urlId) throws SQLException, DataAccessException {

		final String sql = getSelectSql();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("urlId", urlId);
		List<URLData> resultSet = jdbcTemplate.query(sql, paramMap, new URLRowMapper());
		assert(resultSet!=null);
		assert(resultSet.size()==1);
		assert(resultSet.get(0)!=null);

		return resultSet.get(0).getOriginalUrl();
	}


	/**
	 * This method fetches the short url which is already existing and 
	 * which matches the value of the input url argument.
	 * 
	 * @param url
	 * @return the shortened url whose value is same url
	 * @throws SQLException
	 * @throws DataAccessException
	 * @author Shubham
	 */
	@Override
	public String selectAlreadyExistingUrl(String url) throws SQLException,	DataAccessException {
		
		final String sql = getSelectUrlSql();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("originalUrl", url);
		List<URLData> resultSet = jdbcTemplate.query(sql, paramMap, new URLRowMapper());
		return (resultSet!=null && !resultSet.isEmpty() && resultSet.get(0)!=null)? resultSet.get(0).getShortUrl():null;
		
	}

	/**
	 * This method updates the last accessed time, that is the most recent time
	 * the row that corresponds to the input argument url is being accessed.
	 * 
	 * @param url
	 * @return the number of rows affected/updated as a result of the update, should be 1
	 * @throws SQLException
	 * @throws DataAccessException
	 * @author Shubham
	 */
	@Transactional(propagation=Propagation.MANDATORY)
	@Override
	public int updateLastAccessedTime(String url) throws SQLException, DataAccessException {
		final String sql = getUpdateLastAccessedSql();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("originalUrl", url);
		return jdbcTemplate.update(sql, paramMap);
	}


	public String getInsertSql() {
		return insertSql;
	}

	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}

	public String getUpdateSql() {
		return updateSql;
	}

	public void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}

	public String getSelectSql() {
		return selectSql;
	}

	public void setSelectSql(String selectSql) {
		this.selectSql = selectSql;
	}

	public String getSelectUrlSql() {
		return selectUrlSql;
	}

	public void setSelectUrlSql(String selectUrlSql) {
		this.selectUrlSql = selectUrlSql;
	}

	public String getUpdateLastAccessedSql() {
		return updateLastAccessedSql;
	}

	public void setUpdateLastAccessedSql(String updateLastAccessedSql) {
		this.updateLastAccessedSql = updateLastAccessedSql;
	}




}
