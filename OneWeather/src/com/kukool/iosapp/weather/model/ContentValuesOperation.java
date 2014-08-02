package com.kukool.iosapp.weather.model;

import com.kukool.iosapp.weather.provider.Weather.Weather_Column;
import com.kukool.iosapp.weather.ui.MetricCheckButton;

import android.content.ContentValues;
import android.database.Cursor;

public class ContentValuesOperation {
	
	private static final String TAG = "ContentValuesOperation";
	private static final boolean DEBUG = true;

	public static ContentValues getContentValuesByPos(Cursor cursor, int position) {
		if (position < 0 || position >= cursor.getCount()) {
			return null;
		}
//		 if (!mCursor.moveToPosition(position)) {
//	            throw new IllegalStateException("couldn't move cursor to position " + position);
//	        }
		ContentValues values = new ContentValues();
		cursor.moveToPosition(position);
		if (!cursor.isAfterLast()) {
			values.put(Weather_Column._ID,  cursor.getInt(cursor.getColumnIndex(Weather_Column._ID)));		
			values.put(Weather_Column.CITY, cursor.getString(cursor.getColumnIndex(Weather_Column.CITY)));
			values.put(Weather_Column.STATE, cursor.getString(cursor.getColumnIndex(Weather_Column.STATE)));
			values.put(Weather_Column.LOCTION, cursor.getString(cursor.getColumnIndex(Weather_Column.LOCTION)));
			values.put(Weather_Column.CITY_CN, cursor.getString(cursor.getColumnIndex(Weather_Column.CITY_CN)));
			values.put(Weather_Column.METRIC, cursor.getInt(cursor.getColumnIndex(Weather_Column.METRIC)));		
			values.put(Weather_Column.POSITION, cursor.getInt(cursor.getColumnIndex(Weather_Column.POSITION)));
			values.put(Weather_Column.REFRESH_TIME, cursor.getString(cursor.getColumnIndex(Weather_Column.REFRESH_TIME)));
			values.put(Weather_Column.LOCAL_TIME, cursor.getString(cursor.getColumnIndex(Weather_Column.LOCAL_TIME)));
			values.put(Weather_Column.CURRENT_WEATHERICON, cursor.getInt(cursor.getColumnIndex(Weather_Column.CURRENT_WEATHERICON)));
			values.put(Weather_Column.WEATHER_ICON_DAY1, cursor.getInt(cursor.getColumnIndex(Weather_Column.WEATHER_ICON_DAY1)));
			values.put(Weather_Column.WEATHER_ICON_DAY2, cursor.getInt(cursor.getColumnIndex(Weather_Column.WEATHER_ICON_DAY2)));
			values.put(Weather_Column.WEATHER_ICON_DAY3, cursor.getInt(cursor.getColumnIndex(Weather_Column.WEATHER_ICON_DAY3)));
			values.put(Weather_Column.WEATHER_ICON_DAY4,  cursor.getInt(cursor.getColumnIndex(Weather_Column.WEATHER_ICON_DAY4)));
			values.put(Weather_Column.WEATHER_ICON_DAY5,  cursor.getInt(cursor.getColumnIndex(Weather_Column.WEATHER_ICON_DAY5)));
			values.put(Weather_Column.WEATHER_ICON_DAY6,  cursor.getInt(cursor.getColumnIndex(Weather_Column.WEATHER_ICON_DAY6)));


            values.put(Weather_Column.CURRENT_WEATHERTEXT, cursor.getString(cursor.getColumnIndex(Weather_Column.CURRENT_WEATHERTEXT)));
            values.put(Weather_Column.CURRENT_NIGHT_WEATHERTEXT, cursor.getString(cursor.getColumnIndex(Weather_Column.CURRENT_NIGHT_WEATHERTEXT)));
            values.put(Weather_Column.DAY1_WEATHERTEXT, cursor.getString(cursor.getColumnIndex(Weather_Column.DAY1_WEATHERTEXT)));
            values.put(Weather_Column.DAY2_WEATHERTEXT, cursor.getString(cursor.getColumnIndex(Weather_Column.DAY2_WEATHERTEXT)));
            values.put(Weather_Column.DAY3_WEATHERTEXT, cursor.getString(cursor.getColumnIndex(Weather_Column.DAY3_WEATHERTEXT)));
            values.put(Weather_Column.DAY4_WEATHERTEXT, cursor.getString(cursor.getColumnIndex(Weather_Column.DAY4_WEATHERTEXT)));
            values.put(Weather_Column.DAY5_WEATHERTEXT, cursor.getString(cursor.getColumnIndex(Weather_Column.DAY5_WEATHERTEXT)));
            values.put(Weather_Column.DAY6_WEATHERTEXT, cursor.getString(cursor.getColumnIndex(Weather_Column.DAY6_WEATHERTEXT)));

            values.put(Weather_Column.CURRENT_HUMIDITY, cursor.getString(cursor.getColumnIndex(Weather_Column.CURRENT_HUMIDITY)));
            values.put(Weather_Column.CURRENT_REALFEEL, cursor.getString(cursor.getColumnIndex(Weather_Column.CURRENT_REALFEEL)));
            values.put(Weather_Column.CURRENT_WINDSPEED, cursor.getString(cursor.getColumnIndex(Weather_Column.CURRENT_WINDSPEED)));
            values.put(Weather_Column.CURRENT_WINDDIRECTION, cursor.getString(cursor.getColumnIndex(Weather_Column.CURRENT_WINDDIRECTION)));
            values.put(Weather_Column.CURRENT_VISIBILITY, cursor.getString(cursor.getColumnIndex(Weather_Column.CURRENT_VISIBILITY)));

//			if (DEBUG) {
//				Log.e(TAG, "getContentValuesByPos ---------------->" );
//				Log.e(TAG, "getContentValuesByPos  position = " + position);
//				Log.e(TAG, "getContentValuesByPos  city = " + cursor.getString(cursor.getColumnIndex(Weather_Column.CITY)));
//				Log.e(TAG, "getContentValuesByPos  state = " + cursor.getString(cursor.getColumnIndex(Weather_Column.STATE)));
//			}
			int metric = cursor.getInt(cursor.getColumnIndex(Weather_Column.METRIC));
			if (metric == MetricCheckButton.C_ON) { // 如果metric为摄氏
				values.put(Weather_Column.CURRENT_TEMPERATURE_C, cursor.getInt(cursor.getColumnIndex(Weather_Column.CURRENT_TEMPERATURE_C)));
    			
    			values.put(Weather_Column.HIGH_TEMPERATURE_DAY1_C, cursor.getInt(cursor.getColumnIndex(Weather_Column.HIGH_TEMPERATURE_DAY1_C)));
    			values.put(Weather_Column.LOW_TEMPERATURE_DAY1_C, cursor.getInt(cursor.getColumnIndex(Weather_Column.LOW_TEMPERATURE_DAY1_C)));
    			
    			values.put(Weather_Column.HIGH_TEMPERATURE_DAY2_C, cursor.getInt(cursor.getColumnIndex(Weather_Column.HIGH_TEMPERATURE_DAY2_C)));
    			values.put(Weather_Column.LOW_TEMPERATURE_DAY2_C, cursor.getInt(cursor.getColumnIndex(Weather_Column.LOW_TEMPERATURE_DAY2_C)));
    			
    			values.put(Weather_Column.HIGH_TEMPERATURE_DAY3_C,  cursor.getInt(cursor.getColumnIndex(Weather_Column.HIGH_TEMPERATURE_DAY3_C)));
    			values.put(Weather_Column.LOW_TEMPERATURE_DAY3_C, cursor.getInt(cursor.getColumnIndex(Weather_Column.LOW_TEMPERATURE_DAY3_C)));
    			
    			values.put(Weather_Column.HIGH_TEMPERATURE_DAY4_C,  cursor.getInt(cursor.getColumnIndex(Weather_Column.HIGH_TEMPERATURE_DAY4_C)));
    			values.put(Weather_Column.LOW_TEMPERATURE_DAY4_C,  cursor.getInt(cursor.getColumnIndex(Weather_Column.LOW_TEMPERATURE_DAY4_C)));
    			
    			values.put(Weather_Column.HIGH_TEMPERATURE_DAY5_C,  cursor.getInt(cursor.getColumnIndex(Weather_Column.HIGH_TEMPERATURE_DAY5_C)));
    			values.put(Weather_Column.LOW_TEMPERATURE_DAY5_C,  cursor.getInt(cursor.getColumnIndex(Weather_Column.LOW_TEMPERATURE_DAY5_C)));
    			
    			values.put(Weather_Column.HIGH_TEMPERATURE_DAY6_C,  cursor.getInt(cursor.getColumnIndex(Weather_Column.HIGH_TEMPERATURE_DAY6_C)));
    			values.put(Weather_Column.LOW_TEMPERATURE_DAY6_C,  cursor.getInt(cursor.getColumnIndex(Weather_Column.LOW_TEMPERATURE_DAY6_C)));
			} else {
				values.put(Weather_Column.CURRENT_TEMPERATURE_F, cursor.getInt(cursor.getColumnIndex(Weather_Column.CURRENT_TEMPERATURE_F)));
    			
    			values.put(Weather_Column.HIGH_TEMPERATURE_DAY1_F, cursor.getInt(cursor.getColumnIndex(Weather_Column.HIGH_TEMPERATURE_DAY1_F)));
    			values.put(Weather_Column.LOW_TEMPERATURE_DAY1_F, cursor.getInt(cursor.getColumnIndex(Weather_Column.LOW_TEMPERATURE_DAY1_F)));
    			
    			values.put(Weather_Column.HIGH_TEMPERATURE_DAY2_F, cursor.getInt(cursor.getColumnIndex(Weather_Column.HIGH_TEMPERATURE_DAY2_F)));
    			values.put(Weather_Column.LOW_TEMPERATURE_DAY2_F, cursor.getInt(cursor.getColumnIndex(Weather_Column.LOW_TEMPERATURE_DAY2_F)));
    			
    			values.put(Weather_Column.HIGH_TEMPERATURE_DAY3_F,  cursor.getInt(cursor.getColumnIndex(Weather_Column.HIGH_TEMPERATURE_DAY3_F)));
    			values.put(Weather_Column.LOW_TEMPERATURE_DAY3_F, cursor.getInt(cursor.getColumnIndex(Weather_Column.LOW_TEMPERATURE_DAY3_F)));
    			
    			values.put(Weather_Column.HIGH_TEMPERATURE_DAY4_F,  cursor.getInt(cursor.getColumnIndex(Weather_Column.HIGH_TEMPERATURE_DAY4_F)));
    			values.put(Weather_Column.LOW_TEMPERATURE_DAY4_F,  cursor.getInt(cursor.getColumnIndex(Weather_Column.LOW_TEMPERATURE_DAY4_F)));
    			
    			values.put(Weather_Column.HIGH_TEMPERATURE_DAY5_F,  cursor.getInt(cursor.getColumnIndex(Weather_Column.HIGH_TEMPERATURE_DAY5_F)));
    			values.put(Weather_Column.LOW_TEMPERATURE_DAY5_F,  cursor.getInt(cursor.getColumnIndex(Weather_Column.LOW_TEMPERATURE_DAY5_F)));
    			
    			values.put(Weather_Column.HIGH_TEMPERATURE_DAY6_F,  cursor.getInt(cursor.getColumnIndex(Weather_Column.HIGH_TEMPERATURE_DAY6_F)));
    			values.put(Weather_Column.LOW_TEMPERATURE_DAY6_F,  cursor.getInt(cursor.getColumnIndex(Weather_Column.LOW_TEMPERATURE_DAY6_F)));
			}
		}	
		cursor.close();
		return values;
	}
	
	
}
