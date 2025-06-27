<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Wordle</h1>
    <div class="top-margin">
        <c:forEach items="${gameModes}" var="mode">
            <a class="btn-success" href="${pageContext.request.contextPath}/play?gameMode=${mode.id}">${mode.label}</a>
        </c:forEach>
        <c:if test="${gameModes.size() == 0}">
            <p>You have already played all available game-modes. Please wait, until they reset to play the next word</p>
        </c:if>
    </div>
    <div class="top-margin-double">
        <a class="btn" href="${pageContext.request.contextPath}/validation">Add or validate words</a>
    </div>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="${pageContext.request.contextPath}/js/wordle-functions.js"></script>
<script>
    onload = function () {
        loadNavBar();
    }
</script>
</body>
</html>