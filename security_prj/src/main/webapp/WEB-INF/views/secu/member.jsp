<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<html>
<head>
	<title>Home</title>
<link href="https://unpkg.com/nes.css@latest/css/nes.min.css" rel="stylesheet">	
</head>
<body>
<h1>
	Hello world!  
</h1>

<p>member</p>

	<sec:authorize access="hasAnyRole('ROLE_ADMIN')">
		<a href="/secu/admin">관리자 페이지로 이동<a>
	</sec:authorize>
	<sec:authorize access="isAuthenticated()">
		<a href="/customLogout">로그아웃</a><a>
	</sec:authorize>	
</body>
</html>
