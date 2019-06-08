<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Home</title>
<style>
	body {
		background-color: ${pickedBgCol}
	}
</style>
</head>
<body>
	<table border="1">
	<tr><th>Kut (stupnjevi)</th><th>Sinus</th><th>Kosinus</th></tr>
	<c:forEach var="v" items="${trigValues}">
		<tr><td>${v.angle}</td><td>${v.sin}</td><td>${v.cos}</td></tr>
	</c:forEach>
	</table>
</body>
</html>