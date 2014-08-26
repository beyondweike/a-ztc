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
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class Pass extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.more_setpassword);
    }  
    public void mback(View bind_return_btn){
		Pass.this.finish();
    } 
    public void merr(View passerr){
		Pass.this.finish();
    } 
    public void mok(View passok){
    	TextView mpass0 = (TextView)findViewById(R.id.oldpass);
    	TextView mpass1 = (TextView)findViewById(R.id.newpass);
    	TextView mpass2 = (TextView)findViewById(R.id.newpass2);
    	if (mpass0.getText().toString().equals("")==true){
    		Toast.makeText(getApplicationContext(),"��Ǹ�����������ľ�����",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if (mpass1.getText().toString().equals("")==true){
    		Toast.makeText(getApplicationContext(),"��Ǹ������������������",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if (mpass1.getText().toString().length()<6 || mpass1.getText().toString().length()>12){
    		Toast.makeText(getApplicationContext(),"��Ǹ��������ĳ���Ϊ6-12λ",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if (mpass1.getText().toString().equals(mpass2.getText().toString())==false){
    		Toast.makeText(getApplicationContext(),"��Ǹ����������������벻һ��",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if (pul.isConnect(Pass.this)==false)
    	{  Toast.makeText(getApplicationContext(),"��Ǹ����������ʧ��",Toast.LENGTH_SHORT).show();
    	   return;
    	}
    	Httpget(getResources().getString(R.string.weburl)+"index.php?c=main&a=getpass&username="+pul.susername+"&userpass="+mpass0.getText().toString()+"&userpass1="+mpass1.getText().toString()+"&pid="+pul.MD5(pul.susername+mpass0.getText().toString()));
    	
    }
    
    public void Httpget(final String url){
	    final ProgressDialog pd = ProgressDialog.show(Pass.this, "��ʾ��", "���ݼ����У����Ժ󡭡�");
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
    	if (getd.equals("Err0")==true || getd.equals("")==true)
    	{
    	   Toast.makeText(getApplicationContext(),"��Ǹ����֤ʧ��������",Toast.LENGTH_SHORT).show();
     	   return;
    	}
    	if (getd.equals("Err1")==true)
    	{
    	   Toast.makeText(getApplicationContext(),"��Ǹ�����ľ��������",Toast.LENGTH_SHORT).show();
     	   return;
    	} 
    	new AlertDialog.Builder(Pass.this)    
    	.setTitle("��ʾ��")  
    	.setMessage("��ϲ�������޸ĳɹ���")  
    	.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {   
			public void onClick(DialogInterface dialog, int whichButton) {   
				Pass.this.finish();
				}   
				})  
    	.show();  
    }
}
