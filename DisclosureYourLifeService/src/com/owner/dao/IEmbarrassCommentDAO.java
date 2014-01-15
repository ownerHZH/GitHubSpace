package com.owner.dao;

import java.util.List;

import com.owner.domain.EmbarrassComment;

public interface IEmbarrassCommentDAO {
	public List<EmbarrassComment> getAllEmbarrassComment(int pageno,int pagesize);
	public int insertEmbarrassComment(EmbarrassComment embarrassComment);
}
