package com.gui.engine;

import android.graphics.Rect;

public class windows {
	
	public windows son;//儿子
	public windows brer;//弟弟
	public windows Brother;//哥哥	
	private Rect rect;	
	public windows(Rect r)
	{
		rect = r;
	}
	public static windows Cread(int top,int left,int right,int bottom)
	{
		Rect tem;
		tem = new Rect(top, left, right, bottom);
		windows wnd = new windows(tem);		
		return wnd;
	}

}
