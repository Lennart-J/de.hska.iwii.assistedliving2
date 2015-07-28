<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<title>Assisted Living 2</title>
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/normalize.css">
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/skeleton.css">
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
<script>var ctx = "${pageContext.request.contextPath}"</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/main.js"></script>

</head>
<body>
	<jsp:include page="/pages/header.jsp">
		<jsp:param name="content" value="book-form-content"/>
		<jsp:param name="title" value="Book Form"/>
	</jsp:include>
	<div class="main-wrapper">
		<div id="img-wrapper">
			<canvas id="img-canvas"></canvas>
			<img id ="map-img" src="${pageContext.request.contextPath}/images/bwb.png"/>
		</div>
		<div id="form-wrapper">
			<form action="configure" method="GET">
			<fieldset id="config-form">
			<legend>Konfiguration:</legend>
			<p>
				Bitte wählen sie per Mausklick einen Ort auf der Karte aus!
			</p>
			<label for="bundesland">Bundesland: </label>
			<select id="bundesland" name="bundesland">
				<option selected value="37">Baden-Würtemberg</option>
				<option value="38">Bayern</option>
				<option value="39">Berlin</option>
				<option value="40">Brandenburg</option>
				<option value="41">Bremen</option>
				<option value="42">Hambrg</option>
				<option value="43">Hessen</option>
				<option value="44">Mecklenburg-Vorpommern</option>
				<option value="45">Niedersachsen</option>
				<option value="46">Nordrhein-Westfalen</option>
				<option value="47">Rheinland-Pfalz</option>
				<option value="48">Saarland</option>
				<option value="49">Sachsen</option>
				<option value="50">Sachsenanhalt</option>
				<option value="51">Schleswig-Holstein</option>
				<option value="52">Thüringen</option>
			</select>
			<input id="coordinates-input" type="hidden" name="coordinates" value="1,1">
			<div id="submit-wrapper">
				<input id="submit" type="submit" name="submit">
				<input id ="reset" type="reset" name="reset">
			</div>
			
			</fieldset>
		</form>
		</div>
		
	</div>
<jsp:include page="/pages/footer.jsp">
	<jsp:param name="content" value="book-form-content"/>
	<jsp:param name="title" value="Book Form"/>
</jsp:include>
</body>
</html>