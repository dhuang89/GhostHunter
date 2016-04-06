package com.dlh4fx.ghosthunter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.dlh4fx.ghosthunter.SurfaceViewExample.OurView;

public class coin implements pickupObj {
	
	
	int x, y; 
	int height, width;
	int direction;
	Bitmap b;
	OurView ov;
	int currentFrame = 0;
	int waitFrame = 0;
	Rect dst;
	
	public coin (OurView ourView, Bitmap user, int startx, int starty, Canvas c) {
		// TODO Auto-generated constructor stub
		b = user;
		ov = ourView;
		// 4 rows
		height = b.getHeight();
		width = (b.getWidth() / 10);
		x = startx;
		y = starty;

	}

	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		int srcY = direction * height;
		int srcX = currentFrame * width;
		Rect src = new Rect (srcX , srcY, srcX + width, srcY + height);
		dst = new Rect ( x, y, x + width, y + height);
		canvas.drawBitmap(b, src, dst, null);

	}
	
	@Override
	public Rect getDst() {
		return dst;
	}

	@Override
	public void incCurrentFrame()
	{
		waitFrame++;
		waitFrame = waitFrame % 100;
		int cf1 = waitFrame / 10;
		currentFrame = cf1;
	}	

}
