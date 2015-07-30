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
var url = "";
var year, month, day, hours, minutes;
var test;

document.addEventListener("DOMContentLoaded", function(event) {
	test = $('#interval-input');
	console.log(test);
	select = document.getElementById("bundesland");
	console.log(select);
	select.addEventListener("change", function() {
		context.clearRect(0, 0, canvas.width, canvas.height);
		var img = document.getElementById("map-img");
		img.src = ctx + "/images/" + bundeslandImages[parseInt(select.value)]+ ".png";
		$('.thumbnail').css({visibility:"hidden"}).find('img').attr('src', '');
	});
	init_canvas();
	document.getElementById("reset").addEventListener("click", reset);
	document.getElementById("submit").addEventListener("click", submit);
	
	$('.thumbnail').on('mouseover', function() {
		$('#map-img').attr('src', $(this).find('img').attr('src'));
	});
	$('.thumbnail').on('mouseleave', function() {
		$('#map-img').attr('src', ctx + "/images/" + bundeslandImages[parseInt(select.value)]+ ".png");	
	});
	$('#interval-input').on('change', function() {
		if ($(this).val() < this.min) {
          $(this).val(this.min);
		}     
	});
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
		img.onload = null;
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
	var date;
	var dateInput = $('#date-input').val();
	var timeInput = $('#time-input').val();
	
	if (dateInput != '' && timeInput != '') {
		date = new Date(dateInput + " " + timeInput).getTime();
		//console.log(new Date(dateInput + " " + timeInput), date);
	} else {
		date = Date.now();
	}
	
	
	
	//calculate the interval in milliseconds from the input field
	var intervalArr = $('#interval-input').val().split(':');
	var intervalMiliseconds = parseInt(intervalArr[0]) * 3600000 + parseInt(intervalArr[1]) * 60000;
	var urls1 = constructUrls(date - 600000);
	var urls2 = constructUrls(date - (intervalMiliseconds + 600000));
	
	$('#first-thumb').attr('href', urls2[1]);
	$('#first-thumb img').attr('src', urls2[0]);
	$('#second-thumb').attr('href', urls1[1]);
	$('#second-thumb img').attr('src', urls1[0]);
	$('.thumbnail').css({visibility:"visible"});
	//Testbilder
	//"http://kachelmannwetter.com/images/data/cache/px250/px250_2015_06_27_37_0400.png"
	//"http://kachelmannwetter.com/images/data/cache/px250/px250_2015_06_27_37_0410.png"
	
	$.get('configure', 
			{bundesland: select.value, coordinates: coordinates, url1: urls1[1], url2: urls2[1]}, 
			function(data) {
				console.log("Data: ", data);
				$("#third-thumb img").attr("src","data:image/png;base64," + data["prog"]);
				$("#fourth-thumb img").attr("src","data:image/png;base64," + data["morphProg"]);
				var msg = "";
				if (data["raining"]) {
					msg = "Es regnet bald!"
					document.getElementById("alarm").play();
				} else {
					msg = "Es bleibt trocken."
				}
				$("#message").html(msg);
			}
	);
}

function constructUrls(date) {
	var urls = [];
	var baseDate = new Date(date);
	year = baseDate.getFullYear();
	month = baseDate.getMonth() + 1;
	if (month < 10) month = "0" + month;
	day = baseDate.getDate();
	if (day < 10) day = "0" + day;
	hours = Math.floor((date / (1000*60*60)) % 24);
	if (hours < 10) hours = "0" + hours;
	minutes = Math.floor((date / (1000*60)) % 60);
	minutes -= minutes % 5;
	console.log("baseDate: " + baseDate.getMinutes());
	if (minutes < 10) minutes = "0" + minutes;
	
	url = "http://kachelmannwetter.com/images/data/cache/px250/download_px250_";
	url += year + "_";
	url += month + "_";
	url += day + "_";
	url += parseInt(select.value) + "_";
	url += hours;
	url += minutes;
	url += ".png";
	
	urls.push(url);
	urls.push(url.replace(/download_/, ""));
	
	return urls;
}

function reset() {
	var img = document.getElementById("map-img");
	img.src = ctx + "/images/bwb.png";
	context.clearRect(0, 0, canvas.width, canvas.height);
	$('.thumbnail').css({visibility:"hidden"}).find('img').attr('src', '');
}