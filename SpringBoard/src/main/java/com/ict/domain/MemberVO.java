package com.ict.domain;

import java.sql.Date;
import java.util.List;

import lombok.Data;

@Data
public class MemberVO {
	private String userId;
	private String userPw;
	private String userName;
	private boolean enable;
	
	private Date regDate;
	private Date updateDate;
	private List<AuthVO> authList;
	

}
