package com.owner.dao;

import java.util.List;

import com.owner.domain.IncomeComment;

public interface IIncomeCommentDAO {
	public List<IncomeComment> getAllIncomeComment(int iid);
	public int insertIncomeComment(IncomeComment incomeComment);
}
