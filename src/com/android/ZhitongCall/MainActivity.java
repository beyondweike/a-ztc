package com.android.ZhitongCall;

import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.view.Window;

public class MainActivity extends Activity {
	private static final String[] PHONES_PROJECTION = new String[] {  
	        Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,Phone.CONTACT_ID };
	 static Cursor phoneCursor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        getlist();
        new Handler().postDelayed(new Runnable(){   
			// Ϊ�˼��ٴ���ʹ������Handler����һ����ʱ�ĵ���
			            public void run() {  
			            	Intent i = new Intent(MainActivity.this, Myxf.class);    
			                //ͨ��Intent����������Myxf���Activity
			            	MainActivity.this.startActivity(i);    //����Myxf����
			            	MainActivity.this.finish();
			            }   
			        }, 4000);
    }  
    public void getlist(){
   	 ContentResolver resolver =this.getContentResolver();  
        //phoneCursor = resolver.query(Phone.CONTENT_URI,PHONES_PROJECTION, null,null,"sort_key COLLATE LOCALIZED asc"); 
        phoneCursor = resolver.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);         
   }
}
