package com.ict.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ict.domain.BoardAttachVO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class UploadController {
	
	private boolean checkImageType(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath());
			
			return contentType.startsWith("image");
					
		} catch(IOException e) {
			e.printStackTrace();
		} return false;
	}
	
	private String getFolder() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = new Date();
		
		String str = sdf.format(date);
		
		return str.replace("-", File.separator);
	}
	
	
	@ResponseBody
	@PostMapping(value="/uploadAjaxAction", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<BoardAttachVO>> uploadAjaxPost(MultipartFile[] uploadFile) {
		
		// AttachFileDTO는 파일 한 개의 정보를 저장합니다.
		// 현재 파일 업로드는 여러 파일을 동시에 업로드 하므로 List<AttachFileDTO>를 받도록 처리합니다.
		List<BoardAttachVO> list = new ArrayList<>();
		log.info("update ajax post....");
		
		// 어떤 폴더에 저장할 것인지 위치 지정
		String uploadFolder = "/Users/user/upload_data/temp";
		
		// 폴더 경로 받아오기
		String uploadFolderPath = getFolder();
		
		// 폴더 생성
		File uploadPath = new File(uploadFolder, getFolder());
		log.info("upload path : " + uploadPath);
		
		if(uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}


		
		log.info("ajax post update!");
				
		for(MultipartFile multipartFile : uploadFile) {
			log.info("-----------------------");
			log.info("Upload File Name : " + multipartFile.getOriginalFilename());
			log.info("Upload File Size : " + multipartFile.getSize());
		
			BoardAttachVO attachVO = new BoardAttachVO();
			
			String uploadFileName = multipartFile.getOriginalFilename();
			
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
		
			log.info("last file name : " + uploadFileName);
			
			// 상단에 만든 DTO에 파일 이름 저장
			attachVO.setFileName(uploadFileName);
			
			// uuid(ㅠㅠ아이디) 발급 부분
			UUID uuid = UUID.randomUUID();
			
			uploadFileName = uuid.toString() + "_" + uploadFileName;
			
			try {
				File saveFile = new File(uploadPath, uploadFileName);
				multipartFile.transferTo(saveFile);
				
				attachVO.setUuid(uuid.toString());
				attachVO.setUploadPath(uploadFolderPath);
				
				
				// 이 아래부터 썸네일 생성 로직
				if(checkImageType(saveFile)) {
					attachVO.setFileType(true);
					
					FileOutputStream thumbnail = 
							new FileOutputStream(
									new File(uploadPath, "s_" + uploadFileName));
					
					Thumbnailator.createThumbnail(
							multipartFile.getInputStream(), thumbnail, 100, 100);
					thumbnail.close();
							
				}
				list.add(attachVO);
			} catch (Exception e) {
				log.error(e.getMessage());
			}	
			
		}		
		return new ResponseEntity<>(list, HttpStatus.OK);
				
	}
	
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName){
		log.info("fileName: " + fileName);
		
		// 1. 파일 생성
		File file = new File("/Users/user/upload_data/temp/" + fileName); // 실제로는 fileName에 폴더 경로까지 포함
		
		log.info("file : " + file);
		
		ResponseEntity<byte[]> result = null;
		
		try {
			// 2. json 대신 파일을 줘야 하는데 http방식 통신이므로 헤더를 생성
			HttpHeaders header = new HttpHeaders(); // import org.springframework.http.HttpHeaders;

			// 3. 컨텐츠 타입이 json이 아닌 파일임을 헤더에 명시
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			
			// 4. ResponseEntity에 파일을 포함시켜 전달
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),
											header,
											HttpStatus.OK);
		} catch(Exception e){
			e.printStackTrace();
		}		
		return result;
	}
														// OCTET : 용량이 큰 파일의 경우 쪼개서 보낸대
	@GetMapping(value="/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(String fileName){
		
		log.info("download file : " + fileName);
		
		Resource resource = new FileSystemResource("/Users/user/upload_data/temp/" + fileName);
		
		log.info("resource : " + resource);
		
		String resourceName = resource.getFilename();
		
		HttpHeaders headers = new HttpHeaders();
		
		try {
			headers.add("Content-Disposition",
						"attachment; filename=" +
						new String(resourceName.getBytes("UTF-8"), "ISO-8859-1"));			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}
	
	@PostMapping("/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName, String type){
		
		log.info("deleteFile : " + fileName);
		
		File file = null;
		
		try {
			file = new File("/Users/user/upload_data/temp/" + URLDecoder.decode(fileName, "UTF-8"));
			
			file.delete();
			
			if(type.equals("image")) {
				String largeFileName = file.getAbsolutePath().replace("s_", "");
				
				log.info("largeFileName : " + largeFileName);
				
				file = new File(largeFileName);
				
				file.delete();
			}
			
			
		} catch(UnsupportedEncodingException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<String>("deleted", HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@Controller
//	public class emailController{
//		@PostMapping("{toUserId}@naver.com")
//		public void sendMail(@PathVariable String toUserId, String sendUserId, String content) {
//			// 대충 DB에 
//		}
//	}

}
