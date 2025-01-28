<%--
  Created by IntelliJ IDEA.
  User: fpechtl
  Date: 28.01.2025
  Time: 16:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<script>
    const socket = new WebSocket('ws://localhost:2347/data/notifications')

    socket.onmessage = function (event) {
        //TODO: Update UI
        console.log('Notification received: ', event.data);
    };

    function sendNotification(msg) {
        socket.send(msg);
    }
</script>
</body>
</html>
