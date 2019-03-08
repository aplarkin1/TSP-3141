var coord = {lat: 0, lng: 0};
var long;
var lati;

function geoloc() {
	function success(position) {
		coord = {lat: position.coords.latitude, lng: position.coords.longitude};
	}
	
	function error() {
		console.log("unable to get location");
	}
	
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(success, error);
		return coord;
	} else {
		console.log("Browser is blocking location");
	}
}
