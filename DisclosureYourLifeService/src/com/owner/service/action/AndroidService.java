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
import com.owner.domain.Consume;
import com.owner.domain.Embarrass;
import com.owner.domain.Income;
import com.owner.domain.JsonEntity;
import com.owner.domain.Picture;
import com.owner.impl.IConsumeDAOImpl;
import com.owner.impl.IEmbarrassDAOImpl;
import com.owner.impl.IIncomeDAOImpl;
import com.owner.impl.IPictureDAOImpl;
import com.owner.tools.GsonTool;

public class AndroidService extends ActionSupport implements ServletRequestAware, ServletResponseAware {
 
	private static final long serialVersionUID = 1L;
	private HttpServletResponse response;
	private HttpServletRequest request;
    
	//获取上传的图片所需字段
	private File[] pictures;
	private String[] picturesFileName;	
	
	//获取消费和收入提交的数据字段 和客户端上传时一一对应
	private String consumes;
	private String incomes;
	private String embarrass;
	
	private int pageno=0;//分页的字段
	private int pagesize=10;
	
	private int eid;//查询图片的eid号
	
	public void loadMorePicture()
	{
		IPictureDAO dao=new IPictureDAOImpl();
		JsonEntity jsonEntity = new JsonEntity();
		
		try {
			ArrayList<Picture> pList=(ArrayList<Picture>) dao.queryPictureByEid(eid+"");
			jsonEntity.setData(GsonTool.getGson().toJson(pList));
			jsonEntity.setMsg("数据返回成功");
			jsonEntity.setStatus(0);
			jsonEntity.setTotal(pList.size());
		} catch (Exception e1) {
			jsonEntity.setData("");
			jsonEntity.setMsg("数据返回失败");
			jsonEntity.setStatus(1);
			jsonEntity.setTotal(0);
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
	
	public void consumeUpload()
	{   
		ArrayList<Consume> cList=GsonTool.getGson().fromJson(consumes,GsonTool.type_consumeList);
		if(cList.size()>0)
		{
			IConsumeDAO dao = new IConsumeDAOImpl();
			JsonEntity jsonEntity = new JsonEntity();
			jsonEntity.setData("");						
			jsonEntity.setTotal(0);
			
			try {
				dao.insertConsume(cList);
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
	}
	
	public void incomeUpload()
	{
		ArrayList<Income> cList=GsonTool.getGson().fromJson(incomes,GsonTool.type_incomeList);
		if(cList.size()>0)
		{
			IIncomeDAO dao = new IIncomeDAOImpl();
			JsonEntity jsonEntity = new JsonEntity();
			jsonEntity.setData("");						
			jsonEntity.setTotal(0);
			
			try {
				dao.insertIncome(cList);
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
	}
	
	public void getUploadFile()
    {
		int eid=0;//插入返回的主键ID
		final String path =ServletActionContext.getServletContext().getRealPath("/");
		final String spath="http://192.168.1.150:8080/DisclosureYourLife/";// 图片所在服务器的路径
		
		List<Picture> pics=new ArrayList<Picture>();//保存上传图片的路径
		Embarrass em=GsonTool.getGson().fromJson(embarrass,GsonTool.type_embarrass);
		if(picturesFileName!=null&&picturesFileName.length>0)
		{
			em.setEpic(spath+picturesFileName[0]);
		}
		System.out.println("======上传的实体========="+em.toString());
		JsonEntity jsonEntity=null;
		try {
			IEmbarrassDAO dao = new IEmbarrassDAOImpl();
			eid=dao.insertEmbarrass(em);//插入糗事儿数据
			if(eid>0)
			{
				saveFile(path,pics,eid,spath);//把传递上来的文件保存到服务器
				IPictureDAO pdao = new IPictureDAOImpl();
				if(pics!=null&&pics.size()>0)
				{
					pdao.insertPicture(pics);
				}
							
				jsonEntity = new JsonEntity();
				jsonEntity.setData("");
				jsonEntity.setMsg("查询保存成功");
				jsonEntity.setStatus(0);
				jsonEntity.setTotal(0);
			}
		} catch (Exception e1) {
			jsonEntity = new JsonEntity();
			jsonEntity.setData("");
			jsonEntity.setMsg("查询保存失败");
			jsonEntity.setStatus(1);
			jsonEntity.setTotal(0);
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

	private void saveFile(final String path, List<Picture> pics, int eid,final String spath) {
		if(pictures!=null&&pictures.length>0)
		{
			for (int i = 0; i < pictures.length; i++) {
				OutputStream output = null;// 输出流
				InputStream input = null;// 输入流
				System.out.println("====name===" + picturesFileName[i]);
				File f = new File(path, picturesFileName[i]);
				System.out.println("====·保存的全路径===" + path
						+ picturesFileName[i]);
				saveToList(spath, pics, eid, i);//把图片信息保存到List当中
				try {
					// 把.tmp后缀的文件数据保存到我们要得到的文件当中
					input = new FileInputStream(pictures[i]);
					output = new FileOutputStream(f);
					int c = 0;
					byte[] b = new byte[1024];
					while ((c = input.read(b)) != -1) {
						output.write(b, 0, c);
					}
					output.flush();
					input.close();
					output.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}	
	}

	/**
	 * 把图片信息保存到List当中，为了后面保存到picture表当中
	 * @param path 路径
	 * @param pics 全路径
	 * @param eid  picture当中的主键
	 * @param i    索引
	 */
	private void saveToList(final String spath, List<Picture> pics, int eid,
			int i) {
		Picture p = new Picture();
		p.setEid(eid);
		p.setPpath(spath);
		p.setPname(picturesFileName[i]);
		p.setPic(spath+picturesFileName[i]);
		pics.add(p);
	}
	
	//获取糗事儿列表的action
	public void getEmbarrassList()
    {
		IEmbarrassDAO dao=new IEmbarrassDAOImpl();
		IPictureDAO pdao=new IPictureDAOImpl();
		
        System.out.println(this.request.getParameter("did"));//传递进来的参数
        List<Embarrass> embarrassList = null;
        JsonEntity jsonEntity=null;
		try {
			embarrassList = dao.getAllEmbarrass(pageno,pagesize);
			for(int i=0;i<embarrassList.size();i++)
			{
				embarrassList.get(i).setPics(
						pdao.queryPictureByEid(embarrassList.get(i).getEid()+""));
			}
			jsonEntity=new JsonEntity();
	        jsonEntity.setData(GsonTool.getGson().toJson(embarrassList).toString());
	        jsonEntity.setMsg("获取数据成功");
	        jsonEntity.setStatus(0);
	        jsonEntity.setTotal(embarrassList.size());
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
	
	//获取收入列表的action
	public void getIncomeList()
    {
		IIncomeDAO dao=new IIncomeDAOImpl();
        System.out.println(this.request.getParameter("did"));//传递进来的参数
        List<Income> incomeList = null;
        JsonEntity jsonEntity=null;
		try {
			incomeList = dao.getAllIncome(pageno,pagesize);
			jsonEntity=new JsonEntity();
	        jsonEntity.setData(GsonTool.getGson().toJson(incomeList).toString());
	        jsonEntity.setMsg("获取数据成功");
	        jsonEntity.setStatus(0);
	        jsonEntity.setTotal(incomeList.size());
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
		
	//获取Consume列表action
	public void getConsumeList()
    {		
		IConsumeDAO dao=new IConsumeDAOImpl();
        System.out.println(this.request.getParameter("did"));        
        List<Consume> movieList = null;
		try {
			movieList = dao.getAllConsume(pageno,pagesize);
		} catch (Exception e1) {
			System.out.println("===================查询出错=====");
			e1.printStackTrace();
		}
        JsonEntity jsonEntity=new JsonEntity();
        jsonEntity.setData(GsonTool.getGson().toJson(movieList).toString());
        jsonEntity.setMsg("查询数据成功");
        jsonEntity.setStatus(0);
        jsonEntity.setTotal(movieList.size());
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
	
	public File[] getPictures() {
		return pictures;
	}

	public void setPictures(File[] pictures) {
		if(pictures!=null&&pictures.length>0)
		{
			this.pictures = pictures;
		}else {
			this.pictures=null;
		}		
	}

	public String[] getPicturesFileName() {
		return picturesFileName;
	}

	public void setPicturesFileName(String[] picturesFileName) {
		if(picturesFileName!=null&&picturesFileName.length>0)
		{
			for(int i=0;i<picturesFileName.length;i++)
			{
				picturesFileName[i]=picturesFileName[i].replaceAll("%", "_");
			}
			this.picturesFileName = picturesFileName;
		}else {
			this.picturesFileName=null;
		}
		
	}

	public String getConsumes() {
		return consumes;
	}

	public void setConsumes(String consumes) {
		this.consumes = consumes;
	}

	public String getIncomes() {
		return incomes;
	}

	public void setIncomes(String incomes) {
		this.incomes = incomes;
	}

	public String getEmbarrass() {
		return embarrass;
	}

	public void setEmbarrass(String embarrass) {
		this.embarrass = embarrass;
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

	public int getEid() {
		return eid;
	}

	public void setEid(int eid) {
		this.eid = eid;
	}
}
