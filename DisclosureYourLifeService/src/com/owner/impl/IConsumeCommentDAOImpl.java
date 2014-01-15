package com.owner.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.owner.dao.IConsumeCommentDAO;
import com.owner.domain.ConsumeComment;
import com.owner.tools.SessionFactory;

public class IConsumeCommentDAOImpl implements IConsumeCommentDAO {
	
	private SqlSessionFactory sessionFactory = SessionFactory.getInstance()  
            .getSqlSessionFactory();
	    
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsumeComment> getAllConsumeComment(int pageno,int pagesize){
		List<ConsumeComment> consumeCommentList=null;
		SqlSession session = null;
		try {
			session = sessionFactory.openSession(); 
			Map<String, Integer> hashMap=new HashMap<String, Integer>();
			hashMap.put("pageno", pageno);
			hashMap.put("pagesize", pagesize);
			consumeCommentList=(List<ConsumeComment>)session.selectList(
					"com.owner.domain.ConsumeCommentMapper.selectAll",hashMap);
		} finally
		{
			session.close();
		}
		return consumeCommentList;
	}

	@Override
	public int insertConsumeComment(ConsumeComment consumeComment) {
		SqlSession session = null;
		int f=0;
		try {
			session=sessionFactory.openSession();
			f=session.insert("com.owner.domain.ConsumeCommentMapper.insertConsumeComment",consumeComment);
			session.commit();
		} finally
		{
			session.close();
		}
		return f;
	}
}
