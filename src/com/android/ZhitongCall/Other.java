package com.android.ZhitongCall;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class Other extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.more_add);
        pul.addActivity(Other.this);
    }  
    public void mback(View bind_return_btn){
		Other.this.finish();
    } 
    public void mpass(View more_change_account_rl){
    	Intent i = new Intent(Other.this, Pass.class); 
    	Other.this.startActivity(i);
    }
    public void mpay(View more_recharge_account_rl){
    	Intent i = new Intent(Other.this, Msg.class); 
    	Other.this.startActivity(i);
    } 
    
    public void mabout(View more_about_rl){
    	Intent i = new Intent(Other.this, About.class); 
    	Other.this.startActivity(i);
    } 
    public void mexit(View more_exit_rl){ 
    	pul.exit();
    } 
    public void mhelp(View more_help_rl){
    	pul.sact="help";
    	Intent i = new Intent(Other.this, Order.class); 
    	Other.this.startActivity(i);
    }
    public void mbook(View more_city_code_set_rl){
    	pul.sact="book";
    	Intent i = new Intent(Other.this, Order.class); 
    	Other.this.startActivity(i);
    }
    public void mupdate(View more_soft_update_rl){
    	if (pul.isConnect(Other.this)==false)
    	{  Toast.makeText(getApplicationContext(),"抱歉：网络连接失败",Toast.LENGTH_SHORT).show();
    	   return;
    	}
    	Httpget(getResources().getString(R.string.weburl)+"index.php?c=main&a=getver");
    	
    }
    
    public void Httpget(final String url){
	    final ProgressDialog pd = ProgressDialog.show(Other.this, "提示：", "数据加载中，请稍后……");
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
    	if (getd.toString().equals(getResources().getString(R.string.appver).toString())==true)
    	{
    		new AlertDialog.Builder(Other.this)  
        	.setTitle("提示：")
        	.setMessage("恭喜：当前已是最新版本")
        	.setPositiveButton("确定", null)
        	.show();
    	}
    	else
    	{
    		new AlertDialog.Builder(Other.this) 
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
    }
}
