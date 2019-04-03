$("document").ready(function() {
	var long = 0;
	var lat = 0;

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
	getGeo();
	setInterval(function() {
		getGeo();

			$.post("/api/updateLocation",
				{
				"lon": long,
				"lat": lat,
				"elev": 0
				});
		
	}, 5000);
});
