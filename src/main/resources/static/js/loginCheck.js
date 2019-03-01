$("document").ready(function() {
	var logCheck = document.cookie.indexOf('apiToken=');
	console.log(logCheck);
	console.log(document.cookie);
	
	if (logCheck !== -1) {
		$("#logSquare").attr({
		      "href" : "/info",
		      "title" : "Account"
		    });
	}
});

