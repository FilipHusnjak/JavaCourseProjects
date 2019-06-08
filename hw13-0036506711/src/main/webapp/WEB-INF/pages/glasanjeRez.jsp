<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Glasanje rezultati</title>
<style type="text/css">
	table.rez td {
		text-align: center;
	}
	body {
		background-color: ${pickedBgCol}
	}
</style>
</head>
<body>
	<h1>Rezultati glasanja</h1>
	<p>Ovo su rezultati glasanja.</p>
	<table border="1" class="rez">
		<thead>
			<tr>
				<th>Bend</th>
				<th>Broj glasova</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="v" items="${results}">
				<tr>
					<td>${v.key.name}</td>
					<td>${v.value}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<h2>Grafički prikaz rezultata</h2>
	<img alt="Pie-chart" src="glasanje-grafika" width="500" height="350" />

	<h2>Rezultati u XLS formatu</h2>
	<p>
		Rezultati u XLS formatu dostupni su <a href="glasanje-xls">ovdje</a>
	</p>
	<h2>Razno</h2>
	<p>Primjeri pjesama pobjedničkih bendova:</p>
	<ul>
		<c:forEach var="v" items="${winners}">
			<li><a href="${v.key.link}" target="_blank">${v.key.name}</a></li>
		</c:forEach>
	</ul>
</body>
</html>