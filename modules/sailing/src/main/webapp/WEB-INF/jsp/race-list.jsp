<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Races</h1>
    <div>
        <%--        TODO: Make input field longer and add margins--%>
        <input type="text" id="newRaceUrl" placeholder="https://www.manage2sail.com/...">
        <div class="btn" onclick="addRace()" title="Link to manage2sail result page">Add new race-results</div>
    </div>
    <table id="searchableTable" class="table sortable">
        <thead>
        <tr class="underlined-row">
            <th class="clickable" colspan="1">Race Name</th>
            <th class="clickable" colspan="1">Race Group</th>
            <th class="clickable" colspan="1">Date</th>
            <th class="clickable" colspan="1">Crossable</th>
            <th class="clickable" colspan="1">ORC</th>
            <th class="clickable" colspan="1">Participants</th>
            <th class="clickable" colspan="1">Score Modifier</th>
            <%--            TODO: Add row with button to remove a race from database--%>
            <%--            TODO: Add row with button to reload results for that race--%>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${races}" var="race">
            <tr>
                <td>${race.name}</td>
                <td>${race.raceGroup}</td>
                <td>${race.startDate}</td>
                <td>${race.bufferRace}</td>
                <td>${race.orcRace}</td>
                <td>${race.participants}</td>
                <td>${race.scoreModifier}</td>
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
    let races = [
        <c:forEach items="${races}" var="race" varStatus="loop">
        {
            raceName: "${race.name}",
            raceGroup: "${race.raceGroup}",
            scoreModifier: ${race.scoreModifier},
            orcRace: ${race.orcRace},
            bufferRace: ${race.bufferRace},
            participants: ${race.participants}
        }${not loop.last ? ',' : ''}
        </c:forEach>
    ]
</script>
<script>
    onload = function () {
        loadNavBar();
    }
</script>
</body>
</html>