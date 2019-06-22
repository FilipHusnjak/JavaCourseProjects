<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>New Blog</title>
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
			New Blog
		</h1>

		<form action="" method="post">
		<div>
			<div>
		  		<span class="formLabel">Title:</span><input type="text" name="title" value='<c:out value="${formular.title}"/>' size="20">
		 	</div>
		 	<br>
		 	<c:if test="${formular.hasError('title')}">
		 		<div class="error"><c:out value="${formular.getError('title')}"/></div>
		 	</c:if>
		</div>
		
		<div>
		 	<div>
		  		<span class="formLabel">Text:</span>
		  		<textarea name="text" rows = "5" cols = "60" name = "description"><c:out value="${formular.title}"/></textarea>
		 	</div>
		 	<c:if test="${formular.hasError('text')}">
		 		<div class="error"><c:out value="${formular.getError('text')}"/></div>
		 	</c:if>
		</div>

		<div class="formControls">
		  	<span class="formLabel">&nbsp;</span>
		  	<input type="submit" name="method" value="Save">
		  	<input type="submit" name="method" value="Cancel">
		</div>
		</form>
	</body>
</html>
