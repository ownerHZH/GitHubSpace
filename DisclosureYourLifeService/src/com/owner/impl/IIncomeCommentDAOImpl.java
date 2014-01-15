package com.owner.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.owner.dao.IIncomeCommentDAO;
import com.owner.domain.IncomeComment;
import com.owner.tools.SessionFactory;

public class IIncomeCommentDAOImpl implements IIncomeCommentDAO {
	
	private SqlSessionFactory sessionFactory = SessionFactory.getInstance()  
            .getSqlSessionFactory();
	    
	@SuppressWarnings("unchecked")
	@Override
	public List<IncomeComment> getAllIncomeComment(int pageno,int pagesize){
		List<IncomeComment> incomeCommentList=null;
		SqlSession session = null;
		try {
			session = sessionFactory.openSession(); 
			Map<String, Integer> hashMap=new HashMap<String, Integer>();
			hashMap.put("pageno", pageno);
			hashMap.put("pagesize", pagesize);
			incomeCommentList=(List<IncomeComment>)session.selectList(
					"com.owner.domain.IncomeCommentMapper.selectAll",hashMap);
		} finally
		{
			session.close();
		}
		return incomeCommentList;
	}

	@Override
	public int insertIncomeComment(IncomeComment incomeComment) {
		SqlSession session = null;
		int f=0;
		try {
			session=sessionFactory.openSession();
			f=session.insert("com.owner.domain.IncomeCommentMapper.insertIncomeComment",incomeComment);
			session.commit();
		} finally
		{
			session.close();
		}
		return f;
	}
}
