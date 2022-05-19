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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ict.domain.BoardAttachVO;
import com.ict.domain.BoardVO;
import com.ict.domain.Criteria;
import com.ict.domain.PageMaker;
import com.ict.domain.SearchCriteria;
import com.ict.mapper.BoardMapper;
import com.ict.service.BoardService;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

// 컨트롤러가 컨트롤러 기능을 할 수 있도록 처리해주세요.
@Controller
@Log4j
@RequestMapping("/board")
public class BoardController {

	// 컨트롤러는 Service만 호출하도록 구조를 바꿉니다.
	// Service를 BoardController 내부에서 쓸 수 있도록 선언/주입해주세요.
	@Autowired
	private BoardService service;//(실제 주입되는 요소는 ServiceImpl이므로 호출지점도 ServiceImpl임)
	
	
	// 전체 글 목록을 볼 수 있는 페이지인 boardList.jsp로 연결되는
	// /boardList 주소를 get방식으로 선언해주세요.
	// 메서드 내부에서는 boardMapper의 .getAllList를 호출해 그 결과를 바인딩합니다.
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/boardList")
	// @RequestParam(name="사용할변수명", defaultValue="지정하고싶은기본값") 변수 왼쪽에 저렇게 붙여주면 처리완료.
	// @PathVariable의 경우 defaultValue를 직접 줄 수 없으나, required=false를 이용해 필수입력을 안받게 처리한 후
	// 컨트롤러 내부에서 디폴트값을 입력해줄 수 있다.
	// 기본형 자료는 null을 저장할 수 없기 때문에 wrapper class를 이용해 Long을 선언합니다.
	public String boardList(SearchCriteria cri, Model model) {
		//if(pageNum == null) {
		//	pageNum = 1L;// Long형은 숫자 뒤에 L을 붙여야 대입됩니다.
		//}
		// model.addAttribute("바인딩이름", 바인딩자료);
		List<BoardVO> boardList = service.getList(cri);
		log.info("넘어온 글 관련 정보 목록 : " + boardList);
		model.addAttribute("boardList", boardList);
		
		// 버튼 처리를 위해 추가로 페이지메이커 생성 및 세팅
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);// cri 입력
		int countPage = service.countPageNum(cri);// 131대신 실제로 DB내 글 개수를 받아옴
		pageMaker.setTotalBoard(countPage);// calcData()호출도 되면서 순식간에 prev, next, startPage, endPage세팅
		model.addAttribute("pageMaker", pageMaker);
		
		return "board/boardList";
	}
	
	// 글 하나만 조회할 수 있는 디테일 페이지인 boardDetail.jsp로 연결되는
	// /boardDetail 주소를 get방식으로 선언해주세요.
	// 주소 뒤에 ?bno=번호 형식으로 적힌 번호 글만 조회합니다.
	// 숙제 : @PathVariable적용 방식으로 바꿔보세요, boardList에서 제목클릭시 넘어오게해주세요.
	@GetMapping("/boardDetail/{bno}")
	public String boardDetail(@PathVariable long bno, Model model) {
		BoardVO board = service.select(bno);
		model.addAttribute("board", board);
		return "board/boardDetail";
	}
	
	// insert 페이지를 위한 form으로 연결되는 컨트롤러를 먼저 만들겠습니다.
	// get방식으로 /boardInsert 주소를 접속시 form페이지로 연결됩니다.
	// 폼 페이지의 이름은 boardForm.jsp 입니다.
	@GetMapping("/boardInsert")
	public String boardForm() {
		return "board/boardForm";
	}
	
	// /boardInsert인데 post방식을 처리하는 메서드를 새로 만들어주세요.
	// BoardVO를 입력받도록 해 주시면 실제로는 BoardVO의 멤버변수명으로 들어오는 자료를 입력받습니다.
	// 입력받은 BoardVO를 토대로 mapper쪽의 insert 메서드를 실행해 주시고
	// 리다이렉트는 return "redirect:/목적지주소" 형식으로 리턴구문을 작성하면 됩니다.
	// boardList로 돌려보내주세요.
	@PostMapping("/boardInsert")
	public String boardInsert(BoardVO board) {
		// 폼에서 날린 데이터 들어오는지 디버깅
		log.info("들어온 데이터 디버깅 : " + board);
		// insert 로직 실행
		service.insert(board);
		return "redirect:/board/boardList";
	}
	
	// 글삭제 로직은 Post방식으로 진행합니다
	// /boardDelete 주소로 처리하고
	// bno를 받아서 해당 글을 삭제합니다.
	// 글 삭제 버튼은 detail페이지 하단에 form으로 만들어서 bno를 hidden으로 전달하는
	// submit 버튼을 생성해서 처리하게 해주세요.
	// 삭제 수행 후 boardList로 리다이렉트해주세요.
	@PostMapping("/boardDelete")
	public String boardDelete(long bno, SearchCriteria cri, RedirectAttributes rttr) {
		// 삭제로직 실행
		service.delete(bno);
		// rttr
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("keyword", cri.getKeyword());
		
		// 리턴으로 리스트페이지 복귀
		return "redirect:/board/boardList";
	}
	
	// /boardUpdateForm 를 POST방식으로 접속하는 form 연결 메서드를 만들겠습니다.
	// update로직은 이미 데이터가 입력이 되어 있어야 합니다.
	// 따라서 내가 수정하고자 하는 글의 정보를 VO로 받아온다음
	// 폼 페이지에 포워딩해서 기입해놔야합니다.
	// 폼페이지 이름은 boardUpdateForm.jsp 입니다.
	@PostMapping("/boardUpdateForm")
	public String boardUpdateForm(long bno, Model model) {
		BoardVO board = service.select(bno);
		model.addAttribute("board", board);
		return "board/boardUpdateForm";//${board};
	}
	
	// /boardUpdate 를 post방식으로 접속하는 메서드를 만들겠습니다.
	// update(BoardVO) 를 실행해서, 폼에서 날려준 데이터를 토대로
	// 해당 글의 내용이 수정되도록 만들어주시면 됩니다.
	// 수정 후에는 수정요청이 들어온 글 번호의 디테일페이지로 리다이렉트 시켜주세요.
	@PostMapping("/boardUpdate")
	public String boardUpdate(BoardVO board, SearchCriteria cri, RedirectAttributes rttr) {
		log.info("수정로직입니다. " + board);
		log.info("검색조건 : " + cri.getSearchType());
		log.info("키워드 : " + cri.getKeyword());
		log.info("페이지 번호 : " + cri.getPageNum());
		
		// rttr.addAttribute("파라미터명", "전달자료")
		// 는 호출되면 redirect 주소 뒤에 파라미터를 붙여줍니다.
		// rttr.addFlashAttribute()는 넘어간 페이지에서 파라미터를
		// 쓸 수 있도록 전달하는 것으로 둘의 역할이 다르니 주의해주세요.
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("keyword", cri.getKeyword());
		// update 호출
		service.update(board);
		// redirect:주소?글번=getter
		return "redirect:/board/boardDetail/" + board.getBno();
		
	}
	
	// 이미지 첨부용 //////////////////
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
	
}









