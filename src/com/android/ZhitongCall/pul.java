package com.android.ZhitongCall;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

public class pul extends Activity { 
	public static String spid = "";
	public static String susername = "";
	public static String sact = "";
	public static int ssend=0;
	public static String mcnum = "";
	public static List<Activity> activityList = new LinkedList<Activity>();  
    @Override
    public void onCreate(Bundle savedInstanceState) {
    }  
    
    static boolean isConnect(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理） 
    try { 
        ConnectivityManager connectivity = (ConnectivityManager) context 
                .getSystemService(Context.CONNECTIVITY_SERVICE); 
        if (connectivity != null) { 
            // 获取网络连接管理的对象 
            NetworkInfo info = connectivity.getActiveNetworkInfo(); 
            if (info != null&& info.isConnected()) { 
                // 判断当前网络是否已经连接 
                if (info.getState() == NetworkInfo.State.CONNECTED) { 
                    return true; 
                } 
            } 
        } 
    } catch (Exception e) { 
    } 
        return false; 
    }
    static void addActivity(Activity activity) {  
        activityList.add(activity);  
    } 
    static void exit() {  
        for (Activity activity : activityList) {  
            activity.finish();  
        }  
        System.exit(0);  
    }  
    @SuppressLint("ParserError")
	static String MD5(String str) {  //md5
    	str=str+"fxoex12c9";
    	MessageDigest md5 = null; 
    	try { 
    	md5 = MessageDigest.getInstance("MD5"); 
    	} catch (Exception e) { 
    	e.printStackTrace(); 
    	return ""; 
    	} 
    	char[] charArray = str.toCharArray(); 
    	byte[] byteArray = new byte[charArray.length]; 
    	for (int i = 0; i < charArray.length; i++) { 
    	byteArray[i] = (byte) charArray[i]; 
    	} 
    	byte[] md5Bytes = md5.digest(byteArray); 
    	StringBuffer hexValue = new StringBuffer(); 
    	for (int i = 0; i < md5Bytes.length; i++) { 
    	int val = ((int) md5Bytes[i]) & 0xff; 
    	if (val < 16) { 
    	hexValue.append("0"); 
    	} 
    	hexValue.append(Integer.toHexString(val)); 
    	} 
    	return hexValue.toString(); 
    	}
     static boolean isMobileNO(String mobiles){   //是否是手机号  
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");     
        Matcher m = p.matcher(mobiles);       
        return m.matches();     
    }   
   
  	 public static String appendSeprator(String srcStr,String seprator,int count)
  	 { 
  		 StringBuffer sb=new StringBuffer(srcStr); 
  	     int index=count; 
  	     while(sb.length()>count&&index<sb.length()-1) 
  	     { 
  	     sb.insert(index, seprator); 
  	     index+=count+1; } 
  	     return sb.toString();
  	     } 
  	public static String getStreamString(InputStream tInputStream){
  		if (tInputStream != null){
  		try{
  		BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(tInputStream));
  		StringBuffer tStringBuffer = new StringBuffer();
  		String sTempOneLine = new String("");
  		while ((sTempOneLine = tBufferedReader.readLine()) != null){
  		tStringBuffer.append(sTempOneLine);
  		}
  		return tStringBuffer.toString();
  		}catch (Exception ex){
  		ex.printStackTrace();
  		}
  		}
  		return "0";
  		}
    
}