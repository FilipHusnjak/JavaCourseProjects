<%@page import="java.awt.Color"%>
<%@page import="java.util.Random"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Home</title>
<style>
	body {
		background-color: ${pickedBgCol};
		color: <% 
					Random random = new Random();
					int r = random.nextInt(255); 
					int g = random.nextInt(255); 
					int b = random.nextInt(255);
					int color = r << 16 | g << 8 | b;
					String hex = "#" + Integer.toHexString(color);
					out.write(hex);
				%>;
		text-align: center;
	}
</style>
</head>
<body>
	<h2>Software development process:</h2>
	<h3>I can't fix this</h3>
	<h3>Crisis of confidence</h3>
	<h3>Questions career</h3>
	<h3>Questions life</h3>
	<h3>Oh it was a typo, cool</h3>
</body>
</html>