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
	<h1>로그인 페이지</h1>
	<h2><c:out value="${error }"></c:out></h2>
	<h2><c:out value="${logout }"></c:out></h2>
	
	<form action="/login" method="post">
		ID : <input type="text" name="username"><br>
		PW : <input type="password" name="password"><br/>
		자동로그인 : <input type="checkbox" name="remember-me"><br/>
		<input type="submit" value="로그인">
 		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">			
	</form>
	
</body>
</html>