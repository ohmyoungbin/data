<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page session="false" %>
<html>
<head>
	<title>FCM_TEST</title>
</head>
<body>
<h2>Notification 보낼내용입력</h2>

<form action="push.do" method="post">
<textarea name="message" rows="4" cols="50" placeholder="메시지를 입력하세요"></textarea><br/>
<input type="submit" name="submit" value="Send" id="submitBtn">
</form>

</body>
</html>
