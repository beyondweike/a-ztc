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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class Fpass extends Activity {
	private static Context mContext = null; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fpass);
        mContext = this;
    }  
    public void fpassyzmoks(View fpassyzmok){ //������֤��
    	TextView musername = (TextView)findViewById(R.id.fpassname);
    	if (musername.getText().toString().equals("")==true){
    		Toast.makeText(getApplicationContext(),"��Ǹ�����������ĵ�¼�˺�",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if (pul.isMobileNO(musername.getText().toString())==false){
    		Toast.makeText(getApplicationContext(),"��Ǹ����������ȷ���ֻ���",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if (pul.isConnect(mContext)==false)
    	{  Toast.makeText(getApplicationContext(),"��Ǹ����������ʧ��",Toast.LENGTH_SHORT).show();
    	   return;
    	}
    	if (pul.ssend>2){
    	   Toast.makeText(getApplicationContext(),"��Ǹ�����ŷ��ʹ�������",Toast.LENGTH_SHORT).show();
      	   return;
    	}
    	Httpget(getResources().getString(R.string.weburl)+"index.php?c=main&a=fpass&username="+musername.getText().toString()+"&pid="+pul.MD5(musername.getText().toString()));
    }
    
    public void Httpget(final String url){
	    final ProgressDialog pd = ProgressDialog.show(mContext, "��ʾ��", "���ݼ����У����Ժ󡭡�");
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
    public void Httpgets(final String url){
	    final ProgressDialog pd = ProgressDialog.show(mContext, "��ʾ��", "���ݼ����У����Ժ󡭡�");
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
		                        handlers.sendMessage(message); 
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
    private Handler handlers = new Handler() {  
        public void handleMessage(Message msg) {  
        	switch(msg.what) {
        	            case 1:
        	            	callbacks(msg.obj + "");
        	            }
        };  
    };  
    public void callback(String getd){
    	if (getd.equals("Err0")==true || getd.equals("")==true)
    	{
    	   Toast.makeText(getApplicationContext(),"��Ǹ����֤ʧ��������",Toast.LENGTH_SHORT).show();
     	   return;
    	}
    	pul.ssend=pul.ssend+1;
    	new AlertDialog.Builder(mContext)  
    	.setTitle("��ʾ��")
    	.setMessage("��ϲ����֤�뷢�ͳɹ�")
    	.setPositiveButton("ȷ��", null)
    	.show();
    }
    public void callbacks(String getd){
    if (getd.equals("Err0")==true || getd.equals("")==true)
	{
	   Toast.makeText(getApplicationContext(),"��Ǹ����֤ʧ��������",Toast.LENGTH_SHORT).show();
 	   return;
	}
    new AlertDialog.Builder(mContext)  
	.setTitle("��ʾ��")
	.setMessage("��ϲ�������޸ĳɹ�")
	.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {   
		public void onClick(DialogInterface dialog, int whichButton) {   
			Intent i = new Intent(Fpass.this, Myxf.class);    
			Fpass.this.startActivity(i);
			Fpass.this.finish();
			}   
			})
	.show();
    }
    public void fpassoks(View fpassok) throws ClientProtocolException, IOException{ //�޸�����
    	TextView musername = (TextView)findViewById(R.id.fpassname);
    	TextView myzm = (TextView)findViewById(R.id.fpassyzm);
    	TextView muserpass = (TextView)findViewById(R.id.fpass1);
    	TextView muserpasss = (TextView)findViewById(R.id.fpass2);
    	if (musername.getText().toString().equals("")==true){
    		Toast.makeText(getApplicationContext(),"��Ǹ�����������ĵ�¼�˺�",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if (muserpass.getText().toString().equals("")==true){
    		Toast.makeText(getApplicationContext(),"��Ǹ������������������",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if (myzm.getText().toString().equals("")==true){
    		Toast.makeText(getApplicationContext(),"��Ǹ��������������֤��",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if (pul.isMobileNO(musername.getText().toString())==false){
    		Toast.makeText(getApplicationContext(),"��Ǹ����������ȷ���ֻ���",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if (muserpass.getText().toString().length()<6 || muserpass.getText().toString().length()>12){
    		Toast.makeText(getApplicationContext(),"��Ǹ����¼����ĳ���Ϊ6-12λ",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if (muserpass.getText().toString().equals(muserpasss.getText().toString())==false){
    		Toast.makeText(getApplicationContext(),"��Ǹ����������������벻һ��",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if (pul.isConnect(mContext)==false)
    	{  Toast.makeText(getApplicationContext(),"��Ǹ����������ʧ��",Toast.LENGTH_SHORT).show();
    	   return;
    	}
    	Httpgets(getResources().getString(R.string.weburl)+"index.php?c=main&a=fpassok&username="+musername.getText().toString()+"&userpass="+muserpass.getText().toString()+"&useryzm="+myzm.getText().toString()+"&pid="+pul.MD5(musername.getText().toString()));
    }
    
    public void mback(View bind_return_btn){
        Intent i = new Intent(Fpass.this, Myxf.class);    
        Fpass.this.startActivity(i);
    	Fpass.this.finish();
    } 
}
