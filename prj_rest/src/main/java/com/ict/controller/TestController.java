package com.ict.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.domain.TestVO;

@RestController
// 클래스 위에 붙이 RequestMappin은
@RequestMapping("/test")
public class TestController {

	@RequestMapping("/hello")
	public String sayHello() {
		return "Hello Hello";
	}
	
	@RequestMapping("/sendVO")
	public TestVO sendTestVO() {
		TestVO testVO = new TestVO();
		
		testVO.setName("이병건");
		testVO.setAge(40);
		testVO.setMno(1);
		return testVO;
	}
	
	@RequestMapping("/sendVOList")
	public List<TestVO> sendVoList(){
		
		List<TestVO> list = new ArrayList<>();
		for (int i=0; i <10; i++) {
			TestVO vo = new TestVO();
			vo.setMno(i);
			vo.setName(i + "병건");
			vo.setAge(20 + i);
			list.add(vo);
		}
		return list;
	}
	
	@RequestMapping("/sendMap")
	public Map<Integer, TestVO> sendMap(){
		
		Map<Integer, TestVO> map = new HashMap<>();
		
		for(int i = 0; i < 10; i++) {
			TestVO vo = new TestVO();
			vo.setName("이병건");
			vo.setMno(i);
			vo.setAge(50+i);
			map.put(i, vo);
		}
		return map;
	}
	
	@RequestMapping("/sendErrorAuth")
	public ResponseEntity<Void> sendListAuth(){
		return
			new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 익명클래스(변수에 저장하지 않고 다이렉트로 리턴)
	}
	
	@RequestMapping("/sendErrorNot")
	public ResponseEntity<List<TestVO>> sendListNot(){
		
		List<TestVO> list = new ArrayList<>();
		for (int i=0; i<10; i++) {
			TestVO vo = new TestVO();
			vo.setMno(i);
			vo.setName("이병건");
			vo.setAge(20 + i);
			list.add(vo);
		}
		return
			new ResponseEntity<List<TestVO>>(list, HttpStatus.NOT_FOUND);
	}
	
}
