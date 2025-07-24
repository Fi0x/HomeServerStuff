<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>New Race</h1>
    <table>
        <tbody>
        <tr>
            <td>Race Name</td>
            <td><input></td>
        </tr>
        <tr>
            <td>Race Group</td>
            <td><input></td>
        </tr>
        <tr>
            <td>Start Date</td>
            <td><input></td>
        </tr>
        <tr>
            <td>End Date</td>
            <td><input></td>
        </tr>
        <tr>
            <td>Crossable</td>
            <td><input type="checkbox"></td>
        </tr>
        <tr>
            <td>ORC</td>
            <td><input type="checkbox"></td>
        </tr>
        <tr>
            <td>Participants</td>
            <td><input type="number"></td>
        </tr>
        </tbody>
    </table>
    <%--    TODO: Add inputs to enter general race data--%>
    <%--    TODO: Save the race-info and then open modify-race-results page--%>
    <a class="btn-success">Save Race Information</a>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="../../js/sailing-functions.js"></script>
<script>
    let baseUrl = "${pageContext.request.contextPath}/api";
    let baseUrlNormal = "${pageContext.request.contextPath}";
</script>
<script>
    onload = function () {
        loadNavBar();
    }
</script>
</body>
</html>