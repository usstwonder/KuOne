package com.gui.engine;

import java.util.Enumeration;
import java.util.Vector;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.kukool.iosapp.weather.activity.AddCity;
import com.kukool.iosapp.weather.model.CityListInfo;
import com.kukool.iosapp.weather.model.WebActionCityList;
import com.kukool.iosapp.weather.model.WebActionCityList.DownLoadCityListListener;

public class Location extends Application {
	String city = null, province = null, country = "中国";
	public LocationClient mLocationClient = null;
	private String aaa = null;
	private WebActionCityList mWebActionCityList;
	private Vector<CityListInfo> mCityListInfo;
	Context context;

	@Override
	public void onCreate() {
		context = this.getApplicationContext();
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(false); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setAddrType("all"); // 设置地址信息，仅设置为“all”时有地址信息，默认无地址信息
		option.setScanSpan(Integer.parseInt("900")); // 设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.registerLocationListener(new MyLocationListenner());
		mLocationClient.setLocOption(option);
		Log.i("CITY", "START");
		mWebActionCityList = new WebActionCityList(this);
		mWebActionCityList.addDownLoadCityListListener(new CityInfoDownLoad(
				getApplicationContext()));
		mLocationClient.start();
		/**
		 * //如下3行代码为 位置提醒相关代码 mNotifyer = new NotifyLister();
		 * mNotifyer.SetNotifyLocation
		 * (42.03249652949337,113.3129895882556,3000,"gps"
		 * );//4个参数代表要位置提醒的点的坐标，具体含义依次为：纬度，经度，距离范围，坐标系类型(gcj02,gps,bd09,bd09ll)
		 * mLocationClient.registerNotify(mNotifyer);
		 */
		super.onCreate();
		// Log.d("locSDK_Demo1", "... Application onCreate... pid=" +
		// Process.myPid());
	}

	public class MyLocationListenner implements BDLocationListener {
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
				Log.i("TAG","GPS L");
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				Log.i("TAG","NETWORK L");
				province = location.getProvince();
				sb.append("\n省：");
				sb.append(location.getProvince());
				sb.append("\n市：");
				sb.append(location.getCity());
				city = location.getCity();
				Log.i("CITY", "city: " + city);
				SharedPreferences sp = context.getSharedPreferences(
						"locationinfo", MODE_PRIVATE);
				Editor editor = sp.edit();
				if (city != null && city.contains("市")) {
					editor.putString("ncity",
							city.substring(0, city.length() - 1));
					editor.commit();
					if (city.substring(0, city.length() - 1).compareTo("成都") == 0) {
						aaa = "chengdu";
					} else if (city.substring(0, city.length() - 1).compareTo(
							"重庆") == 0) {
						aaa = "chongqing";
					} else if (city.substring(0, city.length() - 1).compareTo(
							"长沙") == 0) {
						aaa = "changsha";
					} else if (city.substring(0, city.length() - 1).compareTo(
							"大连") == 0) {
						aaa = "dalian";
					} else if (city.substring(0, city.length() - 1).compareTo(
							"长春") == 0) {
						aaa = "changchun";
					} else if (city.substring(0, city.length() - 1).compareTo(
							"沈阳") == 0) {
						aaa = "shenyang";
					}

					else {
						aaa = AddCity.getPinYin(city.substring(0,
								city.length() - 1));
						// Log.i(TAG,"code: "+aaa);
					}

					mWebActionCityList.startLoadCityList(
							city.substring(0, city.length() - 1), aaa);

				}

				sb.append("\n区/县：");
				sb.append(location.getDistrict());

				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				// 加入数据库

			}
			sb.append("\nsdk version : ");
			sb.append(mLocationClient.getVersion());
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

}

class CityInfoDownLoad implements DownLoadCityListListener {
	private Context context;

	public CityInfoDownLoad(Context context) {
		super();
		this.context = context;
	}

	@Override
	public void onStartDownLoadCityList() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinishDownLoadCityList(Vector<CityListInfo> cityListInfo) {
		// TODO Auto-generated method stub
		Enumeration<CityListInfo> enums = cityListInfo.elements();
		SharedPreferences sp = context.getSharedPreferences("locationinfo", 0);
		String city = sp.getString("ncity", "");
		String aaa=null;
		if (!"".equals(city)) {
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
			}else if (city.compareTo("沈阳") == 0) {
				aaa = "shenyang";
			}

			else {
				aaa = AddCity.getPinYin(city);
				
			}
			while (enums.hasMoreElements()) {
                     CityListInfo cli=enums.nextElement();
                     Log.i("Location",cli.getLocation()+" "+cli.getCity());
                     if(cli.getCity()!=null&&cli.getCity().toLowerCase().equals(aaa)){
                    	 SharedPreferences sp2 = context.getSharedPreferences(
         						"locationinfo", 0);
               
         				Editor editor = sp2.edit();
         				editor.putString("nlocation",
    							cli.getLocation());
         				editor.commit();
         				break;
                     }
			}
		}
	}

}