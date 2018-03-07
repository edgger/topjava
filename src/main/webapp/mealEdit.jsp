<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<jsp:useBean id="dateTimeFormatter" scope="request" type="java.time.format.DateTimeFormatter"/>
<form name="mealEdit" action="<c:url value="/meals"/>" method="post">
    <input type="hidden" name="id" value="<c:out value="${meal.id}"/>">
    <label><b>Date|Time</b>
        <input name="dateTime" type="text" value="<c:out value="${meal.dateTime.format(dateTimeFormatter)}"/>" >
    </label>
    <label><b>Description</b>
        <input name="description" type="text" value="<c:out value="${meal.description}"/>">
    </label>
    <label><b>Calories</b>
        <input name="calories" type="text" value="<c:out value="${meal.calories}"/>">
    </label>
    <br>
    <button type="submit">Save</button>
    <a href="meals">Cancel</a>
</form>
</body>
</html>
