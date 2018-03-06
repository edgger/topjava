<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<table>
    <tr>
        <th>Date/Time</th>
        <th>Description</th>
        <th>Calories</th>
        <th>Edit</th>
    </tr>
    <jsp:useBean id="mealsWithExceed" scope="request" type="java.util.List"/>
    <jsp:useBean id="dateTimeFormatter" scope="request" type="java.time.format.DateTimeFormatter"/>
    <c:forEach var="entry" items="${mealsWithExceed}">
        <tr ${entry.exceed ? 'bgcolor="#f08080"' : ''}>
        <td>${entry.dateTime.format(dateTimeFormatter)}</td>
        <td>${entry.description}</td>
        <td>${entry.calories}</td>
        <td></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
