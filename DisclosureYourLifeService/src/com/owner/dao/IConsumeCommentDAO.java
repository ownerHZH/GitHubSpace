package com.owner.dao;

import java.util.List;

import com.owner.domain.ConsumeComment;

public interface IConsumeCommentDAO {
	public List<ConsumeComment> getAllConsumeComment(int cid);
	public int insertConsumeComment(ConsumeComment consumeComment);
}
