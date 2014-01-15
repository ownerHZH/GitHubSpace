package com.owner.dao;

import java.util.List;

import com.owner.domain.Embarrass;

public interface IEmbarrassDAO {
	/**
	 * 获取数据库中所有的糗事儿列表信息
	 * @return List<Embarrass>
	 */
	public List<Embarrass> getAllEmbarrass(int pageno,int pagesize);
	
	/**
	 * 根据传递进来的糗事儿实体添加到数据库
	 * @param Embarrass
	 * @return 1  成功 0 失败
	 */
	public int insertEmbarrass(Embarrass embarrass);
	
	/**
	 * 根据传递进来的糗事儿实体列表添加到数据库
	 * @param Embarrass
	 * @return 1  成功 0 失败
	 */
	public void insertEmbarrass(List<Embarrass> embarrasses);
	
	/**
	 * 根据id查询所在的信息 这个id是第多少的意思
	 * @param id
	 * @return 返回糗事儿实体
	 */
	public Embarrass queryEmbarrassById(String eid);
	
	/**
	 * 根据用户的id查询这个用户所有的糗事儿信息
	 * @param iid
	 * @return 返回这个用户所传递的所有信息
	 */
	public List<Embarrass> queryEmbarrassByUid(String uid);
}
