package com.ict.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ict.domain.ReplyVO;
import com.ict.service.ReplyService;

import lombok.extern.log4j.Log4j;

@RestController
@Log4j
@RequestMapping("/replies")
public class ReplyController {
	
	@Autowired
	private ReplyService service;
	
	// consumes는 이 메서드의 파라미터를 넘겨줄 때 어떤 형식으로 넘겨줄지를
	// 설정하는데 기본적으로 rest방식에서는 Json을 사용합니다.
	// produces는 입력받은 데이터를 토대로 로직을 실행한 다음
	// 사용자에게 결과로 보여줄 데이터의 형식(즉 리턴자료형)을 나타냅니다.
	// 아래 메서드는 json을 사용하므로 무조건 jackson-databind 추가해야 작동
	@PostMapping(value="", consumes="application/json", produces= {MediaType.TEXT_PLAIN_VALUE})
	// produces에 TEXT_PLANE_VALUE를 줬으므로 결과코드와 문자열을 넘김
	public ResponseEntity<String> register(
					// rest컨트롤러에서 받는 파라미터 앞에
					// @RequestBody 어노테이션이 붙어야
					// consumes와 연결됨
					@RequestBody ReplyVO vo){
		// 깡통 entity를 먼저 생성
		ResponseEntity<String> entity = null;
		try {
			// 먼저 글쓰기 로직 실행 후 에러가 없다면...
			service.addReply(vo);
			entity = new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
		} catch(Exception e) {
			// 에러나면 에러메세지와 함께 ResponseEntity 생성
			entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		// 위의 try블럭이나 catch블럭에서 얻어온 entity변수 리턴
		return entity;
	}

	@GetMapping(value="/all/{bno}",
		// 단일 숫자데이터 bno만 넣어서 조회하기도 하고
		// PathVariable 어노테이션으로 이미 입력 데이터가 명시되었으므로
		// consumes는 따로 주지 않아도 됩니다.
		// produces는 댓글 목록이 XML로도, JSON로도 표현될 수 있도록
		// 아래와 같이 2개를 모두 얹습니다.
			produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<List<ReplyVO>> list (@PathVariable("bno") Long bno){
		ResponseEntity<List<ReplyVO>> entity = null;
		
		try {
			entity = new ResponseEntity<>(service.listReply(bno), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return entity;
	}
	
	// 일반 방식이 아닌 rest방식에서는 삭제 로직을 post가 아닌
	// delete 방식으로 요청하기 때문에 @DeleteMapping 씁니다.
	@DeleteMapping(value="/{rno}", produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> remove(@PathVariable("rno") Long rno){
		ResponseEntity<String> entity = null;
		
		try {
			service.removeReply(rno);
			entity = new ResponseEntity<String>(
					"SUCCESS", HttpStatus.OK);				
		} catch (Exception e) {
			entity = new ResponseEntity<String>(
					e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return entity;
	}
	
	@RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH},
					value="/{rno}",
					consumes="application/json",
					produces= {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> modify(
			// VO는 우선 payload에 적힌 데이터(json)로 받아옵니다.
			// @RequestBody가 붙은 vo는
			// payload에 적힌 데이터를 vo로 환산해서 가져옵니다.
			// 
			@RequestBody ReplyVO vo,
			// 단, 댓글번호는 PathVariable로 받아옵니다.
			@PathVariable("rno") Long rno){
		ResponseEntity<String> entity = null;
		
		try {
			// payload에는 reply만 넣어줘도 되는데 그 이유는
			// rno는 요청주소로 받아오기 때문입니다.
			// 단, rno로 주소를 받아오는 경우는 아직 replyVO에
			// rno가 세팅이 되지 않은 상태이므로 Setter로 rno까지 지정을 해줍니다.
			log.info("세팅 전 vo : " + vo);
			vo.setRno(rno);
			log.info("세팅 후 vo : " + vo);
			service.modifyReply(vo);
			
			entity = new ResponseEntity<String>(
					"SUCCESS", HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<String>(
					e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return entity;
	}
}
