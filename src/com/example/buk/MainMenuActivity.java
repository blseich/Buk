package com.example.buk;

import com.buklist.buk.savedBuks;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MainMenuActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_layout);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    //onclick of the button, start the buklist activity
    //Intent intent = new Intent(this, savedBuks.class);
    //startActivity(intent);
}
