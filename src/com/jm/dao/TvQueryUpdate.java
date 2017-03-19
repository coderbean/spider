package com.jm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.jm.bean.ShowItem;
import com.jm.db.MyDbPool;

public class TvQueryUpdate {
	/**
	 * 将一条节目信息插入数据库
	 * @param showItem 节目信息  connection调用这个函数的线程的数据库连接
	 * @return 数据库收到影响的行数
	 * @throws SQLException
	 */
	public int insert2show_table(ShowItem showItem,Connection connection){
		PreparedStatement preparedStatement = null;
		int i = -1;
		String sql = "INSERT INTO show_table("
				+ "show_time,"
				+ "week_day,"
				+ "show_date,"
				+ "show_name,"
				+ "channel_name) VALUES(?,?,?,?,?);";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, showItem.getShowTime());
			preparedStatement.setInt(2, showItem.getWeekDay());
			preparedStatement.setString(3, showItem.getShowDate());
			preparedStatement.setString(4, showItem.getShowName());
			preparedStatement.setString(5, showItem.getChannelName());
			
			i = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			MyDbPool.attemptClose(preparedStatement);
		}
		
		return i;
	}
//	public boolean batInsert2show_table(List<ShowItem> showItemList,Connection connection){
//		
//		for(ShowItem showItem:showItemList){
//			if(-1 == this.insert2show_table(showItem,connection)){
//				return false;
//			}
//		}
//		return true;
//	}
	public boolean batInsert2show_table(List<ShowItem> showItemList,Connection connection){
		PreparedStatement preparedStatement = null;
		String sql = "INSERT INTO show_table("
				+ "show_time,"
				+ "week_day,"
				+ "show_date,"
				+ "show_name,"
				+ "channel_name) VALUES(?,?,?,?,?);";
		try {
			preparedStatement = connection.prepareStatement(sql);
			for(ShowItem showItem:showItemList){
				preparedStatement.setString(1, showItem.getShowTime());
				preparedStatement.setInt(2, showItem.getWeekDay());
				preparedStatement.setString(3, showItem.getShowDate());
				preparedStatement.setString(4, showItem.getShowName());
				preparedStatement.setString(5, showItem.getChannelName());
				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		} finally{
			MyDbPool.attemptClose(connection);
		}
		return true;
	}
}
