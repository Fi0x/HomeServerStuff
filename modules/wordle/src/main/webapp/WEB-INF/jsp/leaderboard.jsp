<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Leaderboard</h1>
    <div>
        <c:forEach items="${gameModes}" var="mode">
            <label class="filter-option" title="Will include sensors with tag '${mode}'">
                <span class="align-content-center">${mode}</span>
                <input class="filter-checkbox" checked="true" type="checkbox"
                       onclick="updateFilterState('${mode}', this)">
            </label>
        </c:forEach>
    </div>
    <c:forEach items="${gameResults}" var="gameResult">
        <div class="gameType${gameResult.type}">
            <h3>${gameResult.type} : ${gameResult.word} (<fmt:formatDate value="${gameResult.dateTime}"
                                                                         pattern="dd.MM HH:mm"/>)</h3>
            <table class="table">
                <thead>
                <tr>
                    <th>Position</th>
                    <th>Player</th>
                    <th>Tries</th>
                    <th>Time</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${gameResult.playerResults}" var="playerResult" varStatus="loop">
                    <tr>
                        <td>${loop.index + 1}</td>
                        <td>${playerResult.playerName}</td>
                        <td>${playerResult.tries}</td>
                        <td>${playerResult.requiredTime/1000}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:forEach>
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