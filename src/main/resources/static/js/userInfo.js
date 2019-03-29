$("document").ready(function() {
	$.get("api/getAccountInformation", function(data, status)
	{
		if (this.status == 200){
			var obj = JSON.parse(data);
		}
	});
});