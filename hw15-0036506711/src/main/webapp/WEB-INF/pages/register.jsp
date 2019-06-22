<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>User</title>
		<style type="text/css">
		.error {
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
			</c:otherwise>
		</c:choose>
		
		<hr>
		
		<a href="${pageContext.request.contextPath}/servleti/main">User list</a><br>
		
		<hr>
		
		<h1>
			New Account
		</h1>

		<form action="register" method="post">
		<div>
		 	<div>
		  		<span class="formLabel">First Name</span><input type="text" name="firstName" value='<c:out value="${formular.firstName}"/>' size="20">
		 	</div>
		 	<c:if test="${formular.hasError('firstName')}">
		 		<div class="error"><c:out value="${formular.getError('firstName')}"/></div>
		 	</c:if>
		</div>
		<br>
		<div>
		 	<div>
		  		<span class="formLabel">Last Name</span><input type="text" name="lastName" value='<c:out value="${formular.lastName}"/>' size="20">
		 	</div>
		 	<c:if test="${formular.hasError('lastName')}">
		 		<div class="error"><c:out value="${formular.getError('lastName')}"/></div>
		 	</c:if>
		</div>
		<br>
		<div>
		 	<div>
		  		<span class="formLabel">Nickname</span><input type="text" name="nick" value='<c:out value="${formular.nick}"/>' size="20">
		 	</div>
		 	<c:if test="${formular.hasError('nick')}">
		 		<div class="error"><c:out value="${formular.getError('nick')}"/></div>
		 	</c:if>
		</div>
		<br>
		<div>
		 	<div>
		  		<span class="formLabel">Email</span><input type="text" name="email" value='<c:out value="${formular.email}"/>' size="20">
		 	</div>
		 	<c:if test="${formular.hasError('email')}">
		 		<div class="error"><c:out value="${formular.getError('email')}"/></div>
		 	</c:if>
		</div>
		<br>
		<div>
		 	<div>
		  		<span class="formLabel">Password</span><input type="password" name="password" size="20">
		 	</div>
		 	<c:if test="${formular.hasError('password')}">
		 		<div class="error"><c:out value="${formular.getError('password')}"/></div>
		 	</c:if>
		</div>
		<br>
		<div class="formControls">
		  	<span class="formLabel">&nbsp;</span>
		  	<input type="submit" name="method" value="Save">
		  	<input type="submit" name="method" value="Cancel">
		</div>
		</form>
	</body>
</html>
