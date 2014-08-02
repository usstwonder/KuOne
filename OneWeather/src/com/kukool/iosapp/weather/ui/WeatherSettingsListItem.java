package com.kukool.iosapp.weather.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.kukool.iosapp.weather.activity.WeatherSettings;
import com.kukool.iosapp.weather.R;


public class WeatherSettingsListItem extends RelativeLayout implements
		OnClickListener {

	private final static boolean DEBUG = false;
	private final static String TAG = "WeatherSettingsListItem";

	/** 左边圆圈TAG */
	private final static int TAG_SMSLEFT_BUTTON = 0;
	/** 删除按钮TAG */
	private final static int TAG_DELETE_BUTTON = 1;

    private final static int TAG_CITY_BUTTON = 2;
    private final static int GESTURE_SCROLL_X_THRESHOLD = 40;

	private final static long ANIMATION_DURATION = 250;

	/** 左边圆圈 */
//	private SmsLeftYuan mMinus;
	/** 删除按钮 */
	private View mDelete;
	/** 删除item使用的Handler */
	private Handler mHandler;

    private View mCity;

    /** 是否显示删除按钮 true 显示；false 不显示 */
	private boolean isDeleteBtnShow = false;
	/** item id */
	private int mItemId;
    private Context mContext;
    private GestureDetector mGestureDetectorDelete;


    public WeatherSettingsListItem(Context context) {
		super(context);
        mContext = context;
	}

	public WeatherSettingsListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
        mContext = context;
	}

	public WeatherSettingsListItem(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
        mContext = context;
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();

//		mMinus = (SmsLeftYuan) this.findViewById(R.id.img_iphone_item_mius);
//		mMinus.setVisibility(View.VISIBLE);
//		mMinus.setTag(TAG_SMSLEFT_BUTTON);
//		mMinus.setOnClickListener(this);
		
		mDelete = (Button) findViewById(R.id.del_button);
		mDelete.setTag(TAG_DELETE_BUTTON);

        mCity = findViewById(R.id.city);
        //mCity.setOnClickListener(this);
        mCity.setTag(TAG_CITY_BUTTON);

        mGestureDetectorDelete = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            public boolean onScroll(android.view.MotionEvent e1, android.view.MotionEvent e2, float distanceX, float distanceY) {
                int deltaX = (int)(e1.getX() - e2.getX());
//                int deltaY = (int) e1.getY() - (int) e2.getY();
                int absDeltaX = Math.abs(deltaX);

//                Log.i("Wonder", "onScroll, distanceX="+distanceX+", deltaX="+deltaX);

                if (absDeltaX > GESTURE_SCROLL_X_THRESHOLD){
                    if(deltaX > 0){
                        switchDeleteMode(true);
                    }else{
                        switchDeleteMode(false);
                    }
                }
                return true;
                //return false;
            }


            public boolean onSingleTapUp(android.view.MotionEvent e) {
                Log.i("Wonder", "onSingleTapUp");
                cityPressed();
                return true;
            }

            public void onLongPress(android.view.MotionEvent e) {
                Log.i("Wonder", "onLongPress");
            }


            public boolean onFling(android.view.MotionEvent e1, android.view.MotionEvent e2, float velocityX, float velocityY) {
                Log.i("Wonder", "onFling");
                int x1 = (int)e1.getX();
                int x2 = (int)e2.getX();

                if( x1 > x2){
                    switchDeleteMode(true);
                }else{
                    switchDeleteMode(false);
                }

                return true;
            }

            public void onShowPress(android.view.MotionEvent e) {
                Log.i("Wonder", "onShowPress");
            }

            public boolean onDown(android.view.MotionEvent e) {
                Log.i("Wonder", "onDown");
                return true;
            }

//            public boolean onDoubleTap(android.view.MotionEvent e) {
//                Log.i("Wonder", "onDoubleTap");
//                return false;
//            }

//            public boolean onDoubleTapEvent(android.view.MotionEvent e) {
//                Log.i("Wonder", "onDoubleTapEvent");
//                return false;
//            }

//            public boolean onSingleTapConfirmed(android.view.MotionEvent e) {
//                Log.i("Wonder", "onSingleTapConfirmed");
//                return true;
//            }
        });

	}

    public boolean onTouchEvent(android.view.MotionEvent event) {
//        WeatherSettingsListView parent = (WeatherSettingsListView) getParent();
        //Log.i("Wonder", "ListItem: mGestureDetectorDelete.onTouchEvent(ev)");
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if(isInDeleteMode()){
                    return false;
                }
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        boolean bRet = mGestureDetectorDelete.onTouchEvent(event);
        if(! bRet){
//            Log.i("Wonder", "ListItem: mGestureDetectorDelete.onTouchEvent(ev) return false");
        }

        return bRet;
    }


    /**
	 * 删除或取消删除
	 */
	public void showDeleteBtn() {
		TranslateAnimation anim;
//		Animation animRoteZ;
		
		if (!isDeleteBtnShow) { // 左边圆圈转动90度，同时右边删除按钮出来

			isDeleteBtnShow = true;
			mDelete.setClickable(true);
			WeatherSettingsListView parent = (WeatherSettingsListView) getParent();
			if (parent == null) return;
			parent.setMSelectedItem(this);
			anim = new TranslateAnimation(mDelete.getMeasuredWidth(), 0, 0, 0);
			anim.setDuration(ANIMATION_DURATION);
//			animRoteZ = new AnimationRotateZ(90, false);
//			animRoteZ.setDuration(ANIMATION_DURATION);
//			animRoteZ.setFillAfter(true);

//			mMinus.toRotate(animRoteZ);

			mDelete.clearAnimation();
			mDelete.startAnimation(anim);
			mDelete.setVisibility(View.VISIBLE);

            if (mHandler != null) {
                Message msg = mHandler.obtainMessage(WeatherSettings.MESSAGE_CHANGE_DELETE_MODE);
                msg.obj = isDeleteBtnShow;
                mHandler.sendMessage(msg);
            }
		}
	}

    public void hideDeleteBtn() {
        TranslateAnimation anim;
//        Animation animRoteZ;

        if (isDeleteBtnShow) { // 左边圆圈转动90度，同时右边删除按钮隐藏
            isDeleteBtnShow = false;
            mDelete.setClickable(false);
            WeatherSettingsListView parent = (WeatherSettingsListView) getParent();
            if (parent == null) return;
            parent.setMSelectedItem(null);
            anim = new TranslateAnimation(0, mDelete.getMeasuredWidth(), 0, 0);
            anim.setDuration(ANIMATION_DURATION);
//            animRoteZ = new AnimationRotateZ(90, true);
//            animRoteZ.setDuration(ANIMATION_DURATION);
//            animRoteZ.setFillAfter(true);

//            mMinus.toRotate(animRoteZ);

            mDelete.clearAnimation();
            //mDelete.startAnimation(anim);
            mDelete.setVisibility(View.GONE);

            if (mHandler != null) {
                Message msg = mHandler.obtainMessage(WeatherSettings.MESSAGE_CHANGE_DELETE_MODE);
                msg.obj = isDeleteBtnShow;
                mHandler.sendMessage(msg);
            }
        }
    }


    public void cityPressed() {
        WeatherSettingsListView parent = (WeatherSettingsListView) getParent();
        int id = getItemId();
        Log.i("Wonder", "cityPressed(): " + id);

        ((WeatherSettings)mContext).backtoWeatherofCity(id);

    }

	public void onClick(View v) {
		// TODO Auto-generated method stub
        clearDeleteMode();

		int tag = (Integer) v.getTag();
		switch (tag) {
		case TAG_SMSLEFT_BUTTON: // 左边圆圈事件处理
            //switchDeleteMode();
            break;

        case TAG_CITY_BUTTON:
            cityPressed();
            break;
        }

	}

	public void setMHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}

	public int getItemId() {
		return mItemId;
	}

	public void setItemId(int itemId) {
		mItemId = itemId;
	}

    private void switchDeleteMode(boolean isOn){
        WeatherSettingsListView parent = (WeatherSettingsListView) getParent();
        if (parent == null)
        {
            return;
        }

        if(isDeleteBtnShow == isOn){
            return;
        }

        if (parent.getMSelectedItem() != null)
        {
                parent.getMSelectedItem().hideDeleteBtn();
        }
        else
        {
            if(isOn){
                showDeleteBtn();
            }else{
                hideDeleteBtn();
            }
        }
    }

    private boolean isInDeleteMode(){
        WeatherSettingsListView parent = (WeatherSettingsListView) getParent();

        if ( (parent != null) && (parent.getMSelectedItem() != null) )
        {
            return true;
        }

        return false;
    }

    private void clearDeleteMode(){
        WeatherSettingsListView parent = (WeatherSettingsListView) getParent();

        if ( (parent != null) && (parent.getMSelectedItem() != null) )
        {
            parent.getMSelectedItem().hideDeleteBtn();
        }
    }

    public void init() {
        mDelete.setClickable(false);
        mDelete.setVisibility(View.GONE);
        isDeleteBtnShow = false;
    }

}
