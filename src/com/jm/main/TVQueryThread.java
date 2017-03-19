package com.jm.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.jm.db.MyDbPool;
import com.jm.sitehandler.SiteHandler;

public class TVQueryThread implements Runnable {

	@Override
	public void run() {
		String dateStr = null;
		try {
			Calendar calendar = Calendar.getInstance();		
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = calendar.getTime();
			dateStr = sdf.format(date);
			
			Connection connection = MyDbPool.getConnection();			
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM show_table WHERE show_date = ?");
			preparedStatement.setString(1, dateStr);
			ResultSet resultSet =preparedStatement.executeQuery();
			while(resultSet.next()){
				if(resultSet.getInt(1)>0){
					System.out.println("数据库已存在本周（"+dateStr+"）数据！将在下周同一时间更新数据！");
					return;
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.out.println("查询数据表错误！");
			return;
		}
		System.out.println("========================="+dateStr+"开始更新数据库============================");
		Set<String> urlSet = new HashSet<String>();
		Set<String> synUrlSet = Collections.synchronizedSet(urlSet);
		BlockingQueue<String> urlBlockingQueue = new LinkedBlockingQueue<String>();
		SiteHandler siteHandler = new SiteHandler(synUrlSet,urlBlockingQueue);
		urlSet.add("http://www.tvsou.com/programys/TV_1/Channel_1/W4.htm");
		try {
			urlBlockingQueue.put("http://www.tvsou.com/programys/TV_1/Channel_1/W4.htm");
		} catch (InterruptedException e) {
			System.out.print("urlBlockingQueue.put()--");
			e.printStackTrace();
		}
		
		for(int i=0;i<30;i++){
			System.out.println("正在启动第"+i+"个线程.....");
			new Thread(siteHandler).start();
		}
	}
	
}
