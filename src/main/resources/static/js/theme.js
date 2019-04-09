$("document").ready(function() {
	changeTheme()
});

function changeTheme(){
	var theme = Cookies.get("theme");
	 if(theme == "light"){
		 $("#cssTheme").remove();
		 $("#enhancedStyles").remove();
		 $("head").append('<link id="cssTheme" href="https://stackpath.bootstrapcdn.com/bootswatch/4.3.1/lumen/bootstrap.min.css" rel="stylesheet" integrity="sha384-iqcNtN3rj6Y1HX/R0a3zu3ngmbdwEa9qQGHdkXwSRoiE+Gj71p0UNDSm99LcXiXV" crossorigin="anonymous">'); 
	 } else if(theme == "dark"){
		 $("#cssTheme").remove();
		 $("#enhancedStyles").remove();
		 $("head").append('<link href="https://stackpath.bootstrapcdn.com/bootswatch/4.3.1/slate/bootstrap.min.css" rel="stylesheet" integrity="sha384-FBPbZPVh+7ks5JJ70RJmIaqyGnvMbeJ5JQfEbW0Ac6ErfvEg9yG56JQJuMNptWsH" crossorigin="anonymous">'); 
	 } else if(theme == "enhanced"){
		 $("#cssTheme").remove();
		 $("#enhancedStyles").remove();
		 $("head").append('<link href="https://stackpath.bootstrapcdn.com/bootswatch/4.3.1/flatly/bootstrap.min.css" rel="stylesheet" integrity="sha384-T5jhQKMh96HMkXwqVMSjF3CmLcL1nT9//tCqu9By5XSdj7CwR0r+F3LTzUdfkkQf" crossorigin="anonymous">'); 
		 $("head").append('<link id="enhancedStyles" rel="stylesheet" type="text/css" href="/css/enhanced.css">')
	 } else {
		 Cookies.set('theme', 'enhanced');
		 $("#cssTheme").remove();
		 $("#enhancedStyles").remove();
		 $("head").append('<link href="https://stackpath.bootstrapcdn.com/bootswatch/4.3.1/flatly/bootstrap.min.css" rel="stylesheet" integrity="sha384-T5jhQKMh96HMkXwqVMSjF3CmLcL1nT9//tCqu9By5XSdj7CwR0r+F3LTzUdfkkQf" crossorigin="anonymous">'); 
		 $("head").append('<link id="enhancedStyles" rel="stylesheet" type="text/css" href="/css/enhanced.css">')
	 }
}
