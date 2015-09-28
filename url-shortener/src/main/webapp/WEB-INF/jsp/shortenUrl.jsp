<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib  uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<title>Shorten Your URLs - Shortify</title>
<!-- CSS -->
<link rel="stylesheet" href="/css/style.css">

<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
            <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
</head>
<body>

	<div id="msgWrapper"></div>
	<div class="page-container">

		<br /> <span class="form-title">Shorten Your Url</span>
		<div class="form-container">
			<div id="url-shortener-form-container" class="inner-form-container">
				<form id="url-shortener-form" action="/url/shorten" method="post"
					enctype="application/x-www-form-urlencoded">
					<br /> <input type="text" id="longUrl" name="longUrl"
						placeholder="Paste your URL here" type="url"
						pattern="(http|ftp|https)://[\w-]+(\.[\w-]+)+([\w.,@?^=%&amp;:/~+#-]*[\w@?^=%&amp;/~+#-])?"
						required /> <span class="tooltip">Invalid, blank or
						already a short URL !</span>
					<button id="urlShortenButton" type="submit">Shorten URL</button>
					<br /> <br />
				</form>
			</div>
		</div>
	</div>

	<c:choose>

		<c:when test="${not empty requestScope.errorMessage}">

			<span class="errorMsg msgFromServer">${requestScope.errorMessage}</span>

		</c:when>

		<c:otherwise>
			<c:choose>
				<c:when test="${not empty requestScope.shortenedURL}">

					<c:set var="splitURL"
						value="${fn:split(requestScope.shortenedURL, '/')}" />
					<c:set var="lastPart" value="${splitURL[fn:length(splitURL)-1]}" />
					<span class="successMsg msgFromServer"> Your shortened URL
						generated is <a id="shortUrl" target="_blank"
						href="/url/navigate?shortUrl=${lastPart}">${requestScope.shortenedURL}</a>
					</span>

				</c:when>

			</c:choose>

		</c:otherwise>





	</c:choose>


	<!-- Javascript -->
	<script src="/js/jquery-1.10.2.min.js"></script>
	<script src="/js/scripts.js"></script>

</body>
</html>