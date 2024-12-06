<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>${sensor.name} (${sensor.address})</h1>
    <p>${sensor.description}</p>
    <canvas id="dataChart"></canvas>
    <table id="searchableTable" class="table sortable">
        <thead>
        <tr class="underlined-row">
            <th class="clickable">Measurement</th>
            <th class="clickable">Time</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${data}" var="datapoint">
            <tr>
                <td>${datapoint.value}${sensor.unit}</td>
                <td>
                    <fmt:formatDate value="${datapoint.key}" pattern="dd.MM HH:mm:ss"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<%@include file="../common/scripts.jspf" %>
<script>
    let sensorData = [
        <c:forEach items="${data}" var="entry" varStatus="loop">
        {
            x: "${entry.key}",
            y: ${entry.value}
        }${not loop.last ? ',' : ''}
        </c:forEach>
    ]
</script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="${pageContext.request.contextPath}/js/data-functions.js"></script>
<script src="${pageContext.request.contextPath}/js/data-chart.js" type="module"></script>
</body>
</html>