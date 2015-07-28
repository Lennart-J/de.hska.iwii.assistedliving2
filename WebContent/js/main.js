/**
 * 
 */
var bundeslandImages = {
	37 : "bwb",
	38 : "bayern",
	39 : "berlin",
	40 : "brandenburg",
	41 : "bremen",
	42 : "hamburg",
	43 : "hessen",
	44 : "mecklenburg-vorpommern",
	45 : "niedersachsen",
	46 : "nrw",
	47 : "rheinland-pfalz",
	48 : "saarland",
	49 : "sachsen",
	50 : "sachsenanhalt",
	51 : "schleswigholstein",
	52 : "thüringen"
};

document.addEventListener("DOMContentLoaded", function(event) {
	var select = document.getElementById("bundesland");
	console.log(select);
	select.addEventListener("change", function() {
		var id = parseInt(select.value);
		console.log(id);
		var img = document.getElementById("map-img");
		img.src = img.src.replace(/[^\/]+(?=\/$|$)/, bundeslandImages[id]+ ".png");
	});
});

function init_canvas() {
	var canvas = document.getElementById("img-canvas");
	var context = canvas.getContext("2d");
	var width = canvas.width;
	var height = canvas.height;
}