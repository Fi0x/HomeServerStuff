<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Register</h1>
    <%--@elvariable id="userDto" type="io.fi0x.languagegenerator.logic.dto.UserDto"--%>
    <table>
        <form:form method="post" modelAttribute="userDto" action="/register">
        <tbody>
        <tr>
            <td>
                Username:
            </td>
            <td>
                <form:input type="text" path="username"/>
            </td>
            <td>
                    <%--@elvariable id="usernameError" type="java.lang.String"--%>
                <a class="error">${usernameError}</a>
            </td>
        </tr>
        <tr>
            <td>
                Password:
            </td>
            <td>
                <form:input type="password" path="password"/>
            </td>
            <td>
                    <%--@elvariable id="passwordError" type="java.lang.String"--%>
                <a class="error">${passwordError}</a>
            </td>
        </tr>
        <tr>
            <td>
                Repeat password:
            </td>
            <td>
                <form:input type="password" path="matchingPassword"/>
            </td>
            <td>
                    <%--@elvariable id="matchingPasswordError" type="java.lang.String"--%>
                <a class="error">${matchingPasswordError}</a>
            </td>
        </tr>
        <tr>
            <td>
            </td>
            <td>
            </td>
            <td>
                    <%--@elvariable id="passwordMatchError" type="java.lang.String"--%>
                <a class="error">${passwordMatchError}</a>
            </td>
        </tr>
        <tr>
            <td>
            </td>
            <td>
                <input type="submit" class="btn-success">
            </td>
        </tr>
        </tbody>
    </table>
    </form:form>
    <a href="custom-login" class="btn">Login with an existing account</a>
</div>
<%@include file="../common/scripts.jspf" %>
</body>
</html>