package com.kukool.iosapp.weather.ui;

import java.text.SimpleDateFormat;
import java.util.*;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.TextView;
import com.gui.engine.list_windows;
import com.kukool.iosapp.weather.R;
import com.kukool.iosapp.weather.activity.LaunchActivity;
import com.kukool.iosapp.weather.activity.WeatherMain;
import com.kukool.iosapp.weather.model.ContentValuesOperation;
import com.kukool.iosapp.weather.model.DatabaseAction;
import com.kukool.iosapp.weather.model.Network;
import com.kukool.iosapp.weather.model.Utils;
import com.kukool.iosapp.weather.model.WebAction;
import com.kukool.iosapp.weather.model.WebAction.WebDownLoadListener;
import com.kukool.iosapp.weather.provider.Weather;
import com.kukool.iosapp.weather.provider.Weather.Weather_Column;

//import com.kukool.iosapp.weather.activity.DisplayWeatherAndSettings.WeatherGestureListener;

public class DisplayWeatherView extends WeatherView implements
        WebDownLoadListener, OnClickListener {
    private static final String TAG = "DisplayWeatherView";
    private static final boolean DEBUG = true;
    private int mCurPosition = 0;
    private DatabaseAction mDatabaseAction;
    private String languageDefault;
    private ContentValues mContentValues;

    private View switcherItem;
    private View weatherOtherInfo;
    private View currentWeather;

    private int[] tempArray;
    private int[] weatherIconArray;
    private String cityName;

    Typeface mTypeface = Typeface.createFromAsset(getContext().getAssets(), "helvetica-neue-lt.ttf");


    public DisplayWeatherView(WeatherMain activity, int pos) {
        super(activity);

        mCurPosition = pos;
        mDatabaseAction = new DatabaseAction(mParentActivity);
        languageDefault = Locale.getDefault().getLanguage();

        nowLoadData();
        reloadWebData(true);

        switcherItem = LayoutInflater.from(activity).inflate(R.layout.weather_main_switcher_item, this);

        weatherOtherInfo = switcherItem.findViewById(R.id.cur_weather_other_info_container);
        currentWeather = switcherItem.findViewById(R.id.cur_weather_container);
        weatherOtherInfo.setOnClickListener(this);
        currentWeather.setOnClickListener(this);

        initViews();
    }

    private void initViews(){
        initOtherInfo(switcherItem);
        initCityAndCurrentWeather(switcherItem);
        initWeatherSummaryOfToday(switcherItem);
        initTodayHoursWeather(switcherItem);

        for(int i=0; i<5; i++){
            initWeatherSummaryNextDay(switcherItem.findViewById(R.id.weather_summary_next_day_1 + i), i+1);
        }
    }

    private void initOtherInfo(View pagerItem){
        View view;
        TextView textKey;
        TextView textValue;
        String str;

        str = (String) mContentValues.get(Weather_Column.CURRENT_HUMIDITY);
        view = pagerItem.findViewById(R.id.weather_humidity);
        textKey = (TextView)view.findViewById(R.id.weather_other_info_key);
        textKey.setText(R.string.humidity);
        textValue = (TextView)view.findViewById(R.id.weather_other_info_value);
        textValue.setText(str);

        str = (String) mContentValues.get(Weather_Column.CURRENT_WINDDIRECTION);
        str = str + " " + (String) mContentValues.get(Weather_Column.CURRENT_WINDSPEED) + "m/s";
        view = pagerItem.findViewById(R.id.weather_wind);
        textKey = (TextView)view.findViewById(R.id.weather_other_info_key);
        textKey.setText(R.string.wind);
        textValue = (TextView)view.findViewById(R.id.weather_other_info_value);
        textValue.setText(str);

        str = (String) mContentValues.get(Weather_Column.CURRENT_VISIBILITY) + "km";
        view = pagerItem.findViewById(R.id.weather_visibility);
        textKey = (TextView)view.findViewById(R.id.weather_other_info_key);
        textKey.setText(R.string.visibility);
        textValue = (TextView)view.findViewById(R.id.weather_other_info_value);
        textValue.setText(str);

        str = (String) mContentValues.get(Weather_Column.CURRENT_REALFEEL);
        int metric = (Integer) mContentValues.get(Weather_Column.METRIC);
        if(MetricCheckButton.F_ON == metric){
            int temperature = Integer.parseInt(str);
            str = Utils.getFTmep(temperature);
        }
        str = str + getResources().getString(R.string.degree_label);
        view = pagerItem.findViewById(R.id.weather_feels);
        textKey = (TextView)view.findViewById(R.id.weather_other_info_key);
        textKey.setText(R.string.feelslike);
        textValue = (TextView)view.findViewById(R.id.weather_other_info_value);
        textValue.setText(str);
    }

    private void initCityAndCurrentWeather(View pagerItem){
        TextView textView;
        String str;

        textView = (TextView)pagerItem.findViewById(R.id.city_name);
        textView.setText(getCityName());

        str = (String) mContentValues.get(Weather_Column.CURRENT_WEATHERTEXT);
        textView = (TextView)pagerItem.findViewById(R.id.cur_weather_text);
        textView.setText(getWeatherText(str));

        str = String.valueOf(tempArray[0]);
        textView = (TextView)pagerItem.findViewById(R.id.cur_temperature);
        textView.setTypeface(mTypeface);
        textView.setText(str);
    }

    private void initWeatherSummaryOfToday(View pagerItem){
        TextView textView;
        String str;

        str = String.valueOf(mArrayDayOfWeek[0]);
        textView = (TextView)pagerItem.findViewById(R.id.today_day_name);
        textView.setText(str);

        str = String.valueOf(tempArray[2 * 0 + 1]);
        textView = (TextView)pagerItem.findViewById(R.id.today_high_temperature);
        textView.setText(str);

        str = String.valueOf(tempArray[2 * 0 + 2]);
        textView = (TextView)pagerItem.findViewById(R.id.today_low_temperature);
        textView.setText(str);
    }

    private void initWeatherSummaryNextDay(View summaryView, int idx){
        TextView textView;
        ImageView imageView;
        Bitmap bitmap = getDrawBitmap(weatherIconArray[idx + 1]);
        String str;

        str = String.valueOf(mArrayDayOfWeek[idx]);
        textView = (TextView)summaryView.findViewById(R.id.day_name);
        textView.setText(str);

        imageView = (ImageView)summaryView.findViewById(R.id.weather_icon);
        imageView.setImageBitmap(bitmap);

        str = String.valueOf(tempArray[2 * idx + 1]);
        textView = (TextView)summaryView.findViewById(R.id.high_temperature);
        textView.setText(str);

        str = String.valueOf(tempArray[2 * idx + 2]);
        textView = (TextView)summaryView.findViewById(R.id.low_temperature);
        textView.setText(str);
    }

    private void initTodayHoursWeather(View pagerItem){
        View itemView;
        TextView timeView, temperatureView;
        ImageView iconView;
        String str;
        int max_v = tempArray[1];
        int min_v = tempArray[2];
        double delta;
        Bitmap bitmap = getDrawBitmap(weatherIconArray[0]);
        Bitmap bitmapNextDay = getDrawBitmap(weatherIconArray[1 + 1]);
        final int HOURS_IN_DAY = 24;
        int i;
        int todayTempArray[] = new int[HOURS_IN_DAY];

        //String localTime = (String) mContentValues.get(Weather_Column.LOCAL_TIME);
        Date curDate = new Date(System.currentTimeMillis());
        //int hourIdx = Utils.getHour(localTime);
        int hourIdx = curDate.getHours();

        if(min_v > max_v){
            int tmp;
            tmp = min_v;
            min_v = max_v;
            max_v = tmp;
        }
        delta = (max_v - min_v) * 2.0 / HOURS_IN_DAY ;

        for(i=0; i<12 ; i++){
            todayTempArray[i] = min_v + (int)Math.abs(i * delta);
        }
        for(i=12; i<24; i++){
            todayTempArray[i] = max_v - (int)Math.abs((i-12) * delta);
        }

        for( i = 0; i < 13; i++){
            if(0 == i){
                str = getResources().getString(R.string.now);
            }else{
                str = String.format("%02d:00", hourIdx % HOURS_IN_DAY);
            }

            itemView = pagerItem.findViewById(R.id.hours_weather_0 + i);

            timeView = (TextView)itemView.findViewById(R.id.hours_weather_time);
            timeView.setTypeface(mTypeface);
            timeView.setText(str);
            temperatureView = (TextView)itemView.findViewById(R.id.hours_weather_temperature);
            // Temperature is offset by one hour, because the lowest temperature is at 1:00 but not 0:00
            temperatureView.setText(String.valueOf(todayTempArray[(hourIdx-1+24)%HOURS_IN_DAY]));
            iconView = (ImageView)itemView.findViewById(R.id.hours_weather_icon);
            if(hourIdx > 23){
                iconView.setImageBitmap(bitmapNextDay);
            } else {
                iconView.setImageBitmap(bitmap);
            }

            hourIdx++;
        }

    }


    public void onClick(View v) {

        if (v == weatherOtherInfo) {
            weatherOtherInfo.setVisibility(GONE);
            weatherOtherInfo.setClickable(false);
            currentWeather.setVisibility(VISIBLE);
            currentWeather.setClickable(true);
        } else if (v == currentWeather) {
            currentWeather.setVisibility(GONE);
            currentWeather.setClickable(false);
            weatherOtherInfo.setVisibility(VISIBLE);
            weatherOtherInfo.setClickable(true);
        }

    }


    public void nowLoadData() {
        if (mParentActivity == null) {
            return;
        }

        loadDataFromDB();
    }

    public void onWeatherDataDownloaded() {
        if (mParentActivity == null) {
            return;
        }

        loadDataFromDB();
        initViews();
        invalidate();
    }

    private void loadDataFromDB(){
        Cursor cursor = mDatabaseAction.queryAll();
        Log.i("Wonder", "loadDataFromDB(): cursor.count="+cursor.getCount());
        mContentValues = ContentValuesOperation.getContentValuesByPos(
                cursor, mCurPosition);
        tempArray = getTempArray(mContentValues);
        weatherIconArray = getWeatherIconArray(mContentValues);
        cityName = (String)mContentValues.get(Weather_Column.CITY);
        cursor.close();
    }


    // 重新下载网络数据
    public void reloadWebData(boolean isnowu) {
        long currentMillis = System.currentTimeMillis();
        long lastRefreshTime = 0l;

        if(mContentValues == null){
            return;
        }

        try {
            lastRefreshTime = Long.parseLong((String) mContentValues
                    .get(Weather_Column.REFRESH_TIME));
        } catch (Exception e) {
            Log.e(TAG, "reloadWebData ..  refresh_time = null");
            lastRefreshTime = System.currentTimeMillis();
        }
        long timeDelta = currentMillis - lastRefreshTime;
        // long hourMillis = DateUtils.MINUTE_IN_MILLIS;
        long hourMillis = DateUtils.HOUR_IN_MILLIS;
        // 如果两次刷新时间间隔大于
        // 一个小时则更新数据
        if (timeDelta > hourMillis || isnowu) {
            Log.i("Wonder", "Updating from NetWork: timeDelta="+timeDelta
                    +", hourMillis="+hourMillis+", isnowu="+isnowu);
            int id = (Integer) mContentValues.get(Weather_Column._ID);
            Uri uri = ContentUris.withAppendedId(
                    Weather.Weather_Column.CONTENT_URI, id);
            String location = (String) mContentValues
                    .get(Weather_Column.LOCTION);
            // if (DEBUG) {
            // Log.e(TAG,
            // " timeDelta > hourMillis ...you  should reloadWebData  location = "
            // + location);
            // }
            WebAction webAction = new WebAction(mParentActivity, uri, location,
                    1);
            webAction.setWebDownLoadListener(this);
            webAction.startLoadData();
        }
    }

    @Override
    public void onStartWebDownLoad() {
    }

    @Override
    public void onFinishWebDownLoad() {
        post(new Runnable() {
            @Override
            public void run() {
                onWeatherDataDownloaded();
            }
        });
    }


    public void drawBackground(){
        Window window = mParentActivity.getWindow();
        String localTime = (String) mContentValues.get(Weather_Column.LOCAL_TIME);
        if (Utils.isDayTime(localTime)) {
            window.setBackgroundDrawableResource(R.drawable.startbg);
        } else {
            window.setBackgroundDrawableResource(R.drawable.startbg_night);
        }
    }


    private String getCityName(){
        String city;

        if (languageDefault == "zh"
                || languageDefault.equals("zh")) {
            String cityCn = (String) mContentValues
                    .get(Weather_Column.CITY_CN);
            if (cityCn.equals(Utils.NO_CHINISE_CITY)) {
                city = cityName;
            } else {
                city = cityCn;
            }

        } else {
            city = cityName;
        }

        return city;
    }

    public int getPositionInDB(){
        return mDatabaseAction.getCityPosition(cityName);
    }

    public int getCurPosition(){
        return mCurPosition;
    }

    final HashMap<String, Integer> mMapWeatherText = new HashMap<String, Integer>(){
        {
            put("Sunny", R.string.weathertext_sunny);
            put("Clear", R.string.weathertext_clear);
            put("Misty", R.string.weathertext_misty);
            put("Drizzle", R.string.weathertext_drizzle);
            put("fine", R.string.weathertext_fine);
            put("fair", R.string.weathertext_fair);
            put("cloudy", R.string.weathertext_cloudy);
            put("foggy", R.string.weathertext_foggy);
            put("Rain", R.string.weathertext_rain);
            put("Haze", R.string.weathertext_haze);
            put("Overcast", R.string.weathertext_overcast);
            put("Mostly Cloudy", R.string.weathertext_mostly_cloudy);
            put("Hail", R.string.weathertext_hail);
            put("Snow", R.string.weathertext_snow);
            put("Heavy Snow", R.string.weathertext_heavy_snow);
            put("Light Snow", R.string.weathertext_light_snow);
            put("Partly Cloudy", R.string.weathertext_partly_cloudy);
            put("Some Clouds", R.string.weathertext_some_clouds);
            put("Mostly Clear", R.string.weathertext_mostly_clear);
            put("Partly Sunny", R.string.weathertext_partly_sunny);
            put("Light Rain", R.string.weathertext_light_rain);
        }
    };

    private String getWeatherText(String englishText) {
        String key;
        int value;

        for (Map.Entry<String, Integer> entry: mMapWeatherText.entrySet()) {
            key = entry.getKey();

            if(englishText.equalsIgnoreCase(key)){
                value = entry.getValue();
                return getResources().getString(value);
            }
        }

        return englishText;
    }

}
