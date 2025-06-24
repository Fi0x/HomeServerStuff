<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Race results</h1>
    <table id="searchableTable" class="table sortable">
        <thead>
        <tr class="underlined-row">
            <th class="clickable" colspan="1">Ship</th>
            <th class="clickable" colspan="1">Skipper</th>
            <th class="clickable" colspan="1">Class</th>
            <c:forEach items="${races}" var="race">
                <c:if test="${race.bufferRace}">
                    <th class="clickable" colspan="2">${race.name}</th>
                </c:if>
            </c:forEach>
            <%--            <th class="clickable" colspan="2">Drei Länder Cup</th>--%>
            <%--            <th class="clickable" colspan="2">Graf Zeppelin Regatta</th>--%>
            <%--            <th class="clickable" colspan="2">Rund Um</th>--%>
            <%--            <th class="clickable" colspan="2">West-Ost Regatta</th>--%>
            <%--            <th class="clickable" colspan="2">Altnauer TagNacht Regatta</th>--%>
            <%--            <th class="clickable" colspan="2">Blue Planet Flug Trophy</th>--%>
            <th class="clickable" colspan="2">Final Race</th>
            <th class="clickable" colspan="2">Gesamt</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${raceResults}" var="result">
            <tr>
                <td>${result.shipName}</td>
                <td>${result.skipper}</td>
                <td>${result.shipClass}</td>
                    <%--                TODO: Fill remaining columns--%>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="../../js/functions.js"></script>
<script>
    let baseUrl = "${pageContext.request.contextPath}/api"
</script>
<script>
    onload = function () {
        loadNavBar();
    }
</script>
</body>
</html>