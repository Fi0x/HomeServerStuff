<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>${recipe.name}</h1>
    <form:form method="post" modelAttribute="recipe" action="/recipe/create">
        <table class="table">
            <tbody>
            <tr>
                <td>Name</td>
                <td>
                    <form:input path="name"/> minutes
                </td>
            </tr>
            <tr>
                <td>Rating</td>
                <td>
                    <label>
                            <%--TODO: Make this work--%>
                        <input type="range" value="rating" min="0" max="10" step="0.1"
                               onchange="document.getElementById('ratingText').innerText = value"/>
                        <span id="ratingText"></span>
                    </label>
                </td>
            </tr>
            <tr>
                <td>Time</td>
                <td>
                    <form:input path="time"/> minutes
                </td>
            </tr>
            <tr>
                <td class="align-top">Ingredients</td>
                <td>
                        <%--TODO: Transform this into a list that allows the user to also delete and add entries--%>
                    <form:input path="ingredients"/>
                </td>
            </tr>
            <tr>
                <td class="align-top">Tags</td>
                <td>
                        <%--TODO: Transform this into a list that allows the user to also delete and add entries--%>
                    <form:input path="tags"/>
                </td>
            </tr>
            <tr>
                <td class="align-top">Public</td>
                <td>
                        <%--TODO: Make this work (Check langauge-generator)--%>
                    <input type="checkbox" src="visible"/>
                </td>
            </tr>
            </tbody>
        </table>
        <input type="submit" class="btn-success"/>
    </form:form>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="${pageContext.request.contextPath}/js/functions.js"></script>
</body>
</html>