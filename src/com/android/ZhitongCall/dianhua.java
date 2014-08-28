package com.android.ZhitongCall;

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
import android.content.res.Resources.NotFoundException;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "WorldReadableFiles", "HandlerLeak" })
public class dianhua extends Activity {
	private static Context mContext = null; 
	private WebView content_wv;
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dianhua);
        mContext = this;
        content_wv = (WebView) findViewById(R.id.bmap); 
        findViewById(R.id.bottom_home_layout_ly).setSelected(false);
    	findViewById(R.id.bottom_card_layout_ly).setSelected(true);
    	findViewById(R.id.bottom_msg_layout_ly).setSelected(false);
    	findViewById(R.id.bottom_order_layout_ly).setSelected(false);
    	findViewById(R.id.bottom_other_layout_ly).setSelected(false);
        if (pul.spid.equals("") || pul.susername.equals(""))
        {
        	new AlertDialog.Builder(mContext)  
        	.setTitle("��¼��ʱ")
        	.setMessage("��Ǹ�����ĵ�¼�ѳ�ʱ�����µ�¼")
        	.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {   
    			public void onClick(DialogInterface dialog, int whichButton) {   
    				Intent i = new Intent(dianhua.this, Myxf.class);    
    	        	dianhua.this.startActivity(i);
    	        	dianhua.this.finish();
    				}   
    				})
        	.show();
        }
        
        pul.addActivity(dianhua.this);
        TextView username =(TextView) findViewById(R.id.fcall);
        username.setText(pul.susername);
        if(pul.mcnum!="" && !pul.mcnum.equals("")){
        	Callnum(pul.mcnum);
        	pul.mcnum="";
        }
        WebSettings ws = content_wv.getSettings();
        ws.setBuiltInZoomControls(false); // ������ʾ���Ű�ť
        ws.setSupportZoom(false); //֧������
        ws.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        ws.setDefaultTextEncodingName("utf-8"); //�����ı�����
        ws.setAppCacheEnabled(false);
        ws.setAllowFileAccess(true); // ��������ļ�
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);//���û���ģʽ
        content_wv.addJavascriptInterface(this, "java2js");
        content_wv.setWebViewClient(new WebViewClientDemo());
        content_wv.loadUrl(getResources().getString(R.string.weburl)+"index.php?c=main&a=getcalllist&username="+pul.susername+"&pid="+pul.spid);
    } 
   
	public void mcallok(View callok) throws ClientProtocolException, NotFoundException, IOException, JSONException{
    	TextView mtcall = (TextView)findViewById(R.id.tcall);
    	Callnum(mtcall.getText().toString());
    	mtcall.setText("");
    } 
    private class WebViewClientDemo extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {   
            if (url.startsWith("mcall:")) {
            	 String [] v_a=url.split(":");
            	 Callnum(v_a[1]);
                  }
            return true;
        }
    }

    private void Callnum(String mnum){
		// TODO Auto-generated method stub
    	TextView mmsg =(TextView) findViewById(R.id.mmsg);
    	if (mmsg.getText().equals("�����ύ�У����Ժ�...")){
    		return;
    	}
    	if (mnum.equals("")==true){
    		Toast.makeText(getApplicationContext(),"��Ǹ����������Ҫ����ĵ绰����",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if (mnum.substring(0,1).equals("00")==true){
    		Toast.makeText(getApplicationContext(),"��Ǹ��������ĺ��벻��ȷ",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if (pul.isConnect(mContext)==false)
    	{  Toast.makeText(getApplicationContext(),"��Ǹ����������ʧ��",Toast.LENGTH_SHORT).show();
    	   return;
    	}
    	mmsg.setText("�����ύ�У����Ժ�...");
    	Httpget(getResources().getString(R.string.weburl)+"index.php?c=main&a=getcall&username="+pul.susername+"&tcall="+mnum+"&pid="+pul.MD5(pul.susername+mnum));
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
				try {
					callback(msg.obj + "");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	            }
        };  
    };  
    public void callback(String getd) throws JSONException{
    	TextView mmsg =(TextView) findViewById(R.id.mmsg);
    	if (getd.equals("Err0")==true || getd.equals("")==true)
    	{
    		mmsg.setText("��Ǹ����֤ʧ��������");
     	   return;
    	}
    	if (getd.equals("Err2")==true)
    	{
    		mmsg.setText("��Ǹ��ͬһ���벦��Ƶ��");
     	   return;
    	}
    	if (getd.equals("Err1")==true)
    	{
    	   mmsg.setText("��ͨ���º������㲦��Ŷ");
     	   return;
    	}
    	if (getd.equals("Err3")==true)
    	{
    	   mmsg.setText("��Ǹ���ϴ�ͨ������ͳ����");
     	   return;
    	}
    	
    	if (getd.equals("Err"))
    	{
    		mmsg.setText("�ύ�ɹ���ϵͳ���Ȳ��������ֻ���Ȼ���Զ���ͨ�Է��ֻ�");
      	    return;
    	}
    	
    	JSONTokener jsonParser = new JSONTokener(getd);
        JSONObject person = (JSONObject) jsonParser.nextValue();
    	if (person.getString("zt").toString().equals("OK")==true)
    	{
    	   mmsg.setText("�ύ�ɹ���ϵͳ���Ȳ��������ֻ���Ȼ���Զ���ͨ�Է��ֻ�");
     	   return;
    	}
    	mmsg.setText("��Ǹ�������ύ����������");
    }
    
	public void mback(View bind_return_btn){
		dianhua.this.finish();
    } 
    public void mcall1(View bottom_home_bn_style){
    	Intent i = new Intent(dianhua.this, Call.class); 
    	dianhua.this.startActivity(i);
    }
    public void mcall2(View bottom_card_bn_style){
    	Intent i = new Intent(dianhua.this, Txl.class); 
    	dianhua.this.startActivity(i);
    }
    public void mcall3(View bottom_msg_bn_style){
    	Intent i = new Intent(dianhua.this, Msg.class); 
    	dianhua.this.startActivity(i);
    }
    public void mcall4(View home_bottom_order_bn){
    	Intent i = new Intent(dianhua.this, Order.class); 
    	dianhua.this.startActivity(i);
    }
    public void mcall5(View home_bottom_other_bn){
    	Intent i = new Intent(dianhua.this, Other.class); 
    	dianhua.this.startActivity(i);
    }
}

