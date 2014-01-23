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
import com.owner.domain.Consume;
import com.owner.domain.Embarrass;
import com.owner.domain.Income;
import com.owner.domain.JsonEntity;
import com.owner.domain.Picture;
import com.owner.domain.PlainLook;
import com.owner.impl.IConsumeDAOImpl;
import com.owner.impl.IEmbarrassDAOImpl;
import com.owner.impl.IIncomeDAOImpl;
import com.owner.impl.IPictureDAOImpl;
import com.owner.impl.IPlainLookDAOImpl;
import com.owner.tools.Constant;
import com.owner.tools.GsonTool;

public class PlainLookAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 2968249033954126435L;
	private HttpServletResponse response;
	private HttpServletRequest request;
    
	//获取上传的图片所需字段
	private File[] pictures;
	private String[] picturesFileName;	
	
	private String plainLook;
	
	private int pageno=0;//分页的字段
	private int pagesize=10;	
	
	private PlainLook plainLookEntity;
	
	private String gobid;
	private String whichForm;//是以何种排名方式显示数据  "good"  "bad"
	public void updatePlainLookCount()
	{
		//传递的数据格式为：{ 例： 37+good or 37+bad }
		IPlainLookDAO dao = new IPlainLookDAOImpl();		
		JsonEntity jsonEntity=null;
		try {
			    System.out.println("id为"+gobid);
			    String[] goodOrBad=gobid.split("[+]");
			    if(goodOrBad[1].trim().equals("good"))
			    {
			    	dao.updatePlainLookCount(goodOrBad[0].trim());
			    }else
			    {
			    	dao.updatePlainLookBCount(goodOrBad[0].trim());
			    }
			    				    
				jsonEntity = new JsonEntity();
				jsonEntity.setData("");
				jsonEntity.setMsg("上传保存成功");
				jsonEntity.setStatus(0);
				jsonEntity.setTotal(0);
		} catch (Exception e1) {
			jsonEntity = new JsonEntity();
			jsonEntity.setData("");
			jsonEntity.setMsg("上传保存失败");
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
			
	public void uploadPlainLook()
    {
		final String path =ServletActionContext.getServletContext().getRealPath("/")+"plainLookPictures\\";
		final String spath=Constant.FileSavedPathString+"plainLookPictures/";// 图片所在服务器的路径
		plainLookEntity=GsonTool.getGson().fromJson(plainLook, GsonTool.type_plainLook);
		List<PlainLook> plainLooks=new ArrayList<PlainLook>();//保存上传图片的路径
		JsonEntity jsonEntity=null;
		try {
			    IPlainLookDAO dao = new IPlainLookDAOImpl();
				saveFile(path,plainLooks,spath);//把传递上来的文件保存到服务器
				if(plainLooks!=null&&plainLooks.size()>0)
				{
					dao.insertPlainLook(plainLooks);
				}
							
				jsonEntity = new JsonEntity();
				jsonEntity.setData("");
				jsonEntity.setMsg("上传保存成功");
				jsonEntity.setStatus(0);
				jsonEntity.setTotal(0);
		} catch (Exception e1) {
			jsonEntity = new JsonEntity();
			jsonEntity.setData("");
			jsonEntity.setMsg("上传保存失败");
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

	private void saveFile(final String path, List<PlainLook> plainLooks,final String spath) {
		if(pictures!=null&&pictures.length>0)
		{
			for (int i = 0; i < pictures.length; i++) {
				OutputStream output = null;// 输出流
				InputStream input = null;// 输入流
				System.out.println("====name===" + picturesFileName[i]);
				File dir=new File(path);
				if(!dir.exists())
				{
					dir.mkdirs();
				}
				File f = new File(path, picturesFileName[i]);
				System.out.println("====·保存的全路径===" + path
						+ picturesFileName[i]);
				saveToList(spath, plainLooks,i);//把图片信息保存到List当中
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
	 * 把图片信息保存到List当中，为了后面保存到plainLook表当中
	 * @param path 路径
	 * @param pics 全路径
	 * @param i    索引
	 */
	private void saveToList(final String spath, List<PlainLook> plainLooks,
			int i) {
		PlainLook p = new PlainLook();
		//plainLookEntity
		p.setUid(plainLookEntity.getUid());
		p.setTitle(plainLookEntity.getTitle());
		p.setPath(spath+picturesFileName[i]);
		plainLooks.add(p);
	}
	
	public void getPlainLookList()
    {
		IPlainLookDAO dao=new IPlainLookDAOImpl();		
        List<PlainLook> plainLookList = null;
        JsonEntity jsonEntity=null;
		try {
			if(whichForm.equals("good"))
			{
				plainLookList = dao.getAllPlainLook(pageno,pagesize);
			}else
			{
				plainLookList = dao.getAllPlainLookByBcount(pageno, pagesize);
			}
			
			jsonEntity=new JsonEntity();
	        jsonEntity.setData(GsonTool.getGson().toJson(plainLookList).toString());
	        jsonEntity.setMsg("获取数据成功");
	        jsonEntity.setStatus(0);
	        jsonEntity.setTotal(plainLookList.size());
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

	public String getPlainLook() {
		return plainLook;
	}

	public void setPlainLook(String plainLook) {
		this.plainLook = plainLook;
	}

	public String getGobid() {
		return gobid;
	}

	public void setGobid(String gobid) {
		this.gobid = gobid;
	}

	public String getWhichForm() {
		return whichForm;
	}

	public void setWhichForm(String whichForm) {
		this.whichForm = whichForm;
	}
}
