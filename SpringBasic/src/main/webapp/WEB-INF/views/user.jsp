<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	유저 번호 : ${user.userNum }<br/>
	유저 아이디 : ${user.userId }<br/>
	유저 비밀번호 : ${user.userPw }<br/>
	유저 이름 : ${user.userName }<br/>
	유저 나이 : ${user.userAge }<br/>
</body>
</html>