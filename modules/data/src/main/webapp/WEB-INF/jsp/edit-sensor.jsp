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
        <h3>
            Adjust values
        </h3>
        <table class="transparent-background settings-section">
            <tbody>
            <tr>
                <td class="sub-section">
                    <p>Sensor min-value</p>
                    <p>
                        <a onclick="changeValue(-0.1, 'valueMin')" class="btn round-button">-</a>
                        <span id="valueMin">${sensor.minValue != null ? sensor.minValue : 0}</span>
                        <a onclick="changeValue(0.1, 'valueMin')" class="btn round-button">+</a>
                    </p>
                </td>
                <td class="sub-section">
                    <p>Current value adjustment</p>
                    <p>
                        <a onclick="changeValue(-0.1, 'valueAdjustment')" class="btn round-button">-</a>
                        <span id="valueAdjustment">${sensor.valueAdjustment != null ? sensor.valueAdjustment : 0}</span>
                        <a onclick="changeValue(0.1, 'valueAdjustment')" class="btn round-button">+</a>
                    </p>
                </td>
                <td class="sub-section">
                    <p>Sensor max-value</p>
                    <p>
                        <a onclick="changeValue(-0.1, 'valueMax')" class="btn round-button">-</a>
                        <span id="valueMax">${sensor.maxValue != null ? sensor.maxValue : 0}</span>
                        <a onclick="changeValue(0.1, 'valueMax')" class="btn round-button">+</a>
                    </p>
                </td>
            </tr>
            <tr>
                <td class="center" colspan="3">
                    <a href="javascript:call();" class="btn btn-success">Save adjustments</a>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="section">
        <h3>
            Tags
        </h3>
    </div>
    <%--        TODO: Add an option to change sensor tags and create custom ones--%>
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
    function call() {
        let adjustment = document.getElementById('valueAdjustment').innerText;
        let min = document.getElementById('valueMin').innerText;
        let max = document.getElementById('valueMax').innerText;
        window.location = '${pageContext.request.contextPath}/sensor/${sensor.address}/${sensor.name}/update?valueAdjustment=' + adjustment + '&min=' + min + '&max=' + max;
    }

    function changeValue(adjustment, element) {
        let valueElement = document.getElementById(element);
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