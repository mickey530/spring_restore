<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
	.uploadResult{
		width:100%;
		background-color: grey;
	}
	.uploadResult ul{
		display: flex;
		flex-flow:row;
		justify-content:center;
		align-items: center;
	}
	.uploadResult ul li {
		list-style: none;
		padding: 10px;
	}
	.uploadResult ul li img{
		width: 100px;
	}
</style>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<!-- /boardInsert 로 보내는 post방식 폼을 생성해주세요.
	폼에서 보내는 요소와 name속성값은 쿼리문을 참조해서 만들어주세요.
	insert로직을 실행하는 컨트롤러도 생성해주시고 return값은 ""; 로 우선 두겠습니다. 
	vo를 살펴본 결과 title, content, writer 3개 요소를 보내면 됨-->
	<form action="/board/boardInsert" method="post">
		제목 : <input type="text" name="title">
		글쓴이 : <input type="text" name="writer"><br/>
		본문 : <textarea name="content" rows="20" cols="100"></textarea><br/> 
		<input type="submit" value="글쓰기"><input type="reset" value="초기화">
	</form>
	
	<h1>Upload ajax</h1>
	
	<div class="uploadDiv">
		<input type="file" accept=".jpg" name="uploadFile" multiple>
	</div>
	
	<div class="uploadResult">
		<ul>
			<!-- 업로드 파일이 들어갈 자리 -->
		</ul>
	</div>
	
	
	<button id="uploadBtn">upload</button>
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
	
	<script>
	
	var _csrf = '${_csrf.token}';
    var _csrf_header = '${_csrf.headerName}';
            
		$(document).ready(function(){
								// .(아무문자한개)*(개수제한없음) \.(.을 아무문자 1개가 아닌 . 자체로 쓸 때)
								// "(.*?)@(.*?)\.(com|net|etc..)$" <- 이메일 검증 정규표현식
			var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
			var maxSize = 5242880; // 5MB
			
			function checkExtension(fileName, fileSize){
				if(fileSize >= maxSize){
					alert("파일 사이즈 윤성");
					return false;
				}
				
				if (regex.test(fileName)){
					alert("해당 종류의 파일은 첨부할 ㅅ 없습니다.")
					return false;
				}
				return true;
			}
			
			
			// 첨부가 안 된 상태의 .uploadDiv를 깊은복사해서
			// 첨부 완료 후에 복사된 .uploadDiv로 덮어씌우기
			var cloneObj = $(".uploadDiv").clone();
			$('#uploadBtn').on("click", function(e){
				
			})
			
			// 업로드 버튼 클릭 시
			$('#uploadBtn').on("click", function(e){
				
				var formData = new FormData();
				
				var inputFile = $("input[name='uploadFile']");
				
				var files = inputFile[0].files;
				
				console.log(files);
		
		
				for(var i = 0; i < files.length; i++){
					if(!checkExtension(files[i].name, files[i].size)){
						return false;
					}
					formData.append("uploadFile", files[i]);
				}
				
				$.ajax({
					url : '/uploadAjaxAction',
					processData : false,
					contentType : false,
					data : formData,
					type : "POST",
					dataType : 'json',
					beforeSend: function(xhr){
		                xhr.setRequestHeader(_csrf_header, _csrf);
		            },
					success : function(result){
						console.log(result);
						
						showUploadedFile(result);
						
						// 업로드 성공시 미리 복사해둔 .uploadDiv로 덮어씌워서
						// 첨부파일이 없는 상태로 되돌려놓기
						$(".uploadDiv").html(cloneObj.html());
						alert("Uploaded");
						
					}
				});
			}); // onclick upload Btn
			
			var uploadResult = $(".uploadResult ul");
			
			// 업로드된 파일 화면상에 띄우는 로직
			function showUploadedFile(uploadResultArr){
				var str = "";
				
				$(uploadResultArr).each(function(i, obj){
					if(!obj.fileType){
						var fileCallPath = encodeURIComponent(
								obj.uploadPath + "/" +
								obj.uuid + "_" + obj.fileName);
						
						str += "<li "
							+ "data-path='" + obj.uploadPath + "' data-uuid='" + obj.uuid
							+ "' data-filename='" + obj.fileName + "' data-type='" + obj.fileType
							+ "'><a href='/download?fileName=" + fileCallPath
							+ "'>" + "<img src='/resources/IMG_5756.JPG'>"
							+ obj.fileName + "</a>"
							+ "<span data-file=\'" + fileCallPath + "\' data-type='file'> X <span>"
							+ "</li>";

					}else{
						var fileCallPath = encodeURIComponent(
								obj.uploadPath + "/s_" +
								obj.uuid + "_" + obj.fileName);
						var fileCallPathOrigin = encodeURIComponent(
								obj.uploadPath + "/" +
								obj.uuid + "_" + obj.fileName);
						
						str += "<li "
							+ "data-path='" + obj.uploadPath + "' data-uuid='" + obj.uuid
							+ "' data-filename='" + obj.fileName + "' data-type='" + obj.fileType
							+ "'><a href='/download?fileName=" + fileCallPathOrigin
							+ "'>" + "<img src='display?fileName="+fileCallPath+"'>"
							+ obj.fileName + "</a>"
							+ "<span data-file=\'" + fileCallPath + "\' data-type='image'> X <span>"
							+ "</li>";
					}
					
					let num = 22;
					console.log(num);
					console.log(`${num}`)
					console.log("인덱스 번호 : " + i);
					console.log("실제 자료 : " + obj);
					console.log(`백틱 테스트 실제 자료 : ${obj}`);
					
					
				})
				uploadResult.append(str);
			} // showUploadedFile
			
			
		});
		
		// 삭제로직
		$(".uploadResult").on("click", "span", function(e){
			var targetFile = $(this).data("file");
			var type = $(this).data("type");
			
			var targetLi = $(this).closest("li");
			
			$.ajax({
				url : '/deleteFile',
				data : {fileName:targetFile, type:type},
				dataType : 'text',
				type : 'POST',
				beforeSend: function(xhr){
	                xhr.setRequestHeader(_csrf_header, _csrf);
	            },
				success : function(result){
					alert(result);
					targetLi.remove();
				}
			});
			
			// 제출버튼 누를 경우, 첨부파일 정보를 폼에 추가해서 전달하는 코드
			$("#submitBtn").on("click", function(e){
				
				// 1. 버튼 기능 막기
				e.preventDefault();
				
				// 2. var formObj = $("form");로 폼태그를 가져옵니다.
				var formObj = $("form");
				
				// 3. 5월 19일 수업에서는 첨부파일 내에 들어있던 이미지 정보를 콘솔에 찍기만 하고 종료하고
				// 내일 수업에 DB에 넣는 부분까지 진행합니다.
				$(".uploadResult ul li").each(function(i, obj){
					console.log($(obj));
				})
				
			})
		});
		
		
		
		
		
	</script>
	
</body>
</html>