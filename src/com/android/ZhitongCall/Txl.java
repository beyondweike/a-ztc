package com.android.ZhitongCall;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import net.sourceforge.pinyin4j.PinyinHelper;  
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;  
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;  
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;  
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Txl extends ListActivity {
    private ArrayList<HashMap<String, Object>>   listItems;    //存放文字、图片信息
    private SimpleAdapter listItemAdapter;           //适配器    
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;  
    private static final int PHONES_NUMBER_INDEX = 1;

    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.txl);
		findViewById(R.id.bottom_home_layout_ly).setSelected(false);
    	findViewById(R.id.bottom_card_layout_ly).setSelected(true);
    	findViewById(R.id.bottom_msg_layout_ly).setSelected(false);
    	findViewById(R.id.bottom_order_layout_ly).setSelected(false);
    	findViewById(R.id.bottom_other_layout_ly).setSelected(false);
        if (pul.spid.equals("") || pul.susername.equals(""))
        {
        	new AlertDialog.Builder(this)  
        	.setTitle("登录超时")
        	.setMessage("抱歉：您的登录已超时请重新登录")
        	.setPositiveButton("确定", new DialogInterface.OnClickListener() {   
    			public void onClick(DialogInterface dialog, int whichButton) {   
    				Intent i = new Intent(Txl.this, Myxf.class);    
    	        	Txl.this.startActivity(i);
    	        	Txl.this.finish();
    				}   
    				})
        	.show();
        }
        pul.mcnum="";
    	pul.addActivity(Txl.this);
    	
    	final ProgressDialog pd = ProgressDialog.show(this, "提示：", "数据加载中，请稍后……");
    	new Handler().postDelayed(new Runnable(){   
			            public void run() {  
			            	initListView();
			        		setListAdapter(listItemAdapter);
			        		pd.dismiss();
			            }   
			        }, 100);
		
    }
    
    private void initListView()   {   
        listItems = new ArrayList<HashMap<String, Object>>();
        
      	if (MainActivity.phoneCursor != null && MainActivity.phoneCursor.moveToFirst()) 
      	{  
      		do
      	    {  
	      	    //String phoneNumber = MainActivity.phoneCursor.getString(PHONES_NUMBER_INDEX);
      	    	String phoneNumber = MainActivity.phoneCursor.getString(MainActivity.phoneCursor.getColumnIndex(Calls.NUMBER)); 
      	    	if(phoneNumber==null || phoneNumber.length()<=2)
      	    	{
	      	    	continue;
	      	    }
      	    	
      	    	String name = MainActivity.phoneCursor.getString(MainActivity.phoneCursor.getColumnIndexOrThrow(Calls.CACHED_NAME)); 
	      	    String dateStr=MainActivity.phoneCursor.getString(MainActivity.phoneCursor.getColumnIndex(Calls.DATE));
	      	    long datetime = Long.parseLong(dateStr);
	      	    SimpleDateFormat sfd = new SimpleDateFormat("M月d日 H:m");
	      	  	String datetimeStr= sfd.format(new Date(datetime));
	      	  	
	      	  	Log.i("name,number:",name+","+phoneNumber);
	      	  	
	      	  	
	      	  	//phoneNumber = MainActivity.phoneCursor.getString(MainActivity.phoneCursor.getColumnIndex(Calls.CACHED_NUMBER_LABEL)); 
	      	    
	      	  	String line2=phoneNumber;
	      	    if(name==null || name.length()==0 || name.startsWith("未知"))
	      	    {
	      	    	name=line2;
	      	    	line2=null;
	      	    }
	      	    if(line2==null)
	      	    {
	      	    	line2=datetimeStr;
	      	    }
	      	    else
	      	    {
	      	    	line2=line2+"  "+datetimeStr;
	      	    }
	      			  
		      	HashMap<String, Object> map = new HashMap<String, Object>();   
		          //map.put("tname", "【"+getFirstSpell(MainActivity.phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX))+"】"+MainActivity.phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX));
		      	  //map.put("ttel", "电话:"+pul.appendSeprator(phoneNumber," ",4));
		        map.put("timg", R.drawable.reg_name_icon);
		        map.put("tname", name==null?"未知号码":name);
		        map.put("ttel", line2);
		        map.put("number", phoneNumber);
		          
		        listItems.add(map);
      	    }while (MainActivity.phoneCursor.moveToNext());
      	  //MainActivity.phoneCursor.close();  
      	}

        
        listItemAdapter = new SimpleAdapter(this,listItems,    
                R.layout.txl_item,
                new String[] {"tname", "ttel","timg"},   
                new int[ ] {R.id.tname,R.id.ttel,R.id.timg}  
        );   
    }
    
    @Override  
    protected void onListItemClick(ListView l, View v, int position, long id) {  
        // TODO Auto-generated method stub  
    	//TextView text=(TextView)v.findViewById(R.id.ttel);
    	//showInfo(text.getText().toString().replace("电话:", "").replace(" ", ""));
    	HashMap<String, Object> map=listItems.get(position);
    	String number=(String)map.get("number");
    	showInfo(number);
    }     
      
   
    public static String getFirstSpell(String chinese) { 
    	if(chinese.replaceAll("[a-z]*[A-Z]*[\u4e00-\u9fa5]*\\d*\\s*", "").length()!=0){
   		 return "其它";
   	    }	
        StringBuffer pybf = new StringBuffer();   
        char[] arr = chinese.toCharArray();   
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();   
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);   
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);   
        for (int i = 0; i < arr.length; i++) {   
                if (arr[i] > 128) {   
                        try {   
                                String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);   
                                if (temp != null) {   
                                        pybf.append(temp[0].charAt(0));   
                                }   
                        } catch (BadHanyuPinyinOutputFormatCombination e) {   
                                e.printStackTrace();   
                        }   
                } else {   
                        pybf.append(arr[i]);   
                }   
        }   
        return pybf.toString().replaceAll("\\W", "").trim().substring(0, 1).toUpperCase();   
     }   
    public void showInfo(final String num){
    	AlertDialog.Builder builder = new AlertDialog.Builder(Txl.this);           
    	builder.setIcon(R.drawable.txlcall);  
    	builder.setTitle("确定拨打 "+num+" 吗？");  
    	builder.setPositiveButton("确定拨打", new DialogInterface.OnClickListener() {  
    	    public void onClick(DialogInterface dialog, int whichButton) { 
    	    	pul.mcnum=num;
    	    	Intent i = new Intent(Txl.this, dianhua.class); 
    	    	Txl.this.startActivity(i);
    	    }  
    	});  
    	builder.setNegativeButton("取消选择", new DialogInterface.OnClickListener() {  
    	    public void onClick(DialogInterface dialog, int whichButton) {  
    	    	dialog.dismiss();
    	    }  
    	});  
    	builder.create().show(); 
    }
     
    public void mgetcall(View tcallbtn){
    	Intent i = new Intent(Txl.this, dianhua.class); 
    	Txl.this.startActivity(i);
    } 
	public void mback(View bind_return_btn){
		Txl.this.finish();
    } 
    public void mcall1(View bottom_home_bn_style){
    	Intent i = new Intent(Txl.this, Call.class); 
    	Txl.this.startActivity(i);
    }
    public void mcall2(View bottom_card_bn_style){
    	Intent i = new Intent(Txl.this, Txl.class); 
    	Txl.this.startActivity(i);
    }
    public void mcall3(View bottom_msg_bn_style){
    	Intent i = new Intent(Txl.this, Msg.class); 
    	Txl.this.startActivity(i);
    }
    public void mcall4(View home_bottom_order_bn){
    	pul.sact="order";
    	Intent i = new Intent(Txl.this, Order.class); 
    	Txl.this.startActivity(i);
    }
    public void mcall5(View home_bottom_other_bn){
    	Intent i = new Intent(Txl.this, Other.class); 
    	Txl.this.startActivity(i);
    }

}