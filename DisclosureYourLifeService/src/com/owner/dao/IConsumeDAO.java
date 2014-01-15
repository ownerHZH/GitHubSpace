package com.owner.dao;

import java.util.List;

import com.owner.domain.Consume;

public interface IConsumeDAO {
	public List<Consume> getAllConsume(int pageno,int pagesize);
	public int insertConsume(Consume consume);
	public Consume queryConsumeById(String id);
	public List<Consume> queryConsumeByUid(String uid);
	public void insertConsume(List<Consume> consumes);
}
