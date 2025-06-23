<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>ORC Certificates</h1>
    <div>
        <input type="text" id="newCertificateText" placeholder="Certificate id">
        <div class="btn" onclick="addCertificate()">Add new Certificate</div>
    </div>
    <label class="search-label">
        <input type="text" id="searchText" onkeyup="searchFunction()" class="search-input" placeholder="Search...">
    </label>
    <table id="searchableTable" class="table sortable">
        <thead>
        <tr class="underlined-row">
            <th class="clickable">Country</th>
            <th class="clickable">Ship</th>
            <th class="clickable">Class</th>
            <th class="clickable">Type</th>
            <th class="clickable">Single Number</th>
            <th class="clickable">Triple Number Long Low</th>
            <th class="clickable">Triple Number Long Medium</th>
            <th class="clickable">Triple Number Long High</th>
            <th class="clickable">Triple Number Up Down Low</th>
            <th class="clickable">Triple Number Up Down Medium</th>
            <th class="clickable">Triple Number Up Down High</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${certificates}" var="certificate">
            <tr class="clickable-row"
                onclick="window.location='${certificate.url}'">
                <td>${certificate.country}</td>
                <td>${certificate.shipName}</td>
                <td>${certificate.shipClass}</td>
                <td>${certificate.certificateType}</td>
                <td>${certificate.singleNumber}</td>
                <td>${certificate.tripleLongLow}</td>
                <td>${certificate.tripleLongMid}</td>
                <td>${certificate.tripleLongHigh}</td>
                <td>${certificate.tripleUpDownLow}</td>
                <td>${certificate.tripleUpDownMid}</td>
                <td>${certificate.tripleUpDownHigh}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="../../js/functions.js"></script>
<script>
    let baseUrl = "${pageContext.request.contextPath}/api"
</script>
<script>
    onload = function () {
        loadNavBar();
    }
</script>
</body>
</html>