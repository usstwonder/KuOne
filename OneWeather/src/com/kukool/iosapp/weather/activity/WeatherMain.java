package com.kukool.iosapp.weather.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
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
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.ViewSwitcher;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.location.LocationClient;
import com.gui.engine.Location;
import com.kukool.iosapp.weather.R;
import com.kukool.iosapp.weather.model.ContentValuesOperation;
import com.kukool.iosapp.weather.model.DatabaseAction;

import com.kukool.iosapp.weather.model.Utils;
import com.kukool.iosapp.weather.model.WebAction;
import com.kukool.iosapp.weather.provider.Weather;
import com.kukool.iosapp.weather.provider.Weather.Weather_Column;
import com.kukool.iosapp.weather.ui.DisplayWeatherView;
import com.kukool.iosapp.weather.ui.DotView;
import com.kukool.iosapp.weather.ui.IphoneTouchInterceptor;
import com.kukool.iosapp.weather.ui.WeatherView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;

public class WeatherMain extends Activity implements OnClickListener {
	private DatabaseAction mDatabaseAction;
	public static final int CUSTOM_NOTIFY_ID = 1;
	private static final String TAG = "WeatherMain";
	private static final boolean DEBUG = true;
	public Bitmap[] mBitmapArray1_8 = new Bitmap[8];
	public Bitmap[] mBitmapArray11_27 = new Bitmap[16];
	public Bitmap[] mBitmapArray29_44 = new Bitmap[16];
	private static final long ANIMATION_DURATION = 250;
	int len = 0;
	// 显示天气
	public View mDotView;
	private LocationClient mLocClient;
	public ViewPager mViewSwitcher;
	public GestureDetector mGestureDetector;
	public boolean isone;
	// 天气设置
	ContentValues mContentValues;
    protected Button mBtnYahoo;
    protected Button mBtnSettings;

    private final ArrayList<View> views = new ArrayList<View>();
    private int mPosition;
    private int mCount;
    private boolean needForceRefresh;
    private int setPosition = -1;
    private DisplayWeatherView mCurrentView;

	private int mInitialMetric;
    private boolean isExist=false;
    private String city,aaa,location;

	 public final Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
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
					}

					try {
						// 先保存城市到数据�?
						/**/			
						final int metric = mDatabaseAction.getCurrentMetric();
						ContentValues values = new ContentValues();
						values.put(Weather.Weather_Column.CITY,aaa);
						values.put(Weather.Weather_Column.STATE,"China("+aaa+")");
						values.put(Weather.Weather_Column.LOCTION,location);
						values.put(Weather.Weather_Column.CITY_CN,
								city);
						values.put(Weather.Weather_Column.METRIC, metric);
						values.put(Weather.Weather_Column.POSITION,
                                mDatabaseAction.getMinPosition()-1);
						Uri uri = mDatabaseAction.doInsert(values);
						// 然后读取网络数据

						new WebAction(WeatherMain.this, uri,location, 1).startLoadData();

					} catch (Exception e) {
					}

				}
				if (msg.what == 110) {
					Log.i(TAG, "NOT");
					final int finalPos = 0;
					Cursor cursor = mDatabaseAction.queryAll();
					Log.d(TAG, "count ==" + cursor.getCount());
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

					Log.e(TAG, "City ==" + city);

					int curWeathericon = (Integer) contentValues
							.get(Weather_Column.CURRENT_WEATHERICON);
					int[] tempArray = WeatherView.getTempArray(contentValues);

					int curTemp = tempArray[0];
					int highTemp = tempArray[1];
					int lowTemp = tempArray[2];
//					makeNotification(curWeathericon, city, curTemp, highTemp,
//							lowTemp);
                    cursor.close();
				}
			}
		};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        UmengUpdateAgent.update(this);

        // baidu push
        PushManager.startWork(getApplicationContext(),
                PushConstants.LOGIN_TYPE_API_KEY,
                getMetaValue(this, "api_key"));

        // setTags
        List<String> tags = new ArrayList<String>();
        tags.add(getMetaValue(this, "UMENG_CHANNEL"));
        PushManager.setTags(getApplicationContext(), tags);

		setContentView(R.layout.weather_main);
		mLocClient = ((Location)getApplication()).mLocationClient;
		Intent i = getIntent();
		isone = i.getBooleanExtra("isone", isone);
		registerConnectionReceiver();
		mDatabaseAction = new DatabaseAction(this);

        initBitmapArray();
        initDataBase();
		initView();

        FeedbackAgent agent = new FeedbackAgent(this);
        agent.sync();
	
		SharedPreferences sp =getSharedPreferences("locationinfo", MODE_PRIVATE);
		 city=sp.getString("ncity","");
		 location=sp.getString("nlocation","");
		if("".equals(city)||"".equals(location)){
			return;
		}
		
		//
//		databaseAction = new DatabaseAction(this);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Cursor cursor=mDatabaseAction.queryAll();
				Log.i(TAG,"KOK "+cursor.getCount());
				if(cursor.getCount() > 0){
					while(cursor.moveToNext()){
						if(city.equals(cursor.getString(cursor.getColumnIndex("city_cn")))){
							//已经存在
							Log.i(TAG,"KOK "+cursor.getString(3));
							isExist=true;
							
						}
					}
					if(!isExist){
                        addLocalCity();
                    }
				}
                cursor.close();
			}
		}).start();
		
	
		
	}

    public int getmPosition(){
        return mPosition;
    }

    private void addLocalCity(){
        Message mess=Message.obtain();
        mess.what=666;
        mHandler.sendMessage(mess);
    }

	@Override
	protected void onResume() {
		super.onResume();
        MobclickAgent.onResume(this);
        if(initDataBase() <= 0 ){
            finish();
        }
        refreshViewPager();
        if(setPosition >= 0){
            mViewSwitcher.setCurrentItem(setPosition);
            setPosition = -1;
        }
        refreshDotView();
	}

	@Override
	protected void onPause() {
		super.onPause();
        MobclickAgent.onPause(this);
	}

    private int initDataBase(){
        mCount = mDatabaseAction.getCursorCount();
        return mCount;
    }

	private void initView() {

        mDotView = findViewById(R.id.dot_view);

        PagerAdapter mPagerAdapter = new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return mCount;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View)object);
                Log.i("Wonder", "destroyItem(): position="+position);
                views.remove(object);
                Log.i("Wonder", "views.size="+views.size()) ;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = makeView(position);
                container.addView(view);
                views.add(view);
                Log.i("Wonder", "instantiateItem(): position="+position);
                Log.i("Wonder", "views.size="+views.size()) ;
                return view;
            }

            @Override
            public int getItemPosition(java.lang.Object object) {
                int position = ((DisplayWeatherView)object).getPositionInDB();
                if(position < 0 || needForceRefresh){
                    return POSITION_NONE;
                } else {
                    return position;
                }
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                Log.i("Wonder", "setPrimaryItem(): position = "+position);
                mCurrentView = (DisplayWeatherView) object;
                //mCurrentView.drawBackground();
            }
        };

        ViewPager.OnPageChangeListener myPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                mPosition = i;
                Log.i("Wonder", "onPageSelected(): i = "+i);
                for(View view : views){
                    DisplayWeatherView weatherView = (DisplayWeatherView)view;
                    if(weatherView.getCurPosition() == i){
                        weatherView.drawBackground();
                    }
                }
                refreshDotView();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        };


		mViewSwitcher = (ViewPager) findViewById(R.id.switcher);
        mViewSwitcher.setAdapter(mPagerAdapter);
        mViewSwitcher.setOnPageChangeListener(myPageChangeListener);

        mBtnYahoo = (Button) findViewById(R.id.button_yahoo);
        mBtnYahoo.setOnClickListener(this);

        mBtnSettings = (Button) findViewById(R.id.button_settings);
        mBtnSettings.setOnClickListener(this);

        mInitialMetric = mDatabaseAction.getCurrentMetric();
	}


	public Bitmap getDrawBitmap(int weatherIcon) {

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

	private void makeNotification(int weatherDrawable, String city,
			int curTemp, int highTemp, int lowTemp) {
		try {
			Context context = this;

			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

			int icon = android.R.drawable.stat_notify_voicemail;
			CharSequence tickerText = "";
			long when = 0;
			Notification notification = new Notification(icon, tickerText, when);

			notification.flags |= Notification.FLAG_ONGOING_EVENT;
//			notification.titleFlags = Notification.FLAG_HIDDEN_TITLE;

			RemoteViews contentView = new RemoteViews(context.getPackageName(),
					R.layout.weather_notify);

			// contentView.setImageViewResource(R.id.img,
			// getDrawBitmap(weatherDrawable));
			contentView.setImageViewBitmap(R.id.img,
					getDrawBitmap(weatherDrawable));

			contentView.setTextViewText(R.id.local_, city);
			contentView.setTextViewText(R.id.highest_temp, highTemp
					+ getString(LaunchActivity.RSV(R.string.du)));
			contentView.setTextViewText(R.id.lowest_temp, lowTemp
					+ getString(LaunchActivity.RSV(R.string.du)));
			contentView.setTextViewText(R.id.currentTemp, curTemp
					+ getString(LaunchActivity.RSV(R.string.du)));

			// 指定个性化视图
			notification.contentView = contentView;

			Intent intent = new Intent(context, LaunchActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
					intent, 0);
			// 指定内容意图
			notification.contentIntent = contentIntent;

			mNotificationManager.notify(CUSTOM_NOTIFY_ID, notification);
			Log.i(TAG, "notify");
		} catch (Exception e) {
			Log.i(TAG, e.toString() + " Exception");
		}
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


    public View makeView(int pos) {
        DisplayWeatherView view = new DisplayWeatherView(this, pos);
        view.setLayoutParams(new ViewSwitcher.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        return view;
    }


	private IphoneTouchInterceptor.DropListener mDropListener = new IphoneTouchInterceptor.DropListener() {
		public void drop(int f, int t) {
			// Log.e(TAG, "DropListener from = " + f);
			// Log.e(TAG, "DropListener to = " + t);
			final DatabaseAction databaseAction = new DatabaseAction(
                    WeatherMain.this);
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


    private void goYahooCityWebsit(){

        if (mContentValues != null) {
            ContentValues contentValues = mContentValues;
            String city;
            String languageDefault = Locale.getDefault().getLanguage();
            if (languageDefault == "en"
                    || languageDefault.equals("en")) {
                city = (String) contentValues
                        .get(Weather_Column.CITY);

            } else {
                city = (String) contentValues
                        .get(Weather_Column.CITY_CN);
            }
        }

        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);

    }

	@Override
	public void onClick(View v) {
        if (v.equals(mBtnYahoo)) {
            goYahooCityWebsit();
        } else if (v.equals(mBtnSettings)) {
            goSettings();
        }
	}

	ConnectionChangeReceiver mConnectionReceiver;

	private void registerConnectionReceiver() {
		mConnectionReceiver = new ConnectionChangeReceiver();
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		this.registerReceiver(mConnectionReceiver, filter);
		filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
		this.registerReceiver(mConnectionReceiver, filter);
	}

	private void unregisterConnectionReceiver() {
		this.unregisterReceiver(mConnectionReceiver);
	}

	private class ConnectionChangeReceiver extends BroadcastReceiver {
		private volatile boolean isParsingXml = false;

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// 检查WIFI是否连接上AP
			String action = intent.getAction();
			// 监听WIFI状态变化
			if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
				WifiManager wifiManager = (WifiManager) context
						.getSystemService(Context.WIFI_SERVICE);
				int state = wifiManager.getWifiState();
				if (state == wifiManager.WIFI_STATE_DISABLED)// WIFI网卡不可用
				{

				}
				if (state == wifiManager.WIFI_STATE_DISABLING)// WIFI正在关闭
				{

				}
				if (state == wifiManager.WIFI_STATE_ENABLED)// WIFI网卡可用
				{
//					DisplayWeatherView view = getCurrentView();
//					view.nowloadData(0);
//                    mViewSwitcher.getAdapter().notifyDataSetChanged();
                    updateAllWebData();
				}
				if (state == wifiManager.WIFI_STATE_ENABLING)// WIFI网卡正在打开
				{

				}
				if (state == wifiManager.WIFI_STATE_UNKNOWN)// 未知网卡状态
				{

				}

				// Log.e(“Debug”, “Setting wifistate: ” +
				// wifiManager.getWifiState());
			} else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				// //监听连接状态的变化莫测
				NetworkInfo networkInfo = intent
						.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);

				// Log.e(“Debug”, “Setting isConnected: ” +
				// networkInfo.isConnected());
				if (networkInfo.isConnected()) {
					int nType = networkInfo.getType();
//					DisplayWeatherView view = getCurrentView();
//					view.nowloadData(0);
//                    mViewSwitcher.getAdapter().notifyDataSetChanged();
                    updateAllWebData();
                    if (nType == ConnectivityManager.TYPE_MOBILE) {
					}

					else if (nType == ConnectivityManager.TYPE_WIFI) {

					}
					// Toast.makeText(context, “sa”, Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	// @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Location locat=((Location)this.getApplication());
		if(locat!=null&&locat.mLocationClient!=null){
			locat.mLocationClient.stop();
		}
		unregisterConnectionReceiver();
	}

    private void goSettings() {
        //int count = new DatabaseAction(this).getCursorCount();
        startActivityForResult(new Intent().setAction(Utils.ACTION_WEATHER_SETTINGS), 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {

        if(RESULT_OK == resultCode)
        {
            int newMetric = mDatabaseAction.getCurrentMetric();
            int position = data.getExtras().getInt("position");
            if( newMetric != mInitialMetric){
                mInitialMetric = newMetric;
                needForceRefresh = true;
            }

            if (position >= 0) {
                goToCity(position);
            }
//            needForceRefresh = false;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void goToCity(int position){
        //mViewSwitcher.setCurrentItem(position);
        setPosition = position;
    }

    public void refreshViewPager(){
        mViewSwitcher.getAdapter().notifyDataSetChanged();
        needForceRefresh = false;
    }

    private void refreshDotView(){
        if (mDotView != null){
            mDotView.invalidate();
        }
    }

    private void updateAllWebData(){
        for(View view: views){
            ((DisplayWeatherView)view).reloadWebData(false);
        }
    }

    private void initBitmapArray(){
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
    }

    // 获取AppKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return apiKey;
    }


}