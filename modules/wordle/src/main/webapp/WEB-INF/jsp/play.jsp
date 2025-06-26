<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Play Wordle</h1>
    <table class="riddleTable">
        <tbody id="fieldTable">
        </tbody>
    </table>
    <div>
        <c:forEach items="${keyboard.firstRow}" var="key"><a
                class="btn keyboardKey${key.width}">${key.label}</a></c:forEach>
    </div>
    <div>
        <c:forEach items="${keyboard.secondRow}" var="key"><a
                class="btn keyboardKey${key.width}">${key.label}</a></c:forEach>
    </div>
    <div>
        <c:forEach items="${keyboard.thirdRow}" var="key"><a
                class="btn keyboardKey${key.width}">${key.label}</a></c:forEach>
    </div>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="${pageContext.request.contextPath}/js/wordle-functions.js"></script>
<script>
    onload = function () {
        loadNavBar();
        initializeTable();
    }
</script>
</body>
</html>