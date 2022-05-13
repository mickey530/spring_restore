<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<title>Home</title>
<link href="https://unpkg.com/nes.css@latest/css/nes.min.css" rel="stylesheet">	
</head>
<body>
<h1>
all 주소
</h1>

	<sec:authorize access="isAnonymous()">
		<a href="/customLogin">로그인<a>
	</sec:authorize>
	<sec:authorize access="isAuthenticated()">
		<a href="/customLogout">로그아웃</a><a>
	</sec:authorize>
</body>
</html>
