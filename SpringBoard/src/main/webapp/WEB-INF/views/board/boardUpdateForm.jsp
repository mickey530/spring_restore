<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="container">
		<h1>${board.bno }번글 수정페이지입니다.</h1>
		<%-- ${board} --%>
		<form action="/boardUpdate" method="post">
			<div class="row">
				<div class="col-md-9">
					<input type="text" class="form-control" name="title" value="${board.title }" />
				</div>
				<div class="col-md-3">
					<input type="text" class="form-control" name="writer" value="${board.writer }" />
				</div>
			</div>
			
			<textarea rows="10" class="form-control" name="content">${board.content }</textarea>
			<div class="row justify-content-end">
				<div class="col-md-1">
					<input type="hidden" name="bno" value="${board.bno}">
					<input type="hidden" name="pageNum" value="${param.pageNum }" >
					<input type="hidden" name="searchType" value="${param.searchType}">
					<input type="hidden" name="keyword" value="${param.keyword}">
					<input type="submit" value="수정" class="btn">
				</div>
			</div>
		</form>
		
<%-- 		<form action="/boardUpdate" method="post">
			제목 : <input type="text" name="title" value="${board.title }">
			글쓴이 : <input type="text" name="writer" value="${board.writer }"><br/>
			본문 : <textarea name="content" rows="20" cols="100">${board.content }</textarea><br/> 
			
			<!-- 수정을 했다면, 수정완료 후에도 페이지번호, 검색조건, 검색어가 유지되도록
			전달받은 데이터를 폼으로 다시 넘겨줘야합니다. -->
			<input type="hidden" name="bno" value="${board.bno}">
			<input type="hidden" name="pageNum" value="${param.pageNum }" >
			<input type="hidden" name="searchType" value="${param.searchType}">
			<input type="hidden" name="keyword" value="${param.keyword}">
			<input type="submit" value="수정">
		</form> --%>
	</div>
</body>
</html>







