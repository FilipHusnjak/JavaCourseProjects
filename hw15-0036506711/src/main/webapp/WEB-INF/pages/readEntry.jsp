<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>Blog</title>
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
		
		<h1>Blog: <c:out value="${entry.title}"/></h1>
		<h2>Creator: <c:out value="${entry.creator.nick}"/></h2>
		<h2>Created at: <c:out value="${entry.createdAt}"/></h2>
		<h2>Last modified at: <c:out value="${entry.lastModifiedAt}"/></h2>
		
		<hr>
		
		<h2>
			Text:
		</h2>
		<h4>
			<c:out value="${entry.text}"/>
		</h4>
		
		<hr>
		
		<h2> Comments:</h2>
		<ol>
		<c:forEach var="v" items="${comments}">
			<li>Message: <c:out value="${v.message}"/><br>
			Posted by: <c:out value="${v.usersEmail}"/><br>
			Posted on: <c:out value="${v.postedOn}"/></li>
			<br>
		</c:forEach>
		</ol>
		<hr>
		<h2>
			New comment:
		</h2>
		<form action="" method="post">
			<div>
				<div>
			  		<span class="formLabel">Message</span><input type="text" value="${formular.message}" name="message" size="50"/>
			 	</div>
			 	<c:if test="${formular.hasError('message')}">
		 			<div class="error"><c:out value="${formular.getError('message')}"/></div>
		 		</c:if>
			 	<c:if test="${!someoneLogged}">
			 		<br>
					<div>
			  			<span class="formLabel">Email</span><input type="text" value="${formular.email}" name="email" size="20"/>
			 		</div>
			 		<c:if test="${formular.hasError('email')}">
		 				<div class="error"><c:out value="${formular.getError('email')}"/></div>
		 			</c:if>
				</c:if>
				<div class="formControls">
			  		<span class="formLabel">&nbsp;</span>
			  		<input type="submit" name="method" value="Save">
				</div>
			</div>
		</form>
		<c:if test="${logged}">
			<hr>
			<a href="edit/${entry.id}">Edit blog</a>
		</c:if>
	</body>
</html>