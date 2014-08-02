package com.kukool.iosapp.weather.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Window;

/**
 *禁止横屏 
 */
public class PortraitActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
				| ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		Rect frame = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;    
		int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();  
		int titleBarHeight = contentTop - statusBarHeight;
	}
}
