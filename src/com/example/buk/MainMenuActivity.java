package com.example.buk;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
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

    
    
    
    @Override
    public void onResume() {
    	super.onResume();
    	Log.d("onResume", "onResume Was Called Right NOW!");
    }
    //onclick of the button, start the buklist activity
    //Intent intent = new Intent(this, savedBuks.class);
    //startActivity(intent);
    public void doScan(View view)
    {
    	Intent intent = new Intent("com.google.zxing.client.android.SCAN"); intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE"); startActivityForResult(intent, 0);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
    	if(requestCode == 0)     
    	{         
    		if(resultCode == RESULT_OK)
    		{             
    			String contents = intent.getStringExtra("SCAN_RESULT");  
    			String format = intent.getStringExtra("SCAN_RESULT_FORMAT");              
    			Log.i("xZing", "contents: "+contents+" format: "+format);              
    			// Handle successful scan         
    		}         
    		else if(resultCode == RESULT_CANCELED) 
    		{              
    			// Handle cancel            
    			Log.i("xZing", "Cancelled");         
    		}      
    	}
    		
    }


}
