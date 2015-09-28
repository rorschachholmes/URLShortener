package com.url.shortener.util;

import java.math.BigInteger;

/**
 * 
 * This class is the Util class for conversions
 * 
 * This class converts a decimal number to its base62 equivalent
 * and also converts a base62 number to its decimal equivalent
 * 
 * @author Shubham
 *
 */
public class URLShortenerUtil {

	// This is the alphabets in BASE62 format [a-z][A-Z][0-9]
	private static final String BASE_62_ALPHABETS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final BigInteger BASE_62 = new BigInteger("62");
	private static final String ERROR_MESSAGE = "Input is invalid";
	
	
	/**
	 * This method converts a decimal number to its base62 equivalent
	 * 
	 * @param decimalString
	 * @return base62 format equivalent
	 * @throws ArithmeticException
	 * @throws NumberFormatException
	 */
	public static String convertNumberToBase62(String decimalString) throws ArithmeticException,NumberFormatException {

		StringBuilder numberInBase62 = new StringBuilder();
		BigInteger numberInDecimal = new BigInteger(decimalString);

	
			while (!numberInDecimal.equals(BigInteger.ZERO))
			{
				numberInBase62.append(BASE_62_ALPHABETS.charAt((numberInDecimal.mod(BASE_62)).intValue()));
				numberInDecimal = numberInDecimal.divide(BASE_62);
			}

			return numberInBase62.reverse().toString();   
		
	}

	/**
	 * This method converts a base62 format number to its decimal equivalent
	 * @param number
	 * @return number in decimal format
	 * @throws ArithmeticException
	 * @throws IllegalArgumentException
	 */
	public static String convertNumberToDecimal(String number) throws ArithmeticException,IllegalArgumentException  {
		BigInteger numberInDecimal = new BigInteger("0");

		if(number!=null && !number.isEmpty()){

			int len = number.length();
			for (int i=0;i<len; i++)
			{
				numberInDecimal = ((numberInDecimal).multiply(BASE_62)).add(new BigInteger(Integer.toString(BASE_62_ALPHABETS.indexOf(number.charAt(i))))); 
			}


			return numberInDecimal.toString();
		}

		else {
			throw new IllegalArgumentException(ERROR_MESSAGE);
		}
	}

}
