package com.owner.dao;

import java.util.List;

import com.owner.domain.PlainLook;

public interface IPlainLookDAO {
	/**
	 * 获取数据库中所有的图片列表信息
	 * @return List<PlainLook>
	 */
	public List<PlainLook> getAllPlainLook();
	
	/**
	 * 根据传递进来的图片实体添加到数据库
	 * @param PlainLook
	 * @return 1  成功 0 失败
	 */
	public int insertPlainLook(PlainLook plainLook);
	
	/**
	 * 根据id查询所在的信息 这个id是第多少的意思
	 * @param pid
	 * @return 返回图片实体
	 */
	public PlainLook queryPlainLookById(String pid);
	
	/**
	 * 根据用户的id查询这个用户所有的图片信息
	 * @param eid
	 * @return 返回这个用户所传递的所有信息
	 */
	public List<PlainLook> queryPlainLookByUid(String uid);
	
	/**
	 * 根据传递进来的图片实体列表添加到数据库
	 * @param List<PlainLook>
	 * @return 1  成功 0 失败
	 */
	public void insertPlainLook(List<PlainLook> plainLooks);
	
	/*
	 * 更新点赞数
	 */
	public void updatePlainLookCount(String gobid);
	/**
	 * 更新被踩数
	 * @param id
	 */
	public void updatePlainLookBCount(String gobid);
}
