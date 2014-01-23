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
			session.insert("com.owner.domain.UserMapper.insertUser",user);
			session.commit();
			f=user.getUid();
		} finally
		{
			session.close();
		}
		return f;
	}

	@Override
	public User queryUserById(User user) {
		SqlSession session = null;
		User u=new User();
		try {
			session=sessionFactory.openSession();
			Map<String,String> hashMap=new HashMap<String, String>();
			hashMap.put("phone", user.getPhone());
			hashMap.put("device", user.getDevice());
			u=(User) session.selectOne("com.owner.domain.UserMapper.selectUser", hashMap);
		} finally
		{
			session.close();
		}
		return u;
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
