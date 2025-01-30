<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>${sensor.name} (${sensor.address})</h1>
    <p>${sensor.description}</p>
    <div class="section">
        <p>Current value adjustment:</p>
        <p>
            <a onclick="changeValue(-0.1)" class="btn round-button">-</a>
            <span id="valueAdjustment">${sensor.valueAdjustment != null ? sensor.valueAdjustment : 0}</span>
            <a onclick="changeValue(0.1)" class="btn round-button">+</a>
        </p>
        <a href="javascript:call();" class="btn btn-success">Save value adjustment</a>
    </div>
    <div class="section">
        <h3>Danger Zone</h3>
        <a href="${pageContext.request.contextPath}/sensor/${sensor.address}/${sensor.name}/update?deleteValues=0"
           class="btn btn-danger">Delete all Data with value 0</a>
        <a href="${pageContext.request.contextPath}/sensor/${sensor.address}/${sensor.name}/update?deleteValues=ALL"
           class="btn btn-danger">Delete All Data</a>
        <a href="${pageContext.request.contextPath}/sensor/${sensor.address}/${sensor.name}/delete"
           class="btn btn-danger">Delete the sensor and all Data</a>
    </div>
</div>
<%@include file="../common/scripts.jspf" %>

<script>
    const valueElement = document.getElementById('valueAdjustment');

    function call() {
        let adjustment = valueElement.innerText;
        window.location = '${pageContext.request.contextPath}/sensor/${sensor.address}/${sensor.name}/update?valueAdjustment=' + adjustment;
    }

    function changeValue(adjustment) {
        let current = Number(valueElement.innerText);
        valueElement.innerText = (current + adjustment).toFixed(1);
    }
</script>
<script>
    onload = function () {
        loadNavBar();
    }
</script>
</body>
</html>