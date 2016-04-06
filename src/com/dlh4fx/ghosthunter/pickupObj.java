package com.dlh4fx.ghosthunter;

import android.graphics.Canvas;
import android.graphics.Rect;

public interface pickupObj {
	public Rect getDst();
	public void onDraw(Canvas c);
	public void incCurrentFrame();

}
