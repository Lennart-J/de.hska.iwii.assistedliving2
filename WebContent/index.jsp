<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<title>Assisted Living 2</title>
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/normalize.css">
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/skeleton.css">
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
	<jsp:include page="/pages/header.jsp">
		<jsp:param name="content" value="book-form-content"/>
		<jsp:param name="title" value="Book Form"/>
	</jsp:include>
<h2>Willkommen</h2>
<br>
<p>
Dies ist das Projekt, welches im Rahmen vom Autonome Systeme Labor im Sommersemester 2015 von Janina Hüther, Simon Bächle und Lennart Jarms durchgeführt wurde. Die Aufgabenstellung war folgende:
</p>
Visualisierung der Regenwahrscheinlichkeit: Das Wetter ist wechselhaft und die Wäsche draußen zum Trocknen aufgehängt. Eine Regenwarnung so rechtzeitig, dass noch "im Trockenen" abgehängt werden kann, wäre doch angenehm. Dazu analysieren Sie den Regenradar-Film, den ein Wetterdienst zur Verfügung stellt. Sie analysieren die Bewegung von Regenfronten, um die Zeit bis zum Eintreffen am Standort vorauszusagen und geben einen akustischen Alarm 10 Minuten vor Eintreffen der Regenfront.
<p>
<a href="http://localhost:8080/de.hska.iwii.assistedliving2/main.jsp">Weiter zur Applikation...</a>
</p>
<jsp:include page="/pages/footer.jsp">
	<jsp:param name="content" value="book-form-content"/>
	<jsp:param name="title" value="Book Form"/>
</jsp:include>
</body>
</html>