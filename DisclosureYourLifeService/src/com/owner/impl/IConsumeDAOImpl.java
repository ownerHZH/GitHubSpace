package com.owner.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import com.owner.dao.IConsumeDAO;
import com.owner.domain.Consume;
import com.owner.tools.SessionFactory;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class IConsumeDAOImpl implements IConsumeDAO {
	
	private SqlSessionFactory sessionFactory = SessionFactory.getInstance()  
            .getSqlSessionFactory();
	    
	@SuppressWarnings("unchecked")
	@Override
	public List<Consume> getAllConsume(int pageno,int pagesize){
		List<Consume> consumeList=null;
		SqlSession session = null;
		try {
			session = sessionFactory.openSession(); 
			Map<String, Integer> hashMap=new HashMap<String, Integer>();
			hashMap.put("pageno", pageno);
			hashMap.put("pagesize", pagesize);
			consumeList=(List<Consume>)session.selectList(
					"com.owner.domain.ConsumeMapper.selectAll",hashMap);
		} finally
		{
			session.close();
		}
		return consumeList;
	}

	@Override
	public int insertConsume(Consume consume) {
		SqlSession session = null;
		int f=0;
		try {
			session=sessionFactory.openSession();
			f=session.insert("com.owner.domain.ConsumeMapper.insertConsume",consume);
			session.commit();
		} finally
		{
			session.close();
		}
		return f;
	}
	
	@Override
	public void insertConsume(List<Consume> consumes) {
		SqlSession sqlSession=null;
		try {
			sqlSession = sessionFactory.openSession(ExecutorType.BATCH, false);
			Map<String, List<Consume>> tmp = new HashMap<String, List<Consume>>();
			tmp.put("consumes", consumes);
			sqlSession.insert("com.owner.domain.ConsumeMapper.insertConsumeBatch", tmp);
			sqlSession.commit();
		}finally
		{
			sqlSession.close();
		}
	}

	@Override
	public Consume queryConsumeById(String cid){
		SqlSession session = null;
		Consume consume=new Consume();
		int f=0;
		try {
			session=sessionFactory.openSession();
			consume=(Consume) session.selectOne("com.owner.domain.ConsumeMapper.selectConsume", cid);
		} finally
		{
			session.close();
		}
		return consume;
	}

	@Override
	public List<Consume> queryConsumeByUid(String uid) {
		List<Consume> consumeList=new ArrayList<Consume>();
		SqlSession session = null;
		try {
			session = sessionFactory.openSession(); 
			consumeList=session.selectList("com.owner.domain.ConsumeMapper.selectPersonsByUid",uid);
		} finally
		{
			session.close();
		}
		return consumeList;
	}
}
