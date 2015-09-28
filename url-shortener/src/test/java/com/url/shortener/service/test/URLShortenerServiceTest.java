/**
 * 
 * This is the Unit Test Suite for the URL Shortener Service
 * 
 */

package com.url.shortener.service.test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.url.shortener.cache.impl.URLDataCache;
import com.url.shortener.dao.impl.URLShortenerDAOImpl;
import com.url.shortener.model.URLData;
import com.url.shortener.service.impl.URLShortenerServiceImpl;
import com.url.shortener.util.URLShortenerUtil;




/**
 * Test Class for URL Shortener Service
 * 
 * @author Shubham
 *
 */

@SuppressWarnings("deprecation")
@PrepareForTest({URLShortenerUtil.class})
@RunWith(PowerMockRunner.class)
public class URLShortenerServiceTest {

	@Mock
	private URLDataCache urlDataCache;

	@Mock
	private URLShortenerDAOImpl urlShortenerDAO;


	private URLShortenerServiceImpl urlShortenerServiceImpl;
	private Properties testDataProps = new Properties();
	private InputStream propInputStream;
	private static final String TEST_DATA_PROPS_FILE_NAME="testData.properties";

	@Before
	public void setUp() throws Exception{

		urlShortenerServiceImpl=new URLShortenerServiceImpl();
		propInputStream = URLShortenerServiceTest.class.getClassLoader().getResourceAsStream(TEST_DATA_PROPS_FILE_NAME);
		testDataProps.load(propInputStream);
		urlShortenerServiceImpl.setShortUrlFormat(testDataProps.getProperty("shorturl.format"));
		MockitoAnnotations.initMocks(this);
		Field f = URLShortenerServiceImpl.class.getDeclaredField("urlDataCache");
		f.setAccessible(true);
		f.set(urlShortenerServiceImpl, urlDataCache);

		f = URLShortenerServiceImpl.class.getDeclaredField("urlShortenerDAO");
		f.setAccessible(true);
		f.set(urlShortenerServiceImpl, urlShortenerDAO);


	}


	@Test
	public void testShortenUrlValid1InCache() throws Exception{

		URLData urlData = new URLData();
		urlData.setOriginalUrl(testDataProps.getProperty("originalUrl1"));
		urlData.setShortUrl(testDataProps.getProperty("shortUrl1"));
		when(urlDataCache.isURLDataInCache(testDataProps.getProperty("originalUrl1"))).thenReturn(true);
		when(urlDataCache.getURLDataFromCache(testDataProps.getProperty("originalUrl1"))).thenReturn(urlData);
		String shortenedUrl = urlShortenerServiceImpl.getShortenedURL(testDataProps.getProperty("originalUrl1"));
		Assert.assertEquals(testDataProps.getProperty("shortUrl1"), shortenedUrl);

	}


	@Test
	public void testShortenUrlValid2ExistingNotInCache() throws Exception{


		URLData urlData = new URLData();
		urlData.setOriginalUrl(testDataProps.getProperty("originalUrl2"));
		urlData.setShortUrl(testDataProps.getProperty("shortUrl2"));
		when(urlDataCache.isURLDataInCache(testDataProps.getProperty("originalUrl2"))).thenReturn(false);
		when(urlShortenerDAO.selectAlreadyExistingUrl(testDataProps.getProperty("originalUrl2"))).thenReturn(testDataProps.getProperty("shortUrl2"));
		when(urlShortenerDAO.updateLastAccessedTime(testDataProps.getProperty("originalUrl2"))).thenReturn(1);
		doNothing().when(urlDataCache).putURLDataToCache(anyString(), any(URLData.class));
		String shortenedUrl = urlShortenerServiceImpl.getShortenedURL(testDataProps.getProperty("originalUrl2"));
		Assert.assertEquals(testDataProps.getProperty("shortUrl2"), shortenedUrl);

	}


	@Test
	public void testShortenUrlValid3NotExisting() throws Exception{


		PowerMockito.mockStatic(URLShortenerUtil.class);
		URLData urlData = new URLData();
		urlData.setOriginalUrl(testDataProps.getProperty("originalUrl3"));
		urlData.setShortUrl(testDataProps.getProperty("shortUrl3"));
		when(urlDataCache.isURLDataInCache(testDataProps.getProperty("originalUrl3"))).thenReturn(false);
		when(urlShortenerDAO.selectAlreadyExistingUrl(testDataProps.getProperty("originalUrl3"))).thenReturn(null);
		when(urlShortenerDAO.insertNewOriginalUrl(testDataProps.getProperty("originalUrl3"))).thenReturn(testDataProps.getProperty("urlIdGenerated3"));
		PowerMockito.when(URLShortenerUtil.convertNumberToBase62(testDataProps.getProperty("urlIdGenerated3"))).thenReturn(testDataProps.getProperty("shortUrlSuffix3"));
		when(urlShortenerDAO.updateDBWithShortUrl(testDataProps.getProperty("urlIdGenerated3"),testDataProps.getProperty("shortUrl3"))).thenReturn(1);
		doNothing().when(urlDataCache).putURLDataToCache(anyString(), any(URLData.class));
		String shortenedUrl = urlShortenerServiceImpl.getShortenedURL(testDataProps.getProperty("originalUrl3"));
		Assert.assertEquals(testDataProps.getProperty("shortUrl3"), shortenedUrl);

	}

	@Test
	public void testExpandUrlValid1ExistingInCache() throws Exception{

		URLData urlData = new URLData();
		urlData.setOriginalUrl(testDataProps.getProperty("originalUrl1"));
		urlData.setShortUrl(testDataProps.getProperty("shortUrl1"));
		when(urlDataCache.isURLDataInCache(testDataProps.getProperty("shortUrlSuffix1"))).thenReturn(true);
		when(urlDataCache.getURLDataFromCache(testDataProps.getProperty("shortUrlSuffix1"))).thenReturn(urlData);
		String expandedUrl = urlShortenerServiceImpl.getExpandedURL(testDataProps.getProperty("shortUrlSuffix1"));
		Assert.assertEquals(testDataProps.getProperty("originalUrl1"), expandedUrl);

	}

	@Test
	public void testExpandUrlValid2NotInCache() throws Exception{

		PowerMockito.mockStatic(URLShortenerUtil.class);
		URLData urlData = new URLData();
		urlData.setOriginalUrl(testDataProps.getProperty("originalUrl2"));
		urlData.setShortUrl(testDataProps.getProperty("shortUrl2"));
		when(urlDataCache.isURLDataInCache(testDataProps.getProperty("shortUrlSuffix2"))).thenReturn(false);
		PowerMockito.when(URLShortenerUtil.convertNumberToDecimal(testDataProps.getProperty("shortUrlSuffix2"))).thenReturn(testDataProps.getProperty("urlIdGenerated2"));
		when(urlShortenerDAO.selectOriginalURLWithId(testDataProps.getProperty("urlIdGenerated2"))).thenReturn(testDataProps.getProperty("originalUrl2"));
		doNothing().when(urlDataCache).putURLDataToCache(anyString(), any(URLData.class));
		String expandedUrl = urlShortenerServiceImpl.getExpandedURL(testDataProps.getProperty("shortUrlSuffix2"));
		Assert.assertEquals(testDataProps.getProperty("originalUrl2"), expandedUrl);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testExpandUrlInvalidEmptyInput() {

		try {
			PowerMockito.mockStatic(URLShortenerUtil.class);
			when(urlDataCache.isURLDataInCache("")).thenReturn(false);
			PowerMockito.when(URLShortenerUtil.convertNumberToDecimal("")).thenThrow(IllegalArgumentException.class);
			urlShortenerServiceImpl.getExpandedURL("");
		}
		catch(Exception e) {
			Assert.assertEquals(IllegalArgumentException.class,e.getClass());
		}
	}









}
