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
import com.owner.dao.IUserDAO;
import com.owner.domain.Consume;
import com.owner.domain.Embarrass;
import com.owner.domain.Income;
import com.owner.domain.JsonEntity;
import com.owner.domain.Picture;
import com.owner.domain.PlainLook;
import com.owner.domain.User;
import com.owner.impl.IConsumeDAOImpl;
import com.owner.impl.IEmbarrassDAOImpl;
import com.owner.impl.IIncomeDAOImpl;
import com.owner.impl.IPictureDAOImpl;
import com.owner.impl.IPlainLookDAOImpl;
import com.owner.impl.IUserDAOImpl;
import com.owner.tools.Constant;
import com.owner.tools.GsonTool;

public class UserAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 2968249033954126435L;
	private HttpServletResponse response;
	private HttpServletRequest request;

	private String user;
	
	public void addUser()
    {
		IUserDAO dao=new IUserDAOImpl();		
        User u=GsonTool.getGson().fromJson(user, GsonTool.type_user);
        int uid=0;
        String receiveStr;
        JsonEntity jsonEntity=null;
	try {
			User uq=dao.queryUserById(u);
			if(uq==null||uq.equals(null))
			{
				uid=dao.insertUser(u);
				u.setUid(uid);
				receiveStr=GsonTool.getGson().toJson(u).toString();
			}else
			{
				receiveStr=GsonTool.getGson().toJson(uq).toString();
			}
			
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


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}
	
}
