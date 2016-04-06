package com.dlh4fx.ghosthunter;

import java.util.Random;

import com.dlh4fx.ghosthunter.SurfaceViewExample.OurView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


public class SpriteGhost {

	int x, y; 
	int destX, destY;
	int xSpeed, ySpeed;
	int height, width;
	int direction;
	Bitmap b;
	OurView ov;
	int currentFrame = 0;
	int waitFrame = 0;
	boolean stop = false;
	boolean reverse = false;
	Rect dst;


	public SpriteGhost(OurView ourView, Bitmap user, int startx, int starty, Canvas c,int speed) {
		// TODO Auto-generated constructor stub
		b = user;
		ov = ourView;
		// 4 rows
		height = b.getHeight() / 8;
		width = (b.getWidth() / 12) - 3;

		if (speed == 1){
			xSpeed = 1;
			ySpeed = 1;
		}
		else if(speed == 2){
			xSpeed = 2;
			ySpeed = 2;
		}
		else{
			xSpeed = 3;
			ySpeed = 3;
		}

		Random rand = new Random();
		int randDir = rand.nextInt(4) + 1;
		int randDis = rand.nextInt(8)*10 + 250;

		switch(randDir){
		case 1: // UP
			if (starty - 250 >= 0)
				y = starty - randDis;
			x = startx;
			break;
		case 2: // RIGHT
			if (startx + 250 <= c.getWidth() - 1)
				y = starty;
			x = randDis + startx;
			break;
		case 3: // DOWN
			if (starty + 250 <= c.getHeight() - 1)
				y = randDis + starty;
			x = startx;
			break;
		case 4: // LEFT
			if (startx - 250 >= 0)
				x = startx - randDis;
			y= starty;
			break;
		}


		direction = 0;
	}

	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		int srcY = direction * height + 2;
		int srcX = currentFrame * width + 3;
		Rect src = new Rect (srcX , srcY, srcX + width, srcY + height);
		dst = new Rect ( x, y, x + width, y + height);
		canvas.drawBitmap(b, src, dst, null);

	}

	public void update(int x1, int y1) {
		// TODO Auto-generated method stub
		//destX = x1-24;
		//destY = y1-40;

		int difx = Math.abs(destX - this.x);
		int dify = Math.abs(destY - this.y);

		int dist = (int) Math.sqrt((difx * difx) + (dify * dify));

		float dx = (float) (this.x - x1);
		float dy = (float) (this.y - y1);

		if (Math.abs(dx) > Math.abs(dy))
			if (dx <= 0)
				direction = 1;
			else
				direction = 3;
		else
			if (dy <= 0)
				direction = 2;
			else
				direction = 0;
		
		if (reverse){
			if (dy > 0) {

				this.y += this.ySpeed;
			} else if (dy < 0) {

				this.y -= this.ySpeed;
			}

			if (dx > 0) {

				this.x += this.xSpeed;
			} else if (dx < 0) {

				this.x -= this.xSpeed;
			}
		}
		else if (!reverse){
			if (dy > 0) {

				this.y -= this.ySpeed;
			} else if (dy < 0) {

				this.y += this.ySpeed;
			}

			if (dx > 0) {

				this.x -= this.xSpeed;
			} else if (dx < 0) {

				this.x += this.xSpeed;
			}
		}

	}

	public void stop()
	{
		stop = true;
	}

	public void incCurrentFrame()
	{
		waitFrame++;
		waitFrame = waitFrame % 30;
		int cf1 = waitFrame / 10;
		currentFrame = cf1;
	}

	public boolean isOutsideFrame(Canvas c)
	{
		if (x < 0 || y < 0  || y + height > c.getHeight() || x + width > c.getWidth())	
			return true;

		return false;
	}

	public void setReverse()
	{
		reverse = true;
	}

	public int getCurrentFrame()
	{
		return currentFrame;
	}

	public Rect getDst()
	{
		return dst;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}
}

