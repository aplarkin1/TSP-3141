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
		 $("head").append('<link href="https://stackpath.bootstrapcdn.com/bootswatch/4.3.1/cyborg/bootstrap.min.css" rel="stylesheet" integrity="sha384-mtS696VnV9qeIoC8w/PrPoRzJ5gwydRVn0oQ9b+RJOPxE1Z1jXuuJcyeNxvNZhdx" crossorigin="anonymous">'); 
	 } else if(theme == "enhanced"){
		 $("#cssTheme").remove();
		 $("#enhancedStyles").remove();
		 $("head").append('<link href="https://stackpath.bootstrapcdn.com/bootswatch/4.3.1/cosmo/bootstrap.min.css" rel="stylesheet" integrity="sha384-uhut8PejFZO8994oEgm/ZfAv0mW1/b83nczZzSwElbeILxwkN491YQXsCFTE6+nx" crossorigin="anonymous">'); 
		 $("head").append('<link id="enhancedStyles" rel="stylesheet" type="text/css" href="/css/enhanced.css">')
	 } else {
		 Cookies.set('theme', 'enhanced');
	 }
}
