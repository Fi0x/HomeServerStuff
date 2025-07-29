<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Edit results for '${raceInfo.name}'</h1>
    <table class="table">
        <thead>
        <tr>
            <th>Ship</th>
            <th>Skipper</th>
            <th>Ship Class</th>
            <th>Race Group</th>
            <th>Position</th>
        </tr>
        </thead>
        <tbody id="resultTableBody">
        <c:forEach items="${raceResults}" var="raceResult">
            <tr>
                <td><input value="${raceResult.shipName}"></td>
                <td><input value="${raceResult.skipper}"></td>
                <td><input value="${raceResult.shipClass}"></td>
                <td><input value="${raceResult.raceGroup}"></td>
                <td><input type="number" value="${raceResult.position}"></td>
                <td><a class="btn btn-danger"
                       onclick="deleteResult(${raceInfo.name}, ${raceInfo.startDate}, ${raceResult.raceGroup}, ${raceResult.skipper}, ${raceResult.shipName}, this, true)">Delete</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <a class="btn btn-success" onclick="createNewResultRow()">Add new result</a>
    <a class="btn btn-success" onclick="saveModifiedResults()">Save Changes</a>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="../../../../js/sailing-functions.js"></script>
<script src="../../../../js/manual-race-functions.js"></script>
<script>
    let baseUrl = "${pageContext.request.contextPath}/api";
    let baseUrlNormal = "${pageContext.request.contextPath}";
</script>
<script>
    let raceInfo = {
        name: "${raceInfo.name}",
        startDate: "${raceInfo.startDate}",
        raceGroup: "${raceInfo.raceGroup}",
        endDate: "${raceInfo.endDate}",
        scoreModifier: "${raceInfo.scoreModifier}",
        url: "${raceInfo.url}",
        orcRace: "${raceInfo.orcRace}",
        bufferRace: "${raceInfo.bufferRace}",
        participants: "${raceInfo.participants}"
    }
</script>
<script>
    onload = function () {
        loadNavBar();
    }
</script>
</body>
</html>