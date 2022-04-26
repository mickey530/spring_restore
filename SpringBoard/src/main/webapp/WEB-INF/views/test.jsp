<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	.replyLi{
		list-style:none;
	}
</style>
</head>
<body>
	<h2>Ajax 테스트</h2>
	
	<p>댓글 창</p>
	<hr/>
	<button onclick="getAllList()">댓글 보기</button>
	<button onclick="hideReply()">댓글 숨기기</button>
	
	<ul id="replies">
	
	
	</ul>
	

	
	<!-- jquery cdn 코드 -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>	
	

	
	
	<script type="text/javascript">
		let bno = 3621;
		
/* 		function getReply(){
			
		// getJSON은 입력한 주소에 get방식으로 요청을 넣습니다.
					// 주소				// 콜백함수
		$.getJSON("/replies/all/" + bno, function(data){

			var str = "";
			console.log(data);
			
			// $(JSON형식데이터).each => 내부 JSON을 향상된 for문 형식으로 처리합니다.
			// 내부에 콜백함수(로직이 실행되었을 때 추가로 실행할 구문을 정의하기 위해 파라미터로 넣는 함수)
			// 를 이용해 data를 하나하나 향상된 for문 형식으로 처리할 때 실행구문을 적을 수 있습니다.
			$(data).each(
				function() {
					str += "<li data-rno='" + this.rno + "' class='replyLi'>"
						+ this.replyer + " : " + this.reply
						+ "</li>";
				});
			
			$("#replies").html(str);			
		});
		
		} */
		
	</script>
	
	<script>
		function hideReply(){
			var hide = "";
			$("#replies").html(hide);
		}
	</script>
	
	
	<ul id="test">
	
	</ul>
	
	<script type="text/javascript">
		var daum = "<a href='https://daum.net'>duam으로 이동</a>";
		$("#test").html(daum);
	</script>
	
	
	 <script> 	 
	 function getAllList(){
		$.getJSON("/replies/all/" + bno, function(data){

			var str = "";
			console.log(data);
			
			$(data).each(
				function() {
					str += "<li data-rno='" + this.rno + "' class='replyLi'>"
						+ this.replyer + " : " + this.reply
						+ "</li>";
				});
			
			$("#replies").html(str);			
		});
	 }
	 </script>

	
</body>
</html>