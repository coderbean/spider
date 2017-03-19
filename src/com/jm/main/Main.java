package com.jm.main;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
	public static void main(String[] args) {	
		
		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
		
		Calendar calendar = Calendar.getInstance();		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = calendar.getTime();
		String dateStr = sdf.format(date);
		System.out.println("时区:"+Calendar.getInstance().getTimeZone().getID());
        System.out.println("Current Time = "+ dateStr +" "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND));
        
        TVQueryThread tvQueryThread = new TVQueryThread();
        //在0~1点执行，周期为7天
        long initialDelay = 24-calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println("程序将在"+initialDelay+"小时后执行！");
        scheduledThreadPool.scheduleAtFixedRate(tvQueryThread, initialDelay, 7*24, TimeUnit.HOURS);
        
//        scheduledThreadPool.scheduleAtFixedRate(tvQueryThread, 0, 10, TimeUnit.MINUTES);
//        scheduledThreadPool.scheduleAtFixedRate(tvQueryThread, 0, 7*24, TimeUnit.HOURS);
	}
}
