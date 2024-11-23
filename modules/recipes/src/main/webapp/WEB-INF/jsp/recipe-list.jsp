<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Saved recipes</h1>
    <label class="search-label">
        <input type="text" id="searchText" onkeyup="searchFunction()" class="search-input" placeholder="Search...">
    </label>
    <div>
        <label class="filter-option" title="Will also include public recipes from other users">
            Show all public recipes
            <input type="checkbox" onclick="updateFilterState()">
        </label>
        <label class="filter-option" title="Will hide your private recipes">
            Show only public recipes
            <input type="checkbox" onclick="updateFilterState()">
        </label>
        <label class="filter-option" title="Will show only your recipes that are private">
            Show only private recipes
            <input type="checkbox" onclick="updateFilterState()">
        </label>
    </div>
    <table id="searchableTable" class="table sortable">
        <thead>
        <tr class="underlined-row">
            <th class="clickable">Recipe</th>
            <th class="clickable">Ingredients</th>
            <th class="clickable">Rating</th>
            <th class="clickable">Time</th>
            <th class="clickable">Tags</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${recipeList}" var="recipe">
            <tr class="clickable-row"
                onclick="window.location='${pageContext.request.contextPath}/recipe/${recipe.id}'">
                <td>${recipe.name}</td>
                <td>${recipe.ingredients}</td>
                <td>${recipe.rating}</td>
                <td>${recipe.time} min</td>
                <td>${recipe.tags}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="${pageContext.request.contextPath}/js/functions.js"></script>
</body>
</html>