package com.dlh4fx.ghosthunter;
import java.util.Random;

import com.dlh4fx.ghosthunter.SurfaceViewExample.OurView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;



public class Bolt {

	int x, y; 
	int destX, destY;
	int xSpeed, ySpeed;
	int height, width;
	int direction;
	Bitmap b;
	OurView ov;
	int currentFrame = 0;
	int waitFrame = 0;
	Rect dst;
	boolean stop = false;


	public Bolt(OurView ourView, Bitmap user, int startx, int starty, int dir) {
		// TODO Auto-generated constructor stub
		b = user;
		ov = ourView;
		direction = dir;

		int extraX = 0;
		int extraY = 0;
		
		// 0 down
		// 1 left
		// 2 right
		// 3 up

		// 4 rows
		height = b.getHeight() / 7;
		width = (b.getWidth() / 7);
		switch(direction){	
		case 0: // DOWN
			xSpeed = 0;
			ySpeed = 10;
			extraY = 50;
			break;
		case 1: // LEFT
			xSpeed = -10;
			ySpeed = 0;
			extraX = -30;
			extraY = 20;
			break;
		case 2: // RIGHT
			xSpeed = 10;
			ySpeed = 0;
			extraX = 30;
			extraY = 20;
			break;
		case 3: // UP
			xSpeed = 0;
			ySpeed = -10;
			extraY = -50;
			break;
		}
		
		x = startx + extraX;
		y = starty + extraY;

	}

	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		int srcY = height * 3;
		int srcX = currentFrame * width;
		Rect src = new Rect (srcX , srcY, srcX + width, srcY + height);
		dst = new Rect ( x, y, x + width, y + height);
		canvas.drawBitmap(b, src, dst, null);

	}

	public void update() {
		this.x += this.xSpeed;
		this.y += this.ySpeed;
	}

	public int getCurrentFrame(){
		return currentFrame;
	}
	
	public void incCurrentFrame()
	{
		waitFrame++;
		waitFrame = waitFrame % 15;
		int cf1 = waitFrame / 5;
		currentFrame = cf1;
	}
	
	public boolean checkHit (Rect ghostBox)
	{
		if (ghostBox != null && dst != null){
			Rect temp = new Rect(dst.centerX() - 5, dst.centerY() - 5, dst.centerX() + 5, dst.centerY() + 5);
			return ghostBox.intersect(temp);
		}
		return false;
	}
	
	public boolean isOutsideFrame(Canvas c)
	{
		if (x < 0 || y < 0  || y + height > c.getHeight() || x + width > c.getWidth())	
			return true;
		
		return false;
	}
	
}
