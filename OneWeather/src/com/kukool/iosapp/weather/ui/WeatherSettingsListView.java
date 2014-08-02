package com.kukool.iosapp.weather.ui;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;


public class WeatherSettingsListView extends IphoneTouchInterceptor {

	private WeatherSettingsListItem mSelectedItem;

	
	public WeatherSettingsListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setDivider(null);
		this.setScrollbarFadingEnabled(true);
		this.setDrawingCacheAlpha(60);
		this.setCacheColorHint(Color.TRANSPARENT); 
		// list view宽度的百分比
		this.setDrawingCacheHorizontalMargin(0.032f);
	}
	
	public WeatherSettingsListItem getMSelectedItem() {
		return mSelectedItem;
	}


	public void setMSelectedItem(WeatherSettingsListItem selectedItem) {
		mSelectedItem = selectedItem;	
	}

}
