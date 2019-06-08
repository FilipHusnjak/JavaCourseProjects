<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>${poll.title}</title>
<style>
	body {
		background-color: ${pickedBgCol}
	}
</style>
</head>
<body>
	<h1>${poll.title}</h1>
 	<p>${poll.message}</p>
	<ol>
	<c:forEach var="v" items="${pollOptions}">
		<li><a href="glasanje-glasaj?id=${v.ID}&pollID=${pollID}">${v.title}</a></li>
	</c:forEach>
	</ol>
</body>
</html>