<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>Blog Entries</title>
	</head>

	<body>
		<c:choose>
			<c:when test="${someoneLogged}">
				Logged user: <br>
				First name: <c:out value="${loggedUser.firstName}"/><br>
				Last name: <c:out value="${loggedUser.lastName}"/><br>
				<a href="${pageContext.request.contextPath}/servleti/logout">Logout</a><br>
				<a href="${pageContext.request.contextPath}/servleti/author/${loggedUser.nick}">Your blogs</a>
			</c:when>
			<c:otherwise>
				Not logged in!<br>
			</c:otherwise>
		</c:choose>
		<hr>
		<a href="${pageContext.request.contextPath}/servleti/main">User list</a><br>
		<hr>
		<h1>List of blogs of user: <c:out value="${user.nick}"/></h1>
		<ul>
			<c:forEach var="v" items="${entries}">
				<li><h2><a href="${user.nick}/${v.id}"><c:out value="${v.title}"/></a></h2></li>
			</c:forEach>
		</ul>
		<c:if test="${logged}">
			<hr>
			<h2><a href="${user.nick}/new">New Blog</a></h2><br>
		</c:if>
	</body>
</html>