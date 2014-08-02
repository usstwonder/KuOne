package com.kukool.iosapp.weather.ui;

/**
 * 初始化天气视�

 * 资源, 以及一些静态的方法
 */
import com.kukool.iosapp.weather.activity.LaunchActivity;
import com.kukool.iosapp.weather.R;
import com.kukool.iosapp.weather.activity.WeatherMain;
import com.kukool.iosapp.weather.model.Utils;
import com.kukool.iosapp.weather.provider.Weather.Weather_Column;

import android.content.ContentValues;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.RelativeLayout;

public class WeatherView  extends RelativeLayout {
	protected static final String TAG = "WeatherView";
	protected static final boolean DEBUG = true;
	
	protected String[] mArrayDayOfWeek;
	protected WeatherMain mParentActivity;
	

	public WeatherView(WeatherMain activity) {
		super(activity);
		// TODO Auto-generated constructor stub
		mParentActivity = activity;		
		
		initRes();
	}
	 

	protected void initRes() {

		if (mArrayDayOfWeek == null) {
			mArrayDayOfWeek = new Utils(mParentActivity).getArrayDayOfWeek(System
					.currentTimeMillis());
		}

	}
	
	
	protected Bitmap getDrawBitmap(int weatherIcon) {
        return mParentActivity.getDrawBitmap(weatherIcon);
	}
	
	public static int[] getTempArray(ContentValues contentValues) {
		int curTemp = 0;
		int highTempDay1 = 0;
		int lowTempDay1 = 0;
		int highTempDay2 = 0;
		int lowTempDay2 = 0;
		int highTempDay3 = 0;
		int lowTempDay3 = 0;
		int highTempDay4 = 0;
		int lowTempDay4 = 0;
		int highTempDay5 = 0;
		int lowTempDay5 = 0;
		int highTempDay6 = 0;
		int lowTempDay6 = 0;
		int metric = (Integer) contentValues.get(Weather_Column.METRIC);
		switch (metric) {
		case MetricCheckButton.C_ON: // 如果metric为摄�
			curTemp = (Integer) contentValues
					.get(Weather_Column.CURRENT_TEMPERATURE_C);

			highTempDay1 = (Integer) contentValues
					.get(Weather_Column.HIGH_TEMPERATURE_DAY1_C);
			lowTempDay1 = (Integer) contentValues
					.get(Weather_Column.LOW_TEMPERATURE_DAY1_C);

			highTempDay2 = (Integer) contentValues
					.get(Weather_Column.HIGH_TEMPERATURE_DAY2_C);
			lowTempDay2 = (Integer) contentValues
					.get(Weather_Column.LOW_TEMPERATURE_DAY2_C);

			highTempDay3 = (Integer) contentValues
					.get(Weather_Column.HIGH_TEMPERATURE_DAY3_C);
			lowTempDay3 = (Integer) contentValues
					.get(Weather_Column.LOW_TEMPERATURE_DAY3_C);

			highTempDay4 = (Integer) contentValues
					.get(Weather_Column.HIGH_TEMPERATURE_DAY4_C);
			lowTempDay4 = (Integer) contentValues
					.get(Weather_Column.LOW_TEMPERATURE_DAY4_C);

			highTempDay5 = (Integer) contentValues
					.get(Weather_Column.HIGH_TEMPERATURE_DAY5_C);
			lowTempDay5 = (Integer) contentValues
					.get(Weather_Column.LOW_TEMPERATURE_DAY5_C);

			highTempDay6 = (Integer) contentValues
					.get(Weather_Column.HIGH_TEMPERATURE_DAY6_C);
			lowTempDay6 = (Integer) contentValues
					.get(Weather_Column.LOW_TEMPERATURE_DAY6_C);
			break;
		case MetricCheckButton.F_ON:// 如果metric为华�
			curTemp = (Integer) contentValues
					.get(Weather_Column.CURRENT_TEMPERATURE_F);

			highTempDay1 = (Integer) contentValues
					.get(Weather_Column.HIGH_TEMPERATURE_DAY1_F);
			lowTempDay1 = (Integer) contentValues
					.get(Weather_Column.LOW_TEMPERATURE_DAY1_F);

			highTempDay2 = (Integer) contentValues
					.get(Weather_Column.HIGH_TEMPERATURE_DAY2_F);
			lowTempDay2 = (Integer) contentValues
					.get(Weather_Column.LOW_TEMPERATURE_DAY2_F);

			highTempDay3 = (Integer) contentValues
					.get(Weather_Column.HIGH_TEMPERATURE_DAY3_F);
			lowTempDay3 = (Integer) contentValues
					.get(Weather_Column.LOW_TEMPERATURE_DAY3_F);

			highTempDay4 = (Integer) contentValues
					.get(Weather_Column.HIGH_TEMPERATURE_DAY4_F);
			lowTempDay4 = (Integer) contentValues
					.get(Weather_Column.LOW_TEMPERATURE_DAY4_F);

			highTempDay5 = (Integer) contentValues
					.get(Weather_Column.HIGH_TEMPERATURE_DAY5_F);
			lowTempDay5 = (Integer) contentValues
					.get(Weather_Column.LOW_TEMPERATURE_DAY5_F);

			highTempDay6 = (Integer) contentValues
					.get(Weather_Column.HIGH_TEMPERATURE_DAY6_F);
			lowTempDay6 = (Integer) contentValues
					.get(Weather_Column.LOW_TEMPERATURE_DAY6_F);
			break;
		}

		int[] intArray = { curTemp, highTempDay1, lowTempDay1, highTempDay2,
				lowTempDay2, highTempDay3, lowTempDay3, highTempDay4,
				lowTempDay4, highTempDay5, lowTempDay5, highTempDay6,
				lowTempDay6 };
		return intArray;
	}

	public static int[] getWeatherIconArray(ContentValues contentValues) {
		int curWeathericon = (Integer) contentValues
				.get(Weather_Column.CURRENT_WEATHERICON);
		int weatherIconDay1 = (Integer) contentValues
				.get(Weather_Column.WEATHER_ICON_DAY1);
		int weatherIconDay2 = (Integer) contentValues
				.get(Weather_Column.WEATHER_ICON_DAY2);
		int weatherIconDay3 = (Integer) contentValues
				.get(Weather_Column.WEATHER_ICON_DAY3);
		int weatherIconDay4 = (Integer) contentValues
				.get(Weather_Column.WEATHER_ICON_DAY4);
		int weatherIconDay5 = (Integer) contentValues
				.get(Weather_Column.WEATHER_ICON_DAY5);
		int weatherIconDay6 = (Integer) contentValues
				.get(Weather_Column.WEATHER_ICON_DAY6);
		int[] intArray = { curWeathericon, weatherIconDay1, weatherIconDay2,
				weatherIconDay3, weatherIconDay4, weatherIconDay5,
				weatherIconDay6 };
		return intArray;
	}
	
}
