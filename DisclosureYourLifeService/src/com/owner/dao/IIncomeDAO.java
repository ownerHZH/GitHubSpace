package com.owner.dao;

import java.util.List;

import com.owner.domain.Consume;
import com.owner.domain.Income;

public interface IIncomeDAO {
	/**
	 * 获取数据库中所有的收入列表信息
	 * @return List<Income>
	 */
	public List<Income> getAllIncome(int pageno,int pagesize);
	
	/**
	 * 根据传递进来的收入实体添加到数据库
	 * @param income
	 * @return 1  成功 0 失败
	 */
	public int insertIncome(Income income);
	
	/**
	 * 根据id查询所在的信息 这个id是第多少的意思
	 * @param id
	 * @return 返回收入实体
	 */
	public Income queryIncomeById(String id);
	
	/**
	 * 根据用户的id查询这个用户所有的收入信息
	 * @param iid
	 * @return 返回这个用户所传递的所有信息
	 */
	public List<Income> queryIncomeByUid(String iid);
	/**
	 * 根据传递进来的收入实体列表添加到数据库
	 * @param List<Income>
	 * @return 1  成功 0 失败
	 */
	public void insertIncome(List<Income> incomes);
}
