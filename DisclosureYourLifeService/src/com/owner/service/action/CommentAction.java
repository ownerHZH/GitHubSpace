package com.owner.service.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.owner.dao.IConsumeCommentDAO;
import com.owner.dao.IEmbarrassCommentDAO;
import com.owner.dao.IIncomeCommentDAO;
import com.owner.domain.ConsumeComment;
import com.owner.domain.EmbarrassComment;
import com.owner.domain.IncomeComment;
import com.owner.domain.JsonEntity;
import com.owner.impl.IConsumeCommentDAOImpl;
import com.owner.impl.IEmbarrassCommentDAOImpl;
import com.owner.impl.IIncomeCommentDAOImpl;
import com.owner.tools.GsonTool;

public class CommentAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	private static final long serialVersionUID = -5270238115526523577L;
	private HttpServletResponse response;
	private HttpServletRequest request;
	
	private String consumeComment;
	private String incomeComment;
	private String embarrassComment;
	private int pageno=0;//分页的字段
	private int pagesize=10;
	
	//提交消费评论
	public void ConsumeCommentCommit(){
		ConsumeComment cc=GsonTool.getGson().fromJson(consumeComment,GsonTool.type_consumeComment);

		IConsumeCommentDAO dao = new IConsumeCommentDAOImpl();
		JsonEntity jsonEntity = new JsonEntity();
		jsonEntity.setData("");						
		jsonEntity.setTotal(0);
		
		try {
			dao.insertConsumeComment(cc);
			jsonEntity.setMsg("数据插入成功");
			jsonEntity.setStatus(0);//成功
		} catch (Exception e1) {
			jsonEntity.setMsg("数据插入失败");
			jsonEntity.setStatus(1);//失败
			e1.printStackTrace();
		}	
		try {
			this.response.setCharacterEncoding("UTF-8");
			this.response.setContentType("text/html;charset=UTF-8");
			this.response.getWriter().write(
					GsonTool.getGson().toJson(jsonEntity));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//提交收入评论
	public void IncomeCommentCommit(){
		IncomeComment ic=GsonTool.getGson().fromJson(incomeComment,GsonTool.type_incomeComment);

		IIncomeCommentDAO dao = new IIncomeCommentDAOImpl();
		JsonEntity jsonEntity = new JsonEntity();
		jsonEntity.setData("");						
		jsonEntity.setTotal(0);
		
		try {
			dao.insertIncomeComment(ic);
			jsonEntity.setMsg("数据插入成功");
			jsonEntity.setStatus(0);//成功
		} catch (Exception e1) {
			jsonEntity.setMsg("数据插入失败");
			jsonEntity.setStatus(1);//失败
			e1.printStackTrace();
		}	
		try {
			this.response.setCharacterEncoding("UTF-8");
			this.response.setContentType("text/html;charset=UTF-8");
			this.response.getWriter().write(
					GsonTool.getGson().toJson(jsonEntity));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//提交糗事儿评论
	public void EmbarrassCommentCommit(){
		EmbarrassComment ec=GsonTool.getGson().fromJson(embarrassComment,GsonTool.type_embarrassComment);
		
		IEmbarrassCommentDAO dao = new IEmbarrassCommentDAOImpl();
		JsonEntity jsonEntity = new JsonEntity();
		jsonEntity.setData("");						
		jsonEntity.setTotal(0);
		
		try {
			dao.insertEmbarrassComment(ec);
			jsonEntity.setMsg("数据插入成功");
			jsonEntity.setStatus(0);//成功
		} catch (Exception e1) {
			jsonEntity.setMsg("数据插入失败");
			jsonEntity.setStatus(1);//失败
			e1.printStackTrace();
		}	
		try {
			this.response.setCharacterEncoding("UTF-8");
			this.response.setContentType("text/html;charset=UTF-8");
			this.response.getWriter().write(
					GsonTool.getGson().toJson(jsonEntity));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//查询消费评论
	public void QueryConsumeComment(){
		IConsumeCommentDAO dao=new IConsumeCommentDAOImpl();
        List<ConsumeComment> consumeCommentList = null;
        JsonEntity jsonEntity=new JsonEntity();
		try {
			consumeCommentList = dao.getAllConsumeComment(pageno,pagesize);
	        jsonEntity.setData(GsonTool.getGson().toJson(consumeCommentList).toString());
	        jsonEntity.setMsg("获取数据成功");
	        jsonEntity.setStatus(0);
	        jsonEntity.setTotal(consumeCommentList.size());
		} catch (Exception e1) {
			System.out.println("===================查询出错=====");
			e1.printStackTrace();
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
	//查询收入评论
	public void QueryIncomeComment(){
		IIncomeCommentDAO dao=new IIncomeCommentDAOImpl();
        List<IncomeComment> incomeCommentList = null;
        JsonEntity jsonEntity=new JsonEntity();
		try {
			incomeCommentList = dao.getAllIncomeComment(pageno,pagesize);
	        jsonEntity.setData(GsonTool.getGson().toJson(incomeCommentList).toString());
	        jsonEntity.setMsg("获取数据成功");
	        jsonEntity.setStatus(0);
	        jsonEntity.setTotal(incomeCommentList.size());
		} catch (Exception e1) {
			System.out.println("===================查询出错=====");
			e1.printStackTrace();
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
	//查询糗事儿评论
	public void QueryEmbarrassComment(){
		IEmbarrassCommentDAO dao=new IEmbarrassCommentDAOImpl();
        List<EmbarrassComment> embarrassCommentList = null;
        JsonEntity jsonEntity=new JsonEntity();
		try {
			embarrassCommentList = dao.getAllEmbarrassComment(pageno,pagesize);
	        jsonEntity.setData(GsonTool.getGson().toJson(embarrassCommentList).toString());
	        jsonEntity.setMsg("获取数据成功");
	        jsonEntity.setStatus(0);
	        jsonEntity.setTotal(embarrassCommentList.size());
		} catch (Exception e1) {
			System.out.println("===================查询出错=====");
			e1.printStackTrace();
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
	public String getConsumeComment() {
		return consumeComment;
	}
	public void setConsumeComment(String consumeComment) {
		this.consumeComment = consumeComment;
	}
	public String getIncomeComment() {
		return incomeComment;
	}
	public void setIncomeComment(String incomeComment) {
		this.incomeComment = incomeComment;
	}
	public String getEmbarrassComment() {
		return embarrassComment;
	}
	public void setEmbarrassComment(String embarrassComment) {
		this.embarrassComment = embarrassComment;
	}
	public int getPageno() {
		return pageno;
	}
	public void setPageno(int pageno) {
		this.pageno = pageno;
	}
	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

}
