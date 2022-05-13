<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>로그아웃 페이지</h1>
	<h2><c:out value="${error }"></c:out></h2>
	<h2><c:out value="${logout }"></c:out></h2>
	
	<form action="/customLogout" method="post">
		<input type="submit" value="로그아웃">
 		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">			
	</form>
	
</body>
</html>