package com.kukool.iosapp.weather.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network {

	private Context mContext;
	
	public Network(Context context) {
		mContext = context;
	}
	
	public boolean isConnected() {
		boolean isNetworkUp = false;
		// 监听网络链接的状态
		ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null) {
        	isNetworkUp = info.isAvailable();
        }
		return isNetworkUp;
	}
}
