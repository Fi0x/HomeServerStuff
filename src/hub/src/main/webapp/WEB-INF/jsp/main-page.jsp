<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Home-Server</h1>
    <p>Here you can find a variety of useful tools</p>
    <c:forEach items="${services}" var="service">
        <a href="http://${service.ip}:${service.port}" class="btn">${service.name}</a>
    </c:forEach>
</div>
<%@include file="../common/scripts.jspf" %>
</body>
</html>