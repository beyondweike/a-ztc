package com.android.ZhitongCall;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class reg extends Activity {
	private static Context mContext = null; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.reg);
        mContext = this;
    }  
    
    public void regok(View register_btn) throws IOException{ //注册
    	TextView musername = (TextView)findViewById(R.id.register_acount);
    	TextView muserpass = (TextView)findViewById(R.id.register_pwd);
    	TextView muserpasss = (TextView)findViewById(R.id.register_confirm_pwd);
    	if (musername.getText().toString().equals("")==true){
    		Toast.makeText(getApplicationContext(),"抱歉：请输入您的登录账号",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if (muserpass.getText().toString().equals("")==true){
    		Toast.makeText(getApplicationContext(),"抱歉：请输入您的登录密码",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if (pul.isMobileNO(musername.getText().toString())==false){
    		Toast.makeText(getApplicationContext(),"抱歉：请输入正确的手机号",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if (muserpass.getText().toString().length()<6 || muserpass.getText().toString().length()>12){
    		Toast.makeText(getApplicationContext(),"抱歉：登录密码的长度为6-12位",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if (muserpass.getText().toString().equals(muserpasss.getText().toString())==false){
    		Toast.makeText(getApplicationContext(),"抱歉：您两次输入的密码不一致",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if (pul.isConnect(mContext)==false)
    	{  Toast.makeText(getApplicationContext(),"抱歉：网络连接失败",Toast.LENGTH_SHORT).show();
    	   return;
    	}
    	TelephonyManager telephonyManager=(TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        InputStream inStream=mContext.getAssets().open("agent.html");
        String maid =pul.getStreamString(inStream);
    	Httpget(getResources().getString(R.string.weburl)+"index.php?c=main&a=regok&aid="+maid+"&username="+musername.getText().toString()+"&userpass="+muserpass.getText().toString()+"&imei="+imei+"&pid="+pul.MD5(musername.getText().toString()+muserpass.getText().toString()));
    	
    }
    
    public void Httpget(final String url){
	    final ProgressDialog pd = ProgressDialog.show(mContext, "提示：", "数据加载中，请稍后……");
	    new Thread(){ 
            public void run() {  
            	  HttpGet httpGet=new HttpGet(url);
			         HttpResponse response;
			         String result = "";
						try {
							response = new DefaultHttpClient().execute(httpGet);
							if(response.getStatusLine().getStatusCode()==200){
			                    HttpEntity entity=response.getEntity();
			                    result=EntityUtils.toString(entity, HTTP.UTF_8);
			                    if (result.equals("")==true)
			                	{  
			                    	result="Err";
			                	}
			                    Message message = new Message();  
			                    message.what = 1;
			                    message.obj = result;
		                        handler.sendMessage(message); 
			                 }
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    	pd.dismiss();
            }   
         }.start(); 
        }
    private Handler handler = new Handler() {  
        public void handleMessage(Message msg) {  
        	switch(msg.what) {
        	            case 1:
        	            	callback(msg.obj + "");
        	            }
        };  
    };  
    public void callback(String getd){
    	TextView musername = (TextView)findViewById(R.id.register_acount);
    	if (getd.equals("Err0")==true || getd.equals("")==true)
    	{
    	   Toast.makeText(getApplicationContext(),"抱歉：验证失败请重试",Toast.LENGTH_SHORT).show();
     	   return;
    	}
    	if (getd.equals("Err1")==true || getd.equals(pul.MD5(musername.getText().toString()))==false)
    	{
    	   Toast.makeText(getApplicationContext(),"抱歉：该手机号已被注册",Toast.LENGTH_SHORT).show();
     	   return;
    	} 
    	pul.spid=getd;
    	pul.susername=musername.getText().toString();
    	Intent i = new Intent(reg.this, Call.class);
    	reg.this.startActivity(i);
    	reg.this.finish();
    }
    
    public void mback(View bind_return_btn){
    	Intent i = new Intent(reg.this, Myxf.class);    
        reg.this.startActivity(i);
    	reg.this.finish();
    } 
}
