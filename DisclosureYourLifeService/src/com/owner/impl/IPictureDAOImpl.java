package com.owner.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import com.owner.dao.IPictureDAO;
import com.owner.domain.Picture;
import com.owner.tools.SessionFactory;

public class IPictureDAOImpl implements IPictureDAO {
	
	private SqlSessionFactory sessionFactory = SessionFactory.getInstance()  
            .getSqlSessionFactory();
	    
	@SuppressWarnings("unchecked")
	@Override
	public List<Picture> getAllPicture() {
		List<Picture> pictureList=null;
		SqlSession session = null;
		try {
			session = sessionFactory.openSession(); 
			pictureList=(List<Picture>)session.selectList("com.owner.domain.PictureMapper.selectAll");
		} finally
		{
			session.close();
		}
		return pictureList;
	}

	@Override
	public int insertPicture(Picture picture) {
		SqlSession session = null;
		int f=0;
		try {
			session=sessionFactory.openSession();
			f=session.insert("com.owner.domain.PictureMapper.insertPicture",picture);
			session.commit();
		} finally
		{
			session.close();
		}
		return f;
	}

	@Override
	public Picture queryPictureById(String pid) {
		SqlSession session = null;
		Picture Picture=new Picture();
		int f=0;
		try {
			session=sessionFactory.openSession();
			Picture=(Picture) session.selectOne("com.owner.domain.PictureMapper.selectPicture", pid);
		} finally
		{
			session.close();
		}
		return Picture;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Picture> queryPictureByEid(String eid) {
		List<Picture> pictureList=new ArrayList<Picture>();
		SqlSession session = null;
		try {
			session = sessionFactory.openSession(); 
			pictureList=session.selectList("com.owner.domain.PictureMapper.selectPictureByEid",eid);
		} finally
		{
			session.close();
		}
		return pictureList;
	}

	@Override
	public void insertPicture(List<Picture> pictures) {
		SqlSession sqlSession=null;
		try {
			sqlSession = sessionFactory.openSession(ExecutorType.BATCH, false);
			Map<String, List<Picture>> tmp = new HashMap<String, List<Picture>>();
			tmp.put("pictures", pictures);
			sqlSession.insert("com.owner.domain.PictureMapper.insertPictureBatch", tmp);
			sqlSession.commit();
		}finally
		{
			sqlSession.close();
		}
	}

}
