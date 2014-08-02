package com.kukool.iosapp.weather.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.kukool.iosapp.weather.R;
import com.kukool.iosapp.weather.activity.AddCity;
import com.kukool.iosapp.weather.provider.Cities.Cities_Column;
import com.kukool.iosapp.weather.provider.Weather.Weather_Column;

public class OneWeatherProvider extends ContentProvider {

	String city = "北京", province, country;
	String aaa = "Beijing";

	class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				province = location.getProvince();
				sb.append("\n省：");
				sb.append(location.getProvince());
				sb.append("\n市：");
				sb.append(location.getCity());
				city = location.getCity();
				// Log.i("CITY","city: "+city);
				sb.append("\n区/县：");
				sb.append(location.getDistrict());

				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				// 加入数据库
				if (city.compareTo("成都") == 0) {
					aaa = "chengdu";
				} else if (city.compareTo("重庆") == 0) {
					aaa = "chongqing";
				} else if (city.compareTo("长沙") == 0) {
					aaa = "changsha";
				} else if (city.compareTo("大连") == 0) {
					aaa = "dalian";
				} else if (city.compareTo("长春") == 0) {
					aaa = "changchun";
				} else if (city.compareTo("沈阳") == 0) {
					aaa = "shenyang";
				}

				else {
					aaa = AddCity.getPinYin(city);
					// Log.i(TAG,"code: "+aaa);
				}

				// try {
				//
				// String location2 =
				// "ASI%7CCN%7CCH024%7CSHANGHAI%7C";//cityList.get(position).getLocation();
				// // 先保存城市到数据�?
				//
				// DatabaseAction databaseAction = new
				// DatabaseAction(LaunchActivity.this);
				// final int metric = databaseAction.getCurrentMetric();
				// ContentValues values = new ContentValues();
				// values.put(Weather.Weather_Column.CITY, aaa);
				// values.put(Weather.Weather_Column.STATE, "China("+aaa+")");
				// values.put(Weather.Weather_Column.LOCTION, location2);
				// values.put(Weather.Weather_Column.CITY_CN,city);
				// values.put(Weather.Weather_Column.METRIC, 1);
				// values.put(Weather.Weather_Column.POSITION,
				// databaseAction.getMinPosition() - 1);
				//
				// databaseAction.doInsert(null);
				// Uri uri = databaseAction.doInsert(values);
				//
				// Message msg = mHandler.obtainMessage(DOWNLOAD_CITYLIST);
				//
				// Bundle bundle = new Bundle();
				// bundle.putString("uri", uri.toString());
				// bundle.putString("location", location2);
				// msg.setData(bundle);
				// mHandler.sendMessage(msg);

				// } catch (Exception e) {
				// }

			}
			sb.append("\nsdk version : ");
			// sb.append(mLocationClient.getVersion());
			// logMsg(sb.toString());
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append("Poi time : ");
			sb.append(poiLocation.getTime());
			sb.append("\nerror code : ");
			sb.append(poiLocation.getLocType());
			sb.append("\nlatitude : ");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(poiLocation.getLongitude());
			sb.append("\nradius : ");
			sb.append(poiLocation.getRadius());
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			}
			if (poiLocation.hasPoi()) {
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
			} else {
				sb.append("noPoi information");
			}
			// logMsg(sb.toString());
		}
	}

	public LocationClient mLocationClient = null;
	private static final boolean DEBUG = true;
	private static final String TAG = "OneWeatherProvider";

	public static final String AUTHORITY = "com.kukool.provider.oneweather";

	private static final String DATABASE_NAME = "weather.db";
	private static final int DATABASE_VERSION = 2;
	// 表名字
	private static final String WEATHER_TABLE_NAME = "weather";
	private static final String CITIES_TABLE_NAME = "cities";

	private static final UriMatcher sUriMatcher;
	private static final int WEATHERS = 1;
	private static final int WEATHERS_ID = 2;
	private static final int CITIES = 3;
	private static final int CITIES_ID = 4;

	private static HashMap<String, String> sWeathersProjectionMap;
	private static HashMap<String, String> sCitiesProjectionMap;
	static {

		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AUTHORITY, "weather", WEATHERS);
		sUriMatcher.addURI(AUTHORITY, "weather/#", WEATHERS_ID);
		sUriMatcher.addURI(AUTHORITY, "cities", CITIES);
		sUriMatcher.addURI(AUTHORITY, "cities/#", CITIES_ID);

		sWeathersProjectionMap = new HashMap<String, String>();
		sWeathersProjectionMap.put(Weather_Column._ID, Weather_Column._ID);
		// 城市
		sWeathersProjectionMap.put(Weather_Column.CITY, Weather_Column.CITY);
		sWeathersProjectionMap.put(Weather_Column.STATE, Weather_Column.STATE);
		sWeathersProjectionMap.put(Weather_Column.LOCTION,
				Weather_Column.LOCTION);
		sWeathersProjectionMap.put(Weather_Column.CITY_CN,
				Weather_Column.CITY_CN);
		// 单位 位置 更新时间 当地时间
		sWeathersProjectionMap
				.put(Weather_Column.METRIC, Weather_Column.METRIC);
		sWeathersProjectionMap.put(Weather_Column.POSITION,
				Weather_Column.POSITION);
		sWeathersProjectionMap.put(Weather_Column.REFRESH_TIME,
				Weather_Column.REFRESH_TIME);
		sWeathersProjectionMap.put(Weather_Column.LOCAL_TIME,
				Weather_Column.LOCAL_TIME);
		// 当前时间
		sWeathersProjectionMap.put(Weather_Column.CURRENT_WEATHERICON,
				Weather_Column.CURRENT_WEATHERICON);
        sWeathersProjectionMap.put(Weather_Column.CURRENT_WEATHERTEXT,
                Weather_Column.CURRENT_WEATHERTEXT);
		sWeathersProjectionMap.put(Weather_Column.CURRENT_TEMPERATURE_F,
				Weather_Column.CURRENT_TEMPERATURE_F);
		sWeathersProjectionMap.put(Weather_Column.CURRENT_TEMPERATURE_C,
				Weather_Column.CURRENT_TEMPERATURE_C);

        sWeathersProjectionMap.put(Weather_Column.CURRENT_HUMIDITY,
                Weather_Column.CURRENT_HUMIDITY);
        sWeathersProjectionMap.put(Weather_Column.CURRENT_REALFEEL,
                Weather_Column.CURRENT_REALFEEL);
        sWeathersProjectionMap.put(Weather_Column.CURRENT_WINDSPEED,
                Weather_Column.CURRENT_WINDSPEED);
        sWeathersProjectionMap.put(Weather_Column.CURRENT_WINDDIRECTION,
                Weather_Column.CURRENT_WINDDIRECTION);
        sWeathersProjectionMap.put(Weather_Column.CURRENT_VISIBILITY,
                Weather_Column.CURRENT_VISIBILITY);

        //night
        sWeathersProjectionMap.put(Weather_Column.CURRENT_NIGHT_WEATHERTEXT,
                Weather_Column.CURRENT_NIGHT_WEATHERTEXT);
		// 第一天
		sWeathersProjectionMap.put(Weather_Column.WEATHER_ICON_DAY1,
				Weather_Column.WEATHER_ICON_DAY1);
        sWeathersProjectionMap.put(Weather_Column.DAY1_WEATHERTEXT,
                Weather_Column.DAY1_WEATHERTEXT);
		sWeathersProjectionMap.put(Weather_Column.HIGH_TEMPERATURE_DAY1_F,
				Weather_Column.HIGH_TEMPERATURE_DAY1_F);
		sWeathersProjectionMap.put(Weather_Column.HIGH_TEMPERATURE_DAY1_C,
				Weather_Column.HIGH_TEMPERATURE_DAY1_C);
		sWeathersProjectionMap.put(Weather_Column.LOW_TEMPERATURE_DAY1_F,
				Weather_Column.LOW_TEMPERATURE_DAY1_F);
		sWeathersProjectionMap.put(Weather_Column.LOW_TEMPERATURE_DAY1_C,
				Weather_Column.LOW_TEMPERATURE_DAY1_C);
		// 第二天
		sWeathersProjectionMap.put(Weather_Column.WEATHER_ICON_DAY2,
				Weather_Column.WEATHER_ICON_DAY2);
        sWeathersProjectionMap.put(Weather_Column.DAY2_WEATHERTEXT,
                Weather_Column.DAY2_WEATHERTEXT);
		sWeathersProjectionMap.put(Weather_Column.HIGH_TEMPERATURE_DAY2_F,
				Weather_Column.HIGH_TEMPERATURE_DAY2_F);
		sWeathersProjectionMap.put(Weather_Column.HIGH_TEMPERATURE_DAY2_C,
				Weather_Column.HIGH_TEMPERATURE_DAY2_C);
		sWeathersProjectionMap.put(Weather_Column.LOW_TEMPERATURE_DAY2_F,
				Weather_Column.LOW_TEMPERATURE_DAY2_F);
		sWeathersProjectionMap.put(Weather_Column.LOW_TEMPERATURE_DAY2_C,
				Weather_Column.LOW_TEMPERATURE_DAY2_C);
		// 第三天
		sWeathersProjectionMap.put(Weather_Column.WEATHER_ICON_DAY3,
				Weather_Column.WEATHER_ICON_DAY3);
        sWeathersProjectionMap.put(Weather_Column.DAY3_WEATHERTEXT,
                Weather_Column.DAY3_WEATHERTEXT);
		sWeathersProjectionMap.put(Weather_Column.HIGH_TEMPERATURE_DAY3_F,
				Weather_Column.HIGH_TEMPERATURE_DAY3_F);
		sWeathersProjectionMap.put(Weather_Column.HIGH_TEMPERATURE_DAY3_C,
				Weather_Column.HIGH_TEMPERATURE_DAY3_C);
		sWeathersProjectionMap.put(Weather_Column.LOW_TEMPERATURE_DAY3_F,
				Weather_Column.LOW_TEMPERATURE_DAY3_F);
		sWeathersProjectionMap.put(Weather_Column.LOW_TEMPERATURE_DAY3_C,
				Weather_Column.LOW_TEMPERATURE_DAY3_C);
		// 第四天
		sWeathersProjectionMap.put(Weather_Column.WEATHER_ICON_DAY4,
				Weather_Column.WEATHER_ICON_DAY4);
        sWeathersProjectionMap.put(Weather_Column.DAY4_WEATHERTEXT,
                Weather_Column.DAY4_WEATHERTEXT);
		sWeathersProjectionMap.put(Weather_Column.HIGH_TEMPERATURE_DAY4_F,
				Weather_Column.HIGH_TEMPERATURE_DAY4_F);
		sWeathersProjectionMap.put(Weather_Column.HIGH_TEMPERATURE_DAY4_C,
				Weather_Column.HIGH_TEMPERATURE_DAY4_C);
		sWeathersProjectionMap.put(Weather_Column.LOW_TEMPERATURE_DAY4_F,
				Weather_Column.LOW_TEMPERATURE_DAY4_F);
		sWeathersProjectionMap.put(Weather_Column.LOW_TEMPERATURE_DAY4_C,
				Weather_Column.LOW_TEMPERATURE_DAY4_C);
		// 第五天
		sWeathersProjectionMap.put(Weather_Column.WEATHER_ICON_DAY5,
				Weather_Column.WEATHER_ICON_DAY5);
        sWeathersProjectionMap.put(Weather_Column.DAY5_WEATHERTEXT,
                Weather_Column.DAY5_WEATHERTEXT);
		sWeathersProjectionMap.put(Weather_Column.HIGH_TEMPERATURE_DAY5_F,
				Weather_Column.HIGH_TEMPERATURE_DAY5_F);
		sWeathersProjectionMap.put(Weather_Column.HIGH_TEMPERATURE_DAY5_C,
				Weather_Column.HIGH_TEMPERATURE_DAY5_C);
		sWeathersProjectionMap.put(Weather_Column.LOW_TEMPERATURE_DAY5_F,
				Weather_Column.LOW_TEMPERATURE_DAY5_F);
		sWeathersProjectionMap.put(Weather_Column.LOW_TEMPERATURE_DAY5_C,
				Weather_Column.LOW_TEMPERATURE_DAY5_C);
		// 第六天
		sWeathersProjectionMap.put(Weather_Column.WEATHER_ICON_DAY6,
				Weather_Column.WEATHER_ICON_DAY6);
        sWeathersProjectionMap.put(Weather_Column.DAY6_WEATHERTEXT,
                Weather_Column.DAY6_WEATHERTEXT);
		sWeathersProjectionMap.put(Weather_Column.HIGH_TEMPERATURE_DAY6_F,
				Weather_Column.HIGH_TEMPERATURE_DAY6_F);
		sWeathersProjectionMap.put(Weather_Column.HIGH_TEMPERATURE_DAY6_C,
				Weather_Column.HIGH_TEMPERATURE_DAY6_C);
		sWeathersProjectionMap.put(Weather_Column.LOW_TEMPERATURE_DAY6_F,
				Weather_Column.LOW_TEMPERATURE_DAY6_F);
		sWeathersProjectionMap.put(Weather_Column.LOW_TEMPERATURE_DAY6_C,
				Weather_Column.LOW_TEMPERATURE_DAY6_C);

		sCitiesProjectionMap = new HashMap<String, String>();
		sCitiesProjectionMap.put(Cities_Column._ID, Cities_Column._ID);
		sCitiesProjectionMap.put(Cities_Column.CITY_STATE_EN,
				Cities_Column.CITY_STATE_EN);
		sCitiesProjectionMap.put(Cities_Column.CITY_CN, Cities_Column.CITY_CN);
	}

	public static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL("CREATE TABLE " + WEATHER_TABLE_NAME + " ("
					+ Weather_Column._ID + " INTEGER PRIMARY KEY,"

					+ Weather_Column.CITY + " TEXT," + Weather_Column.STATE
					+ " TEXT," + Weather_Column.LOCTION + " TEXT,"
					+ Weather_Column.CITY_CN + " TEXT,"

					+ Weather_Column.METRIC + " INTEGER,"
					+ Weather_Column.POSITION + " INTEGER,"
					+ Weather_Column.REFRESH_TIME + " TEXT,"
					+ Weather_Column.LOCAL_TIME + " TEXT,"

					+ Weather_Column.CURRENT_WEATHERICON + " INTEGER,"
                    + Weather_Column.CURRENT_WEATHERTEXT + " TEXT,"
					+ Weather_Column.CURRENT_TEMPERATURE_F + " INTEGER,"
					+ Weather_Column.CURRENT_TEMPERATURE_C + " INTEGER,"

                    + Weather_Column.CURRENT_HUMIDITY + " TEXT,"
                    + Weather_Column.CURRENT_REALFEEL + " INTEGER,"
                    + Weather_Column.CURRENT_WINDSPEED + " INTEGER,"
                    + Weather_Column.CURRENT_WINDDIRECTION + " TEXT,"
                    + Weather_Column.CURRENT_VISIBILITY + " INTEGER,"

                    + Weather_Column.CURRENT_NIGHT_WEATHERTEXT + " TEXT,"

					+ Weather_Column.WEATHER_ICON_DAY1 + " INTEGER,"
                    + Weather_Column.DAY1_WEATHERTEXT + " TEXT,"
					+ Weather_Column.HIGH_TEMPERATURE_DAY1_F + " INTEGER,"
					+ Weather_Column.HIGH_TEMPERATURE_DAY1_C + " INTEGER,"
					+ Weather_Column.LOW_TEMPERATURE_DAY1_F + " INTEGER,"
					+ Weather_Column.LOW_TEMPERATURE_DAY1_C + " INTEGER,"

					+ Weather_Column.WEATHER_ICON_DAY2 + " INTEGER,"
                    + Weather_Column.DAY2_WEATHERTEXT + " TEXT,"
					+ Weather_Column.HIGH_TEMPERATURE_DAY2_F + " INTEGER,"
					+ Weather_Column.HIGH_TEMPERATURE_DAY2_C + " INTEGER,"
					+ Weather_Column.LOW_TEMPERATURE_DAY2_F + " INTEGER,"
					+ Weather_Column.LOW_TEMPERATURE_DAY2_C + " INTEGER,"

					+ Weather_Column.WEATHER_ICON_DAY3 + " INTEGER,"
                    + Weather_Column.DAY3_WEATHERTEXT + " TEXT,"
					+ Weather_Column.HIGH_TEMPERATURE_DAY3_F + " INTEGER,"
					+ Weather_Column.HIGH_TEMPERATURE_DAY3_C + " INTEGER,"
					+ Weather_Column.LOW_TEMPERATURE_DAY3_F + " INTEGER,"
					+ Weather_Column.LOW_TEMPERATURE_DAY3_C + " INTEGER,"

					+ Weather_Column.WEATHER_ICON_DAY4 + " INTEGER,"
                    + Weather_Column.DAY4_WEATHERTEXT + " TEXT,"
					+ Weather_Column.HIGH_TEMPERATURE_DAY4_F + " INTEGER,"
					+ Weather_Column.HIGH_TEMPERATURE_DAY4_C + " INTEGER,"
					+ Weather_Column.LOW_TEMPERATURE_DAY4_F + " INTEGER,"
					+ Weather_Column.LOW_TEMPERATURE_DAY4_C + " INTEGER,"

					+ Weather_Column.WEATHER_ICON_DAY5 + " INTEGER,"
                    + Weather_Column.DAY5_WEATHERTEXT + " TEXT,"
					+ Weather_Column.HIGH_TEMPERATURE_DAY5_F + " INTEGER,"
					+ Weather_Column.HIGH_TEMPERATURE_DAY5_C + " INTEGER,"
					+ Weather_Column.LOW_TEMPERATURE_DAY5_F + " INTEGER,"
					+ Weather_Column.LOW_TEMPERATURE_DAY5_C + " INTEGER,"

					+ Weather_Column.WEATHER_ICON_DAY6 + " INTEGER,"
                    + Weather_Column.DAY6_WEATHERTEXT + " TEXT,"
					+ Weather_Column.HIGH_TEMPERATURE_DAY6_F + " INTEGER,"
					+ Weather_Column.HIGH_TEMPERATURE_DAY6_C + " INTEGER,"
					+ Weather_Column.LOW_TEMPERATURE_DAY6_F + " INTEGER,"
					+ Weather_Column.LOW_TEMPERATURE_DAY6_C + " INTEGER" + ");");

			db.execSQL("CREATE TABLE " + CITIES_TABLE_NAME + " ("
					+ Cities_Column._ID + " INTEGER PRIMARY KEY,"
					+ Cities_Column.CITY_STATE_EN + " TEXT,"
					+ Cities_Column.CITY_CN + " TEXT" + ");");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS weather");
			db.execSQL("DROP TABLE IF EXISTS cities");
			onCreate(db);
		}

	}

	private DatabaseHelper mOpenHelper;

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub

		mOpenHelper = new DatabaseHelper(getContext());
		mLocationClient = new LocationClient(this.getContext());
		mLocationClient.registerLocationListener(new MyLocationListenner());

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(false); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setAddrType("all"); // 设置地址信息，仅设置为“all”时有地址信息，默认无地址信息
		option.setScanSpan(Integer.parseInt("900")); // 设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
		mLocationClient.setLocOption(option);
		// mLocationClient.start();

		return true;
	}

	/***
	 * 这个函数会在系统进行URI的MIME过滤时被调用
	 */
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		int match = sUriMatcher.match(uri);
		// if (DEBUG) {
		// Log.d(TAG, "getType uri = " + uri + ", match =" + match);
		// }
		switch (match) {
		case WEATHERS:
			return Weather_Column.CONTENT_TYPE;
		case WEATHERS_ID:
			return Weather_Column.CONTENT_ITEM_TYPE;
		case CITIES:
			return Cities_Column.CONTENT_TYPE;
		case CITIES_ID:
			return Cities_Column.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);

		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch (sUriMatcher.match(uri)) {
		case WEATHERS:
			qb.setTables(WEATHER_TABLE_NAME);
			qb.setProjectionMap(sWeathersProjectionMap);
			break;
		case WEATHERS_ID:
			qb.setTables(WEATHER_TABLE_NAME);
			qb.setProjectionMap(sWeathersProjectionMap);
			qb.appendWhere(Weather_Column._ID + " = "
					+ uri.getPathSegments().get(1));
			break;
		case CITIES:
			qb.setTables(CITIES_TABLE_NAME);
			qb.setProjectionMap(sCitiesProjectionMap);
			break;
		case CITIES_ID:
			qb.setTables(CITIES_TABLE_NAME);
			qb.setProjectionMap(sCitiesProjectionMap);
			qb.appendWhere(Cities_Column._ID + " = "
					+ uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);

		}
		// 设置默认排序
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = Weather_Column.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}
		// 执行查询
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, orderBy);

		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		// Log.e(TAG, "insert sUriMatcher------->" );
		// Log.e(TAG, "insert sUriMatcher.match(uri) = " + uri);
		// Log.e(TAG, "insert sUriMatcher.match(uri) = " +
		// sUriMatcher.match(uri));
		if (sUriMatcher.match(uri) != WEATHERS
				&& sUriMatcher.match(uri) != CITIES) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		// 设置默认值
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		if (sUriMatcher.match(uri) == WEATHERS) {
			// 2011-5-12
			// 在更新单个字段的时候
			// 如果其他的字段没有设置值
			// 则会用默认值代替 ， 实际情况不允许这样
			setWeatherDefaultValut(values);

		} else if (sUriMatcher.match(uri) == CITIES) {
			setCitiesDefaultValut(values);
		}

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = 0l;
		if (sUriMatcher.match(uri) == WEATHERS) {
			rowId = db.insert(WEATHER_TABLE_NAME, null, values);
		} else if (sUriMatcher.match(uri) == CITIES) {
			rowId = db.insert(CITIES_TABLE_NAME, null, values);
		}
		if (rowId > 0) {
			Uri weatherUri = null;
			if (sUriMatcher.match(uri) == WEATHERS) {
				weatherUri = ContentUris.withAppendedId(
						Weather.Weather_Column.CONTENT_URI, rowId);
			} else if (sUriMatcher.match(uri) == CITIES) {
				weatherUri = ContentUris.withAppendedId(
						Cities.Cities_Column.CONTENT_URI, rowId);
			}
			this.getContext().getContentResolver()
					.notifyChange(weatherUri, null);
			return weatherUri;
		}
		throw new SQLException("Failed to insert row into " + uri);

	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case WEATHERS:
			count = db.delete(WEATHER_TABLE_NAME, selection, selectionArgs);
			break;
		case WEATHERS_ID:
			String weatherId = uri.getPathSegments().get(1);
			count = db.delete(WEATHER_TABLE_NAME, Weather.Weather_Column._ID
					+ "="
					+ weatherId
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);

		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;

	}

	@Override
	public int update(Uri uri, ContentValues initialValues, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		// 设置默认值
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}
		// 2011-5-12
		// 在更新单个字段的时候
		// 如果其他的字段没有设置值
		// 则会用默认值代替 ， 实际情况不允许这样
		// setDefaultValut(values);

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case WEATHERS:
			count = db.update(WEATHER_TABLE_NAME, values, selection,
					selectionArgs);
			break;
		case WEATHERS_ID:
			String weatherId = uri.getPathSegments().get(1);
			count = db.update(
					WEATHER_TABLE_NAME,
					values,
					Weather.Weather_Column._ID
							+ "="
							+ weatherId
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	private void setWeatherDefaultValut(ContentValues values) {
		Log.i(TAG, "添加bj");
		Long now = Long.valueOf(System.currentTimeMillis());
		// 城市
		// SharedPreferences sp
		// =this.getContext().getSharedPreferences("locationinfo",
		// this.getContext().MODE_PRIVATE);
		// city=sp.getString("city","北京");
		// if (city.compareTo("成都") == 0) {
		// aaa = "chengdu";
		// } else if (city.compareTo("重庆") == 0) {
		// aaa = "chongqing";
		// } else if (city.compareTo("长沙") == 0) {
		// aaa = "changsha";
		// } else if (city.compareTo("大连") == 0) {
		// aaa = "dalian";
		// } else if (city.compareTo("长春") == 0) {
		// aaa = "changchun";
		// }
		//
		// else {
		// aaa = AddCity.getPinYin(city);
		// // Log.i(TAG,"code: "+aaa);
		// }
		if (values.containsKey(Weather.Weather_Column.CITY) == false) {
			values.put(Weather.Weather_Column.CITY, "Beijing");

			// Log.i(TAG,"city: "+city);
		}
		if (values.containsKey(Weather.Weather_Column.STATE) == false) {
			values.put(Weather.Weather_Column.STATE, "China(" + "Beijing" + ")");
		}
		if (values.containsKey(Weather.Weather_Column.LOCTION) == false) {
			values.put(Weather.Weather_Column.LOCTION, "ASI|CN|CH002|BEIJING|");
		}
		if (values.containsKey(Weather.Weather_Column.CITY_CN) == false) {
			values.put(Weather.Weather_Column.CITY_CN, this.getContext()
					.getResources().getString(R.string.local_));
		}
		// 单位 位置 更新时间 当地时间
		if (values.containsKey(Weather.Weather_Column.METRIC) == false) {
			values.put(Weather.Weather_Column.METRIC, "1");
		}
		if (values.containsKey(Weather.Weather_Column.POSITION) == false) {
			values.put(Weather.Weather_Column.POSITION, "-1");
		}
		if (values.containsKey(Weather.Weather_Column.REFRESH_TIME) == false) {
			values.put(Weather.Weather_Column.REFRESH_TIME, now);
		}
		if (values.containsKey(Weather.Weather_Column.LOCAL_TIME) == false) {
			values.put(Weather.Weather_Column.LOCAL_TIME, "12:00");
		}
		// 当前时间
		if (values.containsKey(Weather.Weather_Column.CURRENT_WEATHERICON) == false) {
			values.put(Weather.Weather_Column.CURRENT_WEATHERICON, "1");
		}
        if (values.containsKey(Weather.Weather_Column.CURRENT_WEATHERTEXT) == false) {
            values.put(Weather.Weather_Column.CURRENT_WEATHERTEXT, "Unknown");
        }
		if (values.containsKey(Weather.Weather_Column.CURRENT_TEMPERATURE_F) == false) {
			values.put(Weather.Weather_Column.CURRENT_TEMPERATURE_F, "0");
		}
		if (values.containsKey(Weather.Weather_Column.CURRENT_TEMPERATURE_C) == false) {
			values.put(Weather.Weather_Column.CURRENT_TEMPERATURE_C, "0");
		}

        if (values.containsKey(Weather.Weather_Column.CURRENT_HUMIDITY) == false) {
            values.put(Weather.Weather_Column.CURRENT_HUMIDITY, "Unknown");
        }
        if (values.containsKey(Weather.Weather_Column.CURRENT_REALFEEL) == false) {
            values.put(Weather.Weather_Column.CURRENT_REALFEEL, "0");
        }
        if (values.containsKey(Weather.Weather_Column.CURRENT_WINDSPEED) == false) {
            values.put(Weather.Weather_Column.CURRENT_WINDSPEED, "0");
        }
        if (values.containsKey(Weather.Weather_Column.CURRENT_WINDDIRECTION) == false) {
            values.put(Weather.Weather_Column.CURRENT_WINDDIRECTION, "Unknown");
        }
        if (values.containsKey(Weather.Weather_Column.CURRENT_VISIBILITY) == false) {
            values.put(Weather.Weather_Column.CURRENT_VISIBILITY, "0");
        }

        //night
        if (values.containsKey(Weather.Weather_Column.CURRENT_NIGHT_WEATHERTEXT) == false) {
            values.put(Weather.Weather_Column.CURRENT_NIGHT_WEATHERTEXT, "Unknown");
        }
		// 第一天
		if (values.containsKey(Weather.Weather_Column.WEATHER_ICON_DAY1) == false) {
			values.put(Weather.Weather_Column.WEATHER_ICON_DAY1, "1");
		}
        if (values.containsKey(Weather.Weather_Column.DAY1_WEATHERTEXT) == false) {
            values.put(Weather.Weather_Column.DAY1_WEATHERTEXT, "Unknown");
        }
		if (values.containsKey(Weather.Weather_Column.HIGH_TEMPERATURE_DAY1_F) == false) {
			values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY1_F, "0");
		}
		if (values.containsKey(Weather.Weather_Column.HIGH_TEMPERATURE_DAY1_C) == false) {
			values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY1_C, "0");
		}
		if (values.containsKey(Weather.Weather_Column.LOW_TEMPERATURE_DAY1_F) == false) {
			values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY1_F, "0");
		}
		if (values.containsKey(Weather.Weather_Column.LOW_TEMPERATURE_DAY1_C) == false) {
			values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY1_C, "0");
		}
		// 第二天
		if (values.containsKey(Weather.Weather_Column.WEATHER_ICON_DAY2) == false) {
			values.put(Weather.Weather_Column.WEATHER_ICON_DAY2, "1");
		}
        if (values.containsKey(Weather.Weather_Column.DAY2_WEATHERTEXT) == false) {
            values.put(Weather.Weather_Column.DAY2_WEATHERTEXT, "Unknown");
        }
		if (values.containsKey(Weather.Weather_Column.HIGH_TEMPERATURE_DAY2_F) == false) {
			values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY2_F, "0");
		}
		if (values.containsKey(Weather.Weather_Column.HIGH_TEMPERATURE_DAY2_C) == false) {
			values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY2_C, "0");
		}
		if (values.containsKey(Weather.Weather_Column.LOW_TEMPERATURE_DAY2_F) == false) {
			values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY2_F, "0");
		}
		if (values.containsKey(Weather.Weather_Column.LOW_TEMPERATURE_DAY2_C) == false) {
			values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY2_C, "0");
		}
		// 第三天
		if (values.containsKey(Weather.Weather_Column.WEATHER_ICON_DAY3) == false) {
			values.put(Weather.Weather_Column.WEATHER_ICON_DAY3, "1");
		}
        if (values.containsKey(Weather.Weather_Column.DAY3_WEATHERTEXT) == false) {
            values.put(Weather.Weather_Column.DAY3_WEATHERTEXT, "Unknown");
        }
		if (values.containsKey(Weather.Weather_Column.HIGH_TEMPERATURE_DAY3_F) == false) {
			values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY3_F, "0");
		}
		if (values.containsKey(Weather.Weather_Column.HIGH_TEMPERATURE_DAY3_C) == false) {
			values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY3_C, "0");
		}
		if (values.containsKey(Weather.Weather_Column.LOW_TEMPERATURE_DAY3_F) == false) {
			values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY3_F, "0");
		}
		if (values.containsKey(Weather.Weather_Column.LOW_TEMPERATURE_DAY3_C) == false) {
			values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY3_C, "0");
		}
		// 第四天
		if (values.containsKey(Weather.Weather_Column.WEATHER_ICON_DAY4) == false) {
			values.put(Weather.Weather_Column.WEATHER_ICON_DAY4, "1");
		}
        if (values.containsKey(Weather.Weather_Column.DAY4_WEATHERTEXT) == false) {
            values.put(Weather.Weather_Column.DAY4_WEATHERTEXT, "Unknown");
        }
		if (values.containsKey(Weather.Weather_Column.HIGH_TEMPERATURE_DAY4_F) == false) {
			values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY4_F, "0");
		}
		if (values.containsKey(Weather.Weather_Column.HIGH_TEMPERATURE_DAY4_C) == false) {
			values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY4_C, "0");
		}
		if (values.containsKey(Weather.Weather_Column.LOW_TEMPERATURE_DAY4_F) == false) {
			values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY4_F, "0");
		}
		if (values.containsKey(Weather.Weather_Column.LOW_TEMPERATURE_DAY4_C) == false) {
			values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY4_C, "0");
		}
		// 第五天
		if (values.containsKey(Weather.Weather_Column.WEATHER_ICON_DAY5) == false) {
			values.put(Weather.Weather_Column.WEATHER_ICON_DAY5, "1");
		}
        if (values.containsKey(Weather.Weather_Column.DAY5_WEATHERTEXT) == false) {
            values.put(Weather.Weather_Column.DAY5_WEATHERTEXT, "Unknown");
        }
		if (values.containsKey(Weather.Weather_Column.HIGH_TEMPERATURE_DAY5_F) == false) {
			values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY5_F, "0");
		}
		if (values.containsKey(Weather.Weather_Column.HIGH_TEMPERATURE_DAY5_C) == false) {
			values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY5_C, "0");
		}
		if (values.containsKey(Weather.Weather_Column.LOW_TEMPERATURE_DAY5_F) == false) {
			values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY5_F, "0");
		}
		if (values.containsKey(Weather.Weather_Column.LOW_TEMPERATURE_DAY5_C) == false) {
			values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY5_C, "0");
		}
		// 第六天
		if (values.containsKey(Weather.Weather_Column.WEATHER_ICON_DAY6) == false) {
			values.put(Weather.Weather_Column.WEATHER_ICON_DAY6, "1");
		}
        if (values.containsKey(Weather.Weather_Column.DAY6_WEATHERTEXT) == false) {
            values.put(Weather.Weather_Column.DAY6_WEATHERTEXT, "Unknown");
        }
		if (values.containsKey(Weather.Weather_Column.HIGH_TEMPERATURE_DAY6_F) == false) {
			values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY6_F, "0");
		}
		if (values.containsKey(Weather.Weather_Column.HIGH_TEMPERATURE_DAY6_C) == false) {
			values.put(Weather.Weather_Column.HIGH_TEMPERATURE_DAY6_C, "0");
		}
		if (values.containsKey(Weather.Weather_Column.LOW_TEMPERATURE_DAY6_F) == false) {
			values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY6_F, "0");
		}
		if (values.containsKey(Weather.Weather_Column.LOW_TEMPERATURE_DAY6_C) == false) {
			values.put(Weather.Weather_Column.LOW_TEMPERATURE_DAY6_C, "0");
		}
	}

	private void setCitiesDefaultValut(ContentValues values) {
		if (values.containsKey(Cities.Cities_Column.CITY_STATE_EN) == false) {
			values.put(Cities.Cities_Column.CITY_STATE_EN,
					"BeijingChina(Beijing)");
		}
		if (values.containsKey(Cities.Cities_Column.CITY_CN) == false) {
			values.put(
					Cities.Cities_Column.CITY_CN,
					"this.getContext().getResources().getString(R.string.local_)(this.getContext().getResources().getString(R.string.local_))");
		}
	}
}
