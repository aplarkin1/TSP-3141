setInterval(function() {
	$.post("/api/updateLocation",
		{
		"apiToken": 
		"lon": -88.564461,
		"lat":47.121231,
		"elev": 0
	})
}, 30000);