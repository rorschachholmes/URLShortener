$(document).ready(function(){

	$('div#msgWrapper').find('span#alreadyShortUrlMsg').remove();
	$("#url-shortener-form").submit(function(){
		var urlRegex = new RegExp("https?://(lnk\.[url]).*?(\s|$)");
		if(urlRegex.test($("#longUrl").val())){
			$('div#msgWrapper').append("<span id='alreadyShortUrlMsg' class='successMsg'>Already a shortened URL !</span>");
			$("#longUrl").val($("#longUrl").val());
			$("#alreadyShortUrlMsg").show();
			return false;
		}

		else {
			$('div#msgWrapper').find('span#alreadyShortUrlMsg').remove();
			return true;
		}

	})

});
