<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Play Wordle (${gameMode.gameModeName})</h1>
    <table class="riddleTable">
        <tbody id="fieldTable">
        </tbody>
    </table>
    <div class="keyboardRow">
        <c:forEach items="${keyboard.firstRow}" var="key"><a id="key${key.code}"
                                                             class="btn keyboardKey${key.width}"
                                                             onclick="pressKey(${key.code}, `${key.label}`)">${key.label}</a></c:forEach>
    </div>
    <div class="keyboardRow">
        <c:forEach items="${keyboard.secondRow}" var="key"><a id="key${key.code}"
                                                              class="btn keyboardKey${key.width}"
                                                              onclick="pressKey(${key.code}, `${key.label}`)">${key.label}</a></c:forEach>
    </div>
    <div class="keyboardRow">
        <c:forEach items="${keyboard.thirdRow}" var="key"><a id="key${key.code}"
                                                             class="btn keyboardKey${key.width}"
                                                             onclick="pressKey(${key.code}, `${key.label}`)">${key.label}</a></c:forEach>
    </div>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="${pageContext.request.contextPath}/js/wordle-functions.js"></script>
<script>
    let gameSettings = {
        gameModeName: "${gameMode.gameModeName}",
        timestamp: ${gameMode.timestamp},
        playerName: "${gameMode.playerName}",
        started: ${gameMode.started}
    };
</script>
<script>
    let baseUrl = "${pageContext.request.contextPath}/api"
</script>
<script>
    onload = function () {
        loadNavBar();
        initializeTable();
    }
</script>
</body>
</html>