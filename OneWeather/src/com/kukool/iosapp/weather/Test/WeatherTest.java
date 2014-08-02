//package com.kukool.iosapp.weather.Test;
///**
// * 测试用的类
// */
//import android.content.ContentUris;
//import android.content.ContentValues;
//import android.content.Context;
//import android.net.Uri;
//import android.util.Log;
//
//import com.kukool.iosapp.weather.provider.Weather;
//
//public class WeatherTest {
//	private static final String TAG = "WeatherTest";
//	private static final boolean DEBUG = true;
//	
//	private Context mContext;
//	public WeatherTest(Context context) {
//		mContext = context;
//	}
//	
//	private ContentValues getContentValues() {
//		Long now = Long.valueOf(System.currentTimeMillis());
//		ContentValues values = new ContentValues();
//		values.put(Weather.Weather_Column.CITY, "Changsha");
//		values.put(Weather.Weather_Column.STATE, "China(Hunan)");
//		values.put(Weather.Weather_Column.LOCTION, "ASI|CN|CH014|CHANGSHA|");
//
//		values.put(Weather.Weather_Column.METRIC, "1");
//		values.put(Weather.Weather_Column.POSITION, "1");
//		values.put(Weather.Weather_Column.REFRESH_TIME, now);
//
//		values.put(Weather.Weather_Column.CURRENT_WEATHERICON, "1");
//		values.put(Weather.Weather_Column.CURRENT_TEMPERATURE, "1");
//
//		values.put(Weather.Weather_Column.WEATHER_ICON_DAY1, "1");
//		values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY1, "1");
//		values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY1, "1");
//
//		values.put(Weather.Weather_Column.WEATHER_ICON_DAY2, "2");
//		values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY2, "2");
//		values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY2, "2");
//
//		values.put(Weather.Weather_Column.WEATHER_ICON_DAY3, "3");
//		values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY3, "3");
//		values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY3, "3");
//
//		values.put(Weather.Weather_Column.WEATHER_ICON_DAY4, "4");
//		values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY4, "4");
//		values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY4, "4");
//
//		values.put(Weather.Weather_Column.WEATHER_ICON_DAY5, "5");
//		values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY5, "5");
//		values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY5, "5");
//
//		values.put(Weather.Weather_Column.WEATHER_ICON_DAY6, "6");
//		values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY6, "6");
//		values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY6, "6");
//		return values;
//	}
//	
//	
//	public void doInsertValue() {	
//		Log.e(TAG, "doInsertValue");
//		mContext.getContentResolver().insert(Weather.Weather_Column.CONTENT_URI, getContentValues());	
//    }
//	
//	public void doDeleteValue(long id) {
//		Log.e(TAG, "doDeleteValue");
//		mContext.getContentResolver().delete(ContentUris.withAppendedId(Weather.Weather_Column.CONTENT_URI, id), null, null);
//	}
//
//	
//	public void doUpdateValue(Uri uri) {
//		Log.e(TAG, "doUpdateValue");
//		Long now = Long.valueOf(System.currentTimeMillis());
//		ContentValues values = new ContentValues();
//		values.put(Weather.Weather_Column.CITY, "shijiazhuang");
//		values.put(Weather.Weather_Column.STATE, "China(Guangdong)");
//		values.put(Weather.Weather_Column.LOCTION, "ASI|CN|CH014|CHANGSHA|");
//
//		values.put(Weather.Weather_Column.METRIC, "0");
//		values.put(Weather.Weather_Column.POSITION, "1");
//		values.put(Weather.Weather_Column.REFRESH_TIME, now);
//
//		values.put(Weather.Weather_Column.CURRENT_WEATHERICON, "1");
//		values.put(Weather.Weather_Column.CURRENT_TEMPERATURE, "1");
//
//		values.put(Weather.Weather_Column.WEATHER_ICON_DAY1, "1");
//		values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY1, "1");
//		values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY1, "1");
//
//		values.put(Weather.Weather_Column.WEATHER_ICON_DAY2, "2");
//		values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY2, "2");
//		values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY2, "2");
//
//		values.put(Weather.Weather_Column.WEATHER_ICON_DAY3, "3");
//		values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY3, "3");
//		values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY3, "3");
//
//		values.put(Weather.Weather_Column.WEATHER_ICON_DAY4, "4");
//		values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY4, "4");
//		values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY4, "4");
//
//		values.put(Weather.Weather_Column.WEATHER_ICON_DAY5, "5");
//		values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY5, "5");
//		values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY5, "5");
//
//		values.put(Weather.Weather_Column.WEATHER_ICON_DAY6, "6");
//		values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY6, "6");
//		values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY6, "6");
//		mContext.getContentResolver().update(uri, values, null, null);
//	}
//	
//}
