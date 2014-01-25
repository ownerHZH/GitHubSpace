package com.owner.dao;

import java.util.List;

import com.owner.domain.Spinner;

public interface ISpinnerDAO {

	public List<Spinner> getAllSpinner();	

	public int insertSpinner(Spinner spinner);
	public void insertSpinner(List<Spinner> spinners);

	public void updateSpinnerById(int id,Spinner spinner);
		
}
