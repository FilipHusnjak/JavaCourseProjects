<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>
	Polls
</title>
</head>
<body>
	<h1>Ankete:</h1>
	<ul>
		<c:forEach var="v" items="${polls}">
			<li><a href="glasanje?pollID=${v.ID}">${v.title}</li>
		</c:forEach>
	</ul>
</body>
</html>