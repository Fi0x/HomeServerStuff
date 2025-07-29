<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Available classes for race '${raceClasses.get(0).raceEventName}'</h1>
    <table class="table top-margin min-width">
        <thead>
        <tr class="underlined-row">
            <th class="align-text-center">Class</th>
            <th class="align-text-center">Should be loaded</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${raceClasses}" var="raceClass" varStatus="loop">
            <tr>
                <td class="align-text-center">${raceClass.className}</td>
                <td class="align-text-center"><input id="classSelection${loop.index}" type="checkbox"></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <a class="btn-success" onclick="loadClassResults(this)">Load results for selected classes</a>
    <div class="full-width" id="classResultTableParent"></div>
    <a id="saveSelectedButton" class="btn-success" style="display: none" onclick="saveSelectedResults(this)">Save
        selected results</a>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="../../js/sailing-functions.js"></script>
<script src="../../js/new-race-functions.js"></script>
<script>
    let baseUrl = "${pageContext.request.contextPath}/api"
    let baseUrlNormal = "${pageContext.request.contextPath}"
</script>
<script>
    let raceClasses = [
        <c:forEach items="${raceClasses}" var="raceClass" varStatus="loop">
        {
            raceEventName: "${raceClass.raceEventName}",
            className: "${raceClass.className}",
            classUrl: "${raceClass.classUrl}",
            startDate: ${raceClass.startDate},
            endDate: ${raceClass.endDate},
            eventUrl: "${raceClass.eventUrl}",
            eventId: "${raceClass.eventId}"
        }${loop.last ? '' : ','}
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