package com.kukool.iosapp.weather.anim;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
/**
 * 图片原地转动，参数distance是转动的角度，90表示90度。
 * @author lihong 原创
 *
 */
public class AnimationRotateZ  extends Animation{
    private float mCenterX ;						//记录View的中间坐标  
    private float mCenterY ;
    private float mDistance;
    private Camera mCamera = new Camera(); 
    private boolean mResume = false;
    public AnimationRotateZ(float distance, boolean resume) {  
    	mDistance = distance;
    	mResume = resume;
    }  
    

    public void initialize(int width, int height, int parentWidth,  
           int parentHeight) {  
        super.initialize(width, height, parentWidth, parentHeight);  
        //初始化中间坐标值  
        mCenterX = width*0.5f;   
        mCenterY = height*0.5f;  
        //setDuration(160);  
        //setFillAfter(true);  
        setInterpolator(new AccelerateDecelerateInterpolator());  
        

    }  

      
    protected void applyTransformation(float interpolatedTime,  
           Transformation t) {   
        final Matrix matrix = t.getMatrix(); 
        mCamera.save();  
        if (mResume) {
        	 mCamera.rotateZ(mDistance - mDistance*interpolatedTime);
        } else {  	
        	mCamera.rotateZ(mDistance*interpolatedTime);
        }
        mCamera.getMatrix(matrix);  
        matrix.preTranslate(-mCenterX, -mCenterY);  
        matrix.postTranslate(mCenterX, mCenterY);  
        mCamera.restore();  
    }  
	
	
}
