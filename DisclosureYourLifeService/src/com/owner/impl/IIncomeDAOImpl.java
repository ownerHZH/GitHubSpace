package com.owner.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import com.owner.dao.IIncomeDAO;
import com.owner.domain.Consume;
import com.owner.domain.Income;
import com.owner.tools.SessionFactory;

public class IIncomeDAOImpl implements IIncomeDAO {
	
	private SqlSessionFactory sessionFactory = SessionFactory.getInstance()  
            .getSqlSessionFactory();
	    
	@SuppressWarnings("unchecked")
	@Override
	public List<Income> getAllIncome(int pageno,int pagesize) {
		List<Income> IncomeList=null;
		SqlSession session = null;
		try {
			session = sessionFactory.openSession(); 
			Map<String, Integer> hashMap=new HashMap<String, Integer>();
			hashMap.put("pageno", pageno);
			hashMap.put("pagesize", pagesize);
			IncomeList=(List<Income>)session.selectList("com.owner.domain.IncomeMapper.selectAll",hashMap);
		} finally
		{
			session.close();
		}
		return IncomeList;
	}

	@Override
	public int insertIncome(Income income) {
		SqlSession session = null;
		int f=0;
		try {
			session=sessionFactory.openSession();
			session.insert("com.owner.domain.IncomeMapper.insertIncome",income);
			session.commit();
			f=1;
		} finally
		{
			session.close();
		}
		return f;
	}

	@Override
	public Income queryIncomeById(String id) {
		SqlSession session = null;
		Income Income=new Income();
		int f=0;
		try {
			session=sessionFactory.openSession();
			Income=(Income) session.selectOne("com.owner.domain.IncomeMapper.selectIncome", id);
		} finally
		{
			session.close();
		}
		return Income;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Income> queryIncomeByUid(String iid) {
		List<Income> IncomeList=new ArrayList<Income>();
		SqlSession session = null;
		try {
			session = sessionFactory.openSession(); 
			IncomeList=session.selectList("com.owner.domain.IncomeMapper.selectIncomeByUid",iid);
		} finally
		{
			session.close();
		}
		return IncomeList;
	}

	@Override
	public void insertIncome(List<Income> incomes) {
		SqlSession sqlSession=null;
		try {
			sqlSession = sessionFactory.openSession(ExecutorType.BATCH, false);
			Map<String, List<Income>> tmp = new HashMap<String, List<Income>>();
			tmp.put("incomes", incomes);
			sqlSession.insert("com.owner.domain.IncomeMapper.insertIncomeBatch", tmp);
			sqlSession.commit();
		}finally
		{
			sqlSession.close();
		}
	}

}
