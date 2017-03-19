package com.jm.sitehandler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jm.bean.ShowItem;
import com.jm.dao.TvQueryUpdate;
import com.jm.db.DbPool;
import com.jm.db.MyDbPool;
import com.jm.util.net.HttpURLConUtil;

public class SiteHandler implements Runnable {
	
	private Set<String> synUrlSet = null;
	private BlockingQueue<String> urlBlockingQueue = null;
	private TvQueryUpdate tvQueryUpdate = null;
	
	//这里可以后续改成从全局配置文件中读取
	private HttpURLConUtil httpURLConUtil = new HttpURLConUtil("Mozilla/5.0");

	public SiteHandler() {
		super();
		tvQueryUpdate = new TvQueryUpdate();
	}

	public SiteHandler(Set<String> synUrlSet,
			BlockingQueue<String> urlBlockingQueue) {
		this();
		this.synUrlSet = synUrlSet;
		this.urlBlockingQueue = urlBlockingQueue;
	}

	private void dealResponse(String response) throws InterruptedException{
		if(response == null){
			return ;
		}

		//定义表信息字段字段
		int weekDay = -1;
		String channelName = null;
		String showDate = null;
		
		//获取页面中符合规则的URL
		Pattern pattern = Pattern.compile("/program\\w{0,2}/TV_\\d+/Channel_(\\d)+/W([1-7])\\.htm");
		Matcher matcher = pattern.matcher(response);
		while(matcher.find()){
			String newUrl = "http://www.tvsou.com"+matcher.group(0);
			if(this.synUrlSet.contains(newUrl)){
//				System.out.println(newUrl+" exist already!");
			}else{
				this.synUrlSet.add(newUrl);
				this.urlBlockingQueue.put(newUrl);
//				System.out.println(newUrl+" has been added into Queue!");
			}	
		}
		
		//找出节目信息播出周？和Date
		Pattern pattern1 = Pattern.compile("var programDT=\"(\\d{4}-\\d{2}-\\d{2})\";");
		Matcher matcher1 = pattern1.matcher(response);
		
		
		while(matcher1.find()){
			showDate = matcher1.group(1);
		}
		if(null != showDate){
			
		}else{
			return;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(simpleDateFormat.parse(showDate));
			//周日是1，周一是2，-------
			weekDay = calendar.get(Calendar.DAY_OF_WEEK);//获取周？
			if(weekDay == -1){
				return;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("日期字符串解析错误！");
			e.printStackTrace();
		}
		
		Pattern channelNamePattern = Pattern.compile("var title=\"(.*?)\";");
		Matcher channelNameMatcher = channelNamePattern.matcher(response);
		while(channelNameMatcher.find()){
			channelName = channelNameMatcher.group(1);//获取电视台名称
		}
		
		Pattern showTimeAndNamePattern = Pattern.compile("<li [\\s\\S]*?(\\d\\d:\\d\\d)[\\s\\S]*?<a[\\s\\S]*?>(.*?)</a>");
		Matcher showTimeAndNameMatcher = showTimeAndNamePattern.matcher(response);
		
		List<ShowItem> showItemList = new ArrayList<ShowItem>();
		while(showTimeAndNameMatcher.find()){
			showItemList.add(new ShowItem(showTimeAndNameMatcher.group(1), weekDay, showDate, showTimeAndNameMatcher.group(2), channelName));
		}
		Connection connection = null;
		DbPool dbPool = DbPool.INSTANCE;
		//将节目表添加到数据库
		try {
			connection = dbPool.getConnection();
			tvQueryUpdate.batInsert2show_table(showItemList, connection);
		} catch (SQLException e) {
			System.out.println("连接池数据库连接错误！");
			e.printStackTrace();
		}finally{
			MyDbPool.attemptClose(connection);
		}
	}

	@Override
	public void run() {
		String url;
		while(true){
			try {
				System.out.println("Set size is："+this.urlBlockingQueue.size());
				if(this.urlBlockingQueue.size()==0 && this.synUrlSet.size()>10000){
					System.out.println(Thread.currentThread().getName()+"退出！");
					return ;
				}
				url = urlBlockingQueue.take();
//				System.out.println("urlBlockingQueue size is --"+urlBlockingQueue.size());
//				System.out.println(url+" is being taked!____________");
				String response = httpURLConUtil.sendGet(url);
				this.dealResponse(response);
				
//				System.out.println(response); //调试用
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
