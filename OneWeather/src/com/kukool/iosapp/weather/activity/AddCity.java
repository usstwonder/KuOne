package com.kukool.iosapp.weather.activity;

import java.util.ArrayList;


import java.util.Locale;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.kukool.HanziToPinyin.HanziToPinyin;
import com.kukool.HanziToPinyin.HanziToPinyin.Token;
import com.kukool.iosapp.weather.R;
import com.kukool.iosapp.weather.model.CityListInfo;
import com.kukool.iosapp.weather.model.DatabaseAction;
import com.kukool.iosapp.weather.model.Network;
import com.kukool.iosapp.weather.model.Utils;
import com.kukool.iosapp.weather.model.WebAction;
import com.kukool.iosapp.weather.model.WebActionCityList;
import com.kukool.iosapp.weather.model.WebActionCityList.DownLoadCityListListener;
import com.kukool.iosapp.weather.provider.Weather;
import com.umeng.analytics.MobclickAgent;

public class AddCity extends PortraitActivity implements
		DownLoadCityListListener, OnFocusChangeListener {

	private static final String TAG = "AddCity";
	private static final boolean DEBUG = true;

	private TextView mTextView;
	// private IphoneClearEditText mEditText;
	private EditText mEditText;
	private Button mCancelButton;
	private Button mXXXXButton;
	private ListView mListView;
	private CityListAdapter mCityListAdapter;

	private String languageDefault;
	private String countryDefault;
	private WebActionCityList mWebActionCityList;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_city);

		languageDefault = Locale.getDefault().getLanguage();
		countryDefault = Locale.getDefault().getCountry();
		initView();
	}

	public void onFocusChange(View v, boolean hasFocus) {

	}

	public static String getPinYin(String input) {
		ArrayList<Token> tokens = HanziToPinyin.getInstance().get(input);
		StringBuilder sb = new StringBuilder();
		if (tokens != null && tokens.size() > 0) {
			for (Token token : tokens) {
				if (Token.PINYIN == token.type) {
					sb.append(token.target);
				} else {
					sb.append(token.source);
				}
			}
		}
		return sb.toString().toLowerCase();
	}

	private void initView() {
//		if (WeatherSettings.phoneInfo != null)
//			System.out.println("get phone info:"
//					+ WeatherSettings.phoneInfo.getInfo());
		mTextView = (TextView) findViewById(R.id.text);
		mTextView.setTextColor(Color.LTGRAY);
//		int size = (int) mTextView.getTextSize() - 1;// 字体小1
//		mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
//		float ff = mTextView.getTextSize();
		// mEditText = (IphoneClearEditText) findViewById(R.id.edit_text);

		mEditText = (EditText) findViewById(R.id.edit_text);
		mEditText.addTextChangedListener(mTextWatcher);
		// mEditText.setLeft(50);

		mXXXXButton = (Button) findViewById(R.id.btn_xxxx);
		mXXXXButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// String JUST_FINISH_ACTIVITY = "";
				// finishActivity(JUST_FINISH_ACTIVITY);
				mEditText.setText("");
				// mListView.removeAllViewsInLayout();
			}
		});

		mCancelButton = (Button) findViewById(R.id.btn_cancel);

		mCancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				String JUST_FINISH_ACTIVITY = "";
//				
//			
// finishActivity(JUST_FINISH_ACTIVITY);
 
 
	new Thread(new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// InputMethodManager m = (InputMethodManager)
			// mEditText.getContext()
			// .getSystemService(Context.INPUT_METHOD_SERVICE);
			//
			// m.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);

			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(AddCity.this
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);

			Message mess = Message.obtain();
			mess.what = CANCLE_MSG;
			mHandler.sendMessageDelayed(mess, 10);
		}
	}).start();
 
 
 
			}
		});

		mListView = (ListView) findViewById(R.id.list_view);
		mListView.setDivider(null);
		mListView.setScrollbarFadingEnabled(true);

		// 初始化给list设置一个空的adapter
		mCityListAdapter = new CityListAdapter(this, mCityListInfo);
		mListView.setAdapter(mCityListAdapter);

		// 当初次进入时候启动软键盘

		InputMethodManager m = (InputMethodManager) mEditText.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		// m.sendAppPrivateCommand(mEditText,"actionDone", null);

		// m.showSoftInput(mListView, InputMethodManager.SHOW_IMPLICIT);
		mWebActionCityList = new WebActionCityList(this);
		mWebActionCityList.addDownLoadCityListListener(this);
	}

	private TextWatcher mTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (TextUtils.isEmpty(s.toString().trim())) {
				return;
			}
			// 移除之前没执行完的消�?
			mHandler.removeMessages(EDIT_MSG);
			Message msg = mHandler.obtainMessage(EDIT_MSG);
			msg.obj = s;
			mHandler.sendMessageDelayed(msg, DateUtils.SECOND_IN_MILLIS);
		}
	};

	private static final int EDIT_MSG = 100;
	private static final int DOWNLOAD_CITYLIST = 101;
	private static final int CANCLE_MSG=333;
	private static final int SAVE_MSG=111;
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case CANCLE_MSG:
				finishActivity("");
				break;
			case SAVE_MSG:
				finishActivity(getIntent().getAction());
				break;
			case EDIT_MSG:
				CharSequence s = (CharSequence) msg.obj;
				String searchStr = s.toString();
				String aaa;
				// Log.i(TAG,"code1: "+s.toString()+"  ..."+searchStr.compareTo("成都"));
				if (searchStr.compareTo("成都") == 0) {
					aaa = "chengdu";
				} else if (searchStr.compareTo("重庆") == 0) {
					aaa = "chongqing";
				} else if (searchStr.compareTo("长沙") == 0) {
					aaa = "changsha";
				} else if (searchStr.compareTo("大连") == 0) {
					aaa = "dalian";
				} else if (searchStr.compareTo("长春") == 0) {
					aaa = "changchun";
				}else if (searchStr.compareTo("沈阳") == 0) {
					aaa = "shenyang";
				}else if (searchStr.compareTo("厦门") == 0) {
					aaa = "xiamen";
				}else if (searchStr.compareTo("大同") == 0) {
					aaa = "datong";
				}else if (searchStr.compareTo("晋中") == 0) {
					aaa = "jinzhong";
				}else if (searchStr.compareTo("长治") == 0) {
					aaa = "changzhi";
				}

				else {
					aaa = getPinYin(searchStr);
					 Log.i(TAG,"code: "+aaa);
				}
				Log.i(TAG, "code: " + aaa);
				mWebActionCityList.startLoadCityList(searchStr, aaa);
				break;
			case DOWNLOAD_CITYLIST:
				// 每次只取摄氏温度，然后计算华氏温度，
				// 在数据库中保存这两份温度值，所以metric设置�?
				Bundle bundle = msg.getData();
				Uri uri = Uri.parse(bundle.getString("uri"));
				String location = bundle.getString("location");
				// Log.e(TAG, " handleMessage location = " + location);
				 Log.e(TAG, " handleMessage uri = " + location);
				new WebAction(AddCity.this, uri, location, 1).startLoadData();
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// InputMethodManager m = (InputMethodManager)
						// mEditText.getContext()
						// .getSystemService(Context.INPUT_METHOD_SERVICE);
						//
						// m.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);

						InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						inputMethodManager.hideSoftInputFromWindow(AddCity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);

						Message mess = Message.obtain();
						mess.what = SAVE_MSG;
						mHandler.sendMessageDelayed(mess, 10);
					}
				}).start();
				//finishActivity(getIntent().getAction());
				break;
			}
		}

	};

	private void finishActivity(String action) {
		if (action.equals(Utils.ACTION_FIRST_ADD_CITY)) {
			startActivity(new Intent()
					.setAction(Utils.ACTION_FIRST_DISPLAY_WEATHER_AND_SETTINGS));
		}
		finish();
		overridePendingTransition(R.anim.transition_in,
				R.anim.activity_popupwindow_anim_exit);
	}

	@Override
	public void onStartDownLoadCityList() {
		// TODO Auto-generated method stub
		boolean connectNetwork = new Network(this).isConnected();
		if (connectNetwork) {
			mTextView.setText(getResources().getString(
					LaunchActivity.RSV(R.string.verify_city)));
		} else {
			mTextView.setText(getResources().getString(
					LaunchActivity.RSV(R.string.network_error)));
		}
	}

	private Vector<CityListInfo> mCityListInfo;

	@Override
	public void onFinishDownLoadCityList(Vector<CityListInfo> cityListInfo) {
		// TODO Auto-generated method stub
		mCityListInfo = cityListInfo;
		Log.e(TAG, "onFinishDownLoadCityList 0");
		if (mCityListInfo == null) {
			return;
		}
		Log.e(TAG, "onFinishDownLoadCityList 1");

		if (mCityListInfo.size() == 0) {
			mTextView.setText(getResources().getString(
					LaunchActivity.RSV(R.string.no_results_found)));
		} else {
			mTextView.setText(getResources().getString(
					LaunchActivity.RSV(R.string.add_city_title)));
		}
		Log.e(TAG, "onFinishDownLoadCityList 2");

		Log.e(TAG, "onFinishDownLoadCityList 3");
		((CityListAdapter) mListView.getAdapter()).notifyDataSetChanged();
		mListView.setAdapter(new CityListAdapter(this, cityListInfo));
		// mCityListAdapter.notifyDataSetChanged();
		Log.e(TAG, "onFinishDownLoadCityList 4");
	};

	private class CityListAdapter extends BaseAdapter {

		private Context mContext;
		private Vector<CityListInfo> mCityList;
		private final static int MINI_LIST_COUNT = 6;

		public CityListAdapter(Context context, Vector<CityListInfo> cityList) {

			mContext = context;
			mCityList = cityList;
		}

		public int getCount() {
			Log.e(TAG, "getCount " + mCityList + " "
					+ (mCityList != null ? mCityList.size() : "null"));
			if (mCityList == null) {
				return MINI_LIST_COUNT;
			}

			if (mCityList.size() < MINI_LIST_COUNT) {
				return MINI_LIST_COUNT;
			} else {
				return mCityList.size();
			}
		}

		public Object getItem(int position) {
			Log.e(TAG, "getItem " + position + " " + mCityList + " "
					+ (mCityList != null ? mCityList.size() : "null"));
			if (mCityList == null) {
				return null;
			}

			if (position < mCityList.size()) {
				return mCityList.get(position);
			} else {
				return null;
			}
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			Log.e(TAG, "getView " + position + " " + mCityList + " "
					+ (mCityList != null ? mCityList.size() : "null"));
			LayoutInflater flater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			TextView city = (TextView) flater.inflate(
					R.layout.add_city_list_item, parent, false);
			int count = 0;

			Log.e(TAG, "getView 0");
			if (mCityList != null) {
				count = mCityList.size();
			} else {
				count = 0;
			}
			if (position < count) {
				// 英文城市
				String strCity = mCityList.get(position).getCity();
				String strState = mCityList.get(position).getState();
				String cityEn = strCity + strState;
				// 中文城市
				// 只查找在中国境内的城市的中文城市
				// 减少不必要查�?提高效率

				String cityCn = Utils.NO_CHINISE_CITY;
				cityCn = new Utils(mContext).getChineseCity(cityEn);
				if (countryDefault == "CN" || countryDefault.equals("CN")||countryDefault == "TW" || countryDefault.equals("TW")) {
					if (strState.startsWith("China")
							|| strState.startsWith("Taiwan")
							|| strState.startsWith("Hong Kong")
							|| strState.startsWith("Macau")) {
						
						if (cityCn.equals(Utils.NO_CHINISE_CITY)) {
							city.setText(cityEn);
						} else {
							city.setText(cityCn);
						}
					} else {
						city.setText(cityEn);
					}
				} else {
					city.setText(cityEn);
				}
				// 从数据库中读取的中文城市
				// 暂时不采用这种方�? // final String cnCity = getCnCity(mCityList,
				// position);
				// city.setText(cityCn);
				city.setClickable(true);
				city.setBackgroundResource(R.drawable.iphone_weather_rectnoround);
				final int finalPos = position;
				final String finalCityCn = cityCn;
				city.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						saveData(mCityList, finalPos, finalCityCn);
					}
				});
			} else {
				city.setClickable(false);
				city.setBackgroundResource(R.drawable.iphone_weather_rectnoround_white);
			}
			Log.e(TAG, "getView 1");
			return city;
		}

		// // 取得中文的城市名�? // private String getCnCity( Vector<CityListInfo>
		// cityList, int
		// position) {
		// String cityEn = cityList.get(position).getCity()
		// + cityList.get(position).getState();
		// Cursor cursor = new DatabaseAction(mContext).queryCnCities(cityEn);
		// if (cursor.getCount() == 0) {
		// return cityEn;
		// }
		// String cityCn = cityEn;
		// if (cursor != null) {
		// cursor.moveToFirst();
		// cityCn =
		// cursor.getString(cursor.getColumnIndex(Cities_Column.CITY_CN));
		// cursor.close();
		// }
		// return cityCn;
		// }

		private void saveData(Vector<CityListInfo> cityList, int position,
				String cityCn) {
			if (cityList == null) {
				return;
			}
			// Log.e(TAG, " onItemClick position = " + finalPos);
			try {
				String city = cityList.get(position).getCity();
				String state = cityList.get(position).getState();
				String location = cityList.get(position).getLocation();

				// 先保存城市到数据�?
				DatabaseAction databaseAction = new DatabaseAction(AddCity.this);
				final int metric = databaseAction.getCurrentMetric();
				ContentValues values = new ContentValues();
				values.put(Weather.Weather_Column.CITY, city);
				values.put(Weather.Weather_Column.STATE, state);
				values.put(Weather.Weather_Column.LOCTION, location);
				values.put(Weather.Weather_Column.CITY_CN,
						getCityWithNoState(cityCn));
				values.put(Weather.Weather_Column.METRIC, metric);
				values.put(Weather.Weather_Column.POSITION,
						databaseAction.getMaxPosition() + 1);
				Uri uri = databaseAction.doInsert(values);
				// 然后读取网络数据
				Message msg = mHandler.obtainMessage(DOWNLOAD_CITYLIST);
				Bundle bundle = new Bundle();
				bundle.putString("uri", uri.toString());
				bundle.putString("location", location);
				msg.setData(bundle);
				mHandler.sendMessage(msg);
			} catch (Exception e) {
			}
		}

		// 取得没有省份显示的城�?
		private String getCityWithNoState(String cityCn) {
			int length = cityCn.length();
			String returnStr = cityCn;
			for (int i = 0; i < length; i++) {
				if ((char) cityCn.charAt(i) == ',') {
					returnStr = cityCn.substring(0, i);
					break;
				}
			}
			return returnStr;
		}
	}

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}

