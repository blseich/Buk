package com.example.buk;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.objects.buk.Book;
import com.objects.buk.BookList;
import com.objects.buk.BookStorage;

public class MainMenuActivity extends Activity {

	private Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu_layout);
		BookStorage storage = new BookStorage(context);
		
			BookList bookList1 = new BookList(1, "Book List 1");
			for (int i = 0; i < 5; i++) {
				Book temp = new Book(i, "title" + i, "isbn" + i, "author" + i);
				temp.setDescription("description" + i);
				temp.setImgUrl("imgUrl"+i);
				temp.setPrice("price"+i);
				bookList1.addBook(temp);
			}
			//storage.addBookList(bookList1);
			BookList bookListReturned = storage.getBookList(8);
			String toDelete = new String();
			System.out.println(toDelete);
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

	// onclick of the button, start the buklist activity
	// Intent intent = new Intent(this, savedBuks.class);
	// startActivity(intent);
	public void scanNow(View view) {
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE",
				"QR_CODE_MODE");
		startActivityForResult(intent, 0);
	}

	// on click of the button, start the list activity
	public void showLists(View view) {
		Intent intent = new Intent(this, ViewListsActivity.class);
		startActivity(intent);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				//THIS IS THE RESULT OF THE SCAN!!!!!!
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				Log.i("xZing", "contents: " + contents + " format: " + format);
				// Handle successful scan
			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
				Log.i("xZing", "Cancelled");
			}
		}
		
		//if (keyCode != KeyEvent.KEYCODE_BACK)) {
			Intent intent2 = new Intent(this, AfterScanListPicker.class);
			startActivity(intent2); // start list picker
		//}
	}
	
	public void goToSearch(View view) {
		Intent intent = new Intent(this, SearchForBookActivity.class);
		startActivity(intent);
	}

}
