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
<h1>admin 주소</h1>
<h2>다양한 페이지 정보</h2>
<p>principal : <sec:authentication property="principal"/></p>
<p>MemberVO : <sec:authentication property="principal.member"/></p>
<p>사용자의 이름 : <sec:authentication property="principal.member.userName"/></p>
<p>사용자의 아이디 : <sec:authentication property="principal.member.userId"/></p>
<p>사용자의 권한목록 : <sec:authentication property="principal.member.authList"/></p>
<hr/>
	<sec:authorize access="isAuthenticated()">
		<a href="/customLogout">로그아웃</a><a>
	</sec:authorize></body>
</html>
