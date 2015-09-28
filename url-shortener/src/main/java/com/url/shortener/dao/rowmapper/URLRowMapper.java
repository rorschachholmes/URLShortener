package com.url.shortener.dao.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.url.shortener.model.URLData;

/**
 * This is the custom Row Mapper implementation class
 * at the DAO layer to map fetched requests from the
 * database to the model class URLData
 *  
 * @author Shubham
 *
 */
public class URLRowMapper implements RowMapper<URLData> {

	@Override
	public URLData mapRow(ResultSet rs, int rowNum) throws SQLException {
		URLData urlData = new URLData();
		if(rs!=null && !rs.isBeforeFirst()){
			urlData.setOriginalUrl(rs.getString("ORIGINAL_URL"));
			urlData.setShortUrl(rs.getString("SHORT_URL"));
		}
		return urlData;
	}

}
