package com.kukool.iosapp.weather.ui;


import com.kukool.iosapp.weather.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class SmsLeftYuan extends FrameLayout {
	private ImageView mSmsLeftDown,mSmsLeftUp;
	public SmsLeftYuan(Context context) {
		this(context, null);
	}

	public SmsLeftYuan(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public SmsLeftYuan(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mSmsLeftDown =  new ImageView(context,attrs);
		mSmsLeftDown.setImageResource(R.drawable.iphonesms_yuan_1_bg);
		addView(mSmsLeftDown);
		mSmsLeftUp = new ImageView(context,attrs);
		mSmsLeftUp.setBackgroundResource(R.drawable.iphonesms_yuan_1_1);
		addView(mSmsLeftUp);
	}
	public void  toRotate(Animation animation) {
		mSmsLeftUp.clearAnimation();
		mSmsLeftUp.startAnimation(animation);
	}

	public ImageView getMSmsLeftDown() {
		return mSmsLeftDown;
	}

	public void setMSmsLeftDown(ImageView smsLeftDown) {
		if(smsLeftDown == null){
			mSmsLeftDown.setImageDrawable(null);
		}else{
			mSmsLeftDown = smsLeftDown;
		}
	}

	public ImageView getMSmsLeftUp() {
		return mSmsLeftUp;
	}

	public void setMSmsLeftUp(ImageView smsLeftUp) {
		mSmsLeftUp = smsLeftUp;
	}
	
	public void setMSmsLeftUp(int id) {
		mSmsLeftUp.setBackgroundResource(id);
	}
	
	public int getImgWidth(){
		return getResources().getDrawable(R.drawable.iphonesms_yuan_1_bg).getIntrinsicWidth();
	}
	
	@Override
	public void clearAnimation() {
		super.clearAnimation();
		mSmsLeftUp.clearAnimation();
	}
}
