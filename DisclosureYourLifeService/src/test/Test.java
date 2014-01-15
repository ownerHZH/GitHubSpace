package test;

import java.util.ArrayList;
import java.util.List;

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

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*IConsumeDAO dao=new IConsumeDAOImpl();
		List<Consume> consumes=new ArrayList<Consume>();
		Consume consume1=new Consume(1,"买房子1",2001.0);
		Consume consume2=new Consume(1,"买房子2",2020.0);
		Consume consume3=new Consume(1,"买房子3",2030.0);
		Consume consume4=new Consume(1,"买房子4",2040.0);
		Consume consume5=new Consume(1,"买房子5",2050.0);
		consumes.add(consume1);
		consumes.add(consume2);
		consumes.add(consume3);
		consumes.add(consume4);
		consumes.add(consume5);
		dao.insertConsume(consumes);*/
		/*IIncomeDAO dao=new IIncomeDAOImpl();
		List<Income> consumes=new ArrayList<Income>();
		Income consume1=new Income(1,"买房子1",2001.0);
		Income consume2=new Income(1,"买房子2",2020.0);
		Income consume3=new Income(1,"买房子3",2030.0);
		Income consume4=new Income(1,"买房子4",2040.0);
		Income consume5=new Income(1,"买房子5",2050.0);
		consumes.add(consume1);
		consumes.add(consume2);
		consumes.add(consume3);
		consumes.add(consume4);
		consumes.add(consume5);
		dao.insertIncome(consumes);
		System.out.println("=====插入单个===="+dao.insertIncome(consume1));*/
		/*IEmbarrassDAO dao=new IEmbarrassDAOImpl();
		List<Embarrass> consumes=new ArrayList<Embarrass>();
		Embarrass consume1=new Embarrass(1,"买房子1","买房子1","http://gaoxiao.jokeji.cn/UpFilesnew/2013/8/6/20138621250385.jpg");
		Embarrass consume2=new Embarrass(1,"买房子1","买房子1","http://gaoxiao.jokeji.cn/UpFilesnew/2013/8/6/20138621250385.jpg");
		Embarrass consume3=new Embarrass(1,"买房子1","买房子1","http://gaoxiao.jokeji.cn/UpFilesnew/2013/8/6/20138621250385.jpg");
		Embarrass consume4=new Embarrass(1,"买房子1","买房子1","http://gaoxiao.jokeji.cn/UpFilesnew/2013/8/6/20138621250385.jpg");
		Embarrass consume5=new Embarrass(1,"买房子1","买房子1","http://gaoxiao.jokeji.cn/UpFilesnew/2013/8/6/20138621250385.jpg");
		consumes.add(consume1);
		consumes.add(consume2);
		consumes.add(consume3);
		consumes.add(consume4);
		consumes.add(consume5);
		dao.insertEmbarrass(consumes);
		System.out.println("=====插入单个===="+dao.insertEmbarrass(consume1));*/
		/*IPictureDAO dao=new IPictureDAOImpl();
	    ArrayList<Picture> pList=(ArrayList<Picture>) dao.queryPictureByEid(1+"");
	    for(int i=0;i<pList.size();i++)
	    {
	    	System.out.println(pList.get(i).toString());
	    }*/
		IEmbarrassDAO dao=new IEmbarrassDAOImpl();
		IPictureDAO pdao=new IPictureDAOImpl();
        List<Embarrass> embarrassList = null;
		embarrassList = dao.getAllEmbarrass(0,5);
		for(int i=0;i<embarrassList.size();i++)
		{
			embarrassList.get(i).setPics(
					pdao.queryPictureByEid(embarrassList.get(i).getEid()+""));
		}
		for(int i=0;i<embarrassList.size();i++)
		{
			System.out.println(embarrassList.get(i).toString());
		}
	    
	}

}
