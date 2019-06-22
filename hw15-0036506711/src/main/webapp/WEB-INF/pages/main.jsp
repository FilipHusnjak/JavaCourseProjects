<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>Login</title>
		
		<style type="text/css">
		.greska {
		   font-family: fantasy;
		   font-weight: bold;
		   font-size: 0.9em;
		   color: #FF0000;
		   padding-left: 110px;
		}
		.formLabel {
		   display: inline-block;
		   width: 100px;
                   font-weight: bold;
		   text-align: right;
                   padding-right: 10px;
		}
		.formControls {
		  margin-top: 10px;
		}
		</style>
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
				<hr>
				<h1> Login </h1>
				<form action="main" method="post">
					<c:if test="${error}">
		 				<div class="greska">Wrong nickname or password!</div>
					</c:if>
		 			<div>
		  				<span class="formLabel">Nickname:</span><input type="text" value="${nick}" name="nickname" size="10">
		 			</div>
					<br>
		 			<div>
		  				<span class="formLabel">Password:</span><input type="password" name="password" size="10">
		 			</div>
		
					<div class="formControls">
		  				<span class="formLabel">&nbsp;</span>
		 				<input type="submit" name="method" value="Login">
					</div>
				</form>
			
				<hr>
			
				<a href="register">Create New Account</a>
			</c:otherwise>
		</c:choose>
		<hr>
		<a href="${pageContext.request.contextPath}/servleti/main">User list</a><br>
		<hr>
		<h1>List of created users:</h1>
		<ul>
			<c:forEach var="v" items="${users}">
				<li><h2><a href="author/${v.nick}"><c:out value="${v.nick}"/></a></h2></li>
			</c:forEach>
		</ul>
	</body>
</html>
