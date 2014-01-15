package com.owner.dao;

import java.util.List;

import com.owner.domain.User;

public interface IUserDAO {
	/**
	 * 获取数据库中所有的用户列表信息
	 * @return List<User>
	 */
	public List<User> getAllUser();
	
	/**
	 * 根据传递进来的用户实体添加到数据库
	 * @param User
	 * @return 1  成功 0 失败
	 */
	public int insertUser(User User);
	
	/**
	 * 根据id查询所在的信息 这个id是第多少的意思
	 * @param pid
	 * @return 返回用户实体
	 */
	public User queryUserById(String uid);
	
	
	/**
	 * 根据传递进来的用户实体列表添加到数据库
	 * @param List<User>
	 * @return 1  成功 0 失败
	 */
	public void insertUser(List<User> users);
}
