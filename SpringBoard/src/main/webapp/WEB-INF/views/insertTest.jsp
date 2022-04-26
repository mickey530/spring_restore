<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	@font-face {
	     font-family: 'DungGeunMo';
	     src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_six@1.2/DungGeunMo.woff') format('woff');
	     font-weight: normal;
	     font-style: normal;
	}
	
	body {
  	 	font-family: 'DungGeunMo', sans-serif;
	}
	
	#modDiv{
		width: 300px;
		height: 100px;
		background-color: whitesmoke;
		position: absolute;
		top: 50%;
		left: 50%;
		margin-top: -50px;
		margin-left: -150px;
		padding: 10px;
		z-index: 1000;
	}
</style>
</head>
<body>
	<h2>Ajax 댓글 등록 테스트</h2>
	
	<p>댓글 창</p>
	<hr/>
	<ul id="replies">
	
	</ul>
	<hr/>
	
	<div>
		<div>
			<!-- form 전달방식이 아니기 때문에 name이 필요 없음! -->
			REPLYER <input type="text" id="newReplyWriter"> 
		</div>
		<div>
			REPLY TEXT <input type="text" id="newReplyText">
		</div>
		<button id="replyAddBtn">ADD REPLY</button>
	</div>
	
	
	<!-- 모달창의 경우 보통 화면 최하단에 배치 -->
	<!-- modal은 일종의 팝업입니다.
	단, 새 창을 띄우지는 않고 css를 이용해 특정 태그가 보이거나 안 보이도록 처리해서 
	마치 창이 새로 띄워지는 것 처럼 만듭니다.
	따라서 눈에 보이지는 않아도 modal을 구성하는 태그 자체는 화면에 미리 적혀있어야 합니다. -->
	
	<div id="modDiv" style="display:none";>
		<div class="modal-title"></div>
		<div>
			<input type="text" id="reply">
		</div>
		<div>
			<button type="button" id="replyModBtn">Modify</button>
			<button type="button" id="replyDelBtn">Delete</button>
			<button type="button" id="closeBtn">Close</button>
		</div>
	</div>	
	
	<!-- jquery cdn 코드 -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
	
	
	<script>
	var bno = "3621";

	function getAllList(){
		 
		$.getJSON("/replies/all/" + bno, function(data){

			var str = "";
			console.log(data);
			
			$(data).each(
				function() {
					str += "<li data-rno='" + this.rno + "' class='replyLi'>"
						+ this.replyer + " : " + this.reply
						+ "<button>수정/삭제</button></li>";
				});
			
			$("#replies").html(str);			
		});
	 }
	 getAllList();
	 
	 function refresh(){
		 $("#newReplyWriter").val("");
		 $("#newReplyText").val("");
	 }	 
	 
	 </script>
	 
	<script>
		var bno = "3621";
	
		$("#replyAddBtn").on("click", function(){
			var replyer = $("#newReplyWriter").val();
			var reply = $("#newReplyText").val();
			
			$.ajax({
				type : 'post',
				url : '/replies',
				headers : {
					"Content-Type" : "application/json",
					"X-HTTP-Method-Override" : "POST"
				},
				dataType : 'text',
				data : JSON.stringify({
					bno : bno,
					replyer : replyer,
					reply : reply
				}),
				success : function(result){
					if(result == 'SUCCESS'){
						alert("등록되었습니다.");
						getAllList();
						refresh();
					}
				}
				/* error도 설정 가능 */
			});
			
		});
		
		// 이벤트 위임
		// 				// 클릭감지 	// 무엇을? 버튼을
		$("#replies").on("click", ".replyLi button", function() {
			var replyTag = $(this).parent(); // 버튼의 부모? .replyLi
			
			var rno = replyTag.attr("data-rno");
			var reply = replyTag.text();

			$(".modal-title").html(rno); // 타이틀
			$("#reply").val(reply); // 댓글 내용
			$("#modDiv").show("slow"); // 모달 열림
		});
		
		
		// 삭제 버튼
		$("#closeBtn").on("click", function(){
			console.log("클릭감지")
			$("#modDiv").hide("slow"); // 모달 닫힘	
		});
		
		$("#replyDelBtn").on("click", function(){
			let rno = $(".modal-title").html();
			
			$.ajax({
				type : 'delete',
				url : '/replies/' + rno,
				header : {
					"X-HTTP-Method-Override" : "DELETE"
				},
				dataType : 'text',
				success : function(result) {
					console.log("result : " + result);
					if(result == 'SUCCESS') {
						alert("삭제되었습니다.");
						$("#modDiv").hide("slow");
						getAllList();
					}
				}
			})
		});
		
		// 수정 버튼
		$("#replyModBtn").on("click", function(){
			var rno = $(".modal-title").html();
			var reply = $("#reply").val();
			
			$.ajax({
				type : 'patch',
				url : '/replies/' + rno,
				header : {
					"Content-Type" : "application/json",
					"X-HTTP-Method-Override" : "PATCH"					
				},
				contentType: "application/json",
				data: JSON.stringify({reply:reply}),
				dataType : 'text',
				success : function(result){
					console.log("result : " + result);
					if(result == 'SUCCESS'){
						alert("수정되었습니다.");
						$("#modDiv").hide("slow");
						getAllList();
					}
				}
			})
			
		});
		
		
		
	</script>
	 
</body>
</html>