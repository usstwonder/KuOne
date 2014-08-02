package com.kukool.iosapp.weather.service;

import java.util.Timer;
import java.util.TimerTask;

import com.gui.engine.BytesBitmap;
import com.kukool.iosapp.weather.R;
import com.kukool.iosapp.weather.activity.LaunchActivity;
import com.kukool.iosapp.weather.model.ContentValuesOperation;
import com.kukool.iosapp.weather.model.DatabaseAction;
import com.kukool.iosapp.weather.model.Utils;
import com.kukool.iosapp.weather.model.WebAction;
import com.kukool.iosapp.weather.model.WebAction.WebDownLoadListener;
import com.kukool.iosapp.weather.provider.Weather;
import com.kukool.iosapp.weather.provider.Weather.Weather_Column;
import com.kukool.iosapp.weather.ui.MetricCheckButton;
import com.kukool.iosapp.weather.ui.WeatherView;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.RemoteViews;

public class WeatherService extends Service implements WebDownLoadListener {

	public static final int CUSTOM_NOTIFY_ID = 1;

	// DisplayWeatherAndSettings mSettings;
	// DisplayWeatherView mWeatherView;

	WeatherView mWeatherView;

	private DatabaseAction mDatabaseAction;

	ContentValues mContentValues;
	int curWeathericon;
    String curWeathertext;
    String nightWeathertext;
	String city;
	int curTemp;
	int highTemp;
	int lowTemp;

	protected static Bitmap[] mBitmapArray1_8 = new Bitmap[8];
	protected static Bitmap[] mBitmapArray11_27 = new Bitmap[16];
	protected static Bitmap[] mBitmapArray29_44 = new Bitmap[16];

	int updateRateSeconds = 60 * 30;

	private IWeather.Stub binder = new IWeather.Stub() {

		@Override
		public Bundle getWeekWeatherInfo() throws RemoteException {
			// TODO Auto-generated method stub
			getLocalCurrentWeather();
			if (mContentValues != null) {
				Bundle mBundle = new Bundle();
				mBundle.putString("city", "");
			}
			return null;
		}

		@Override
		public Bundle getCurrentWeatherInfo() throws RemoteException {
			Bundle mBundle = new Bundle();
			if(!isNetworkConnected(getApplicationContext())){
				return null;
			}
			getLocalCurrentWeather();
			if (mContentValues != null) {
				
				curWeathericon = (Integer) mContentValues
						.get(Weather_Column.CURRENT_WEATHERICON);
                curWeathertext = (String) mContentValues.get(Weather_Column.DAY1_WEATHERTEXT);
                nightWeathertext = (String) mContentValues.get(Weather_Column.CURRENT_NIGHT_WEATHERTEXT);
//				int[] tempArray = WeatherView.getTempArray(contentValues);
				mBundle.putString("city", city);
				mBundle.putInt("curTemp", curTemp);
				mBundle.putInt("highTemp", highTemp);
				mBundle.putInt("lowTemp", lowTemp);
                mBundle.putString("curWeather", curWeathertext);
                mBundle.putString("nightWeather", nightWeathertext);
				
				//序列化图片
				mBundle.putByteArray("img", (toMakeSerializable(getDrawBitmap(curWeathericon))));
				
				for(int i = 0 ; i < iconArray.length ; i++){
					Bitmap mBitmap = getDrawBitmap(iconArray[i]);
					byte[] mBitmapBytes = toMakeSerializable(mBitmap);
					mBundle.putByteArray("weekIcon"+i, mBitmapBytes);
				}
				
				mBundle.putIntArray("tempweek", tempArray1);
				
				
				
			}
			return mBundle;
		}

		
	};
	
	private byte[] toMakeSerializable(Bitmap bitmap) {
		
		return BytesBitmap.getBytes(bitmap);
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return binder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mDatabaseAction = new DatabaseAction(this);
		// mWeatherView = new WeatherView(activity);

		if (mBitmapArray1_8[0] == null) {
			for (int i = 0; i < 8; i++) {
				mBitmapArray1_8[i] = BitmapFactory.decodeResource(
						getResources(), (R.drawable.weather01 + i));
			}
			for (int i = 0; i < 16; i++) {
				mBitmapArray11_27[i] = BitmapFactory.decodeResource(
						getResources(), (R.drawable.weather11 + i));
			}
			for (int i = 0; i < 16; i++) {
				mBitmapArray29_44[i] = BitmapFactory.decodeResource(
						getResources(), (R.drawable.weather29 + i));
			}
		}

		Log.e("``", "weather service oncreate");
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				doSomething();
			}
		}
	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(1);
			}
		}, 1000, updateRateSeconds * 1000);

		return super.onStartCommand(intent, flags, startId);
	}

	protected void doSomething() {

		getLocalCurrentWeather();

		if (mContentValues != null) {
			//makeNotification(curWeathericon, city, curTemp, highTemp, lowTemp);
		}
	}

	private void getLocalCurrentWeather() {

		int curPosition = 0;
		loadViewData(curPosition);
		makeData();

	}

	private void makeData() {
		ContentValues contentValues = mContentValues;
		if (contentValues == null) {

			return;
		}

		String cityCn = (String) contentValues.get(Weather_Column.CITY_CN);
		if (cityCn.equals(Utils.NO_CHINISE_CITY)) {
			city = (String) contentValues.get(Weather_Column.CITY);
		} else {
			city = cityCn;
		}

		Log.e("``", "City ==" + city);

		curWeathericon = (Integer) contentValues
				.get(Weather_Column.CURRENT_WEATHERICON);
		int[] tempArray = WeatherView.getTempArray(contentValues);

		curTemp = tempArray[0];
		highTemp = tempArray[1];
		lowTemp = tempArray[2];
		
		tempArray1 = WeatherView.getTempArray(contentValues);
		iconArray = WeatherView.getWeatherIconArray(contentValues);
		

	}
	int[] tempArray1 ;
	
	int[] iconArray ; 
	
	private void makeNotification(int weatherDrawable, String city,
			int curTemp, int highTemp, int lowTemp) {
		Context context = this;

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		int icon = android.R.drawable.stat_notify_voicemail;
		CharSequence tickerText = "";
		long when = 0;
		Notification notification = new Notification(icon, tickerText, when);

		notification.flags |= Notification.FLAG_ONGOING_EVENT;
//		notification.titleFlags = Notification.FLAG_HIDDEN_TITLE;

		RemoteViews contentView = new RemoteViews(context.getPackageName(),
				R.layout.weather_notify);

		// contentView.setImageViewResource(R.id.img,
		// getDrawBitmap(weatherDrawable));
		contentView
				.setImageViewBitmap(R.id.img, getDrawBitmap(weatherDrawable));

		contentView.setTextViewText(R.id.local_, city);
		contentView.setTextViewText(R.id.highest_temp, highTemp
				+ getString(LaunchActivity.RSV(R.string.du)));
		contentView.setTextViewText(R.id.lowest_temp, lowTemp
				+ getString(LaunchActivity.RSV(R.string.du)));
		contentView.setTextViewText(R.id.currentTemp, curTemp
				+ getString(LaunchActivity.RSV(R.string.du)));
//		contentView.setImageViewBitmap(R.id.currentTemp, buildUpdate(curTemp
//				+ getString(LaunchActivity.RSV(R.string.du))));

		// 指定个性化视图
		notification.contentView = contentView;

		Intent intent = new Intent(context, LaunchActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		// 指定内容意图
		notification.contentIntent = contentIntent;

		mNotificationManager.notify(CUSTOM_NOTIFY_ID, notification);
	}

	public void loadViewData(int pos) {

		final int finalPos = pos;
		Cursor cursor = mDatabaseAction.queryAll();
		Log.d("WS", "count ==" + cursor.getCount());
		mContentValues = ContentValuesOperation.getContentValuesByPos(cursor,
				finalPos);

		reloadWebData(mContentValues);
	}

	private Bitmap getDrawBitmap(int weatherIcon) {

		Bitmap bitmap = null;
		if (weatherIcon <= 8) {
			bitmap = mBitmapArray1_8[weatherIcon - 1];
		} else if (weatherIcon >= 29) {
			bitmap = mBitmapArray29_44[weatherIcon - 29];
		} else if (weatherIcon <= 26 && weatherIcon >= 11) {
			bitmap = mBitmapArray11_27[weatherIcon - 11];
		}

		return bitmap;
	}

	@Override
	public void onStartWebDownLoad() {

	}

	@Override
	public void onFinishWebDownLoad() {

	}

	public Bitmap buildUpdate(String temp) {
		Bitmap myBitmap = Bitmap.createBitmap(80, 60, Bitmap.Config.ARGB_4444);
		Canvas myCanvas = new Canvas(myBitmap);
		Paint paint = new Paint();
		// Typeface clock = Typeface.createFromAsset(this.getAssets(),
		// "helvetica-neue-lt.ttf");
		paint.setAntiAlias(true);
		paint.setSubpixelText(true);
		// paint.setTypeface(clock);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.WHITE);
		paint.setTextSize(50);
		paint.setTextAlign(Align.CENTER);
		// myCanvas.drawARGB(255, 0, 0, 255);
		myCanvas.drawText(temp, 42, 45, paint);
		return myBitmap;
	}

	private void reloadWebData(ContentValues contentValues) {
		long currentMillis = System.currentTimeMillis();
		long lastRefreshTime = 0l;
		try {
			lastRefreshTime = Long.parseLong((String) contentValues
					.get(Weather_Column.REFRESH_TIME));
		} catch (Exception e) {
			lastRefreshTime = System.currentTimeMillis();
		}
		long timeDelta = currentMillis - lastRefreshTime;
		// long hourMillis = DateUtils.MINUTE_IN_MILLIS;
		long hourMillis = DateUtils.HOUR_IN_MILLIS;
		// 如果两次刷新时间间隔大于
		// 一个小时则更新数据
		if (timeDelta > hourMillis) {
			int id = (Integer) contentValues.get(Weather_Column._ID);
			Uri uri = ContentUris.withAppendedId(
					Weather.Weather_Column.CONTENT_URI, id);
			String location = (String) contentValues
					.get(Weather_Column.LOCTION);
			// if (DEBUG) {
			// Log.e(TAG,
			// " timeDelta > hourMillis ...you  should reloadWebData  location = "
			// + location);
			// }
			WebAction webAction = new WebAction(getApplicationContext(), uri,
					location, 1);
			webAction.setWebDownLoadListener(this);
			webAction.startLoadData();
		}
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

	protected static int[] getWeatherIconArray(ContentValues contentValues) {
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
	
	public boolean isNetworkConnected(Context context) {  
		     if (context != null) {  
		         ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
		                 .getSystemService(Context.CONNECTIVITY_SERVICE);  
		        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
		         if (mNetworkInfo != null) {  
		            return mNetworkInfo.isAvailable();  
		         }  
		     }  
		     return false;  
		} 
}
