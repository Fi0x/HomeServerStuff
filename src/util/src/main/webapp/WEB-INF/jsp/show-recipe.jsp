<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>${recipe.name}</h1>
    <table class="table">
        <tbody>
        <tr>
            <td>Rating</td>
            <td>${recipe.rating}</td>
        </tr>
        <tr>
            <td>Time</td>
            <td>${recipe.time} minutes</td>
        </tr>
        <tr>
            <td class="align-top">Ingredients</td>
            <td>
                <c:forEach items="${recipe.ingredients}" var="ingredient">
                    <p>${ingredient}</p>
                </c:forEach>
            </td>
        </tr>
        <tr>
            <td class="align-top">Tags</td>
            <td>
                <c:forEach items="${recipe.tags}" var="tag">
                    <p>${tag}</p>
                </c:forEach>
            </td>
        </tr>
        </tbody>
    </table>
    <a href="${pageContext.request.contextPath}/recipe/${recipe.id}/edit" class="btn">Edit Recipe</a>
    <a href="${pageContext.request.contextPath}/recipe/${recipe.id}/delete" class="btn">Delete Recipe</a>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="${pageContext.request.contextPath}/js/functions.js"></script>
</body>
</html>