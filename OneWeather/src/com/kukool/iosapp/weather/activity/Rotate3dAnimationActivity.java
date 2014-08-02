package com.kukool.iosapp.weather.activity;

import com.kukool.iosapp.weather.R;
import com.kukool.iosapp.weather.anim.WeatherAnimationUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.kukool.iosapp.weather.R;
abstract class Rotate3dAnimationActivity extends com.kukool.iosapp.weather.activity.PortraitActivity {
	
	private FrameLayout mContainer;
	private RelativeLayout mDisplayWeather;
	private RelativeLayout mWeatherSettings;
	
	// 旋转目标 "天气设置" 或者 "显示天气"
	public static final int ROTATE_TARGET_WEATHER_SETTINGS = 100;
	public static final int ROTATE_TARGET_DISPLAY_WEATHER = 101;
	public static final int FIRST_ANIMATION = 200;
	public static final int SECOND_ANIMATION = 201;
	private static final long ANIMATION_DURATION = 350;
	
	public void applyRotation(int rotateTarget) {
		doBeforeAnim();
		
		mContainer = (FrameLayout) findViewById(R.id.container);
		mDisplayWeather = (RelativeLayout) findViewById(R.id.display_weather);
		mWeatherSettings = (RelativeLayout) findViewById(R.id.weather_settings);
		
		Animation anim = null;
		if (rotateTarget == ROTATE_TARGET_WEATHER_SETTINGS) {
			anim = WeatherAnimationUtils.loadAnimation(this,
					R.anim.rotation1_first_anim);
		} else if (rotateTarget == ROTATE_TARGET_DISPLAY_WEATHER) {
			anim = WeatherAnimationUtils.loadAnimation(this,
					R.anim.rotation2_first_anim);
		}
		anim.setDuration(ANIMATION_DURATION);
		anim.setFillAfter(true);
		anim.setInterpolator(new AccelerateInterpolator());
		anim.setAnimationListener(new DisplayNextView(rotateTarget, FIRST_ANIMATION));
		mContainer.startAnimation(anim);
	}

	
	private final class DisplayNextView implements Animation.AnimationListener {
		private final int mRotateTarget;
		private int mTag;
		private DisplayNextView(int rotateTarget, int tag) {
			mRotateTarget = rotateTarget;
			mTag = tag;
		}

		public void onAnimationStart(Animation animation) {
		}

		// 第一个动画结束后接着做第二个动画
		public void onAnimationEnd(Animation animation) {
			if (mTag == FIRST_ANIMATION) {
				mContainer.post(new SwapViews(mRotateTarget));
			} else if (mTag == SECOND_ANIMATION) {
				doAfterAnim();
			}
		}

		public void onAnimationRepeat(Animation animation) {
		}
	}
	
	// SwapViews 互换视图
	private final class SwapViews implements Runnable {
		private final int mRotateTarget;

		public SwapViews(int rotateTarget) {
			mRotateTarget = rotateTarget;
		}

		public void run() {
			Animation anim = null;
			if (mRotateTarget == ROTATE_TARGET_WEATHER_SETTINGS) {
				mDisplayWeather.setVisibility(View.GONE);
				mWeatherSettings.setVisibility(View.VISIBLE);
				anim = WeatherAnimationUtils.loadAnimation(
						Rotate3dAnimationActivity.this,
						R.anim.rotation1_second_anim);
			}  else if (mRotateTarget == ROTATE_TARGET_DISPLAY_WEATHER) {
				mWeatherSettings.setVisibility(View.GONE);
				mDisplayWeather.setVisibility(View.VISIBLE);
				anim = WeatherAnimationUtils.loadAnimation(
						Rotate3dAnimationActivity.this,
						R.anim.rotation2_second_anim);
			}
			anim.setDuration(ANIMATION_DURATION);
			anim.setFillAfter(true);
			anim.setInterpolator(new DecelerateInterpolator());
			anim.setAnimationListener(new DisplayNextView(mRotateTarget, SECOND_ANIMATION));
			mContainer.startAnimation(anim);
		}
	}
	
	// 第二个动画结束后
	// 具体的实现交给子类
	public abstract void doAfterAnim();
	
	public abstract void doBeforeAnim();
}
