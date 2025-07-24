<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>New Race</h1>
    <table class="centered top-margin bottom-margin">
        <tbody>
        <tr>
            <td>Race Name</td>
            <td><input id="raceName" placeholder="Rund Um"></td>
        </tr>
        <tr>
            <td>Race Group</td>
            <td><input id="raceGroup" placeholder="Orc Sportboote"></td>
        </tr>
        <tr>
            <td>Start Date</td>
            <td><input id="startDate" class="date-input" type="date"></td>
        </tr>
        <tr>
            <td>End Date</td>
            <td><input id="endDate" class="date-input" type="date"></td>
        </tr>
        <tr>
            <td>Crossable</td>
            <td><input id="bufferRace" checked type="checkbox"></td>
        </tr>
        <tr>
            <td>ORC</td>
            <td><input id="orcRace" type="checkbox"></td>
        </tr>
        <tr>
            <td>Participants</td>
            <td><input id="participants" type="number" value="0"></td>
        </tr>
        <tr>
            <td>Score Modifier</td>
            <td><input id="scoreModifier" type="number" value="1"></td>
        </tr>
        </tbody>
    </table>
    <a class="btn btn-success" onclick="saveNewRaceInfo()">Save Race Information</a>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="../../js/sailing-functions.js"></script>
<script src="../../js/manual-race-functions.js"></script>
<script>
    let baseUrl = "${pageContext.request.contextPath}/api";
    let baseUrlNormal = "${pageContext.request.contextPath}";
</script>
<script>
    onload = function () {
        loadNavBar();
        loadDateFields();
    }
</script>
</body>
</html>