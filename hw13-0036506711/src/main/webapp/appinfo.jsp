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
	<h1>Server up time</h1>
	<h2><% 
		long start = (Long) request.getServletContext().getAttribute("serverStartTime");
		long now = System.currentTimeMillis();
		long time = now - start;
		long millis = time % 1000;
		long seconds = (time / 1000) % 60;
		long minutes = (time / (1000 * 60)) % 60;
		long hours = (time / (1000 * 60 * 60)) % 24;
		long days = (time / (1000 * 60 * 60 * 24)) % 365;
		long years = (time / (1000 * 60 * 60 * 24 * 365));
		String result = "";
		out.write(String.format(
				"%d years, %d days, %d hours, %d minutes, %d seconds, %d miliseconds", 
				years, days, hours, minutes, seconds, millis));
	%></h2>
</body>
</html>