package com.android.ZhitongCall;

import java.io.IOException;
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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "WorldReadableFiles", "HandlerLeak" })
public class Myxf extends Activity {
	private static Context mContext = null; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        mContext = this;
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.app_cid),MODE_WORLD_READABLE);
	    String username = preferences.getString("username", "");
	    String userpass = preferences.getString("userpass", "");
	    if (username.equals("")==false)
	    {
	    TextView musername = (TextView)findViewById(R.id.login_name);
    	TextView muserpass = (TextView)findViewById(R.id.login_pwd);
    	musername.setText(username);
    	muserpass.setText(userpass);
	    }
	    
	    if(username.length()>0 && userpass.length()>0)
	    {
	    	Httpget(getResources().getString(R.string.weburl)+"index.php?c=main&a=cklogin&username="+username+"&userpass="+userpass+"&pid="+pul.MD5(username+userpass));
	    }
    }  
    
    @SuppressLint("WorldReadableFiles")
	public void login_btns(View login_btn) throws ClientProtocolException, IOException{ //登录
    	TextView musername = (TextView)findViewById(R.id.login_name);
    	TextView muserpass = (TextView)findViewById(R.id.login_pwd);
    	if (musername.getText().toString().equals("")==true){
    		Toast.makeText(getApplicationContext(),"抱歉：请输入您的登录账号",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if (muserpass.getText().toString().equals("")==true){
    		Toast.makeText(getApplicationContext(),"抱歉：请输入您的登录密码",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if (pul.isConnect(mContext)==false)
    	{  Toast.makeText(getApplicationContext(),"抱歉：网络连接失败",Toast.LENGTH_SHORT).show();
    	   return;
    	}
    	Httpget(getResources().getString(R.string.weburl)+"index.php?c=main&a=cklogin&username="+musername.getText().toString()+"&userpass="+muserpass.getText().toString()+"&pid="+pul.MD5(musername.getText().toString()+muserpass.getText().toString()));
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
    	TextView musername = (TextView)findViewById(R.id.login_name);
    	TextView muserpass = (TextView)findViewById(R.id.login_pwd);
    	CheckBox mcklogin = (CheckBox)findViewById(R.id.register_cb_protocol);
    	if (getd.equals("Err0")==true || getd.equals("")==true)
    	{
    	   Toast.makeText(getApplicationContext(),"抱歉：验证失败请重试",Toast.LENGTH_SHORT).show();
     	   return;
    	}
    	if (getd.equals("Err1")==true || getd.equals(pul.MD5(musername.getText().toString()))==false)
    	{
    	   Toast.makeText(getApplicationContext(),"抱歉：用户名或密码错误",Toast.LENGTH_SHORT).show();
     	   return;
    	} 
    	pul.spid=getd;
    	pul.susername=musername.getText().toString();
    	SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.app_cid),MODE_WORLD_READABLE);
    	Editor editor = preferences.edit();
    	if (mcklogin.isChecked())
    	{
        editor.putString("username", musername.getText().toString());
        editor.putString("userpass", muserpass.getText().toString());
    	}
    	else
    	{
    	editor.putString("username", "");
        editor.putString("userpass", "");
    	}
    	editor.commit();
    	Intent i = new Intent(Myxf.this, Call.class);    
    	Myxf.this.startActivity(i);
    	Myxf.this.finish();
    }
    
    public void login_reg_btns(View login_reg_btn){ //注册
    	Intent i = new Intent(Myxf.this, reg.class);    
    	Myxf.this.startActivity(i);
    	Myxf.this.finish();
    }
    public void fpass(View login_foget_pwd){ //找回密码
    	Intent i = new Intent(Myxf.this, Fpass.class);    
    	Myxf.this.startActivity(i);
    	Myxf.this.finish();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
    	 if(keyCode==KeyEvent.KEYCODE_BACK){
            dialog();  
    	 }
    	 return true;
    }  
    protected void dialog() {  
        AlertDialog.Builder builder = new Builder(Myxf.this); 
        builder.setMessage("确定要退出吗?");  
        builder.setTitle("提示"); 
        builder.setPositiveButton("确认",  
        new android.content.DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int which) { 
                pul.exit(); 
            }  
        });
        builder.setNegativeButton("取消",  
        new android.content.DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
            }  
        });  
        builder.create().show();  
    }  
}
