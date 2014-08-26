package com.android.ZhitongCall;

import java.io.File;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
//import android.webkit.CacheManager;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class Call extends Activity {
	private static Context mContext = null; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.call);
        mContext = this;
        findViewById(R.id.bottom_home_layout_ly).setSelected(true);
    	findViewById(R.id.bottom_card_layout_ly).setSelected(false);
    	findViewById(R.id.bottom_msg_layout_ly).setSelected(false);
    	findViewById(R.id.bottom_order_layout_ly).setSelected(false);
    	findViewById(R.id.bottom_other_layout_ly).setSelected(false);
        if (pul.spid.equals("") || pul.susername.equals(""))
        {
        	new AlertDialog.Builder(mContext)  
        	.setTitle("登录超时")
        	.setMessage("抱歉：您的登录已超时请重新登录")
        	.setPositiveButton("确定", new DialogInterface.OnClickListener() {   
    			public void onClick(DialogInterface dialog, int whichButton) {   
    				Intent i = new Intent(Call.this, Myxf.class);    
    	        	Call.this.startActivity(i);
    	        	Call.this.finish();
    				}   
    				})
        	.show();
        }
        pul.addActivity(Call.this);
        TextView username =(TextView) findViewById(R.id.username);
        username.setText(pul.susername);
        getuser();
    }  
    
    private void getuser(){
    	Httpget(getResources().getString(R.string.weburl)+"index.php?c=main&a=getuser&username="+pul.susername+"&pid="+pul.spid);
    }
    
    public void Httpget(final String url){
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
          }   
       }.start(); 
      }
    private  Handler handler = new Handler() {  
        public void handleMessage(Message msg) {  
        	switch(msg.what) {
        	            case 1:
        	            	callback(msg.obj + "");
        	            }
        };  
    };  
    
    
    public void callback(String getd){
    	JSONTokener jsonParser = new JSONTokener(getd);
    	TextView userye = (TextView) findViewById(R.id.userje);
    	TextView userlast = (TextView) findViewById(R.id.usertime);
    	TextView useryouhui = (TextView) findViewById(R.id.useryouhui);
    	TextView usertime = (TextView) findViewById(R.id.usertime);
    	try {
    		JSONObject person;
            person = (JSONObject) jsonParser.nextValue();
            
            if (person.getString("ver").toString().equals(getResources().getString(R.string.appver).toString())==false)
        	{
            	new AlertDialog.Builder(Call.this) 
        		.setTitle("有新版本")
        		.setMessage("要升级到新版本吗？")
        		.setPositiveButton("是", new DialogInterface.OnClickListener() {   
    				public void onClick(DialogInterface dialog, int whichButton) {   
        				 Uri uri = Uri.parse(getResources().getString(R.string.weburl)+"ZhitongCall.apk");
                         Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                         startActivity(intent);
        				}   
        				})
        		.setNegativeButton("否", null)
        		.show();
        	}
            
			userye.setText(person.getString("ye").toString());
			userlast.setText(person.getString("lasttime").toString());
			userye.setText(person.getString("ye").toString());
			if (person.getString("alltime").toString().equals("0")){
				usertime.setText("还没包月哦，快点我包月！");
			}else{
				usertime.setText(person.getString("alltime").toString());
			}
			useryouhui.setText(person.getString("youhui").toString());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void malltime(View usertime){
    	pul.sact="alltime";
    	Intent i = new Intent(Call.this, Order.class); 
    	Call.this.startActivity(i);
    }

	public boolean onKeyDown(int keyCode, KeyEvent event) {  
		if(keyCode==KeyEvent.KEYCODE_BACK){
            dialog();  
    	 }
    	 return true;
}  
    protected void dialog() {  
        
        AlertDialog.Builder builder = new Builder(Call.this); 
        builder.setMessage("确定要退出吗?");  
        builder.setTitle("提示"); 
        builder.setPositiveButton("确认",  
        new android.content.DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int which) { 
            	/*
            	File file = CacheManager.getCacheFileBaseDir(); 
         	   if (file != null && file.exists() && file.isDirectory()) { 
         	    for (File item : file.listFiles()) { 
         	     item.delete(); 
         	    } 
         	    file.delete(); 
         	    } */
         	    mContext.deleteDatabase("webview.db"); 
         	    mContext.deleteDatabase("webviewCache.db");
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
    public void mpay(View userpay){
    	Intent i = new Intent(Call.this, Msg.class); 
    	Call.this.startActivity(i);
    } 
    public void mpass(View userpass)
    {	
    	Intent i = new Intent(Call.this, Pass.class);    
    	Call.this.startActivity(i);
    }
    
    public void mcall1(View bottom_home_bn_style){
    	Intent i = new Intent(Call.this, Call.class);    
    	Call.this.startActivity(i);
    }
    public void mcall2(View bottom_card_bn_style){
    	Intent i = new Intent(Call.this, Txl.class); 
    	Call.this.startActivity(i);
    }
    public void mcall3(View bottom_msg_bn_style){
    	Intent i = new Intent(Call.this, Msg.class); 
    	Call.this.startActivity(i);
    }
    public void mcall4(View home_bottom_order_bn){
    	Intent i = new Intent(Call.this, Order.class); 
    	Call.this.startActivity(i);
    }
    public void mcall5(View home_bottom_other_bn){
    	Intent i = new Intent(Call.this, Other.class); 
    	Call.this.startActivity(i);
    }
}
