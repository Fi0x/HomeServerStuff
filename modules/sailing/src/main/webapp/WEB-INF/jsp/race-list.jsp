<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Races</h1>
    <div class="top-margin bottom-margin">
        <input class="long-input vertical-align-center" type="text" id="newRaceUrl"
               placeholder="https://www.manage2sail.com/...">
        <div class="btn btn-success vertical-align-center" onclick="addRace()" title="Link to manage2sail result page">
            Add
            new race-results
        </div>
        <a class="btn-edit vertical-align-center" href="${pageContext.request.contextPath}/race/manual"
           title="Type in all the values for a race that is not on manage2sail">Add new Results manually
        </a>
    </div>
    <table id="racesTable" class="table sortable">
        <thead>
        <tr class="underlined-row">
            <th class="clickable" colspan="1">Race Name</th>
            <th class="clickable" colspan="1">Race Group</th>
            <th class="clickable" colspan="1">Date</th>
            <th class="clickable align-text-center" colspan="1">Crossable</th>
            <th class="clickable align-text-center" colspan="1">ORC</th>
            <th class="clickable align-text-center" colspan="1">Participants</th>
            <th class="clickable align-text-center" colspan="1">Score Modifier</th>
        </tr>
        </thead>
    </table>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="../../js/sailing-functions.js"></script>
<script src="../../js/race-list-functions.js"></script>
<script>
    let baseUrl = "${pageContext.request.contextPath}/api";
    let baseUrlNormal = "${pageContext.request.contextPath}";
</script>
<script>
    let races = [
        <c:forEach items="${races}" var="race" varStatus="loop">
        {
            raceName: "${race.name}",
            startDate: "<fmt:formatDate value="${race.startDate}" pattern="dd.MM.yyyy"/>",
            longDate: ${race.longDate},
            raceGroup: "${race.raceGroup}",
            scoreModifier: ${race.scoreModifier},
            orcRace: ${race.orcRace},
            bufferRace: ${race.bufferRace},
            participants: ${race.participants},
            url: "${race.url}"
        }${not loop.last ? ',' : ''}
        </c:forEach>
    ]
</script>
<script>
    onload = function () {
        loadNavBar();
        loadRaceList();
    }
</script>
</body>
</html>