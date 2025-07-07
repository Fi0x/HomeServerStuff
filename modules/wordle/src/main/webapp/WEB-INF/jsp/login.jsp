<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Login</h1>
    <%--@elvariable id="loginDto" type="io.fi0x.languagegenerator.logic.dto.LoginDto"--%>
    <%--@elvariable id="loginError" type="java.lang.String"--%>
    <form:form method="post" modelAttribute="loginDto" action="/login">
        <table>
            <tbody>
            <tr>
                <td>
                    Username:
                </td>
                <td>
                    <form:input type="text" path="username"/>
                </td>
            </tr>
            <tr>
                <td>
                    Password:
                </td>
                <td>
                    <form:input type="password" path="password"/>
                </td>
            </tr>
            <tr>
                <a class="error">${loginError}</a>
            </tr>
            </tbody>
        </table>
        <input type="submit" class="btn-success">
    </form:form>
    <a href="register" class="btn">Register a new account</a>
</div>
<%@include file="../common/scripts.jspf" %>
<script>
    onload = function () {
        loadNavBar();
    }
</script>
</body>
</html>