package com.kukool.iosapp.weather.ui;

import com.kukool.iosapp.weather.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MetricCheckButton extends ViewGroup {
	
	private Button mLeftBtn;
	private Button mRightBtn;
	
	private Drawable mBackgroundOnLeft;
	private Drawable mBackgroundOnRight;
	
	private int mLeftButtonMeasureWidth;
	private int mLeftButtonMeasureHeight;
	private int mRightButtonMeasureWidth;
	private int mRightButtonMeasureHeight;
	
	private int mIsOn = C_ON;
	public static final int F_ON = 0;// 华氏
	public static final int C_ON = 1;// 摄氏
	
	private MetricCheckButtonStateChangeListener mListener;
	
	public MetricCheckButton(Context context) {
        this(context, null);
    }
	
	public MetricCheckButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MetricCheckButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.metricCheckButton, defStyle, 0);

		if (a == null) {
			return;
		}
		
		mBackgroundOnLeft = a.getDrawable(R.styleable.metricCheckButton_backgroundOnLeft);
		mBackgroundOnRight = a.getDrawable(R.styleable.metricCheckButton_backgroundOnRight);
		
		a.recycle();
		
		mLeftBtn = new Button(context);
		mLeftBtn.setBackgroundDrawable(null);
		mLeftBtn.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int on = getOn();
                    on ++;
					setOn(on % 2);
				}
				return false;
			}		
		});
		
		mRightBtn = new Button(context);
		mRightBtn.setBackgroundDrawable(null);
		mRightBtn.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int on = getOn();
                    on ++;
                    setOn(on % 2);
				}
				return false;
			}	
		});
		
		if (mIsOn == F_ON) {
			setBackgroundDrawable(mBackgroundOnLeft);
		} else {
			setBackgroundDrawable(mBackgroundOnRight);
		}
		this.addView(mLeftBtn);
		this.addView(mRightBtn);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mLeftBtn.layout(0, 0, mLeftButtonMeasureWidth, mLeftButtonMeasureHeight );
		mRightBtn.layout(mLeftButtonMeasureWidth, 0, mLeftButtonMeasureWidth + mRightButtonMeasureWidth, mRightButtonMeasureHeight );
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mLeftBackgroundDrawableWidth = mBackgroundOnLeft.getIntrinsicWidth() / 2;
		int mLeftBackgroundDrawableHeight = mBackgroundOnLeft.getIntrinsicHeight();
		int mRightBackgroundDrawableWidth = mBackgroundOnRight.getIntrinsicWidth() / 2;
		int mRightBackgroundDrawableHeight = mBackgroundOnRight.getIntrinsicHeight();
		
		mLeftButtonMeasureWidth = mLeftBackgroundDrawableWidth;
		mLeftButtonMeasureHeight = mLeftBackgroundDrawableHeight;
		mRightButtonMeasureWidth = mRightBackgroundDrawableWidth;
		mRightButtonMeasureHeight = mRightBackgroundDrawableHeight;
		
		int width = mLeftButtonMeasureWidth + mRightButtonMeasureWidth;
		int height = mLeftButtonMeasureHeight > mRightButtonMeasureHeight ? mLeftButtonMeasureHeight : mRightButtonMeasureHeight;
		setMeasuredDimension(width, height);
	}
	
	public void setMetricCheckButtonStateChangeListener(MetricCheckButtonStateChangeListener listener) {
		mListener = listener;
	}
	
	public void setOn(int which) {
		if (mIsOn == which) {
			return;
		}
		mIsOn = which;
		
		if (which == F_ON) {
			setBackgroundDrawable(mBackgroundOnLeft);
		} else if (which == C_ON) {
			setBackgroundDrawable(mBackgroundOnRight);
		}
		
		if (mListener != null) {
			mListener.onStateChange(MetricCheckButton.this, which);
		}
	}
	
	public int getOn() {
		return mIsOn;
	}

	/**
	 * MetricCheckButton状态改变时回调接口
	 * @param whichOn 改变check状态，处于活动的ID，只能是LEFT_ON和RIGHT_ON中的一种
	 */
	public interface MetricCheckButtonStateChangeListener {
		public void onStateChange(View v, int whichOn);
	}
}
