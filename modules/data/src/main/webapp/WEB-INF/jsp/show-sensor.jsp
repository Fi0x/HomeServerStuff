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
    <canvas id="statChart"></canvas>
    <c:choose>
        <c:when test="${username.toLowerCase() != 'anonymoususer'}">
            <div class="section">
                <a href="${pageContext.request.contextPath}/sensor/${sensor.address}/${sensor.name}/edit"
                   class="btn-edit">Edit
                    Sensor details</a>
            </div>
        </c:when>
    </c:choose>
    <table id="searchableTable" class="table sortable">
        <thead>
        <tr class="underlined-row">
            <th class="clickable">Measurement</th>
            <th class="clickable">Time</th>
        </tr>
        </thead>
        <tbody id="sensorDataTable">
        <c:forEach items="${data}" var="datapoint" varStatus="loop">
            <tr id="dataEntry${loop.index}">
                <td>${datapoint.value}${sensor.unit}</td>
                <td>
                    <fmt:formatDate value="${datapoint.key}" pattern="dd.MM., HH:mm:ss"/>
                </td>
                <c:choose>
                    <c:when test="${username.toLowerCase() != 'anonymoususer'}">
                        <td>
                            <a class="btn btn-danger"
                               href="${pageContext.request.contextPath}/data/${sensor.address}/${sensor.name}/${datapoint.key.time}/delete">Delete</a>
                        </td>
                    </c:when>
                </c:choose>
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
            x: "<fmt:formatDate value="${entry.key}" pattern="yyyy-MM-dd HH:mm:ss"/>",
            y: ${entry.value}
        }${not loop.last ? ',' : ''}
        </c:forEach>
    ].sort((first, second) => {
        let firstDate = new Date(first.x);
        let secondDate = new Date(second.x);
        return firstDate.getTime() - secondDate.getTime();
    })
</script>
<script>
    let sensorMinData = [
        <c:forEach items="${minData}" var="entry" varStatus="loop">
        {
            x: "<fmt:formatDate value="${entry.key}" pattern="yyyy-MM-dd HH:mm:ss"/>",
            y: ${entry.value}
        }${not loop.last ? ',' : ''}
        </c:forEach>
    ].sort((first, second) => {
        let firstDate = new Date(first.x);
        let secondDate = new Date(second.x);
        return firstDate.getTime() - secondDate.getTime();
    })
</script>
<script>
    let sensorMaxData = [
        <c:forEach items="${maxData}" var="entry" varStatus="loop">
        {
            x: "<fmt:formatDate value="${entry.key}" pattern="yyyy-MM-dd HH:mm:ss"/>",
            y: ${entry.value}
        }${not loop.last ? ',' : ''}
        </c:forEach>
    ].sort((first, second) => {
        let firstDate = new Date(first.x);
        let secondDate = new Date(second.x);
        return firstDate.getTime() - secondDate.getTime();
    })
</script>
<script>
    let sensorAvgData = [
        <c:forEach items="${avgData}" var="entry" varStatus="loop">
        {
            x: "<fmt:formatDate value="${entry.key}" pattern="yyyy-MM-dd HH:mm:ss"/>",
            y: ${entry.value}
        }${not loop.last ? ',' : ''}
        </c:forEach>
    ].sort((first, second) => {
        let firstDate = new Date(first.x);
        let secondDate = new Date(second.x);
        return firstDate.getTime() - secondDate.getTime();
    })
</script>
<script>
    let sensorInformation = {
        delay: ${sensor.dataDelay},
        name: "${sensor.name}",
        unit: "${sensor.unit}",
        min: ${sensor.minValue != null ? sensor.minValue : 0},
        max: ${sensor.maxValue != null ? sensor.maxValue : 100}
    };
</script>
<script>
    let baseUrl = "${pageContext.request.contextPath}/api/data"
</script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script src="https://cdn.jsdelivr.net/npm/luxon@1.25.0/build/global/luxon.js"></script>
<script src="https://cdn.jsdelivr.net/npm/moment@2.29.1/moment.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-zoom@1.1.1/dist/chartjs-plugin-zoom.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/chartjs-adapter-moment/1.0.0/chartjs-adapter-moment.js"></script>

<script src="${pageContext.request.contextPath}/js/data-chart.js"></script>
<script src="${pageContext.request.contextPath}/js/data-functions.js"></script>
<script src="${pageContext.request.contextPath}/js/data-line-color.js"></script>
<script>
    onload = function () {
        loadNavBar();
        colorDataLines();
        subscribeToDataUpdates(newDataForSingleSensor, `/subscribe?address=${sensor.address}&name=${sensor.name}`);
    }
</script>
</body>
</html>