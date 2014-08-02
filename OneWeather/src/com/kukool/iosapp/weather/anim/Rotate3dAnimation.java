package com.kukool.iosapp.weather.anim;


import com.kukool.iosapp.weather.R;

import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Matrix;


public class Rotate3dAnimation extends Animation {
	
	private static final String TAG = "Rotate3dAnimation";
	private final boolean DEBUG = true;
	
    private final float mFromDegrees;
    private final float mToDegrees;
    private final float mCenterX;
    private final float mCenterY;
    private final float mDepthZ;
    private final boolean mReverse;
    private Camera mCamera;

    public Rotate3dAnimation(float fromDegrees, float toDegrees,
            float centerX, float centerY, float depthZ, boolean reverse) {
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        mCenterX = centerX;
        mCenterY = centerY;
        mDepthZ = depthZ;
        mReverse = reverse;
    }

    public Rotate3dAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        TypedArray a =
            context.obtainStyledAttributes(attrs, R.styleable.Rotate3dAnimation);

        mFromDegrees = a.getFloat(R.styleable.Rotate3dAnimation_fromDegrees, 1.0f);
        mToDegrees = a.getFloat(R.styleable.Rotate3dAnimation_toDegrees, 1.0f);
        mCenterX = a.getFloat(R.styleable.Rotate3dAnimation_centerX, 1.0f);
        mCenterY = a.getFloat(R.styleable.Rotate3dAnimation_centerY, 1.0f);
        mDepthZ = a.getFloat(R.styleable.Rotate3dAnimation_depthZ, 310.0f);
        mReverse = a.getBoolean(R.styleable.Rotate3dAnimation_reverse, false);
		// if (DEBUG) {
		// Log.e(TAG, "---------------------->Rotate3dAnimation") ;
		// Log.e(TAG, "Rotate3dAnimation mFromDegrees = " + mFromDegrees);
		// Log.e(TAG, "Rotate3dAnimation mToDegrees = " + mToDegrees);
		// Log.e(TAG, "Rotate3dAnimation mCenterX = " + mCenterX);
		// Log.e(TAG, "Rotate3dAnimation mCenterY = " + mCenterY);
		// Log.e(TAG, "Rotate3dAnimation mDepthZ = " + mDepthZ);
		// Log.e(TAG, "Rotate3dAnimation mReverse = " + mReverse);
		// }
        a.recycle();
    }
    
    
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
    }

	// Helper for getTransformation. Subclasses should implement this to apply
	// their transforms given an interpolation value.
	// 重写的animation子类都重写applyTransformation这个方法
	// 来定义动画具体的动作
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final float fromDegrees = mFromDegrees;
        float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

        final float centerX = mCenterX;
        final float centerY = mCenterY;
        final Camera camera = mCamera;

        final Matrix matrix = t.getMatrix();

        camera.save();
        
		// Log.e(TAG, "applyTransformation interpolatedTime = " +
		// interpolatedTime);
        
        // 画布的坐标原点 有向z轴里面移动
        // 或者从z轴移出来
        if (mReverse) {
            camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
        } else {
            camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
        }
        //围绕y轴旋转
        camera.rotateY(degrees);
        camera.getMatrix(matrix);
        // 中心点
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
        camera.restore();

    }
}

