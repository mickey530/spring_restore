<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
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
		position: fixed;
		top: 50%;
		left: 50%;
		margin-top: -50px;
		margin-left: -150px;
		padding: 10px;
		z-index: 1000;
	}
	#reply{
		width: 90%;
	}
	
</style>
</head>
<body>
	<div class="container">
		<h1 class="text text-primary">${board.bno }번글 조회중</h1>
		<div class="row">
			<div class="col-md-9">
				<input type="text" class="form-control" value="제목 : ${board.title }" />
			</div>
			<div class="col-md-3">
				<input type="text" class="form-control" value="글쓴이 : ${board.writer }" />
			</div>
		</div>
		<textarea rows="10" class="form-control">${board.content }</textarea>
		<div class="row">
			<div class="col-md-3">쓴날짜 : </div>
			<div class="col-md-3">${board.regdate }</div>
			<div class="col-md-3">수정날짜 : </div>
			<div class="col-md-3">${board.updatedate }</div>
		</div>
		<div class="row justify-content-end">
			<div class="col-md-1">
				<a href="/board/boardList?pageNum=${param.pageNum eq null ? 1 : param.pageNum}&searchType=${param.searchType}&keyword=${param.keyword}" class="btn btn-success btn-sm">글목록</a>
			</div>
			<div class="col-md-1">
				<form action="/board/boardDelete" method="post">
					<input type="hidden" name="bno" value="${board.bno}">
					<input type="hidden" name="pageNum" value="${param.pageNum }" >
					<input type="hidden" name="searchType" value="${param.searchType}">
					<input type="hidden" name="keyword" value="${param.keyword}">
					<input type="submit" value="삭제" class="btn btn-danger btn-sm">
				</form>
			</div>
			<div class="col-md-1">
				<form action="/board/boardUpdateForm" method="post">
					<input type="hidden" name="bno" value="${board.bno}">
					<input type="hidden" name="pageNum" value="${param.pageNum }" >
					<input type="hidden" name="searchType" value="${param.searchType}">
					<input type="hidden" name="keyword" value="${param.keyword}">
					<input type="submit" value="수정" class="btn btn-warning btn-sm">
				</form>
			</div>
		</div>


	<div class="row">
		<div id="replies">
		
		</div>
	</div>
		
		<div class="row box-box-success">
			<div class="box-header">
				<h2 class="text-primary">댓글 작성</h2>
			</div>
			<div class="box-body">
				<strong>Writer</strong>
				<input type="text" id="newReplyWriter" placeholder="Replyer" class="form-control">
				<strong>Reply Text</strong>
				<input type="text" id="newReplyText" placeholder="ReplyText" class="form-control">
			</div>
			<div class="box-footer">
				<button type="button" class="btn btn-success" id="replyAddBtn">Add Reply</button>
			</div>
		</div>
<!-- 		
		<div>
			<div>
				form 전달방식이 아니기 때문에 name이 필요 없음!
				REPLYER <input type="text" id="newReplyWriter"> 
			</div>
			<div>
				REPLY TEXT <input type="text" id="newReplyText">
			</div>
			<button id="replyAddBtn">ADD REPLY</button>
		</div>
-->
		
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
	</div>
		
		<!-- jquery cdn 코드 -->
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
		



	<!-- 여기부터 자바스크립트 비동기 처리 영역 -->
	<script>
		let bno = ${board.bno};
		
		function getAllList(){
			 
			$.getJSON("/replies/all/" + bno, function(data){

				var str = "";
				console.log(data);
				
				$(data).each(function() {
						var timestamp = this.updateDate;
						var date = new Date(timestamp);
						
						var formattedTime = "게시일 : " + date.getFullYear()
											+ "/" + (date.getMonth()+1) // 컴퓨터가 인식하는 달 : 0~11월 그래서 +1 해줌 
											+ "/" + date.getDate()
											+ "|" + date.getHours()
											+ ":" + date.getMinutes()
											+ ":" + date.getSeconds()

						str += "<div class='replyLi' data-rno='" + this.rno + "'><strong>@"
							+ this.replyer + "</strong> - " + formattedTime + "<br>"
							+ "<div class='reply'>" + this.reply + "</div>"
							+ "<button type='button' class='btn btn-outline-info'>수정/삭제</button>"
							+ "</div>";
					});
				
				$("#replies").html(str);			
			});
		 }
		 getAllList();
		 
		 function refresh(){
			 $("#newReplyWriter").val("");
			 $("#newReplyText").val("");
		 }	 
	
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
			var replyText = $(this).siblings(".reply").text();

			$(".modal-title").html(rno); // 타이틀
			$("#reply").val(replyText); // 댓글 내용
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