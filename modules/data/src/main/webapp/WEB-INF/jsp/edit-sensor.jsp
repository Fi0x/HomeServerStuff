<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%--TODO: Add the option to adjust all shown values by a constant to correct wrong calibration--%>
<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>${sensor.name} (${sensor.address})</h1>
    <p>${sensor.description}</p>
    <div>
        <%--TODO: Make value adjustment editable and save data on button press--%>
        <p>Current value adjustment: ${sensor.valueAdjustment}</p>
        <a class="btn-success">Save value adjustment</a>
    </div>
    <%--TODO: Make data deletion work--%>
    <a class="btn-danger">Delete all Data with value 0</a>
    <a class="btn-danger">Delete All Data</a>
</div>
<%@include file="../common/scripts.jspf" %>

<script>
    onload = function () {
        loadNavBar();
    }
</script>
</body>
</html>