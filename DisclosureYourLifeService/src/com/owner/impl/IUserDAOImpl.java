package com.owner.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import com.owner.dao.IUserDAO;
import com.owner.domain.User;
import com.owner.tools.SessionFactory;

public class IUserDAOImpl implements IUserDAO {
	
	private SqlSessionFactory sessionFactory = SessionFactory.getInstance()  
            .getSqlSessionFactory();
	    
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllUser() {
		List<User> UserList=null;
		SqlSession session = null;
		try {
			session = sessionFactory.openSession(); 
			UserList=(List<User>)session.selectList("com.owner.domain.UserMapper.selectAll");
		} finally
		{
			session.close();
		}
		return UserList;
	}

	@Override
	public int insertUser(User user) {
		SqlSession session = null;
		int f=0;
		try {
			session=sessionFactory.openSession();
			f=session.insert("com.owner.domain.UserMapper.insertUser",user);
			session.commit();
		} finally
		{
			session.close();
		}
		return f;
	}

	@Override
	public User queryUserById(String uid) {
		SqlSession session = null;
		User User=new User();
		int f=0;
		try {
			session=sessionFactory.openSession();
			User=(User) session.selectOne("com.owner.domain.UserMapper.selectUser", uid);
		} finally
		{
			session.close();
		}
		return User;
	}

	@Override
	public void insertUser(List<User> users) {
		SqlSession sqlSession=null;
		try {
			sqlSession = sessionFactory.openSession(ExecutorType.BATCH, false);
			Map<String, List<User>> tmp = new HashMap<String, List<User>>();
			tmp.put("users", users);
			sqlSession.insert("com.owner.domain.UserMapper.insertUserBatch", tmp);
			sqlSession.commit();
		}finally
		{
			sqlSession.close();
		}
	}

}
