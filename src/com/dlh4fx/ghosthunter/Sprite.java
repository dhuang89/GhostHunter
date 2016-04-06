package com.dlh4fx.ghosthunter;

import java.util.ArrayList;

import com.dlh4fx.ghosthunter.SurfaceViewExample.OurView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


public class Sprite {

	int x, y; 
	int curHp;
	int destX, destY;
	int xSpeed, ySpeed;
	int height, width;
	int heightHrt, widthHrt;
	int direction;
	Bitmap b, hrt;
	OurView ov;
	int currentFrame = 0;
	Rect dst;


	public Sprite(OurView ourView, Bitmap user, Bitmap heart) {
		// TODO Auto-generated constructor stub
		b = user;
		hrt = heart;
		ov = ourView;
		// 4 rows
		height = b.getHeight() / 4;
		width = b.getWidth() / 4;
		heightHrt = heart.getHeight();
		widthHrt = heart.getWidth() / 2;
		x = 350;
		y = 450;
		xSpeed = 10;
		ySpeed = 10;
		direction = 0;
		curHp = 3;
	}

	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		int srcY = direction * height;
		int srcX = currentFrame * width;
		Rect src = new Rect (srcX , srcY, srcX + width, srcY + height);
		dst = new Rect ( x, y, x + width, y + height);
		canvas.drawBitmap(b, src, dst, null);
		
		dispHearts(canvas);
	}

	public void update(int x1, int y1, Bitmap cur) {
		// TODO Auto-generated method stub
		
		
		
		b = cur;
		destX = x1-24;
		destY = y1-40;

		int difx = Math.abs(destX - this.x);
		int dify = Math.abs(destY - this.y);

		int dist = (int) Math.sqrt((difx * difx) + (dify * dify));


		difx = Math.abs(destX - this.x);
		dify = Math.abs(destY - this.y);
		dist = (int) Math.sqrt((difx * difx) + (dify * dify));

		float dx = (float) (this.x - destX);
		float dy = (float) (this.y - destY);
		double absAngle = (Math.atan(Math.abs(dy) / Math.abs(dx)))
				* (180 / Math.PI);

		// 0 down
		// 1 left
		// 2 right
		// 3 up

		if (Math.abs(dx) > Math.abs(dy))
			if (dx <= 0)
				direction = 2;
			else
				direction = 1;
		else
			if (dy <= 0)
				direction = 0;
			else
				direction = 3;

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		currentFrame = ++currentFrame % 4;

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

	public boolean checkHit(Rect ghostBox){
		if (ghostBox != null && dst != null){
			Rect temp = new Rect(dst.centerX() - 5, dst.centerY() - 5, dst.centerX() + 5, dst.centerY() + 5);
			return ghostBox.intersect(temp);
		}
		return false;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getDirection() {
		return direction;
	}
	
	public int getCurHp(){
		return curHp;
	}
	
	public void setCurHp(int hp){
		curHp = hp;
	}
	
	public boolean isDead(){
		if (curHp == 0)
			return true;
		return false;
	}

	public boolean isClose(ArrayList<SpriteGhost> ghostList){
		
		for (SpriteGhost g : ghostList){
			if (Math.abs(x-g.getX()) < 150 && Math.abs(y-g.getY()) < 150){
				return true;
			}	
		}
		return false;
	}
	
	public void dispHearts(Canvas canvas){
		int srcYF = 0 * heightHrt;
		int srcXF = 0 * widthHrt;
		Rect srcF = new Rect (srcXF , srcYF, srcXF + widthHrt, srcYF + heightHrt);
		
		int srcYE = 0 * heightHrt;
		int srcXE = 1 * widthHrt;
		Rect srcE = new Rect (srcXE , srcYE, srcXE + widthHrt, srcYE + heightHrt);
		
		Rect dst1 = new Rect ( 0, 0, widthHrt, heightHrt);
		Rect dst2 = new Rect ( 5 + widthHrt, 0, 5 + 2 * widthHrt, heightHrt);
		Rect dst3 = new Rect ( 10 + 2 * widthHrt, 0, 10 + 3 * widthHrt, heightHrt);
		
		switch(curHp){
		case 3: 
			canvas.drawBitmap(hrt, srcF, dst1, null);
			canvas.drawBitmap(hrt, srcF, dst2, null);
			canvas.drawBitmap(hrt, srcF, dst3, null);
			break;
		case 2:
			canvas.drawBitmap(hrt, srcF, dst1, null);
			canvas.drawBitmap(hrt, srcF, dst2, null);
			canvas.drawBitmap(hrt, srcE, dst3, null);
			break;
		case 1:
			canvas.drawBitmap(hrt, srcF, dst1, null);
			canvas.drawBitmap(hrt, srcE, dst2, null);
			canvas.drawBitmap(hrt, srcE, dst3, null);
			break;
		
		}
	}

}
