package com.dlh4fx.ghosthunter;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

public class SurfaceViewExample extends Activity implements OnTouchListener{
	private static final int LONG_DELAY = 5000;
	OurView v;
	Sprite sprite;
	SpriteGhost ghost;
	Bitmap user, ghost1, boltPic, dungeonBG,scaled,invincUser, heart,points,alertMe, coins, bombs, repellentpic;
	float x, y;
	ArrayList<SpriteGhost> ghosts;
	ArrayList<Bolt> bolts;
	ArrayList<pickupObj> objs;
	boolean addBolt = false;
	boolean invinc = false;
	long invincTime;
	int scoreTime,scoreGhosts;
	MediaPlayer boltSpawn,ghostSpawn1,ghostDie1, ghostPunch1, repel1, heart1, coin1, bomb1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		v = new OurView(this);
		v.setOnTouchListener(this);
		user = BitmapFactory.decodeResource(getResources(), R.drawable.user1);
		heart = BitmapFactory.decodeResource(getResources(), R.drawable.heart4);
		invincUser = BitmapFactory.decodeResource(getResources(), R.drawable.user2);
		points = BitmapFactory.decodeResource(getResources(), R.drawable.points1);
		ghost1 = BitmapFactory.decodeResource(getResources(), R.drawable.zombie);
		dungeonBG = BitmapFactory.decodeResource(getResources(), R.drawable.dungeon5);
		boltPic = BitmapFactory.decodeResource(getResources(), R.drawable.bolt);
		alertMe = BitmapFactory.decodeResource(getResources(), R.drawable.alert);
		
		coins = BitmapFactory.decodeResource(getResources(), R.drawable.coins);
		bombs = BitmapFactory.decodeResource(getResources(), R.drawable.bomb);
		repellentpic = BitmapFactory.decodeResource(getResources(), R.drawable.repellent);
		
		scaled = Bitmap.createScaledBitmap(dungeonBG,800,1100,false);

		boltSpawn = MediaPlayer.create(SurfaceViewExample.this, R.raw.laser);
		ghostDie1 = MediaPlayer.create(SurfaceViewExample.this, R.raw.ghostdie);
		ghostPunch1 = MediaPlayer.create(SurfaceViewExample.this, R.raw.ghostpunch);
		ghostSpawn1 = MediaPlayer.create(SurfaceViewExample.this, R.raw.ghostspawn);
		
		repel1 = MediaPlayer.create(SurfaceViewExample.this, R.raw.repel);
		heart1 = MediaPlayer.create(SurfaceViewExample.this, R.raw.heart);
		coin1 = MediaPlayer.create(SurfaceViewExample.this, R.raw.coin);
		bomb1 = MediaPlayer.create(SurfaceViewExample.this, R.raw.boom);
		setContentView(v); 
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		v.resume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		v.pause();
	}

	public class OurView extends SurfaceView implements Runnable{

		Thread t = null;
		SurfaceHolder holder;
		boolean isItOK = false;
		boolean spriteLoaded = false;



		public OurView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			holder = getHolder();

		}

		/*		Thread thread = new Thread();
		@Override
		public void run(){
			try{
				Thread.sleep(LONG_DELAY);
				SurfaceViewExample.this.finish();
			}
			catch(Exception e){
				e.printStackTrace()
			}
		}*/

		public void run(){
			// TODO Auto-generated method stub
			sprite = new Sprite (OurView.this, user, heart);
			ghosts = new ArrayList<SpriteGhost>();
			bolts = new ArrayList<Bolt>();
			objs = new ArrayList<pickupObj>();
			long startTime = System.currentTimeMillis();
			long elapsedTime = 0;
			scoreTime = scoreGhosts = 0;

			while (isItOK == true) {
				//perform canvas drawing
				if (!holder.getSurface().isValid()) {
					continue;
				}

				if (!spriteLoaded) { 
					spriteLoaded = true;
				}

				Canvas c = holder.lockCanvas();
				onDraw(c);

				// DRAW GHOSTS EVERY ~5 seconds
				if (scoreTime + scoreGhosts <= 500){
					if (System.currentTimeMillis() - startTime - elapsedTime >= 5000) {
						ghosts.add(new SpriteGhost (OurView.this, ghost1, sprite.getX(), sprite.getY(), c,1));
						elapsedTime+= 5000;
						ghostSpawn1.start();
					}
				}
				if (scoreTime + scoreGhosts <= 1500){
					if (System.currentTimeMillis() - startTime - elapsedTime >= 2000) {
						ghosts.add(new SpriteGhost (OurView.this, ghost1, sprite.getX(), sprite.getY(), c,1));
						elapsedTime+=2000;
						ghostSpawn1.start();
					}
				}
				else{
					if (System.currentTimeMillis() - startTime - elapsedTime >= 1000) {
						ghosts.add(new SpriteGhost (OurView.this, ghost1, sprite.getX(), sprite.getY(), c,1));
						elapsedTime+=1000;
						ghostSpawn1.start();
					}
				}




				if (addBolt && bolts.size() < 10){
					bolts.add(new Bolt(OurView.this,boltPic,sprite.getX(), sprite.getY(),sprite.getDirection()));
					addBolt = false;
					boltSpawn.start();
				}

				scoreTime = (int)((System.currentTimeMillis() - startTime) / 100);
				checkHit(c);
				drawAll(c);

				if (System.currentTimeMillis() - invincTime > 5000)
					invinc = false;

				boolean breakit = false;
				for (SpriteGhost g : ghosts){
					if (sprite.checkHit(g.getDst()) && !invinc){
						sprite.setCurHp(sprite.getCurHp() - 1);
						ghostPunch1.start();
						if (sprite.getCurHp() < 1)
							breakit = true;
						else
							invinc = true;
						invincTime = System.currentTimeMillis();
					}
				}

				for (pickupObj ob : objs){
					if (sprite.checkHit(ob.getDst())){
						if (ob instanceof repellent){
							repel1.start();
							for (SpriteGhost g :ghosts){
								g.setReverse();
								objs.remove(ob);
							}
						}
						else if(ob instanceof bomb){
							killAll();
							bomb1.start();
							objs.remove(ob);
						}
						else if(ob instanceof coin){
							scoreGhosts += 100;
							coin1.start();
							objs.remove(ob);
						}
						else if(ob instanceof heart){
							if (sprite.getCurHp() < 3){
								sprite.setCurHp(sprite.getCurHp() + 1);
								heart1.start();
								objs.remove(ob);
							}
						}
					}
				}

				if (breakit)
					break;

				holder.unlockCanvasAndPost(c);	
			}
			onEnd();

		}

		private void onEnd()
		{
			pause();
			run();
		}

		protected void onDraw(Canvas c) {
			//c.drawARGB(255, 150, 150, 10);
			c.drawBitmap(scaled, 0, 0, null);
			//sprite.onDraw(c);
		}

		public void pause() {
			isItOK = false;
			while (true) {
				try {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "SCORE: " + (scoreTime + scoreGhosts), Toast.LENGTH_LONG).show();
						}
					});
					System.out.println(1);
					t.sleep(LONG_DELAY);
					System.out.println(2);
					onCreate(null);
					//t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}
			t = null;
		}

		public void resume() {
			isItOK = true;
			t = new Thread(this);
			t.start();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent me) {
		// TODO Auto-generated method stub
		x = me.getX();
		y = me.getY();
		if ((int)me.getX() > 550 && (int)me.getY() > 990)	// Attack Button
		{
			addBolt = true;
			return true;
		}
		else
		{
			if (invinc)
				sprite.update((int) x, (int) y,invincUser);
			else
				sprite.update((int) x, (int) y,user);
			return true;
		}
	}

	private void drawAll (Canvas c) {

		sprite.onDraw(c);

		for (SpriteGhost g : ghosts ) {
			// Ghost Following
			g.update(sprite.getX(), sprite.getY());
			g.incCurrentFrame();
			g.onDraw(c);
		}

		for (Bolt b : bolts){
			b.onDraw(c);
			b.update();
			b.incCurrentFrame();
		}

		for (pickupObj ob : objs){
			ob.onDraw(c);
			ob.incCurrentFrame();
		}

		if (sprite.isClose(ghosts))
		{
			c.drawBitmap(alertMe, new Rect (0, 0,alertMe.getWidth(), alertMe.getHeight()), new Rect (sprite.getX() + 15, sprite.getY() - 35, sprite.getX() + alertMe.getWidth() + 15, sprite.getY() + alertMe.getHeight() - 35), null);
		}

		drawScore(c);
	}

	private void checkHit(Canvas c){

		for (int j = 0; j < ghosts.size(); j++)
		{
			for (int k = 0; k < bolts.size(); k++)
			{
				if (bolts.get(k) != null && ghosts.get(j).getDst() != null && bolts.get(k).checkHit(ghosts.get(j).getDst()))
				{
					bolts.remove(k);

					Random rand = new Random();
					int randNum = rand.nextInt(16) + 1;
					System.out.println(randNum);
					if (randNum == 1){
						objs.add(new repellent(v,repellentpic,ghosts.get(j).getX(),ghosts.get(j).getY(),c));
					}
					else if (randNum == 2){
						objs.add (new bomb(v,bombs,ghosts.get(j).getX(),ghosts.get(j).getY(),c));
					}
					else if (randNum == 3){
						objs.add(new coin(v,coins,ghosts.get(j).getX(),ghosts.get(j).getY(),c));
					}
					else if (randNum == 4){
						objs.add(new heart(v,heart,ghosts.get(j).getX(),ghosts.get(j).getY(),c));
					}		

					ghosts.remove(j);
					ghostDie1.start();
					scoreGhosts += 50;
					break;
				}
				else if (bolts.get(k) != null && bolts.get(k).isOutsideFrame(c)){
					bolts.remove(k);
				}
				/*else if (ghosts.get(k) != null && ghosts.get(k).isOutsideFrame(c)){
					ghosts.remove(k);
				}*/
			}
		}

	}

	private void killAll()
	{
		ghosts.removeAll(ghosts);
		ghostDie1.start();
		scoreGhosts += 250;
	}

	private void drawScore(Canvas c)
	{
		int scoreTot = scoreTime + scoreGhosts;
		String stScr = Integer.toString(scoreTot);
		int lngth = stScr.length();

		int ySc = 1040;
		int width = points.getWidth()/10;
		int height = points.getHeight();

		switch(lngth){
		case 1:
			c.drawBitmap(points, new Rect (width * (scoreTot % 10) , 0,width * (scoreTot % 10) + width, height), new Rect (3 * width,ySc, 4 * width, ySc + height), null);
			c.drawBitmap(points, new Rect (0 , 0, width, height), new Rect (2 * width, ySc, 3 * width, ySc + height), null);
			c.drawBitmap(points, new Rect (0 , 0, width, height), new Rect (width, ySc, 2 * width, ySc + height), null);
			c.drawBitmap(points, new Rect (0 , 0, width, height), new Rect (0, ySc, width, ySc + height), null);
			break;
		case 2:
			c.drawBitmap(points, new Rect (width * (scoreTot % 10) , 0,width * (scoreTot % 10) + width, height), new Rect (3 * width, ySc, 4 * width, ySc + height), null);
			scoreTot = scoreTot / 10;
			c.drawBitmap(points, new Rect (width * (scoreTot % 10) , 0,width * (scoreTot % 10) + width, height), new Rect (2 * width, ySc, 3 * width, ySc + height), null);
			c.drawBitmap(points, new Rect (0 , 0, width, height), new Rect (width, ySc, 2 * width, ySc + height), null);
			c.drawBitmap(points, new Rect (0 , 0, width, height), new Rect (0, ySc, width, ySc + height), null);
			break;
		case 3:
			c.drawBitmap(points, new Rect (width * (scoreTot % 10) , 0,width * (scoreTot % 10) + width, height), new Rect (3 * width, ySc, 4 * width, ySc + height), null);
			scoreTot = scoreTot / 10;
			c.drawBitmap(points, new Rect (width * (scoreTot % 10) , 0,width * (scoreTot % 10) + width, height), new Rect (2 * width, ySc, 3 * width, ySc + height), null);
			scoreTot = scoreTot / 10;
			c.drawBitmap(points, new Rect (width * (scoreTot % 10) , 0,width * (scoreTot % 10) + width, height), new Rect (width, ySc, 2 * width, ySc + height), null);
			c.drawBitmap(points, new Rect (0 , 0, width, height), new Rect (0, ySc, width, ySc + height), null);
			break;
		case 4:
			c.drawBitmap(points, new Rect (width * (scoreTot % 10) , 0,width * (scoreTot % 10) + width, height), new Rect (3 * width, ySc, 4 * width, ySc + height), null);
			scoreTot = scoreTot / 10;
			c.drawBitmap(points, new Rect (width * (scoreTot % 10) , 0,width * (scoreTot % 10) + width, height), new Rect (2 * width, ySc, 3 * width, ySc + height), null);
			scoreTot = scoreTot / 10;
			c.drawBitmap(points, new Rect (width * (scoreTot % 10) , 0,width * (scoreTot % 10) + width, height), new Rect (width, ySc, 2 * width, ySc + height), null);
			scoreTot = scoreTot / 10;
			c.drawBitmap(points, new Rect (width * (scoreTot % 10) , 0,width * (scoreTot % 10) + width, height), new Rect (0, ySc, width, ySc + height), null);
			break;
		}
	}
}
