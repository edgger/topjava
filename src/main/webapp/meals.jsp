<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<table border="2">
    <thead>
    <tr>
        <th>Date/Time</th>
        <th>Description</th>
        <th>Calories</th>
        <th>Action</th>
    </tr>
    </thead>
    <jsp:useBean id="mealsWithExceed" scope="request" type="java.util.List"/>
    <jsp:useBean id="dateTimeFormatter" scope="request" type="java.time.format.DateTimeFormatter"/>
    <c:forEach var="entry" items="${mealsWithExceed}">
        <tr ${entry.exceed ? 'bgcolor="#f08080"' : 'bgcolor="#7fffd4"'}>
            <td><c:out value="${entry.dateTime.format(dateTimeFormatter)}"/></td>
            <td><c:out value="${entry.description}"/></td>
            <td><c:out value="${entry.calories}"/></td>
            <td>
                <a href="<c:url value="/meals?action=edit&id=${entry.id}"/>">edit</a>
                <a href="<c:url value="/meals?action=delete&id=${entry.id}"/>">del</a>
            </td>
        </tr>
    </c:forEach>
</table>
<br>
<a href="<c:url value="meals?action=add"/>">Add</a>
</body>
</html>
