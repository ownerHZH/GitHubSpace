package com.owner.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.owner.dao.IEmbarrassCommentDAO;
import com.owner.domain.EmbarrassComment;
import com.owner.tools.SessionFactory;

public class IEmbarrassCommentDAOImpl implements IEmbarrassCommentDAO {
	
	private SqlSessionFactory sessionFactory = SessionFactory.getInstance()  
            .getSqlSessionFactory();
	    
	@SuppressWarnings("unchecked")
	@Override
	public List<EmbarrassComment> getAllEmbarrassComment(int pageno,int pagesize){
		List<EmbarrassComment> embarrassCommentList=null;
		SqlSession session = null;
		try {
			session = sessionFactory.openSession(); 
			Map<String, Integer> hashMap=new HashMap<String, Integer>();
			hashMap.put("pageno", pageno);
			hashMap.put("pagesize", pagesize);
			embarrassCommentList=(List<EmbarrassComment>)session.selectList(
					"com.owner.domain.EmbarrassCommentMapper.selectAll",hashMap);
		} finally
		{
			session.close();
		}
		return embarrassCommentList;
	}

	@Override
	public int insertEmbarrassComment(EmbarrassComment embarrassComment) {
		SqlSession session = null;
		int f=0;
		try {
			session=sessionFactory.openSession();
			f=session.insert("com.owner.domain.EmbarrassCommentMapper.insertEmbarrassComment",embarrassComment);
			session.commit();
		} finally
		{
			session.close();
		}
		return f;
	}
}
