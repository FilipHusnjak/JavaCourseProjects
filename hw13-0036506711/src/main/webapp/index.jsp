<%@ page contentType="text/html; charset=UTF-8"
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
	<h1>Home</h1>
	<hr>
	<h2><a href="colors.jsp">Background color chooser</a></h2>
	<hr>
	<h2>Calcultate trigonometric values:</h2>
	<form action="trigonometric" method="GET">
		Početni kut: <input type="number" name="a" min="0" max="360" step="1" value="0"><br>
		<br>
	    Završni kut: <input type="number" name="b" min="0" max="360" step="1" value="360"><br>
	    <br>
		<input type="submit" value="Tabeliraj"><input type="reset" value="Reset">
	</form>
	<hr>
	<h2><a href="appinfo.jsp">Server uptime</a></h2>
	<hr>
	<h2><a href="stories/funny.jsp">Funny story</a></h2>
	<hr>
	<h2><a href="report.jsp">Report</a></h2>
	<hr>
	<h2><a href="powers?a=1&b=100&n=3">Default Powers</a></h2>
	<h2>Custom Powers:</h2>
	<form action="powers" method="GET">
		Initial number: <input type="number" name="a" min="-100" max="100" step="1" value="1"><br>
		<br>
	    Final number: <input type="number" name="b" min="-100" max="100" step="1" value="100"><br>
	    <br>
	    Number of pages: <input type="number" name="n" min="1" max="5" step="1" value="3"><br>
	    <br>
		<input type="submit" value="Download"><input type="reset" value="Reset">
	</form>
	<hr>
	<h2><a href="glasanje">Voting app</a></h2>
</body>
</html>