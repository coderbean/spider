package com.jm.util.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpURLConUtil {
	private String user_agent = null;
	
	public HttpURLConUtil() {
		super();
	}

	public HttpURLConUtil(String user_agent) {
		super();
		this.user_agent = user_agent;
	}
	
	public String sendGet(String urlStr) throws IOException{
		URL url = new URL(urlStr);
		String responseStr = null;
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		if(null != this.user_agent){
			con.setRequestProperty("User-Agent", this.user_agent);
		}
		int responseCode = con.getResponseCode();
		if(responseCode != 200){
			System.out.println(urlStr+"获取错误！"+responseCode);
		}else{
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(con.getInputStream(),"gbk"));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while((inputLine = reader.readLine())!=null){
				response.append(inputLine);
			}
			reader.close();	
			responseStr = response.toString();
		}
		return responseStr;
	}
	public String sendPost(String urlStr,String urlParameters) throws IOException{
		URL url = new URL(urlStr);
		String responseStr = null;
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		//add reuqest header
        con.setRequestMethod("POST");
        if(this.user_agent != null){
            con.setRequestProperty("User-Agent", this.user_agent);
        }
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
     // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        
        int responseCode = con.getResponseCode();
        if(responseCode != 200){
			System.out.println("获取错误！");
		}else{
			BufferedReader in = new BufferedReader(
	                new InputStreamReader(con.getInputStream(),"utf-8"));
	        String inputLine;
	        StringBuffer response = new StringBuffer();

	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        in.close();
	        responseStr = response.toString();
		}
        return responseStr;
	}
	
}