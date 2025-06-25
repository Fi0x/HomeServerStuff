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
        <input type="text" id="newRaceUrl" placeholder="https://www.manage2sail.com/...">
        <div class="btn" onclick="addRace()" title="Link to manage2sail result page">Add new race-results</div>
    </div>
    <table id="searchableTable" class="table sortable">
        <thead>
        <tr class="underlined-row">
            <th class="clickable" colspan="1">Position</th>
            <th class="clickable left-line" colspan="1">Ship</th>
            <th class="clickable" colspan="1">Skipper</th>
            <th class="clickable" colspan="1">Class</th>
            <c:forEach items="${races}" var="race">
                <th class="clickable left-line" colspan="2">${race.name}</th>
            </c:forEach>
            <th class="clickable left-line" colspan="1">Gesamt</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${raceResults}" var="result">
            <tr>
                <td id="${result.shipName.replace(' ', '')}${result.skipper.replace(' ', '')}positionOverall"></td>
                <td class="left-line">${result.shipName}</td>
                <td>${result.skipper}</td>
                <td>${result.shipClass}</td>
                <c:forEach items="${races}" var="race">
                    <td id="${result.shipName.replace(' ', '')}${result.skipper.replace(' ', '')}position${race.name.replace(' ', '')}${race.raceGroup.replace(' ', '')}"
                        class="left-line"></td>
                    <td id="${result.shipName.replace(' ', '')}${result.skipper.replace(' ', '')}points${race.name.replace(' ', '')}${race.raceGroup.replace(' ', '')}"></td>
                </c:forEach>
                <td id="${result.shipName.replace(' ', '')}${result.skipper.replace(' ', '')}pointsTotal"
                    class="left-line"></td>
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
    let raceResults = [
        <c:forEach items="${raceResults}" var="raceResult" varStatus="loop">
        {
            shipName: "${raceResult.shipName}",
            skipper: "${raceResult.skipper}",
            shipClass: "${raceResult.shipClass}",
            singleRaceResults: [
                <c:forEach items="${raceResult.raceResults}" var="singleResult" varStatus="innerLoop">
                {
                    raceName: "${singleResult.name}",
                    position: ${singleResult.position},
                    score: ${singleResult.score},
                    raceGroup: "${singleResult.raceGroup}",
                    crossed: ${singleResult.crossed}
                }${not innerLoop.last ? ',' : ''}
                </c:forEach>
            ]
        }${not loop.last ? ',' : ''}
        </c:forEach>
    ];
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
        fillRaceResults();
    }
</script>
</body>
</html>