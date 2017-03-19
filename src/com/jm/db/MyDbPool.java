package com.jm.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import com.mchange.v2.c3p0.ComboPooledDataSource;

public class MyDbPool{
	private static ComboPooledDataSource dataSource=null;
	static{//类加载的执行一次
		//加载配置文件
		String path = MyDbPool.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
		int lastIndex = path.lastIndexOf("/") + 1;
		path = path.substring(firstIndex, lastIndex)+"c3p0-config.xml";
		
		System.out.println("C3P0配置文件路径："+path);
		System.setProperty("com.mchange.v2.c3p0.cfg.xml",path);
		
}
	//获取连接
	public  static Connection getConnection() throws SQLException{
		if(dataSource==null){//如果为空
			dataSource=new ComboPooledDataSource();
		}
		try{
		return dataSource.getConnection();
		}catch(SQLException e){
			e.printStackTrace();
			throw new SQLException();	
		}
	}
	//关闭相关东西,释放资源
	public static void attemptClose(ResultSet o){
		try
		    { if (o != null) o.close();}
		catch (Exception e)
		    { e.printStackTrace();}
    }

	public static void attemptClose(Statement o){
		try
		    { if (o != null) o.close();}
		catch (Exception e)
		    { e.printStackTrace();}
    }

	public static void attemptClose(Connection o){
		try
		    { if (o != null) o.close();}
		catch (Exception e)
		    { e.printStackTrace();}
    }
	public static void main(String[] a) throws SQLException{
//		System.out.println(MyDbPool.class.getResource("c3p0-config.xml").getPath());
		
		Connection con=MyDbPool.getConnection();
		String sql="select * from show_table";
		PreparedStatement pre=con.prepareStatement(sql);
		ResultSet rs=pre.executeQuery();
		while(rs.next()){
			System.out.println(rs.getString(1));
		}
		MyDbPool.attemptClose(con);
		MyDbPool.attemptClose(pre);
		MyDbPool.attemptClose(rs);
	}
}

