package com.ict.mapper;

import com.ict.domain.MemberVO;

public interface MemberMapper {

	public MemberVO read(String userId);
	
	public void insertMemberTbl(MemberVO vo);
	
	public void insertMemberAuth(MemberVO vo);
}
