<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Sensors</h1>
    <label class="search-label">
        <input type="text" id="searchText" onkeyup="searchFunction()" class="search-input" placeholder="Search...">
    </label>
    <div>
        <c:forEach items="${tagList}" var="tag">
            <label class="filter-option" title="Will include sensors with tag '${tag}'">
                    ${tag}
                <input type="checkbox" onclick="updateFilterState()">
            </label>
        </c:forEach>
    </div>
    <table id="searchableTable" class="table sortable">
        <thead>
        <tr class="underlined-row">
            <th class="clickable">Name</th>
            <%--            <th class="clickable">Type</th>--%>
            <th class="clickable">Address</th>
            <th class="clickable">Value</th>
            <th class="clickable">Last Update</th>
            <th class="clickable">Tags</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${sensorList}" var="sensor">
            <tr class="clickable-row ${sensor.offline ? 'yellow' : (sensor.minValue > sensor.value || sensor.maxValue < sensor.value ? 'red' : '')}"
                title="${sensor.offline ? 'OFFLINE' : sensor.description}"
                onclick="window.location='${pageContext.request.contextPath}/sensor/${sensor.address}/${sensor.name}'">
                <td>${sensor.name}</td>
                    <%--                <td class="filter-tag">${sensor.type}</td>--%>
                <td>${sensor.address}</td>
                <td>${sensor.value}${sensor.unit}</td>
                <td>
                    <fmt:formatDate value="${sensor.lastUpdate}" pattern="dd.MM HH:mm:ss"/>
                </td>
                <td class="filter-tag">
                    <c:forEach items="${sensor.tags}" var="tag" varStatus="loop">
                        ${tag}${loop.last ? '': ','}
                    </c:forEach>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="${pageContext.request.contextPath}/js/data-functions.js"></script>
<script>
    onload = function () {
        loadNavBar();
    }
</script>
</body>
</html>