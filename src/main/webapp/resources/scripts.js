var map;

function map_init(latitude, longitude) {
	var mapOptions = {
		zoom : 13,
		center : new google.maps.LatLng(latitude, longitude)
	};
	map = new google.maps.Map(document.getElementById('map-gmap'), mapOptions);
}

function validateInput() {
	var ip4AddressInput = window.document.getElementById('ip4Address').value;

	var matcher = new RegExp(
			"^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\.)){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

	var testResult = matcher.test(ip4AddressInput);

	if (testResult == false) {
		showErrorWindow("Invalid IP address: " + ip4AddressInput);
	}

	return testResult;
}

function showErrorWindow(errorMessage) {
	window.document.getElementById('exception').style.display = "block";
	window.document.getElementById('exception-content').innerHTML = errorMessage;
}