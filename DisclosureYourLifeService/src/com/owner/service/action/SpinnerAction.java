package com.owner.service.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.owner.dao.IConsumeDAO;
import com.owner.dao.IEmbarrassDAO;
import com.owner.dao.IIncomeDAO;
import com.owner.dao.IPictureDAO;
import com.owner.dao.IPlainLookDAO;
import com.owner.dao.ISpinnerDAO;
import com.owner.dao.IUserDAO;
import com.owner.domain.Consume;
import com.owner.domain.Embarrass;
import com.owner.domain.Income;
import com.owner.domain.JsonEntity;
import com.owner.domain.Picture;
import com.owner.domain.PlainLook;
import com.owner.domain.Spinner;
import com.owner.domain.User;
import com.owner.impl.IConsumeDAOImpl;
import com.owner.impl.IEmbarrassDAOImpl;
import com.owner.impl.IIncomeDAOImpl;
import com.owner.impl.IPictureDAOImpl;
import com.owner.impl.IPlainLookDAOImpl;
import com.owner.impl.ISpinnerDAOImpl;
import com.owner.impl.IUserDAOImpl;
import com.owner.tools.Constant;
import com.owner.tools.GsonTool;

public class SpinnerAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = -9061610251451412793L;
	private HttpServletResponse response;
	private HttpServletRequest request;
	
	public void getSpinner()
    {
		ISpinnerDAO dao=new ISpinnerDAOImpl();		
        String receiveStr="";
        JsonEntity jsonEntity=null;
	try {
		List<Spinner> spinners=dao.getAllSpinner();
		receiveStr=GsonTool.getGson().toJson(spinners).toString();
		
		jsonEntity=new JsonEntity();
        jsonEntity.setData(receiveStr);
        jsonEntity.setMsg("获取数据成功");
        jsonEntity.setStatus(0);
        jsonEntity.setTotal(1);
	} catch (Exception e1) {
		System.out.println("===================查询出错=====");
		e1.printStackTrace();
		jsonEntity=new JsonEntity();
        jsonEntity.setData("空");
        jsonEntity.setMsg("获取数据失败");
        jsonEntity.setStatus(1);
        jsonEntity.setTotal(0);
	}
        
        try {  
            this.response.setCharacterEncoding("UTF-8"); 
            this.response.setContentType("text/html;charset=UTF-8");
            this.response.getWriter().write(GsonTool.getGson().toJson(jsonEntity));
        } catch (IOException e) {  
            e.printStackTrace();  
        }
    }


	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response=response;
	}


	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
	}
	
}
