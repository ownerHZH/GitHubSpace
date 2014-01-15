package com.owner.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import com.owner.dao.IEmbarrassDAO;
import com.owner.domain.Embarrass;
import com.owner.domain.Income;
import com.owner.tools.SessionFactory;

public class IEmbarrassDAOImpl implements IEmbarrassDAO {
	
	private SqlSessionFactory sessionFactory = SessionFactory.getInstance()  
            .getSqlSessionFactory();
	    
	@SuppressWarnings("unchecked")
	@Override
	public List<Embarrass> getAllEmbarrass(int pageno,int pagesize) {
		List<Embarrass> EmbarrassList=null;
		SqlSession session = null;
		try {
			session = sessionFactory.openSession();
			Map<String, Integer> hashMap=new HashMap<String, Integer>();
			hashMap.put("pageno", pageno);
			hashMap.put("pagesize", pagesize);
			EmbarrassList=(List<Embarrass>)session.selectList("com.owner.domain.EmbarrassMapper.selectAll",hashMap);
		} finally
		{
			session.close();
		}
		return EmbarrassList;
	}

	@Override
	public int insertEmbarrass(Embarrass embarrass) {
		SqlSession session = null;
		int id=0;
		try {
			session=sessionFactory.openSession();
			session.insert("com.owner.domain.EmbarrassMapper.insertEmbarrass",embarrass);
			session.commit();
			id=embarrass.getEid();
		} finally
		{
			session.close();
		}
		return id;
	}

	@Override
	public Embarrass queryEmbarrassById(String eid) {
		SqlSession session = null;
		Embarrass Embarrass=new Embarrass();
		int f=0;
		try {
			session=sessionFactory.openSession();
			Embarrass=(Embarrass) session.selectOne("com.owner.domain.EmbarrassMapper.selectEmbarrass", eid);
		} finally
		{
			session.close();
		}
		return Embarrass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Embarrass> queryEmbarrassByUid(String uid) {
		List<Embarrass> EmbarrassList=new ArrayList<Embarrass>();
		SqlSession session = null;
		try {
			session = sessionFactory.openSession(); 
			EmbarrassList=session.selectList("com.owner.domain.EmbarrassMapper.selectEmbarrassByUid",uid);
		} finally
		{
			session.close();
		}
		return EmbarrassList;
	}

	@Override
	public void insertEmbarrass(List<Embarrass> embarrasses) {
		SqlSession sqlSession=null;
		try {
			sqlSession = sessionFactory.openSession(ExecutorType.BATCH, false);
			Map<String, List<Embarrass>> tmp = new HashMap<String, List<Embarrass>>();
			tmp.put("embarrasses", embarrasses);
			sqlSession.insert("com.owner.domain.EmbarrassMapper.insertEmbarrassBatch", tmp);
			sqlSession.commit();
		}finally
		{
			sqlSession.close();
		}
	}

}
