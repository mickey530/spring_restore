package com.ict.controller.di.classfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Broadcast {
	
	@Autowired
	private Stage stage;
	
	public Broadcast(Stage stage) {
		
	}
	public void onAir() {
		System.out.print("방송 송출중인 ");
		this.stage.perform();
	}
}
