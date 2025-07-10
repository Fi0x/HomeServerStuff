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
            <tr id="cert${certificate.id}" class="clickable-row" onclick="selectCertificate(`${certificate.id}`)">
                <td>${certificate.country}</td>
                <td class="fw-bold">${certificate.shipName}</td>
                <td>${certificate.shipClass}</td>
                <td><a class="btn" href="${certificate.url}">${certificate.certificateType}</a></td>
                <td><span class="fw-bold">${certificate.singleNumber}</span><br>
                    <span id="single${certificate.id}">${(certificate.singleNumber * 60).intValue()} min ${((certificate.singleNumber * 60 - (certificate.singleNumber * 60).intValue()) * 60).intValue()}s</span>
                </td>
                <td><span class="fw-bold">${certificate.tripleLongLow}</span><br>
                    <span id="trilolow${certificate.id}">${(certificate.tripleLongLow * 60).intValue()} min ${((certificate.tripleLongLow * 60 - (certificate.tripleLongLow * 60).intValue()) * 60).intValue()}s</span>
                </td>
                <td><span class="fw-bold">${certificate.tripleLongMid}</span><br>
                    <span id="trilomid${certificate.id}">${(certificate.tripleLongMid * 60).intValue()} min ${((certificate.tripleLongMid * 60 - (certificate.tripleLongMid * 60).intValue()) * 60).intValue()}s</span>
                </td>
                <td><span class="fw-bold">${certificate.tripleLongHigh}</span><br>
                    <span id="trilohigh${certificate.id}">${(certificate.tripleLongHigh * 60).intValue()} min ${((certificate.tripleLongHigh * 60 - (certificate.tripleLongHigh * 60).intValue()) * 60).intValue()}s</span>
                </td>
                <td><span class="fw-bold">${certificate.tripleUpDownLow}</span><br>
                    <span id="triuplow${certificate.id}">${(certificate.tripleUpDownLow * 60).intValue()} min ${((certificate.tripleUpDownLow * 60 - (certificate.tripleUpDownLow * 60).intValue()) * 60).intValue()}s</span>
                </td>
                <td><span class="fw-bold">${certificate.tripleUpDownMid}</span><br>
                    <span id="triupmid${certificate.id}">${(certificate.tripleUpDownMid * 60).intValue()} min ${((certificate.tripleUpDownMid * 60 - (certificate.tripleUpDownMid * 60).intValue()) * 60).intValue()}s</span>
                </td>
                <td><span class="fw-bold">${certificate.tripleUpDownHigh}</span><br>
                    <span id="triuphigh${certificate.id}">${(certificate.tripleUpDownHigh * 60).intValue()} min ${((certificate.tripleUpDownHigh * 60 - (certificate.tripleUpDownHigh * 60).intValue()) * 60).intValue()}s</span>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="../../js/sailing-functions.js"></script>
<script>
    let baseUrl = "${pageContext.request.contextPath}/api"
</script>
<script>
    const certificates = [
        <c:forEach items="${certificates}" var="certificate" varStatus="loop">
        {
            id: `${certificate.id}`,
            shipName: `${certificate.shipName}`,
            certificateType: `${certificate.certificateType}`,
            country: `${certificate.country}`,
            shipClass: `${certificate.shipClass}`,
            url: `${certificate.url}`,
            singleNumber: ${certificate.singleNumber},
            tripleLongLow: ${certificate.tripleLongLow},
            tripleLongMid: ${certificate.tripleLongMid},
            tripleLongHigh: ${certificate.tripleLongHigh},
            tripleUpDownLow: ${certificate.tripleUpDownLow},
            tripleUpDownMid: ${certificate.tripleUpDownMid},
            tripleUpDownHigh: ${certificate.tripleUpDownHigh}
        }${not loop.last ? ',' : ''}
        </c:forEach>
    ];
</script>
<script>
    onload = function () {
        loadNavBar();
    }
</script>
</body>
</html>