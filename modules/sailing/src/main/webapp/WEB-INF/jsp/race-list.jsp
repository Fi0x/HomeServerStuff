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
        <c:forEach items="${races}" var="race">
            <tr>
                <td><input value="${race.name}"></td>
                <td><input value="${race.raceGroup}"></td>
                <td><fmt:formatDate value="${race.startDate}" pattern="dd.MM.yyyy"/></td>
                <td class="align-text-center"><input ${race.bufferRace ? 'checked': ''} type="checkbox"></td>
                <td class="align-text-center"><input ${race.orcRace ? 'checked' : ''} type="checkbox"></td>
                <td class="align-text-center">${race.participants}</td>
                <td class="align-text-center">${race.scoreModifier}</td>
                <td class="align-content-center"><a class="btn-danger" onclick="deleteRace(this)">Delete</a></td>
                <td class="align-content-center"><a class="btn-edit"
                                                    onclick="updateRace(this, '${race.name}', '${race.startDate}', '${race.raceGroup}')">
                    Save Changes</a></td>
                <td><a class="btn" onclick="reloadRace('${race.name}', '${race.startDate}', '${race.raceGroup}')">
                    Reload Results</a></td>
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