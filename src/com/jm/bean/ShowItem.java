package com.jm.bean;

public class ShowItem {
	private String showTime;
	
	private int weekDay;
	private String showDate;
	private String showName;
	private String channelName;
	
	
	
	public ShowItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public ShowItem(String showTime, int weekDay, String showDate,
			String showName, String channelName) {
		super();
		this.showTime = showTime;
		this.weekDay = weekDay;
		this.showDate = showDate;
		this.showName = showName;
		this.channelName = channelName;
	}


	public String getShowTime() {
		return showTime;
	}
	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}
	public int getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(int weekDay) {
		this.weekDay = weekDay;
	}
	public String getShowDate() {
		return showDate;
	}
	public void setShowDate(String showDate) {
		this.showDate = showDate;
	}
	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	
	
}
