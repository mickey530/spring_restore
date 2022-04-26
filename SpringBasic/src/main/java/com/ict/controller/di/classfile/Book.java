package com.ict.controller.di.classfile;

import org.springframework.stereotype.Component;

// 빈 컨테이너에 등록해주세요.
@Component
public class Book {

	public void read() {
		System.out.println("책을 읽습니다.");
	}
	
	
}
