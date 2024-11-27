<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>${recipe.name}</h1>
    <%--@elvariable id="recipe" type="io.github.fi0x.recipes.logic.dto.RecipeDto"--%>
    <form:form method="post" modelAttribute="recipe" action="/recipe/create">
        <form:hidden path="id"/>
        <form:hidden path="username"/>
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
                        <form:input path="rating" type="range" min="0" max="10" step="0.1"
                                    onmousemove="updateSlider(this)"/>
                        <span id="ratingText">${recipe.rating}</span>
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
                    <c:choose>
                        <c:when test="${recipe.ingredients.size() > 0}">
                            <c:forEach begin="0" end="${recipe.ingredients.size() - 1}" varStatus="loop">
                                <p>
                                    <form:input cssClass="long-input" path="ingredients[${loop['index']}]"/>
                                    <button class="btn-danger round-button" onclick="deleteElement()">X</button>
                                </p>
                            </c:forEach>
                        </c:when>
                    </c:choose>
                    <p>
                        <button class="btn-success" onclick="addElement()">Add</button>
                    </p>
                </td>
            </tr>
            <tr>
                <td class="align-top">Tags</td>
                <td>
                    <c:choose>
                        <c:when test="${recipe.tags.size() > 0}">
                            <c:forEach begin="0" end="${recipe.tags.size() - 1}" varStatus="loop">
                                <p>
                                    <form:input cssClass="long-input" path="tags[${loop['index']}]"/>
                                    <button class="btn-danger round-button" onclick="deleteElement()">X</button>
                                </p>
                            </c:forEach>
                        </c:when>
                    </c:choose>
                    <p>
                        <button class="btn-success" onclick="addElement()">Add</button>
                    </p>
                </td>
            </tr>
            <tr>
                <td class="align-top">Public</td>
                <td>
                    <form:checkbox path="visible"/>
                </td>
            </tr>
            </tbody>
        </table>
        <input type="submit" class="btn-success"/>
    </form:form>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="${pageContext.request.contextPath}/js/recipe-functions.js"></script>
<script>
    function updateSlider(slider) {
        document.getElementById('ratingText').innerText = slider.value;
    }
</script>
</body>
</html>