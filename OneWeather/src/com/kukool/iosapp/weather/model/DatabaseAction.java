package com.kukool.iosapp.weather.model;

/**
 * 2011-5-6
 * 读取数据库数据的一些操作
 */
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.kukool.iosapp.weather.provider.Cities;
import com.kukool.iosapp.weather.provider.Weather;
import com.kukool.iosapp.weather.provider.Weather.Weather_Column;

public class DatabaseAction {
	private static final String TAG = "DatabaseAction";
	private static final boolean DEBUG = true;
	
	private Context mContext;

	public DatabaseAction(Context context) {
		mContext = context;		
	}
	
	public Cursor queryAll() {
		return mContext.getContentResolver().query(
				Weather.Weather_Column.CONTENT_URI, null, null, null, Weather.Weather_Column.POSITION + Weather_Column.ASC);
	}
	
	public void deleteById(int id) {
		mContext.getContentResolver().delete(ContentUris.withAppendedId(Weather.Weather_Column.CONTENT_URI, id), null, null);
	}
	
	public int getCursorCount() {
		Cursor cursor = mContext.getContentResolver().query(Weather.Weather_Column.CONTENT_URI, null, null, null, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}
	
	public int getMaxPosition() {
		Cursor cursor = mContext.getContentResolver().query(
				Weather.Weather_Column.CONTENT_URI, null, null, null, Weather.Weather_Column.POSITION + Weather.Weather_Column.DESC);
		if (cursor.getCount() == 0) {
			return -1;
		}	
		int maxPosition = -1;
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			maxPosition = cursor.getInt(cursor.getColumnIndex(Weather_Column.POSITION));
			break;
		}
		cursor.close();
		return maxPosition;
	}
	public int getMinPosition() {
		Cursor cursor = mContext.getContentResolver().query(
				Weather.Weather_Column.CONTENT_URI, null, null, null, Weather.Weather_Column.POSITION + Weather.Weather_Column.ASC);
		if (cursor.getCount() == 0) {
			return -1;
		}	
		int minPosition = -1;
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			minPosition = cursor.getInt(cursor.getColumnIndex(Weather_Column.POSITION));
			break;
		}
		cursor.close();
		return minPosition;
	}

    public int getCityPosition(String cityName) {
        String selection = Weather_Column.CITY + " = ?";
        String[] selectionArgs = {cityName};
        int position;
        Cursor cursor = mContext.getContentResolver().query(
                Weather.Weather_Column.CONTENT_URI, null, selection, selectionArgs, Weather.Weather_Column.POSITION + Weather.Weather_Column.ASC);
        if (cursor.getCount() == 0) {
            return -1;
        }

        cursor.moveToFirst();
        position = cursor.getInt(cursor.getColumnIndex(Weather_Column.POSITION));
        cursor.close();
        return (position - getMinPosition()) ;
    }

	public int updateValuesByUri(Uri uri, ContentValues values) {	
		return mContext.getContentResolver().update(uri, values, null, null);
	}
	
	// 设置指定id的排列位置
	public void updatePosWithId(int id, int position) {
		Uri uri = ContentUris.withAppendedId(Weather.Weather_Column.CONTENT_URI, id);
		ContentValues values = new ContentValues();
		values.put(Weather.Weather_Column.POSITION, String.valueOf(position));
		
		mContext.getContentResolver().update(uri, values, null, null);
	}
	
	// 更新数据库中每个城市的位置
	// 在删除一个城市后要重新设置每个城市的位置
	public void refreshAllPos() {
		Cursor cursor = mContext.getContentResolver().query(
				Weather.Weather_Column.CONTENT_URI, null, null, null,  Weather.Weather_Column.POSITION + Weather_Column.ASC);
		int position = 0;
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int id = cursor.getInt(cursor.getColumnIndex(Weather_Column._ID));
			Uri uri = ContentUris.withAppendedId(Weather.Weather_Column.CONTENT_URI, id);
			ContentValues values = new ContentValues();
			values.put(Weather.Weather_Column.POSITION, String.valueOf(position));
			
			mContext.getContentResolver().update(uri, values, null, null);
			
			position++;
			cursor.moveToNext();
		}
		cursor.close();
	}
	
	public void updateAllMetric(int metric) {
		Cursor cursor = mContext.getContentResolver().query(
				Weather.Weather_Column.CONTENT_URI, null, null, null, null);
		if (cursor.getCount() == 0) {
			return;
		}
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int id = cursor.getInt(cursor.getColumnIndex(Weather_Column._ID));
			Uri uri = ContentUris.withAppendedId(Weather.Weather_Column.CONTENT_URI, id);
			ContentValues values = new ContentValues();
			values.put(Weather.Weather_Column.METRIC, String.valueOf(metric));	
			mContext.getContentResolver().update(uri, values, null, null);
			cursor.moveToNext();
		}
		cursor.close();
	}
	
	public int getCurrentMetric() {
		Cursor cursor = mContext.getContentResolver().query(
				Weather.Weather_Column.CONTENT_URI, null, null, null, null);
		if (cursor.getCount() == 0) {
            cursor.close();
			return 1;// 默认为摄氏
		}
		cursor.moveToFirst();
		int metric = cursor.getInt(cursor.getColumnIndex(Weather_Column.METRIC));
		cursor.close();
		return metric;
	}
	
	public Uri doInsert(ContentValues values) {//插入数据
		return mContext.getContentResolver().insert(Weather.Weather_Column.CONTENT_URI, values);	
	}
	
	// 查询中文城市
	public Cursor queryCities() {
		return mContext.getContentResolver().query(
				Cities.Cities_Column.CONTENT_URI, null, null, null, null);
	}
	
	public Cursor queryCnCities(String cityStateEn) {
		Log.e(TAG, "queryCnCities cityStateEn = " + cityStateEn);
		return mContext.getContentResolver().query(
				Cities.Cities_Column.CONTENT_URI, null, Cities.Cities_Column.CITY_STATE_EN + "= '" + cityStateEn + "'", null, null);
	}
	
	public Uri doInsertCities(ContentValues values) {//插入城市
		return mContext.getContentResolver().insert(Cities.Cities_Column.CONTENT_URI, values);	
	}
	/**
	 * 进行数据库操作回调的接口
	 */
	public interface DataGetterCallback {
	    public void onQueryComplete(ContentValues values, Cursor cursor);
	}

}
