package com.android.ZhitongCall;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;

public class About extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.more_about);
        pul.addActivity(About.this);
    }  
    public void mback(View bind_return_btn){
		About.this.finish();
    } 
}
