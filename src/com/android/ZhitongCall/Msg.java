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
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class Msg extends Activity {
	private static Context mContext = null; 
	private WebView content_wv;
	private int mint=0;
	private ProgressDialog pd;
    @SuppressLint("SetJavaScriptEnabled")
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.msg);
        mContext = this;
        content_wv = (WebView) findViewById(R.id.bmap); 
        findViewById(R.id.bottom_home_layout_ly).setSelected(false);
    	findViewById(R.id.bottom_card_layout_ly).setSelected(false);
    	findViewById(R.id.bottom_msg_layout_ly).setSelected(true);
    	findViewById(R.id.bottom_order_layout_ly).setSelected(false);
    	findViewById(R.id.bottom_other_layout_ly).setSelected(false);
        if (pul.spid.equals("") || pul.susername.equals(""))
        {
        	new AlertDialog.Builder(mContext)  
        	.setTitle("登录超时")
        	.setMessage("抱歉：您的登录已超时请重新登录")
        	.setPositiveButton("确定", new DialogInterface.OnClickListener() {   
    			public void onClick(DialogInterface dialog, int whichButton) {   
    				Intent i = new Intent(Msg.this, Myxf.class);    
    				Msg.this.startActivity(i);
    				Msg.this.finish();
    				}   
    				})
        	.show();
        }
        pul.addActivity(Msg.this);
        WebSettings ws = content_wv.getSettings();
        ws.setBuiltInZoomControls(false); // 设置显示缩放按钮
        ws.setSupportZoom(false); //支持缩放
        ws.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        ws.setDefaultTextEncodingName("utf-8"); //设置文本编码
        ws.setAppCacheEnabled(false);
        ws.setAllowFileAccess(true); // 允许访问文件
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);//设置缓存模式
        ws.setJavaScriptEnabled(true);
        content_wv.addJavascriptInterface(this, "java2js");
        content_wv.setWebViewClient(new WebViewClientDemo());
        content_wv.setWebChromeClient(new WebViewChromeClientDemo());
        content_wv.loadUrl(getResources().getString(R.string.weburl).replace("call20/", "")+"open/pay.php?username="+pul.susername);
      } 
    private class WebViewClientDemo extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {   
            if (url.startsWith("get:"))
            {
            	String [] v_a=url.split(":");
				Httpget(getResources().getString(R.string.weburl)+"index.php?c=main&a=getbook&username="+pul.susername+"&con="+v_a[1]);
            }
            else if (url.startsWith("cz:")){
            	String [] v_a=url.split(":");
				Httpget(getResources().getString(R.string.weburl)+"index.php?c=main&a=getcz&date="+v_a[1]+"&username="+pul.susername+"&pid="+pul.spid);
            }else if (url.startsWith("mpay:")){
            	String [] v_a=url.split(":");
            	mpay(v_a[1],pul.susername);
            }
            else
            {   
            	view.loadUrl(url);
            }
            return true;
        }
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
    	new AlertDialog.Builder(mContext)  
    	.setTitle("提示：")
    	.setMessage("恭喜：数据提交成功！")
    	.setPositiveButton("确定", null)
    	.show();
    }
    
    private class WebViewChromeClientDemo extends WebChromeClient {
        // 设置网页加载的进度条
        public void onProgressChanged(WebView view, int newProgress) {
          if (mint==0)
          {
        	  pd = ProgressDialog.show(mContext, "提示：", "数据加载中，请稍后……");
        	  mint=1;
          }
          if (newProgress >= 100)
            {
              TextView mtitle=(TextView)findViewById(R.id.webtitle);
              mtitle.setText(content_wv.getTitle());
              pd.dismiss();
        	mint=0;
        	}
	       }
        }
        // 获取网页的标题
        public void onReceivedTitle(WebView view, String title) {
        }
    
	public void mback(View bind_return_btn){
		Msg.this.finish();
    } 
    public void mcall1(View bottom_home_bn_style){
    	Intent i = new Intent(Msg.this, Call.class); 
    	Msg.this.startActivity(i);
    }
    public void mcall2(View bottom_card_bn_style){
    	Intent i = new Intent(Msg.this, Txl.class); 
    	Msg.this.startActivity(i);
    }
    public void mcall3(View bottom_msg_bn_style){
    	Intent i = new Intent(Msg.this, Msg.class); 
    	Msg.this.startActivity(i);
    }
    public void mcall4(View home_bottom_order_bn){
    	Intent i = new Intent(Msg.this, Order.class); 
    	Msg.this.startActivity(i);
    	Msg.this.finish();
    }
    public void mcall5(View home_bottom_other_bn){
    	Intent i = new Intent(Msg.this, Other.class); 
    	Msg.this.startActivity(i);;
    }
  //处理支付
    public void mpay(String je, String username){


    }
}

