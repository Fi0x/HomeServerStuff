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
        <div class="btn vertical-align-center" onclick="addRace()" title="Link to manage2sail result page">Add new
            race-results
        </div>
    </div>
    <table id="searchableTable" class="table sortable">
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
        <tbody>
        <c:forEach items="${races}" var="race" varStatus="loop">
            <tr>
                <td><input id="raceName${loop.index}" value="${race.name}"></td>
                <td><input id="raceGroup${loop.index}" value="${race.raceGroup}"></td>
                <td><fmt:formatDate value="${race.startDate}" pattern="dd.MM.yyyy"/></td>
                <td class="align-text-center"><input id="raceBuffer${loop.index}" ${race.bufferRace ? 'checked': ''}
                                                     type="checkbox"></td>
                <td class="align-text-center"><input id="raceOrc${loop.index}" ${race.orcRace ? 'checked' : ''}
                                                     type="checkbox"></td>
                <td class="align-text-center">${race.participants}</td>
                <td class="align-text-center"><input id="raceScore${loop.index}" type="number"
                                                     value="${race.scoreModifier}"></td>
                <td class="align-content-center"><a id="deleteButton${loop.index}" class="btn-danger"
                                                    onclick="deleteRace(this)">Delete</a></td>
                <td class="align-content-center"><a class="btn-edit"
                                                    onclick="updateRace(${loop.index}, '${race.name}', '${race.longDate}', '${race.raceGroup}')">
                    Save Changes</a></td>
                <td><a class="btn"
                       onclick="reloadRace('${race.name}', '${race.longDate}', '${race.raceGroup}', `${race.url}`, this)">
                    Reload Results</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="../../js/sailing-functions.js"></script>
<script>
    let baseUrl = "${pageContext.request.contextPath}/api";
    let baseUrlNormal = "${pageContext.request.contextPath}";
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