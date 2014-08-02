package com.kukool.iosapp.weather.activity;
import com.kukool.iosapp.weather.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

public class ConversationStarterActivity extends Activity {
	  private HandlerThread t;
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        // 做一个简单的view看上去像是要启动的Acitivity
	        setContentView(R.layout.conversation_starter_layout);
	        
	        //异步流程，启动目标Activity
	        final Handler handler = new Handler();
	        handler.postDelayed(
	            new Runnable(){
	                    @Override
	                    public void run() {
	                        // TODO Auto-generated method stub
	                        //启动目标Activity
	                        Intent cIntent = new Intent(ConversationStarterActivity.this,LaunchActivity.class);
	                        startActivity(cIntent);
	                        //关闭所有切换动画
	                        overridePendingTransition(0, 0);
	                        //关掉当前Activity
	                        finish();
	                    }     
	                }, 
	            200);
	    }
	    
	    
	    
}
