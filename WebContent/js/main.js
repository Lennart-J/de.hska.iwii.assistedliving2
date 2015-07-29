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

var canvas, context, select;
var coordinates = "0,0";


document.addEventListener("DOMContentLoaded", function(event) {
	select = document.getElementById("bundesland");
	console.log(select);
	select.addEventListener("change", function() {
		var id = parseInt(select.value);
		context.clearRect(0, 0, canvas.width, canvas.height);
		var img = document.getElementById("map-img");
		img.src = ctx + "/images/" + bundeslandImages[id]+ ".png";		
	});
	init_canvas();
	document.getElementById("reset").addEventListener("click", reset);
	document.getElementById("submit").addEventListener("click", submit);
});

function init_canvas() {
	var img = document.getElementById("map-img");
	img.src = ctx + "/images/bwb.png";
	canvas = document.getElementById("img-canvas");
	context = canvas.getContext("2d");
	console.log(canvas.offsetLeft, canvas.offsetTop);
	
	img.onload = function() {
		canvas.width = img.width;
		canvas.height = img.height;
		context.clearRect(0, 0, canvas.width, canvas.height);
	};

	canvas.addEventListener("click", function(e) {
//		var imageData = context.getImageData(0, 0, img.width, img.height);
//      console.log(imageData);
		var mouseX = getMousePos(canvas, e)["x"];
		var mouseY = getMousePos(canvas, e)["y"];
		var radius = 2;
		context.clearRect(0, 0, canvas.width, canvas.height);
		
		//set value of hidden input field to coordinates so they get passed to the server
		//document.getElementById("coordinates-input").value=mouseX + "," + mouseY;
		coordinates = mouseX + "," + mouseY;
		
		//draw circle
		context.beginPath();
		context.arc(mouseX, mouseY, radius, 0, 2 * Math.PI, false);
		context.fillStyle = 'red';
		context.fill();
		context.lineWidth = 1;
		context.strokeStyle = '#003300';
		context.stroke();
		
		function getMousePos(canvas, evt) {
	        var rect = canvas.getBoundingClientRect();
	        return {
	          x: evt.clientX - rect.left,
	          y: evt.clientY - rect.top
	        };
	      }
	});
}

function submit() {
	//var now = new Date(); 
	//var utc_date = now.getUTCFullYear() + "," + (now.getUTCMonth() + 1) + "," + now.getUTCDate() + "," + now.getUTCHours() + "," + now.getUTCMinutes();
	var now = Date.now();
	$.get('configure', 
			{bundesland: select.value, coordinates: coordinates, date: now}, 
			function(data) {
				console.log(data);
			}
	);
}

function reset() {
	var img = document.getElementById("map-img");
	img.src = ctx + "/images/bwb.png";
	context.clearRect(0, 0, canvas.width, canvas.height);
}