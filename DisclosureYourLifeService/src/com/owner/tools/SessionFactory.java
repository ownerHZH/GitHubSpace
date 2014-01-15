package com.owner.tools;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SessionFactory {
	private String resource="com/owner/tools/sqlMapConfig.xml";  
    private SqlSessionFactory sqlSessionFactory=null;  
    private static SessionFactory sessionFactory=new SessionFactory();  
      
    private SessionFactory() {  
        try {  
            Reader reader=Resources.getResourceAsReader(resource);  
            sqlSessionFactory=new SqlSessionFactoryBuilder().build(reader);  
        } catch (IOException e) {  
            System.out.println("#IOException happened in initialising the SessionFactory:"+e.getMessage());  
            throw new ExceptionInInitializerError(e);  
        }  
    }  
    public static SessionFactory getInstance() {  
        return sessionFactory;  
    }  
    public SqlSessionFactory getSqlSessionFactory() {  
        return sqlSessionFactory;  
    }  
}
