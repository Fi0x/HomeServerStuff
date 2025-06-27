<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Validate words</h1>
    <div class="small-background">
        <label id="verificationWord" class="highlighted">${wordToVerify}</label>
        <div class="top-margin-half">
            <a class="btn-danger clickable"
               onclick="inValidateAndUpdateTexts('${pageContext.request.contextPath}/api/words/invalidate/')">Not a
                word</a>
            <a class="btn-success clickable"
               onclick="inValidateAndUpdateTexts('${pageContext.request.contextPath}/api/words/validate/')">Valid</a>
        </div>
    </div>
    <div id="responseTextId">You did not update any entries in the database yet</div>
    <div class="small-background top-margin">
        <h3>Suggest a new word for the word-list</h3>
        <input maxlength="5" id="newWordInput">
        <a class="btn" onclick="addNewWord()">Add</a>
    </div>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="${pageContext.request.contextPath}/js/wordle-functions.js"></script>
<script>
    let baseUrl = "${pageContext.request.contextPath}/api"
</script>
<script>
    onload = function () {
        loadNavBar();
    }
</script>
<script>
    window.addEventListener("keydown", function (event) {
        if (event.keyCode === 13)
            addNewWord();
    });
</script>
</body>
</html>