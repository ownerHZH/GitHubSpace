package com.owner.dao;

import java.util.List;

import com.owner.domain.Picture;

public interface IPictureDAO {
	/**
	 * 获取数据库中所有的图片列表信息
	 * @return List<Picture>
	 */
	public List<Picture> getAllPicture();
	
	/**
	 * 根据传递进来的图片实体添加到数据库
	 * @param Picture
	 * @return 1  成功 0 失败
	 */
	public int insertPicture(Picture Picture);
	
	/**
	 * 根据id查询所在的信息 这个id是第多少的意思
	 * @param pid
	 * @return 返回图片实体
	 */
	public Picture queryPictureById(String pid);
	
	/**
	 * 根据用户的id查询这个用户所有的图片信息
	 * @param eid
	 * @return 返回这个用户所传递的所有信息
	 */
	public List<Picture> queryPictureByEid(String eid);
	
	/**
	 * 根据传递进来的图片实体列表添加到数据库
	 * @param List<Picture>
	 * @return 1  成功 0 失败
	 */
	public void insertPicture(List<Picture> pictures);
}
