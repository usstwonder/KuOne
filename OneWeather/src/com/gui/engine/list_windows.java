package com.gui.engine;

import android.graphics.Rect;

public class list_windows extends windows{

	private static final String TAG = "list_windows";
	private static final boolean DEBUG = true;	
	public list_windows()
	{
		super(null); 
	}
	public  list_windows(Rect r)
	{
		super(r);
		 
	}
	public int On_Touch_Move(int x,int y)
	{
		return 0;
	}
	public int On_Touch_Up(int x,int y)
	{
		return 0;
	}
	public int On_Touch_Down(int x,int y)
	{
		return 0;
	}
	public int Touch(int state,int x,int y)
	{
		windows left = son;
		while(left != null)
		{
			left = left.brer;
		}
		return -1;
	}
}
