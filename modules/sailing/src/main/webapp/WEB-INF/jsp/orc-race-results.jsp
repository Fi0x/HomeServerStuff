<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Race results</h1>
    <div>
        <input type="text" id="newRaceUrl" placeholder="http://...">
        <div class="btn" onclick="addRace()" title="Link to manage2sail result page">Add new race-results</div>
    </div>
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
            <th class="clickable" colspan="1">Gesamt</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${raceResults}" var="result">
            <tr>
                <td>${result.shipName}</td>
                <td>${result.skipper}</td>
                <td>${result.shipClass}</td>
                <c:forEach items="${races}" var="race">
                    <c:if test="${race.bufferRace}">
                        <td id="${result.shipName.replace(' ', '')}${result.skipper.replace(' ', '')}position${race.name.replace(' ', '')}"></td>
                        <td id="${result.shipName.replace(' ', '')}${result.skipper.replace(' ', '')}points${race.name.replace(' ', '')}"></td>
                    </c:if>
                </c:forEach>
                <td id="${result.shipName.replace(' ', '')}${result.skipper.replace(' ', '')}positionFinalRace"></td>
                <td id="${result.shipName.replace(' ', '')}${result.skipper.replace(' ', '')}pointsFinalRace"></td>
                <td id="${result.shipName.replace(' ', '')}${result.skipper.replace(' ', '')}pointsTotal"></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="../../js/sailing-functions.js"></script>
<script>
    let baseUrl = "${pageContext.request.contextPath}/api"
</script>
<script>
    let races = ${races};
    let raceResults = ${raceResults};
</script>
<script>
    onload = function () {
        loadNavBar();
        fillRaceResults();
    }
</script>
</body>
</html>