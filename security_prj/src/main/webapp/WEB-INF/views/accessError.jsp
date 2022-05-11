<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<style>
  :root {
    --ti-cursor-margin-left: 0.1em;
    --ti-cursor-color: dodgerblue;
  }
  body {
    height: 100vh;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    background: #000000;
    color: white;
    font-size: 3rem;
    font-family: 'Roboto', sans-serif;
  }
  h1 {
    margin-bottom: 0px;
  }
</style>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1 id="title">접근실패!@</h1>
	<h2><c:out value="${SPRING_SECURITY_403_EXCEPTION.getMessage() }"></c:out></h2>
	<h2><c:out value="${errorMessage }"></c:out></h2>
	
	
	<script>
      document.addEventListener('DOMContentLoaded', () => {
        new TypeIt('#title') //
          .pause(1000)
          .delete(7, { delay: 1000 })
          .type('Academy')
          .go();
      });
    </script>
</body>
</html>