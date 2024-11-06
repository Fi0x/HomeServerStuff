<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Home-Server</h1>
    <p>Here you can find a variety of useful tools</p>
    <a href="recipes" class="btn">Show recipes</a>
    <c:choose>
        <c:when test="${languageGeneratorAddress != null}">
            <a href="http://${languageGeneratorAddress}" class="btn">Show Language Generator</a>
        </c:when>
        <c:otherwise>
            <p>Language Generator not reachable</p>
        </c:otherwise>
    </c:choose>
</div>
<%@include file="../common/scripts.jspf" %>
</body>
</html>