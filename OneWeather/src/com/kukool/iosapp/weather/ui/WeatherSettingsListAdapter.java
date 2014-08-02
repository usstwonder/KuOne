package com.kukool.iosapp.weather.ui;


import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.kukool.iosapp.weather.R;
import com.kukool.iosapp.weather.activity.WeatherSettings;
import com.kukool.iosapp.weather.model.DatabaseAction;
import com.kukool.iosapp.weather.model.Utils;
import com.kukool.iosapp.weather.provider.Weather.Weather_Column;


public class WeatherSettingsListAdapter extends CursorAdapter {
	private final static boolean DEBUG = true;
	private final static String TAG = "WeatherSettingsListAdapter";
	
	private Context mContext;
	//在sdk中mCursor隐藏了
	public Cursor mCursor;
	private Handler mHandler;
	private String  languageDefault;
	private String countryDefault;

    public WeatherSettingsListAdapter(Context context, Cursor c,
                                      boolean autoRequery, Handler handler) {
        super(context, c, autoRequery);
        mCursor = c;
        mContext = context;
        mHandler = handler;
        languageDefault = Locale.getDefault().getLanguage();
        countryDefault = Locale.getDefault().getCountry();
    }
	
	@Override
	protected void onContentChanged() {
		super.onContentChanged();
		if (mListener != null) {			
			mListener.onContentChanged(getCount());
		}
	}
	

	// 父类的抽象方法
	// 构造一个view
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater flater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return (WeatherSettingsListItem) flater.inflate(R.layout.weather_settings_list_item, parent, false);
	}
	
	// 父类的抽象方法
	// 对view进行的操作
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
        ((WeatherSettingsListItem) view).init();

		((WeatherSettingsListItem) view).setMHandler(mHandler);
		TextView cityView = (TextView) view.findViewById(R.id.city);
		// 取得中文城市
		String city = null;
		city = (String) cursor.getString(cursor.getColumnIndex(Weather_Column.CITY));
		if (countryDefault == "CN" || countryDefault.equals("CN")||countryDefault == "TW" || countryDefault.equals("TW"))	
		{
			String cityCn = (String) cursor.getString(cursor.getColumnIndex(Weather_Column.CITY_CN));
		
			if (cityCn.equals(Utils.NO_CHINISE_CITY)) {
				
			} else {
				city = (String) cityCn;
			}
		}
		cityView.setText(city);

        //Temperature of each item
        int curTemp = 0;
        TextView TmprView = (TextView) view.findViewById(R.id.temperature);
        int metric = ((WeatherSettings)context).getCurrentMetric();
        if(MetricCheckButton.C_ON == metric)  {
            curTemp = cursor.getInt(cursor.getColumnIndex(Weather_Column.CURRENT_TEMPERATURE_C));
        }else{
            curTemp = cursor.getInt(cursor.getColumnIndex(Weather_Column.CURRENT_TEMPERATURE_F));
        }
        TmprView.setText(String.valueOf(curTemp) + context.getResources().getString(R.string.degree_label));

        //Background of list item
        String localTime = cursor.getString(cursor.getColumnIndex(Weather_Column.LOCAL_TIME));
        if (Utils.isDayTime(localTime)) {
        } else {
            view.setBackgroundResource(R.drawable.iphone_weathersettings_rectnoround_dark);
        }

		// Delete button
		final int id = cursor.getInt(cursor.getColumnIndex(Weather_Column._ID));
		Button delButton = (Button) view.findViewById(R.id.del_button);
		final View finalView = view;
		delButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DatabaseAction databaseAction = new DatabaseAction(mContext);
				databaseAction.deleteById(id);
				databaseAction.refreshAllPos();
				WeatherSettingsListView parent = (WeatherSettingsListView) finalView.getParent();
				if (parent != null)
				parent.setMSelectedItem(null);
				// 在删除一个item后重置DeleteMode为false
				((IphoneTouchInterceptor)finalView.getParent()).setDeleteMode(false);
			}
		});

        //Set ID of item = position
        int position = cursor.getPosition();
        ((WeatherSettingsListItem) view).setItemId(position);
	}
	
	/**
	 * 2011-5-17
	 * 列表数量改变的监听
	 */
	public ContentChangedListener mListener;
	
	public void setContentChangedListener(ContentChangedListener listener) {
		mListener = listener;
	}
	
	public interface ContentChangedListener {
		public void onContentChanged(int count);
	}
	
}
