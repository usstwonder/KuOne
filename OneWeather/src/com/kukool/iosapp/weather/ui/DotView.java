package com.kukool.iosapp.weather.ui;
/**
 * 2011-5-13
 * 天气显示 下面
 * 用来标记位置的点
 */
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

//import com.kukool.iphone.app.IphonePhoneInfo;
import com.kukool.iosapp.weather.activity.WeatherMain;
import com.kukool.iosapp.weather.R;
import com.kukool.iosapp.weather.model.DatabaseAction;

public class DotView extends View {

    private static final String TAG = "DotView";
    private static final boolean DEBUG = true;

    protected WeatherMain mParentActivity;

    private static Bitmap mDotGray;
    private static Bitmap mDotArrow;
    private static Bitmap mDotWhite;

//	private static float mScale = 0;

//	private static int BITMAP_WIDTH = 10;
//	private static int BITMAP_HEIGHT = 10;
//	private static int BASE_HEIGHT = 450;

    //private IphonePhoneInfo phoneInfo = null;


    public DotView(WeatherMain activity) {
        super(activity);
        init(activity);
    }

    public DotView(Context context, AttributeSet attrs) {
//        this( (DisplayWeatherAndSettings)context );
        super(context, attrs);
        init( (WeatherMain)context );
    }

    private void init(WeatherMain activity){
        mParentActivity = activity;
        initRes();
    }

    private void initRes() {
        Resources res = mParentActivity.getResources();
//		if (mScale == 0) {
//			mScale = res.getDisplayMetrics().density;
//			if (mScale != 1) {
//				BITMAP_WIDTH *= mScale;
//				BITMAP_HEIGHT *= mScale;
//				BASE_HEIGHT *= mScale;
//			}
//		}

        if (mDotGray ==null) {
            mDotGray = BitmapFactory.decodeResource(res, R.drawable.dot_gray);
            //mDotGray = Bitmap.createScaledBitmap(dotGray, BITMAP_WIDTH, BITMAP_HEIGHT, false);
        }
        if (mDotWhite ==null) {
            mDotWhite = BitmapFactory.decodeResource(res, R.drawable.dot_white);
            //mDotWhite = Bitmap.createScaledBitmap(dotWhite, BITMAP_WIDTH, BITMAP_HEIGHT, false);
        }
        if(mDotArrow == null)
        {
            mDotArrow = BitmapFactory.decodeResource(res, R.drawable.dot_way);
            //mDotArrow = Bitmap.createScaledBitmap(dotWhite, BITMAP_WIDTH, BITMAP_HEIGHT, false);

        }
    }
    //	public boolean onTouchEvent(MotionEvent ev) {
//		DisplayWeatherView currentView = mParentActivity.getCurrentView();
//		currentView.onTouchEvent(ev);
//		//System.out.println("get phone info:"+phoneInfo.getInfo());
//		return true;
//	}
    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        //当增加或减少城市的时候
//        DisplayWeatherView currentView = mParentActivity.getCurrentView();
//        int currentPos = currentView.getCurrentPosition();// 其实记录的是上次的位置
        int currentPos = mParentActivity.getmPosition();// 其实记录的是上次的位置
        // int curCount = mParentActivity.mCursor.getCount();
        int curCount = new DatabaseAction(mParentActivity).getCursorCount();
        // 如果当前位置是最大位置或者大于最大位置
        // (做删除动作的时候会出现这种情况)
        // 这时将当前位置移到最大位置
        if (curCount > 0) {
            if (currentPos >= curCount) {
                currentPos = curCount - 1;
            }
        }
//		int y = (int)this.getTop();
//		int hhh = canvas.getHeight();
        int top;

        //BASE_HEIGHT = ((hhh - currentView.ViewRect.bottom) - mDotGray.getHeight())/2;
        top = getPaddingTop();
//        Log.i("Wonder", "top = "+top);
//        top -=  mDotGray.getHeight();

//        if(850 < currentView.lcd.heightPixels){
////            BASE_HEIGHT -= 70;
//        }else{
////            BASE_HEIGHT -= 60;
//        }

//		if(canvas.getHeight() == 854)
//		BASE_HEIGHT = currentView.ViewRect.bottom + 50;
//		else
//			BASE_HEIGHT = currentView.ViewRect.bottom + 30;
        // 当只有一个城市的时候不显示dot
        if (curCount <= 1)
        {
            return;
        }
        int d_BITMAP_WIDTH;

        d_BITMAP_WIDTH = mDotGray.getWidth() + 10;
        int basePaddingLeft = (getWidth() - curCount *  d_BITMAP_WIDTH + d_BITMAP_WIDTH) / 2;
        // 灰色图片
        int grayPaddingLeft = basePaddingLeft;


        int w =  mDotGray.getWidth();
        int h =  mDotGray.getHeight();
        for(int i = 0; i< curCount; i++)
        {
            canvas.drawBitmap(mDotGray, grayPaddingLeft, top + 2, null);
            grayPaddingLeft = grayPaddingLeft + d_BITMAP_WIDTH;
        }
        // 白色图片
        int whitePaddingLeft = basePaddingLeft +  currentPos * d_BITMAP_WIDTH;
        canvas.drawBitmap(mDotWhite, whitePaddingLeft, top + 2, null);
        //箭头
        canvas.drawBitmap(mDotArrow, basePaddingLeft - d_BITMAP_WIDTH - 2, top, null);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredHeight = mDotArrow.getHeight() + getPaddingTop() + getPaddingBottom();
        int measuredWidth = measureWidth(widthMeasureSpec);

        setMeasuredDimension(measuredWidth, measuredHeight);
    }



    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result = specSize;
        if (specMode == MeasureSpec.AT_MOST){
            result = specSize;
        }
        else if (specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }

        return result;
    }

}


