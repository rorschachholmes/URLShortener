/**
 * 
 * This is the Unit Test Suite for the URL Shortener Utility
 * 
 */

package com.url.shortener.util.test;

import java.io.InputStream;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.url.shortener.util.URLShortenerUtil;




/**
 * Test Class for URL Shortener Utility
 * 
 * @author Shubham
 *
 */

@SuppressWarnings("deprecation")
public class URLShortenerUtilTest {

	private Properties testDataProps = new Properties();
	private InputStream propInputStream;
	private static final String TEST_DATA_PROPS_FILE_NAME="testData.properties";
	private static final String ERROR_MESSAGE = "Input is invalid";

	@Before
	public void setUp() throws Exception{

		propInputStream = URLShortenerUtil.class.getClassLoader().getResourceAsStream(TEST_DATA_PROPS_FILE_NAME);
		testDataProps.load(propInputStream);

	}



	@Test
	public void testConversionToBase62Valid() throws Exception{

		String numberInBase62 = URLShortenerUtil.convertNumberToBase62(testDataProps.getProperty("urlIdGenerated1"));
		Assert.assertNotNull(numberInBase62);
		Assert.assertEquals(testDataProps.getProperty("shortUrlSuffix1"), numberInBase62);

	}


	@Test
	public void testConversionToDecimalValid() throws Exception{

		String base62Input = testDataProps.getProperty("shortUrlSuffix2");
		String numberInDecimal = URLShortenerUtil.convertNumberToDecimal(base62Input);
		Assert.assertNotNull(numberInDecimal);
		Assert.assertEquals(testDataProps.getProperty("urlIdGenerated2"), numberInDecimal);

	}


	@Test
	public void testConversiontoBase62Invalid1(){

		try {
			URLShortenerUtil.convertNumberToBase62("");
		}
		catch(Exception e) {
			Assert.assertEquals(NumberFormatException.class, e.getClass());
		}

	}


	@Test
	public void testConversiontoBase62Invalid2(){

		try {
			URLShortenerUtil.convertNumberToBase62(null);
		}
		catch(Exception e) {
			Assert.assertEquals(NullPointerException.class, e.getClass());
		}

	}

	@Test
	public void testConversiontoBase62Invalid3(){

		try {
			String decimalString = "ABCDE";
			URLShortenerUtil.convertNumberToBase62(decimalString);
		}
		catch(Exception e) {
			Assert.assertEquals(NumberFormatException.class, e.getClass());
		}

	}
	
	@Test
	public void testConversiontoDecimalInvalid1() throws Exception{

	
		try {
			URLShortenerUtil.convertNumberToDecimal("");
		}
		catch(Exception e) {
			Assert.assertEquals(IllegalArgumentException.class, e.getClass());
			Assert.assertEquals(ERROR_MESSAGE, e.getMessage());
		}

	}
	
	@Test
	public void testConversiontoDecimalInvalid2() throws Exception{

	
		try {
			URLShortenerUtil.convertNumberToDecimal(null);
		}
		catch(Exception e) {
			Assert.assertEquals(IllegalArgumentException.class, e.getClass());
			Assert.assertEquals(ERROR_MESSAGE, e.getMessage());
		}

	}


}
