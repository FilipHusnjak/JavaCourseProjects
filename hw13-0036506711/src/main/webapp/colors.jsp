<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true" %>
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
	<h1>Choose Color</h1>
	<ul>
	<li><a href="setcolor?bgcolor=white">WHITE</a></li>
	<li><a href="setcolor?bgcolor=red">RED</a></li>
	<li><a href="setcolor?bgcolor=green">GREEN</a></li>
	<li><a href="setcolor?bgcolor=cyan">CYAN</a></li>
	</ul>
</body>
</html>