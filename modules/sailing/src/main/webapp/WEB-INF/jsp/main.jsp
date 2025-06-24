<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Sailing</h1>
    <a class="btn" href="${pageContext.request.contextPath}/race-results">Show all race-results</a>
    <c:forEach items="${raceGroups}" var="group">
        <a class="btn" href="${pageContext.request.contextPath}/race-results/orc?group=${group}">ORC ${group}
            results</a>
    </c:forEach>
    <a class="btn" href="${pageContext.request.contextPath}/orc">Show ORC certificates</a>
</div>
<%@include file="../common/scripts.jspf" %>
<script>
    onload = function () {
        loadNavBar();
    }
</script>
</body>
</html>