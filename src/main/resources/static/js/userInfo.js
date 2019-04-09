$("document").ready(function() {
	$.get("api/getAccountInformation", function(data, status)
	{
		if (this.status == 200){
			var obj = JSON.parse(data);
		}
	});
	
	$('#appThemeSelector').change(function() {
		 console.log(this.value)
		 if(this.value == "light"){
			 Cookies.set('theme', 'light');
		 } else if(this.value == "dark"){
			 Cookies.set('theme', 'dark');
		 } else if(this.value == "enhanced"){
			 Cookies.set('theme', 'enhanced');
		 }
		 changeTheme();
	});
	
});