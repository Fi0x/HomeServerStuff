<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Sensors</h1>
    <label class="search-label">
        <input type="text" id="searchText" onkeyup="updateFilterState()" class="search-input" placeholder="Search...">
    </label>
    <div>
        <c:forEach items="${tagList}" var="tag">
            <label class="filter-option" title="Will include sensors with tag '${tag}'">
                <span class="align-content-center">${tag}</span>
                <input class="filter-checkbox" type="checkbox" onclick="updateFilterState()">
            </label>
        </c:forEach>
    </div>
    <table id="searchableTable" class="table sortable">
        <thead>
        <tr class="underlined-row">
            <th>
                <label>
                    <input type="checkbox" onclick="selectAllCheckboxes(this)">
                </label>
            </th>
            <th class="clickable">Name</th>
            <th class="clickable">Address</th>
            <th class="clickable">Value</th>
            <th class="clickable">Last Update</th>
            <th class="clickable">Tags</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${sensorList}" var="sensor">
            <tr id="sensorRow${sensor.address}${sensor.name}"
                class="clickable-row ${sensor.offline ? 'yellow' : (sensor.minValue > sensor.value || sensor.maxValue < sensor.value ? 'red' : '')}"
                title="${sensor.offline ? 'OFFLINE' : sensor.description}"
                onclick="window.location='${pageContext.request.contextPath}/sensor/${sensor.address}/${sensor.name}'">
                <td>
                    <label onclick="selectCheckbox(this, event, '${sensor.address} ${sensor.name}')">
                        <input class="chart-view-checkbox" type="checkbox"/>
                    </label>
                </td>
                <td>${sensor.name}</td>
                <td>${sensor.address}</td>
                <td>${sensor.value}${sensor.unit}</td>
                <td>
                    <fmt:formatDate value="${sensor.lastUpdate}" pattern="dd.MM., HH:mm:ss"/>
                </td>
                <td class="filter-tag">
                    <c:forEach items="${sensor.tags}" var="tag" varStatus="loop">
                        ${tag}${loop.last ? '': ','}
                    </c:forEach>
                </td>
                <td>
                    <span onclick="nextChartColor(this, event)" class="color-tag"
                          id="color-span${sensor.address} ${sensor.name}"></span>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <canvas id="dataChart"></canvas>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="https://cdn.jsdelivr.net/npm/moment@2.29.1/moment.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-zoom@1.1.1/dist/chartjs-plugin-zoom.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/chartjs-adapter-moment/1.0.0/chartjs-adapter-moment.js"></script>
<script src="${pageContext.request.contextPath}/js/combined-data-chart.js"></script>
<script src="${pageContext.request.contextPath}/js/data-functions.js"></script>
<script>
    let sensorNames = [
        <c:forEach items="${sensorList}" var="sensor" varStatus="loop">
        {
            address: "${sensor.address}",
            name: "${sensor.name}",
            tags: "${sensor.tags}",
            id: "${sensor.address} ${sensor.name}",
            unit: "${sensor.unit}"
        }${not loop.last ? ',' : ''}
        </c:forEach>
    ]
</script>
<script>
    let baseUrl = "${pageContext.request.contextPath}/api/data"
</script>
<script>
    onload = function () {
        loadNavBar();
        loadChartData(sensorNames);
        subscribeToDataUpdates(newDataForSensorList, `/subscribe`);
    }
</script>
</body>
</html>