package com.owner.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import com.owner.dao.IPlainLookDAO;
import com.owner.domain.PlainLook;
import com.owner.tools.SessionFactory;

public class IPlainLookDAOImpl implements IPlainLookDAO {
	
	private SqlSessionFactory sessionFactory = SessionFactory.getInstance()  
            .getSqlSessionFactory();
	    
	@SuppressWarnings("unchecked")
	@Override
	public List<PlainLook> getAllPlainLook(int pageno,int pagesize) {
		List<PlainLook> PlainLookList=null;
		SqlSession session = null;
		try {
			session = sessionFactory.openSession(); 
			Map<String, Integer> hashMap=new HashMap<String, Integer>();
			hashMap.put("pageno", pageno);
			hashMap.put("pagesize", pagesize);
			PlainLookList=(List<PlainLook>)session.selectList("com.owner.domain.PlainLookMapper.selectAll",hashMap);
		} finally
		{
			session.close();
		}
		return PlainLookList;
	}

	@Override
	public int insertPlainLook(PlainLook plainLook) {
		SqlSession session = null;
		int f=0;
		try {
			session=sessionFactory.openSession();
			f=session.insert("com.owner.domain.PlainLookMapper.insertPlainLook",plainLook);
			session.commit();
		} finally
		{
			session.close();
		}
		return f;
	}

	@Override
	public PlainLook queryPlainLookById(String id) {
		SqlSession session = null;
		PlainLook PlainLook=new PlainLook();
		int f=0;
		try {
			session=sessionFactory.openSession();
			PlainLook=(PlainLook) session.selectOne("com.owner.domain.PlainLookMapper.selectPlainLook", id);
		} finally
		{
			session.close();
		}
		return PlainLook;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PlainLook> queryPlainLookByUid(String uid) {
		List<PlainLook> plainLookList=new ArrayList<PlainLook>();
		SqlSession session = null;
		try {
			session = sessionFactory.openSession(); 
			plainLookList=session.selectList("com.owner.domain.PlainLookMapper.selectPlainLookByUid",uid);
		} finally
		{
			session.close();
		}
		return plainLookList;
	}

	@Override
	public void insertPlainLook(List<PlainLook> plainLooks) {
		SqlSession sqlSession=null;
		try {
			sqlSession = sessionFactory.openSession(ExecutorType.BATCH, false);
			Map<String, List<PlainLook>> tmp = new HashMap<String, List<PlainLook>>();
			tmp.put("plainLooks", plainLooks);
			sqlSession.insert("com.owner.domain.PlainLookMapper.insertPlainLookBatch", tmp);
			sqlSession.commit();
		}finally
		{
			sqlSession.close();
		}
	}

	@Override
	public void updatePlainLookCount(String gobid) {
		SqlSession session = null;
		try {
			session = sessionFactory.openSession(); 
			Map<String, String> hashMap=new HashMap<String, String>();
			hashMap.put("gobid", gobid);
			session.update("com.owner.domain.PlainLookMapper.updatePlainLookCount",hashMap);
			session.commit();
		} finally
		{
			session.close();
		}
	}

	@Override
	public void updatePlainLookBCount(String gobid) {
		SqlSession session = null;
		try {
			session = sessionFactory.openSession(); 
			Map<String, Integer> hashMap=new HashMap<String, Integer>();
			hashMap.put("gobid", Integer.parseInt(gobid));
			session.update("com.owner.domain.PlainLookMapper.updatePlainLookBCount",hashMap);
			session.commit();
		} finally
		{
			session.close();
		}
	}

	@Override
	public List<PlainLook> getAllPlainLookByBcount(int pageno, int pagesize) {
		List<PlainLook> plainLookList=null;
		SqlSession session = null;
		try {
			session = sessionFactory.openSession(); 
			Map<String, Integer> hashMap=new HashMap<String, Integer>();
			hashMap.put("pageno", pageno);
			hashMap.put("pagesize", pagesize);
			plainLookList=(List<PlainLook>)session.selectList("com.owner.domain.PlainLookMapper.selectAllByBcount",hashMap);
		} finally
		{
			session.close();
		}
		return plainLookList;
	}

}
