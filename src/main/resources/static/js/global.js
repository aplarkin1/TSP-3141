$("document").ready(function() {
	var long = 0;
	var lat = 0;
	var token = document.cookie;
	var logCheck = document.cookie.indexOf('apiToken=');

	function getGeo() {
		function success(position) {
			long = position.coords.longitude;
			lat = position.coords.latitude;
		}
		
		function error() {
			console.log("unable to get location");
		}
		
		if (navigator.geolocation) {
			console.log("getting location");
			navigator.geolocation.getCurrentPosition(success, error);
		} else {
			console.log("Browser is blocking location");
		}
	}

	if (logCheck !== -1) {
		setInterval(function() {
			getGeo();
			$.post("/api/updateLocation",
				{
				"apiToken": token,
				"lon": long,
				"lat": lat,
				"elev": 0
			});
		}, 30000);
	} else {
		console.log("not logged in");
	}
});
