package com.dlh4fx.ghosthunter;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class menu extends Activity {
	MediaPlayer logoMusic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Setting up sound in main menu
		logoMusic = MediaPlayer.create(menu.this, R.raw.ghost_hunter_menu);
        logoMusic.start();
		logoMusic.setLooping(true);
		
        //Button sound
        final MediaPlayer buttonSound = MediaPlayer.create(menu.this, R.raw.button_click);
        
        //Setting up button references
		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) { //activity when button gets clicked
				buttonSound.start();
				startActivity(new Intent("com.dlh4fx.ghosthunter.SURFACEVIEWEXAMPLE"));
			}
		});
		
		
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		logoMusic.release();
	}
	
}
