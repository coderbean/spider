package com.jm.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;
/**
 * 用枚举模式写的单例模式，确保只有一个线程池
 * @author Jacob
 *
 */
public enum DbPool{
    /**
    * 定义一个枚举的元素，它就代表了Singleton的一个实例
    **/
	INSTANCE;
    /**
     * 相当于构造函数
     */
    DbPool(){
    	System.setProperty("com.mchange.v2.c3p0.cfg.xml",System.getProperty("com.mchange.v2.c3p0.cfg.xml"));
    	dataSource=new ComboPooledDataSource();
    }
    private ComboPooledDataSource dataSource=null;
    /**
     * 单例可以有自己的操作
     **/
  	public Connection getConnection() throws SQLException{		
  		return dataSource.getConnection();
  	}
}