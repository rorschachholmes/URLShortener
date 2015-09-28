package com.url.shortener.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.url.shortener.service.URLShortenerService;

/**
 * Main Controller class for handling url shortening and expanding requests
 * @author Shubham
 *
 */
@Controller
public class URLShortenerController {


	@Autowired
	@Qualifier("urlShortenerService")
	private URLShortenerService urlShortenerService;

	@RequestMapping(method=RequestMethod.GET, value="/")
	public String getHomePage(){

		return "shortenUrl";
	}

	public static final String ERROR_MESSAGE = "Oops, error occurred !";

	
	/**
	 * Controller method that is mapped to requests for shortening urls
	 * 
	 * @param request
	 * @param response
	 * @return the shortened URL string back to the view for success
	 * @return the error view for exceptions
	 * 
	 */
	@RequestMapping(method=RequestMethod.POST, value="/url/shorten")
	public String shortenURL(HttpServletRequest request, HttpServletResponse response) {

		String shortenedURL = null;
		try {
			String originalURL = request.getParameter("longUrl");
			shortenedURL = urlShortenerService.getShortenedURL(originalURL);
			if(shortenedURL!=null) {
				request.setAttribute("shortenedURL", shortenedURL);	
			}
			else {
				request.setAttribute("errorMessage", ERROR_MESSAGE);
			}
			return "shortenUrl";
		}

		catch(Exception e) {

			request.setAttribute("errorMessage", ERROR_MESSAGE);
			return "shortenUrl";

		}
	}

	
	/**
	 * Controller method that is mapped to requests for expandin short urls
	 * to original urls
	 * 
	 * @param request
	 * @param response
	 * @return the redirect view to the original url for success
	 * @return the error view for exceptions
	 * 
	 */
	@RequestMapping(method=RequestMethod.GET, value= "/url/navigate")
	public String expandURL(@QueryParam("shortUrl") String shortUrl, HttpServletRequest request, HttpServletResponse response){

		try {

			if(shortUrl!=null && !shortUrl.isEmpty()) {
				String originalUrl = urlShortenerService.getExpandedURL(shortUrl);
				if(originalUrl!=null){
					return "redirect:"+originalUrl;
				}
				else{
					request.setAttribute("errorMessage", ERROR_MESSAGE);
					return "shortenUrl";
				}
			}
			else{
				request.setAttribute("errorMessage", ERROR_MESSAGE);
				return "shortenUrl";
			}
		}
		catch(Exception e){
			request.setAttribute("errorMessage", ERROR_MESSAGE);
			return "shortenUrl";

		}

	}

}
