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
		width: 20px;
	}
</style>

<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
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
			
			function showUploadedFile(uploadResultArr){
				var str = "";
				
				$(uploadResultArr).each(function(i, obj){
					if(!obj.image){
						var fileCallPath = encodeURIComponent(
								obj.uploadPath + "/" +
								obj.uuid + "_" + obj.fileName);
						
						str += "<li><a href='/download?fileName=" + fileCallPath
								+ "'>" + "<img src='/resources/IMG_5756.JPG'>"
								+ obj.fileName + "</a>"
								+ "<span data-file=\'" + fileCallPath + "\' data-type='file'> X </span>"
								+ "</li>";
					}else{
						var fileCallPath = encodeURIComponent(
								obj.uploadPath + "/s_" +
								obj.uuid + "_" + obj.fileName);
						var fileCallPathOrigin = encodeURIComponent(
								obj.uploadPath + "/" +
								obj.uuid + "_" + obj.fileName);
						
						str += "<li><a href='/download?fileName=" + fileCallPathOrigin
							+ "'>" + "<img src='/display?fileName="+fileCallPath
							+ "'>" + obj.fileName + "</a>"
							+ "<span data-file=\'" + fileCallPath + "\' data-type='image'> X </span>"
							+ "</li>"
					}
					
					
					console.log("인덱스 번호 : " + i);
					console.log("실제 자료 : " + obj);
					
					
				})
				uploadResult.append(str);
			} // showUploadedFile
			
			
		});
		
		$(".uploadResult").on("click", "span", function(e){
			var targetFile = $(this).data("file");
			var type = $(this).data("type");
			
			var targetLi = $(this).closest("li");
			
			$.ajax({
				url : '/deleteFile',
				data : {fileName:targetFile, type:type},
				dataType : 'text',
				type : 'POST',
				success : function(result){
					alert(result);
					targetLi.remove();
				}
			});
			
		});
		
		
		
		
		
	</script>
	
	
	
	
	
	
	
	
	
</body>
</html>