package com.kukool.iosapp.weather.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kukool.iosapp.weather.R;
import com.kukool.iosapp.weather.model.DatabaseAction;
import com.kukool.iosapp.weather.model.Utils;
import com.kukool.iosapp.weather.model.WebAction;
import com.kukool.iosapp.weather.provider.Weather;


public class LaunchActivity extends Rotate3dAnimationActivity {
	//public LocationClient mLocationClient = null;
	private static final String TAG = "LaunchActivity";
	private static final boolean DEBUG = true;
//	 class MyLocationListenner implements BDLocationListener {
//		 String city,province,country;
//		 String aaa;
//		@Override
//		public void onReceiveLocation(BDLocation location) {
//			if (location == null)
//				return ;
//			StringBuffer sb = new StringBuffer(256);
//			sb.append("time : ");
//			sb.append(location.getTime());
//			sb.append("\nerror code : ");
//			sb.append(location.getLocType());
//			sb.append("\nlatitude : ");
//			sb.append(location.getLatitude());
//			sb.append("\nlontitude : ");
//			sb.append(location.getLongitude());
//			sb.append("\nradius : ");
//			sb.append(location.getRadius());
//			if (location.getLocType() == BDLocation.TypeGpsLocation){
//				sb.append("\nspeed : ");
//				sb.append(location.getSpeed());
//				sb.append("\nsatellite : ");
//				sb.append(location.getSatelliteNumber());
//			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
//				province=location.getProvince();
//				sb.append("\n省：");
//				sb.append(location.getProvince());
//				sb.append("\n市：");
//				sb.append(location.getCity());
//				city=location.getCity();
//				//Log.i("CITY","city: "+city);
//				sb.append("\n区/县：");
//				sb.append(location.getDistrict());
//				
//				sb.append("\naddr : ");
//				sb.append(location.getAddrStr());
//				//加入数据库
//				if (city.compareTo("成都") == 0) {
//					aaa = "chengdu";
//				} else if (city.compareTo("重庆") == 0) {
//					aaa = "chongqing";
//				} else if (city.compareTo("长沙") == 0) {
//					aaa = "changsha";
//				} else if (city.compareTo("大连") == 0) {
//					aaa = "dalian";
//				} else if (city.compareTo("长春") == 0) {
//					aaa = "changchun";
//				}
//
//				else {
//					aaa = AddCity.getPinYin(city);
//					// Log.i(TAG,"code: "+aaa);
//				}
//				
//				
//
//				try {
//				
//					String location2 = "ASI%7CCN%7CCH024%7CSHANGHAI%7C";//cityList.get(position).getLocation();
//					// 先保存城市到数据�?			
//					
//					DatabaseAction databaseAction = new DatabaseAction(LaunchActivity.this);
//					final int metric = databaseAction.getCurrentMetric();
//					ContentValues values = new ContentValues();
//					values.put(Weather.Weather_Column.CITY, aaa);
//					values.put(Weather.Weather_Column.STATE, "China("+aaa+")");
//					values.put(Weather.Weather_Column.LOCTION, location2);
//					values.put(Weather.Weather_Column.CITY_CN,city);
//					values.put(Weather.Weather_Column.METRIC, 1);
//					values.put(Weather.Weather_Column.POSITION,
//							databaseAction.getMinPosition() - 1);
//					
//					databaseAction.doInsert(null);
//					Uri  uri = databaseAction.doInsert(values);
//					
//			        Message msg = mHandler.obtainMessage(DOWNLOAD_CITYLIST);
//					
//					Bundle bundle = new Bundle();
//					bundle.putString("uri", uri.toString());
//					bundle.putString("location", location2);
//					msg.setData(bundle);
//					mHandler.sendMessage(msg);
//					
//
//				} catch (Exception e) {
//				}
//				
//			}
//			sb.append("\nsdk version : ");
//			//sb.append(mLocationClient.getVersion());
//			//logMsg(sb.toString());
//		}
//		
//		public void onReceivePoi(BDLocation poiLocation) {
//			if (poiLocation == null){
//				return ;
//			}
//			StringBuffer sb = new StringBuffer(256);
//			sb.append("Poi time : ");
//			sb.append(poiLocation.getTime());
//			sb.append("\nerror code : ");
//			sb.append(poiLocation.getLocType());
//			sb.append("\nlatitude : ");
//			sb.append(poiLocation.getLatitude());
//			sb.append("\nlontitude : ");
//			sb.append(poiLocation.getLongitude());
//			sb.append("\nradius : ");
//			sb.append(poiLocation.getRadius());
//			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
//				sb.append("\naddr : ");
//				sb.append(poiLocation.getAddrStr());
//			} 
//			if(poiLocation.hasPoi()){
//				sb.append("\nPoi:");
//				sb.append(poiLocation.getPoi());
//			}else{				
//				sb.append("noPoi information");
//			}
//			//logMsg(sb.toString());
//		}
//	}
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launch_activity);
		Intent i = new Intent();
		// int citiesCount = new DatabaseAction(this).queryCities().getCount();
		// Log.e(TAG, "onCreate citiesCount = " + citiesCount);
		// if (citiesCount <= 0) {
		// new Utils(this).insertChineseCity();
		// }
//		mLocationClient = new LocationClient( getApplicationContext() );
//		mLocationClient.registerLocationListener(new MyLocationListenner());
//		
//		int count = new DatabaseAction(this).getCursorCount();
//		//获得本地城市；
//		LocationClientOption option = new LocationClientOption();
//		option.setOpenGps(false);				//打开gps
//		option.setCoorType("bd09ll");		//设置坐标类型
//		option.setAddrType("all");		//设置地址信息，仅设置为“all”时有地址信息，默认无地址信息
//		option.setScanSpan(Integer.parseInt("900"));	//设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
//		mLocationClient.setLocOption(option);
//		mLocationClient.start();
		int count = new DatabaseAction(this).getCursorCount();
		if (count <= 0)
		{
			initcitydata();
			i.putExtra("isone", true);
		}	
		else
		{
			i.putExtra("isone", false);
		}
	    //findViewById(R.id.display_weather).setVisibility(View.GONE);
	    
		startActivity(i
				.setAction(Utils.ACTION_DISPLAY_WEATHER_AND_SETTINGS));
		//overridePendingTransition(R.anim.transition_in, R.anim.still);
		LaunchActivity.this.finish();
		 
		/*if (count <= 0) 
		{
			applyRotation(ROTATE_TARGET_WEATHER_SETTINGS);
		}
		else 
		{
			initcitydata();
			findViewById(R.id.display_weather).setVisibility(View.GONE);
			startActivity(new Intent()
					.setAction(Utils.ACTION_DISPLAY_WEATHER_AND_SETTINGS));
			overridePendingTransition(R.anim.transition_in, 0);
			LaunchActivity.this.finish();
		}*/
	}

	 
	public static	int RSV(int v)
		{
			//R.string.add_city_suggestion 
		   //String  languageDefault = Locale.getDefault().getLanguage();  
		   //String countryDefault = Locale.getDefault().getCountry();
		   return v;
//		   if (countryDefault == "CN" || countryDefault.equals("CN"))		   
//		   {
//				switch(v)
//				{
//				
//				    case    R.string.refreshing_str:return R.string.refreshing_str_cn;
//					case    R.string.hello: return R.string.hello_cn;
//					case	R.string.app_name: return R.string.app_name_cn;
//					case    R.string.txt_high_temp: return R.string.txt_high_temp_cn;
//					case    R.string.txt_low_tmp: return R.string.txt_low_tmp_cn;
//					case    R.string.degree_label:         return       R.string.degree_label_cn; 
//					case    R.string.refresh_at:           return      	    R.string.refresh_at_cn ;
//					case    R.string.fail_refresh_str:     return      	    R.string.fail_refresh_str_cn ;   
//					case    R.string.add_city_title:         return    	    R.string.add_city_title_cn ;
//					case    R.string.verify_city:             return   	    R.string.verify_city_cn ;
//					case    R.string.no_results_found:         return  	    R.string.no_results_found_cn ;
//					case    R.string.network_error:             return 	    R.string.network_error_cn ;
//					case    R.string.btn_cancel:        return         	    R.string.btn_cancel_cn ;
//					case    R.string.updating_data:       return       	    R.string.updating_data_cn ;
//					case    R.string.success_load_data:    return      	    R.string.success_load_data_cn ;
//					case    R.string.str_done:               return    	    R.string.str_done_cn ;
//					case    R.string.str_max_add_cities:       return  	    R.string.str_max_add_cities_cn ;
//					case    R.string.msg_ok:                   return 	    R.string.msg_ok_cn ;
//					case    R.string.add_city_suggestion:        return	    R.string.add_city_suggestion_cn ;
//					case    R.string.dangditianqi:               return	    R.string.dangditianqi_cn ;
//					case    R.string.local_:                     return	    R.string.local__cn ;
//					case    R.string.highest:                    return	    R.string.highest_cn ;
//					case    R.string.lowest:                     return	    R.string.lowest_cn;
//					case    R.string.h_temp:                     return	    R.string.h_temp_cn;
//					case    R.string.l_temp:                     return	    R.string.l_temp_cn;
//					case    R.string.current_:                   return	    R.string.current__cn;
//					case    R.string.du:                         return	    R.string.du_cn;			
//				
//				}
//		   }
//		   else if (languageDefault == "en" || languageDefault.equals("en"))
//		   {
//			
//			   switch(v)
//				{
//				    case    R.string.refreshing_str:return R.string.refreshing_str_en;
//					case    R.string.hello: return R.string.hello_en;
//					case	R.string.app_name: return R.string.app_name_en;
//					case    R.string.txt_high_temp: return R.string.txt_high_temp_en;
//					case    R.string.txt_low_tmp: return R.string.txt_low_tmp_en;
//					case    R.string.degree_label:         return       R.string.degree_label_en; 
//					case    R.string.refresh_at:           return      	    R.string.refresh_at_en ;
//					case    R.string.fail_refresh_str:     return      	    R.string.fail_refresh_str_en ;   
//					case    R.string.add_city_title:         return    	    R.string.add_city_title_en ;
//					case    R.string.verify_city:             return   	    R.string.verify_city_en ;
//					case    R.string.no_results_found:         return  	    R.string.no_results_found_en ;
//					case    R.string.network_error:             return 	    R.string.network_error_en ;
//					case    R.string.btn_cancel:        return         	    R.string.btn_cancel_en ;
//					case    R.string.updating_data:       return       	    R.string.updating_data_en ;
//					case    R.string.success_load_data:    return      	    R.string.success_load_data_en ;
//					case    R.string.str_done:               return    	    R.string.str_done_en ;
//					case    R.string.str_max_add_cities:       return  	    R.string.str_max_add_cities_en ;
//					case    R.string.msg_ok:                   return 	    R.string.msg_ok_en ;
//					case    R.string.add_city_suggestion:        return	    R.string.add_city_suggestion_en ;
//					case    R.string.dangditianqi:               return	    R.string.dangditianqi_en ;
//					case    R.string.local_:                     return	    R.string.local__en ;
//					case    R.string.highest:                    return	    R.string.highest_en ;
//					case    R.string.lowest:                     return	    R.string.lowest_en;
//					case    R.string.h_temp:                     return	    R.string.h_temp_en;
//					case    R.string.l_temp:                     return	    R.string.l_temp_en;
//					case    R.string.current_:                   return	    R.string.current__en;
//					case    R.string.du:                         return	    R.string.du_en;			
//				
//				}
//		    }	
//			return 0;
		} 
	@Override
	public void doAfterAnim() {
		startActivity(new Intent().setAction(Utils.ACTION_FIRST_ADD_CITY));
		overridePendingTransition(R.anim.activity_popupwindow_anim_enter,
				R.anim.transition_out);
		LaunchActivity.this.finish();
	}

	private void initcitydata() {
		Log.i(TAG,"添加sh");

		try {
		
			String location = "ASI%7CCN%7CCH024%7CSHANGHAI%7C";//cityList.get(position).getLocation();
			// 先保存城市到数据�?			
			
			DatabaseAction databaseAction = new DatabaseAction(LaunchActivity.this);
			final int metric = databaseAction.getCurrentMetric();
			ContentValues values = new ContentValues();
			values.put(Weather.Weather_Column.CITY, "Shanghai");
			values.put(Weather.Weather_Column.STATE, "China(Shanghai)");
			values.put(Weather.Weather_Column.LOCTION, location);
			values.put(Weather.Weather_Column.CITY_CN,getResources().getString(R.string.shanghai));
			values.put(Weather.Weather_Column.METRIC, 1);
			values.put(Weather.Weather_Column.POSITION,
					databaseAction.getMaxPosition() + 1);
			
			databaseAction.doInsert(null);
			Uri  uri = databaseAction.doInsert(values);
			
	        Message msg = mHandler.obtainMessage(DOWNLOAD_CITYLIST);
			
			Bundle bundle = new Bundle();
			bundle.putString("uri", uri.toString());
			bundle.putString("location", location);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
			

		} catch (Exception e) {
		}
	}
	private static final int DOWNLOAD_CITYLIST = 101;
	Handler mHandler = new Handler() {

		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {		
			case DOWNLOAD_CITYLIST:
				// 每次只取摄氏温度，然后计算华氏温度，
				// 在数据库中保存这两份温度值，所以metric设置�?
				Bundle bundle = msg.getData();
				Uri uri = Uri.parse(bundle.getString("uri"));
				String location = bundle.getString("location");			
				new WebAction(LaunchActivity.this, uri, location, 1).startLoadData();
				break;
			}
		}

	};
	
	
	@Override
	public void doBeforeAnim() {
		// TODO Auto-generated method stub
		
	}
}