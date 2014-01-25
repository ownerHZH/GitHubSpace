package com.owner.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import com.owner.dao.ISpinnerDAO;
import com.owner.domain.Spinner;
import com.owner.tools.SessionFactory;

public class ISpinnerDAOImpl implements ISpinnerDAO {
	
	private SqlSessionFactory sessionFactory = SessionFactory.getInstance()  
            .getSqlSessionFactory();
	    
	@SuppressWarnings("unchecked")
	@Override
	public List<Spinner> getAllSpinner() {
		List<Spinner> spinnerList=null;
		SqlSession session = null;
		try {
			session = sessionFactory.openSession(); 
			spinnerList=(List<Spinner>)session.selectList("com.owner.domain.SpinnerMapper.selectAll");
		} finally
		{
			session.close();
		}
		return spinnerList;
	}

	@Override
	public int insertSpinner(Spinner spinner) {
		SqlSession session = null;
		int f=0;
		try {
			session=sessionFactory.openSession();
			session.insert("com.owner.domain.SpinnerMapper.insertSpinner",spinner);
			session.commit();
			f=spinner.getId();
		} finally
		{
			session.close();
		}
		return f;
	}

	@Override
	public void updateSpinnerById(int id,Spinner spinner) {
		SqlSession session = null;
		Spinner u=new Spinner();
		try {
			session=sessionFactory.openSession();
			Map<String,Integer> hashMap=new HashMap<String, Integer>();
			hashMap.put("id", id);
			session.update("com.owner.domain.SpinnerMapper.updateSpinner", hashMap);
			session.commit();
		} finally
		{
			session.close();
		}
	}

	@Override
	public void insertSpinner(List<Spinner> spinners) {
		SqlSession sqlSession=null;
		try {
			sqlSession = sessionFactory.openSession(ExecutorType.BATCH, false);
			Map<String, List<Spinner>> tmp = new HashMap<String, List<Spinner>>();
			tmp.put("spinners", spinners);
			sqlSession.insert("com.owner.domain.SpinnerMapper.insertSpinnerBatch", tmp);
			sqlSession.commit();
		}finally
		{
			sqlSession.close();
		}
	}

}
