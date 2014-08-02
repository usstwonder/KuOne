package com.kukool.iosapp.weather.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;

import com.kukool.iosapp.weather.R;

public class Utils {
	private static final String TAG = "IphoneUtils";
	private static final boolean DEBUG = true;

	public static final String ACTION_ADD_CITY = "com.kukool.iosapp.weather.activity.AddCity";
	public static final String ACTION_FIRST_ADD_CITY = "com.kukool.iosapp.weather.activity.FirstAddCity";
	public static final String ACTION_WEATHER_SETTINGS = "com.kukool.iosapp.weather.activity.WeatherSettings";
	public static final String ACTION_FIRST_DISPLAY_WEATHER_AND_SETTINGS = "com.kukool.iosapp.weather.activity.FirstDisplayWeatherAndSettings";
	public static final String ACTION_DISPLAY_WEATHER_AND_SETTINGS = "com.kukool.iosapp.weather.activity.DisplayWeatherAndSettings";

	public static final String YAHOO_WEATHER_DETAIL_URI = "http://m.yahoo.com/appleww/onesearch?p=";
	public static final String YAHOO_SEARCH_URI = "http://yahoo.xhtml.weather.com/xhtml/search";

	public static final String NO_CHINISE_CITY = "no_chinise_city";
	
	private Context mContext;

	public Utils(Context context) {
		mContext = context;
	}

	// 2011-5-9
	// 取得从当天开始后的六天的
	// DAY_OF_WEEK的str
	public String[] getArrayDayOfWeek(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		String[] arrayDayOfWeek = mContext.getResources().getStringArray(
				R.array.strDayOfWeek);
		String[] targetArray = new String[6];
		for (int i = 0; i < 6; i++) {
			int j = (dayOfWeek + i) % 7;
			targetArray[i] = arrayDayOfWeek[j];
		}
		return targetArray;
	}

	// 是否为白天
	public static boolean isDayTime(String localTime) {
		int hourOfDay = getHour(localTime);
		if (hourOfDay >= 6 && hourOfDay < 18) {
			return true;
		} else {
			return false;
		}
	}

    public static int getHour(String localTime) {
        String time = localTime.substring(0, 2);
        return Integer.parseInt(time);
    }

	// 格式化刷新时间
	public static String formatRefreshTime(long timeMillis) {
		Date date = new Date();
		date.setTime(timeMillis);
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
		return sdf.format(date);
	}

	// 获得华氏温度
	public static String getFTmep(int temp) {
		return String.valueOf(Math.round((double) temp * 9 / 5 + 32));
	}
	
//	// 获得华氏温度
//	public Bitmap[] getDayNightBitmap(int temp) {
//		Resources res = mContext.getResources();
//		Bitmap bgDayTime = BitmapFactory.decodeResource(res, R.drawable.bg_daytime);
//		Bitmap bgNightTime = BitmapFactory.decodeResource(res, R.drawable.bg_nighttime);
//		return new Bitmap[] {bgDayTime, bgNightTime};
//	}
	
	public String getChineseCity(String cityState) {
		String[] arrayCitiesCn = mContext.getResources().getStringArray(
				R.array.arrayCitiesCn);
		String[] arrayCitiesEn = mContext.getResources().getStringArray(
				R.array.arrayCitiesEn);
		String cityCn = NO_CHINISE_CITY;
		for (int i = 0; i < arrayCitiesEn.length; i++) {
			if (cityState.equals(arrayCitiesEn[i])) {
				cityCn = arrayCitiesCn[i];
				break;
			}
		}
		return cityCn;
	}
	
//	// 初次进入该
//	// 工程加载中文城市
//	public void insertChineseCity() {
//		String[] arrayCitiesCn = mContext.getResources().getStringArray(
//				R.array.arrayCitiesCn);
//		String[] arrayCitiesEn = mContext.getResources().getStringArray(
//				R.array.arrayCitiesEn);
//		DatabaseAction databaseAction = new DatabaseAction(mContext);
//		for (int i = 0; i < arrayCitiesEn.length; i++) {
//			ContentValues values = new ContentValues();
//			values.put(Cities_Column.CITY_STATE_EN, arrayCitiesEn[i]);
//			values.put(Cities_Column.CITY_CN,encodeUrlData(arrayCitiesCn[i]));
//			Log.e(TAG, "arrayCitiesEn[i]-----------------> ");
//			Log.e(TAG, "arrayCitiesEn[i] = " + arrayCitiesEn[i]);
//			Log.e(TAG, "arrayCitiesCn[i] = " + arrayCitiesCn[i]);
//			databaseAction.doInsertCities(values);
//		}
//	}
//	
//	private static String encodeUrlData(String urldata) {
//		return urldata.replace("u0028", "(").replace("u0029", ")");
//	}
}
