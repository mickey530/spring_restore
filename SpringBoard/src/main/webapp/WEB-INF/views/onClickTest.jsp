<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<ul id="replies">
		
	</ul>
	
	<button id="requestBtn">댓글 로딩해오기</button>
	
	<!-- jQuery cdn -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<!-- 	
	<script type="text/javascript">
		let bno = 3621;
		
		function getReply(){

		$.getJSON("/replies/all/" + bno, function(data){

			var str = "";
			console.log(data);
			
			$(data).each(
				function() {
					str += "<li data-rno='" + this.rno + "' class='replyLi'>"
						+ this.replyer + " : " + this.reply
						+ "</li>";
				});
			
			$("#requestBtn").html(str);			
		});
		
		}
		
	</script>
	 -->
	 <script>
 	 let bno = 3621;
 	
	 $("#requestBtn").on("click", function(){

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
		 
	 })
	 </script>
	 
	 
</body>
</html>