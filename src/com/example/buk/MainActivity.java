package com.example.buk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final Intent intent = new Intent(this, MainMenuActivity.class);
        
        //Load all images into the intent
        final ImageView bookImg = (ImageView) findViewById(R.id.book_load);
        final ImageView bukWordImg = (ImageView) findViewById(R.id.buk_word_load);
        final ImageView glassesImg = (ImageView) findViewById(R.id.glasses_load);
        final RelativeLayout loadLayout = (RelativeLayout) findViewById(R.id.load_layout);
        
        //Load all animations into the layout
        final Animation dropInBookImg = AnimationUtils.loadAnimation(this, R.anim.drop_in);
        final Animation dropInBukWordImg = AnimationUtils.loadAnimation(this, R.anim.drop_in);
        final Animation dropInGlassesImg = AnimationUtils.loadAnimation(this, R.anim.drop_in);
		final Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		
		//Begin each animation in succession after another animation finishes
        dropInBookImg.setAnimationListener(new AnimationListener() {
        	
        	@Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }
        	
        	@Override
        	public void onAnimationEnd(Animation animation) {
        		bukWordImg.setVisibility(View.VISIBLE);
        		bukWordImg.startAnimation(dropInBukWordImg);
        	}
        });        
        
        dropInBukWordImg.setAnimationListener(new AnimationListener() {
        	
        	@Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }
        	
        	@Override
        	public void onAnimationEnd(Animation animation) {
        		glassesImg.setVisibility(View.VISIBLE);
        		glassesImg.startAnimation(dropInGlassesImg);
        	}
        }); 
        
        dropInGlassesImg.setAnimationListener(new AnimationListener() {
        	
        	@Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }
        	
        	@Override
        	public void onAnimationEnd(Animation animation) {
        		loadLayout.startAnimation(fadeOut);
        		
        	}
        	
        		
        }); 
        
        //Last animation
        fadeOut.setAnimationListener(new AnimationListener(){

        	@Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }
        	
        	@Override
        	public void onAnimationEnd(Animation animation) {
        		//Make this intent invisible so it doesnt show up late
        		loadLayout.setVisibility(View.GONE);
        		//take user to the main menu activity
        		startActivity(intent);
        	}
        });
        
        bookImg.startAnimation(dropInBookImg);
    }

	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
