<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="/score" method="post">
		수학 : <input type="number" name="math" max="100" min="0"><br/>
		영어 : <input type="number" name="english" max="100" min="0"><br/>
		국어 : <input type="number" name="korean" max="100" min="0"><br/>
		사탐 : <input type="number" name="social" max="100" min="0"><br/>
		컴퓨터 : <input type="number" name="computer" max="100" min="0"><br/>
		<input type="submit" value="평균내기">
	</form>
</body>
</html>