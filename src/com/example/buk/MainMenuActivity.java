package com.example.buk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

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

	//begins the scan activity provided by the zebra crossing libraries
	public void scanNow(View view) {
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE",
				"QR_CODE_MODE");
		startActivityForResult(intent, 0);
	}

	//begins the view all list activity
	public void showLists(View view) {
		Intent intent = new Intent(this, ViewListsActivity.class);
		startActivity(intent);
	}
	
	
	//Provides instruction on what to do after the scanner finishes scanning a barcode
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		String contents = "";
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				//THIS IS THE RESULT OF THE SCAN!!!!!!
				contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				Log.i("xZing", "contents: " + contents + " format: " + format);
				// Handle successful scan
			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
				Log.i("xZing", "Cancelled");
			}
		}
		
		//if (keyCode != KeyEvent.KEYCODE_BACK)) {
			Intent intent2 = new Intent(this, SearchForBookActivity.class);
			Bundle b = new Bundle();
			b.putString("ISBN", contents);
			intent2.putExtras(b);
			startActivity(intent2); // start list picker
		//}
	}
	
	//begins the search activity
	public void goToSearch(View view) {
		Intent intent = new Intent(this, SearchForBookActivity.class);
		startActivity(intent);
	}

}
