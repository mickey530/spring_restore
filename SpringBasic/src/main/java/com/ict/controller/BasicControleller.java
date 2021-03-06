package com.ict.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ict.controller.vo.UserVO;

// 어노테이션에 네 종류가 있었는데(@Component, @Repository, @Controller, @Service)
@Controller
public class BasicControleller {

	// RequestMapping의 value는 localhost:8181/어떤주소 로 접속 시 해당 로직이 실행될지 결정합니다.
	// 아무것도 안 적으면 기본적으로 get 방식을 허용합니다.
	@RequestMapping(value="/goA")
	// 아래에 해당 주소로 접속시 실행하고 싶은 메서드를 작성합니다.
	public String goA() {
		System.out.println("goA 접속이 감지되었습니다.");
		// return "goA"; 라고 적으면 views 폴더 내부의 goA.jsp파일을 보여줍니다.
		return "goA";
	}
	
	@RequestMapping(value="/goB")
	public String goB() {
		System.out.println("goB 접속이 감지되었습니다.");
		// return "goA"; 라고 적으면 views 폴더 내부의 goA.jsp파일을 보여줍니다.
		return "b";
	}

	// 본인 성씨를 패턴으로 잡고
	// 결과 페이지는 "xxx의 페이지 입니다." 라는 문장이 뜨도록처리해서 메서드와 어노테이션을 보내주세요.
	@RequestMapping(value="/Choi")
	public String hangyeul() {
		System.out.println("최한결의 페이지입니다.");
		return "myPage";
	}
	
	@RequestMapping(value="/getData", method=RequestMethod.POST)
						// getData?data1=데이터1&data2=데이터2 에 해당하는 요소를 받아옵니다.
	public String getData(String data1, int data2, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		System.out.println("data1 : " + data1);
		System.out.println("data2 : " + data2);
		System.out.println("data2가 정수? : " + (data2+100));
		model.addAttribute("data1", data1);
		model.addAttribute("data2", data2);
		return "getResult";
	}
	
	// 외부에서 전송하는 데이터를 /getMoney 주소로 받아오겠습니다.
	// 이 주소는 int won 이라는 형식으로 금액을 받아서
	// 환율에 따른 환전금액을 콘솔에 찍어줍니다. 환전화폐는 임의로 정해주세요.
	// 결과 페이지는 exchange.jsp 로 하겠습니다.
	// 메서드명은 임의로 만들어주세요.
	
	@RequestMapping(value="/getMoney", method=RequestMethod.POST) // POST 방식으로만 받도록 처리
						// 포워딩시 바인딩을 하고 싶다면 Model을 선언합니다
	public String exchange(int won, Model model) {
		double usd  = won*0.833;
		// model.addAttribute("보낼 이름", 보낼자료);
		// 넘어간 데이터는 .jsp 파일에서 el을 이용해 출력합니다.
		// ex -> model.addAttribute("test", 자료); 로 바인딩 한 경우
		// ${test} 로  .jsp에서 출력 가능
		model.addAttribute("won", won);
		model.addAttribute("usd", usd);
		return "exchange";
	}
	
	// form 페이지와 결과 페이지를 분리해야 합니다.
	// 다만 목적지 주소가 .jsp 기준이 아닌, @RequestMapping상의 주소 기준으로 갑니다.
	// 주소 moneyForm 으로 연결되도록 아래에 어노테이션 + 메서드를 구성해주세요.
	// moneyForm.jsp로 연결됩니다.
	// moneyForm.jsp에는 목적지를 #으로 하고
	// name=won인 폼을 추가로 만들어주세요.
	@RequestMapping(value="/moneyForm")
	public String moneyForm() {
		return "moneyForm";
	}

	// 상단 /getData 주소를 타겟으로 하는 /dataForm을 만들어주세요.
	// data1, data2를 자료형에 맞게 폼으로 입력받아 전송버튼을 누르면
	// 해당 데이터가 결과 페이지에 나올 수 있도록 .jsp파일부터 시작해서 
	// form 태그나 세부 로직까지 완성시켜주세요.
	// 1. 주소 및 연결 메서드 완성 후 보내기
	// 2. form 태그 완성 후 보내기
	
	@RequestMapping(value="/dataForm")
	public String dataForm() {
		return "dataForm";
	}

	// 스프링 5버전 부터 허용
	// @요청메서드 Mapping은 해당 메서드만 허용하는 어노테이션입니다.
//	@PostMapping(value="/onlyPost")
	@GetMapping(value="/onlyGet")
	public String onlyGet() {
		return "onlyGet";
	}
	
	@GetMapping(value="/score")	
	public String score() {
		return "inputScore";
	}
	
	@PostMapping(value="/score")
	public String average(
			int math,
			int english,
			int korean,
			int social,
			@RequestParam("computer") int com, // computer로 들어온 데이터 com 변수에 저장 (변수명을 숨기고 싶을 때 사용)
			Model model) {
		model.addAttribute("math", math);
		model.addAttribute("english", english);
		model.addAttribute("korean", korean);
		model.addAttribute("social", social);
		model.addAttribute("computer", com);

		int total = math + english + korean + social + com;
		double average = (total)/5.0;
		
		model.addAttribute("total", total);
		model.addAttribute("average", average);
		
		return "average";
	}	
	
	// 주소는 /page로 하겠습니다.
	// get 방식 접속만 허용합니다.
	// 메서드명은 임의로 만들어주세요.
	// page.jsp로 연결됩니다.
	@GetMapping(value="/page/{bookNum}/{pageNum}")
	public String getPage(@PathVariable int bookNum, @PathVariable int pageNum, Model model) {
		// page.jsp를 views폴더에 만들어주세요.
		// 해당 페이지는 int pageNum을 받아서 바인딩 합니다. 
		// page.jsp 본문에 현재 ${page} 페이지를 보고 계십니다.
		// 와 합께 입숨 더미데이터를 이용해 본문글을 채워주세요.
		model.addAttribute("book", bookNum);
		model.addAttribute("page", pageNum);
		return "page";
	}
	
	// 환율 계산기를 만들어보겠습니다.
	// 단, 원화금액은 @Pathvariable을 이용해 입력받습니다.
	// 주소는 /rate입니다.
	// get방식으로 처리해주세요.
	// 원화를 입력받으면 rate.jsp 에서 결과로 환전금액을 보여줍니다.
	
	@GetMapping(value="/rate/{won}")
	public String getRate(@PathVariable int won, Model model) {
		final double USD_RATE = 0.833;
		double usd = won*USD_RATE;
		model.addAttribute("won", won);
		model.addAttribute("usd", usd);
		return "rate";
	}
	
	// 리스트를 받아서 처리하기
	@GetMapping("/getList")
	public String getList(
			// 배열자료를 받을 시 @RequestParam 사용이 강제됩니다.
			@RequestParam ArrayList<String> array,
			Model model) {
		// 리스트 자료형의 경우 같은 이름으로 여러 데이터를 연달아 보내면 처리 가능합니다.
		model.addAttribute("array", array);
		return "getList";
	}
	
	// 만약 주소와 매칭된 메서드의 리턴자료형을 String이 아닌 void로 처리하는 경우
	// 지정주소.jsp로 바로 연결됩니다. (view파일(.jsp파일) 이름 지정 불가)
	// 주소와 파일명이 일치한다면 써주셔도 되지만
	// 기본적으로는 String을 쓰는 것이 권장됨
	int i = 0;
	@GetMapping("/test") // test.jsp로 연결됨
	public void goTest(Model model) {
		i++;
		model.addAttribute("i", i);
		// 내부 실행문 없음
	}
	
	// VO를 활용해 회원 데이터를 받는 컨트롤러를 만들어보겠습니다.
	// userInfo가 주소입니다.
	@GetMapping("userInfo")
	public String userForm() {
		return "userForm";
	}

	@PostMapping("userInfo")
	public String getUserInfo(UserVO userVO, Model model) {
		// 변수명은 userVO로 지정했으나, 실제로는 내부 멤버변수의 이름으로 데이터를 받습니다.
//		int userNum = userVO.getUserNum();
//		String userId = userVO.getUserId();
//		String userPw = userVO.getUserPw();
//		String userName = userVO.getUserName();
//		int userAge = userVO.getUserAge();
		
		// 바인딩 문법을 작성해주세요.
		model.addAttribute("user", userVO);
		
		return "user";
	}
	
	
	
	
	
	
}
