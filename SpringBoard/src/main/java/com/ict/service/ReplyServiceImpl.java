package com.ict.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.domain.ReplyVO;
import com.ict.mapper.BoardMapper;
import com.ict.mapper.ReplyMapper;

@Service
public class ReplyServiceImpl implements ReplyService {
	
	@Autowired
	private ReplyMapper mapper;

	// 댓글 쓰기시 board_tbl쪽에도 관여해야 하므로 board 테이블을 수정하는 Mapper를 추가 선언합니다.
	@Autowired
	private BoardMapper boardMapper;
	
	@Transactional
	@Override
	public void addReply(ReplyVO vo) {
		Long bno = vo.getBno();
		mapper.create(vo);
		boardMapper.updateReplyCount(bno, +1);
	}
	
	@Override
	public List<ReplyVO> listReply(Long bno){
		return mapper.getList(bno);
	}
	
	@Override
	public void modifyReply(ReplyVO vo) {
		mapper.update(vo);
	}
	
	@Transactional
	@Override
	public void removeReply(Long rno) {
		Long bno = mapper.getBno(rno);
		mapper.delete(rno);
		boardMapper.updateReplyCount(bno, -1);
	}

}
