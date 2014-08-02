package com.kukool.iosapp.weather.activity;

import android.widget.*;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import com.gui.engine.Location;
import com.kukool.iosapp.weather.R;
import com.kukool.iosapp.weather.model.ContentValuesOperation;
import com.kukool.iosapp.weather.model.DatabaseAction;

import com.kukool.iosapp.weather.model.Utils;
import com.kukool.iosapp.weather.model.WebAction;
import com.kukool.iosapp.weather.provider.Weather;
import com.kukool.iosapp.weather.provider.Weather.Weather_Column;
import com.kukool.iosapp.weather.ui.IphoneTouchInterceptor;
import com.kukool.iosapp.weather.ui.MetricCheckButton;
import com.kukool.iosapp.weather.ui.MetricCheckButton.MetricCheckButtonStateChangeListener;
import com.kukool.iosapp.weather.ui.WeatherSettingsListAdapter;
import com.kukool.iosapp.weather.ui.WeatherSettingsListView;
import com.kukool.iosapp.weather.ui.WeatherView;
import com.umeng.analytics.MobclickAgent;

public class WeatherSettings extends Rotate3dAnimationActivity
		implements MetricCheckButtonStateChangeListener, OnClickListener, OnTouchListener {
	private DatabaseAction mDatabaseAction;
//	public static IphonePhoneInfo phoneInfo = null;
	public static final int CUSTOM_NOTIFY_ID = 1;
	private static final String TAG = "DisplayWeatherAndSettings";
	private static final boolean DEBUG = true;
	public Bitmap[] mBitmapArray1_8 = new Bitmap[8];
	public Bitmap[] mBitmapArray11_27 = new Bitmap[16];
	public Bitmap[] mBitmapArray29_44 = new Bitmap[16];
	private static final long ANIMATION_DURATION = 250;
	int len = 0;
//	private LocationClient mLocClient;
	public boolean isone;
	// 天气设置
	ContentValues mContentValues;
	private int mWhichOn = MetricCheckButton.F_ON;
	private Button mBtnAddCity;

    private View footerView;
	private WeatherSettingsListView mListView;
	private WeatherSettingsListAdapter mListAdapter;
	private MetricCheckButton mCheckBtn;
	private ImageView mYahooSearchBtn;
	private int mInitialMetric;
	private boolean isExist=false;
	 String city,aaa,location;
	 DatabaseAction databaseAction=null;
	 public final Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == MESSAGE_CHANGE_DELETE_MODE) {
					boolean value = false;
					if (msg.obj instanceof Boolean) {
						value = (Boolean) msg.obj;
					}
					mListView.setDeleteMode(value);
				}
				if(msg.what==666){

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
					}
					else if (city.compareTo("沈阳") == 0) {
						aaa = "shenyang";
					}
					else {
						aaa = AddCity.getPinYin(city);
						// Log.i(TAG,"code: "+aaa);
					}
					

					
					try {
						

						// 先保存城市到数据�?
						/**/			
						final int metric = databaseAction.getCurrentMetric();
						ContentValues values = new ContentValues();
						values.put(Weather.Weather_Column.CITY,aaa);
						values.put(Weather.Weather_Column.STATE,"China("+aaa+")");
						values.put(Weather.Weather_Column.LOCTION,location);
						values.put(Weather.Weather_Column.CITY_CN,
								city);
						values.put(Weather.Weather_Column.METRIC, metric);
						values.put(Weather.Weather_Column.POSITION,
								databaseAction.getMinPosition()-1);
						Uri uri = databaseAction.doInsert(values);
						// 然后读取网络数据
					
						// Log.e(TAG, " handleMessage location = " + location);
						// Log.e(TAG, " handleMessage uri = " + uri);
						new WebAction(WeatherSettings.this, uri,location, 1).startLoadData();
						
						
					} catch (Exception e) {
					}
					
					
					
				}
				if (msg.what == 110) {
					Log.i(TAG, "NOT");
					final int finalPos = 0;
					Cursor cursor = mDatabaseAction.queryAll();
					Log.d("WS", "count ==" + cursor.getCount());
					mContentValues = ContentValuesOperation.getContentValuesByPos(
							cursor, finalPos);
					ContentValues contentValues = mContentValues;
					if (contentValues == null) {

						return;
					}
					String city = null;
					String cityCn = (String) contentValues
							.get(Weather_Column.CITY_CN);
					if (cityCn.equals(Utils.NO_CHINISE_CITY)) {
						city = (String) contentValues.get(Weather_Column.CITY);
					} else {
						city = cityCn;
					}

					Log.e("``", "City ==" + city);

					int curWeathericon = (Integer) contentValues
							.get(Weather_Column.CURRENT_WEATHERICON);
					int[] tempArray = WeatherView.getTempArray(contentValues);

					int curTemp = tempArray[0];
					int highTemp = tempArray[1];
					int lowTemp = tempArray[2];
//					makeNotification(curWeathericon, city, curTemp, highTemp,
//							lowTemp);
				}
			}
		};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_settings);
//		mLocClient = ((Location)getApplication()).mLocationClient;
		Intent i = getIntent();
		isone = i.getBooleanExtra("isone", isone);
//		registerConnectionReceiver();
		mDatabaseAction = new DatabaseAction(this);
		initView();
	
		SharedPreferences sp =getSharedPreferences("locationinfo", MODE_PRIVATE);
		 city=sp.getString("ncity","");
		 location=sp.getString("nlocation","");
		if("".equals(city)||"".equals(location)){
			return;
		}
		
		//
		databaseAction = new DatabaseAction(this);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Cursor cursor=databaseAction.queryAll();
				Log.i(TAG,"KOK "+cursor.getCount());
				if(cursor!=null&&cursor.getCount()>0){
					while(cursor.moveToNext()){
						if(city.equals(cursor.getString(cursor.getColumnIndex("city_cn")))){
							//已经存在
							Log.i(TAG,"KOK "+cursor.getString(3));
							isExist=true;
							
						}
					}
					if(!isExist){
						//send add message
						Message mess=Message.obtain();
						mess.what=666;
						mHandler.sendMessage(mess);
					}
				}
			}
		}).start();
		
	
		
	}

	@Override
	protected void onResume() {
		super.onResume();
        MobclickAgent.onResume(this);
		// 当城市列表为空的时�
		// 是从add city进入该页�
		// 显示设置部分
		Resources res = this.getResources();
		if (mBitmapArray1_8[0] == null) {
			for (int i = 0; i < 8; i++) {
				mBitmapArray1_8[i] = BitmapFactory.decodeResource(res,
						(R.drawable.weather01 + i));
				len += mBitmapArray1_8[i].getByteCount();
			}
			for (int i = 0; i < 16; i++) {
				mBitmapArray11_27[i] = BitmapFactory.decodeResource(res,
						(R.drawable.weather11 + i));
				len += mBitmapArray11_27[i].getByteCount();
			}
			for (int i = 0; i < 16; i++) {
				mBitmapArray29_44[i] = BitmapFactory.decodeResource(res,
						(R.drawable.weather29 + i));
				len += mBitmapArray29_44[i].getByteCount();
			}
		}
		if (getIntent().getAction().equals(
				Utils.ACTION_FIRST_DISPLAY_WEATHER_AND_SETTINGS)) {
			findViewById(R.id.display_weather).setVisibility(View.GONE);
			findViewById(R.id.weather_settings).setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
        MobclickAgent.onPause(this);
		Log.i(TAG,"weather2");
		// if (mBitmapArray1_8[0] != null) {
		for (int i = 0; i < 8; i++) {
			if (mBitmapArray1_8[i] != null && !mBitmapArray1_8[i].isRecycled())
				mBitmapArray1_8[i].recycle();
			mBitmapArray1_8[i] = null;
		}
		for (int i = 0; i < 16; i++) {
			if (mBitmapArray11_27[i] != null
					&& !mBitmapArray11_27[i].isRecycled())
				mBitmapArray11_27[i].recycle();
			mBitmapArray11_27[i] = null;
		}
		for (int i = 0; i < 16; i++) {
			if (mBitmapArray29_44[i] != null
					&& !mBitmapArray29_44[i].isRecycled())
				mBitmapArray29_44[i].recycle();
			mBitmapArray29_44[i] = null;
		}
	}

	private void initView() {
		/**
		 * 显示天气
		 * */
//		phoneInfo = new IphonePhoneInfo(getApplicationContext());

        footerView = getLayoutInflater().inflate(R.layout.weather_settings_list_footer_item, null);

		mBtnAddCity = (Button) footerView.findViewById(R.id.add_city);
		mBtnAddCity.setOnClickListener(this);

		mListView = (WeatherSettingsListView) findViewById(R.id.list_view);

        mListView.addFooterView(footerView);
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setOnTouchListener(this);
        footerView.setOnTouchListener(this);
		mListView.setDropListener(mDropListener);
		mListAdapter = new WeatherSettingsListAdapter(this, new DatabaseAction(
				this).queryAll(), true, mHandler);
		mListView.setAdapter(mListAdapter);

		mCheckBtn = (MetricCheckButton) footerView.findViewById(R.id.metric_check_button);
		int metric = new DatabaseAction(this).getCurrentMetric();
		mInitialMetric = metric;
		mWhichOn = metric;
		mCheckBtn.setOn(metric);
		mCheckBtn.setMetricCheckButtonStateChangeListener(this);

		mYahooSearchBtn = (ImageView) footerView.findViewById(R.id.yahoo_search);
		mYahooSearchBtn.setOnClickListener(this);
	}

	public static final int MESSAGE_CHANGE_DELETE_MODE = 1;

	private IphoneTouchInterceptor.DropListener mDropListener = new IphoneTouchInterceptor.DropListener() {
		public void drop(int f, int t) {
			// Log.e(TAG, "DropListener from = " + f);
			// Log.e(TAG, "DropListener to = " + t);
			final DatabaseAction databaseAction = new DatabaseAction(
					WeatherSettings.this);
			final int from = f;
			final int to = t;
			Cursor cursor = databaseAction.queryAll();
			// mCursor 为所有信息的游标
			int colidx = cursor.getColumnIndex(Weather_Column._ID);
			int colposition = cursor.getColumnIndex(Weather_Column.POSITION);
			if (from < to) { // 向下滑动
				// move the item to somewhere later in the list
				cursor.moveToPosition(to);
				int toidx = cursor.getInt(colidx);
				int toposition = cursor.getInt(colposition);
				cursor.moveToPosition(from);
				int fromidx = cursor.getInt(colidx);
				databaseAction.updatePosWithId(fromidx, toposition);
				for (int i = from + 1; i <= to; i++) {
					cursor.moveToPosition(i);
					toidx = cursor.getInt(colidx);
					toposition = cursor.getInt(colposition);
					databaseAction.updatePosWithId(toidx, toposition - 1);
				}
			} else if (from > to) {// 向上滑动
				// move the item to somewhere earlier in the list
				cursor.moveToPosition(to);
				int toidx = cursor.getInt(colidx);
				int toposition = cursor.getInt(colposition);
				cursor.moveToPosition(from);
				int fromidx = cursor.getInt(colidx);
				databaseAction.updatePosWithId(fromidx, toposition);
				for (int i = from - 1; i >= to; i--) {
					cursor.moveToPosition(i);
					toidx = cursor.getInt(colidx);
					toposition = cursor.getInt(colposition);
					databaseAction.updatePosWithId(toidx, toposition + 1);
				}
			}
		}
	};

	// 天气设置
	public boolean onTouch(View v, MotionEvent event) {
		if (v instanceof WeatherSettingsListView) {
			int action = event.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
                clearSettingListDeletMode();
				return true;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				break;
			}
		} else {
//			if (mGestureDetector.onTouchEvent(event)) {
//				return true;
//			}
		}
		return false;
	}

    private void clearSettingListDeletMode(){
        if (mListView.getMSelectedItem() != null) {
            Log.i("Wonder", "MainView: hideDeleteBtn");
            mListView.getMSelectedItem().hideDeleteBtn();
            //mListView.setMSelectedItem(null);
        }
    }

	// MetricCheckButton 状态的改变
	@Override
	public void onStateChange(View v, int whichOn) {
		mWhichOn = whichOn;
        clearSettingListDeletMode();
        HeaderViewListAdapter adpt = (HeaderViewListAdapter)mListView.getAdapter();
        ((WeatherSettingsListAdapter)adpt.getWrappedAdapter()).notifyDataSetChanged();
        saveMtric();
        setReturnResult(-1);
    }


    private void goYahooSearhWebsit(){
//        Uri uri = Uri.parse(Utils.YAHOO_SEARCH_URI);
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setClassName("com.kukool.Safari",
//                "com.kukool.Safari.SafariActivity");
//
//        intent.setData(uri);
//        startActivity(intent);

        Uri uri = Uri.parse(Utils.YAHOO_SEARCH_URI);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

	@Override
	public void onClick(View v) {
        clearSettingListDeletMode();

        if (v.equals(mBtnAddCity)) {
			addCity();
		} else if (v.equals(mYahooSearchBtn)) {
            goYahooSearhWebsit();
        }
	}

	private static final int FREE_VERSION_MAX_CITIES = 11;
	private static final int FULL_VERSION_MAX_CITIES = 25;

	private void addCity() {
		int count = new DatabaseAction(this).getCursorCount();
		if (count < FREE_VERSION_MAX_CITIES) {
			startActivity(new Intent().setAction(Utils.ACTION_ADD_CITY));
			overridePendingTransition(R.anim.activity_popupwindow_anim_enter,
					R.anim.transition_out);
		}
	}

	/**
	 * 更新天气单位
	 */
	private void saveMtric() {
		if (mWhichOn == mInitialMetric) {
			return;
		} else {
			// 因为是在同一个activity做动画，所以在updateAllMetric�
			// 将mWhichOn作为下一次mInitialMetric�
			// 否则mInitialMetric还是上一次改动之前的�
			mInitialMetric = mWhichOn;
		}
		new DatabaseAction(this).updateAllMetric(mWhichOn);
	}

	// 在动画执行后
	// 不需要做任何动作
	@Override
	public void doAfterAnim() {
	}

	@Override
	public void doBeforeAnim() {
		// TODO Auto-generated method stub
	}

	// @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Location locat=((Location)this.getApplication());
		if(locat!=null&&locat.mLocationClient!=null){
			locat.mLocationClient.stop();
		}
	}

    private void setReturnResult(int idxOfCity) {
        Intent data=new Intent();
        data.putExtra("position", idxOfCity);
        setResult(RESULT_OK, data);
    }

    public void backtoWeatherofCity(int idxOfCity) {
        setReturnResult(idxOfCity);
        finish();
    }

    public int getCurrentMetric(){
        return mWhichOn;
    }
}