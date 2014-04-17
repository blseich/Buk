package com.example.buk;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.objects.buk.Book;
import com.objects.buk.BookHelper;
import com.objects.buk.BookStorage;

public class BookView extends Activity {

	Book book = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_view);
		
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		BookStorage db = new BookStorage(this);
		
		//Retrieves book information based on the id passed to the intent
		int id = b.getInt("bookId");
		book = db.getABook(id);
		
		//Inputs the information from the book into the respective fields for the view
		TextView title = (TextView) findViewById(R.id.bookTitle);
		title.setText(book.getTitle());
	    
		TextView author = (TextView) findViewById(R.id.bookAuthor);
		author.setText(book.getAuthor());
		
		TextView desc = (TextView) findViewById(R.id.bookDescription);
		desc.setText(book.getDescription());
		
		TextView price = (TextView) findViewById(R.id.bookPrice);
		price.setText(book.getPrice());
		
		//Creates a network call to retrieve an image from a url
		class GetThumbnail extends AsyncTask<String, Integer, String> {
			Drawable dr;
			int imageId = 0;
			String imgUrl;
			
			public GetThumbnail(String imgUrl, int imageId){
				this.imgUrl = imgUrl;
				this.imageId = imageId;
			}
			
			@Override
			protected String doInBackground(String... params) {
				//Makes network call to get image from a url
				BookHelper bookHelper = new BookHelper();
				dr = bookHelper.LoadImageFromWebOperations(imgUrl);
				return null;
			}
			@Override 
			protected void onPostExecute(String result){
				//builds a bitmap image based on the image retrieved from the url				
				Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
				Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, dr.getIntrinsicWidth()*2, dr.getIntrinsicHeight()*2, true));
				
				ImageView thumbnail = (ImageView)findViewById(imageId);
				thumbnail.setImageDrawable(d);
			}
			
		}		
		new GetThumbnail(book.getImgUrl(), R.id.bookCover).execute("1");
		
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.book_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void removeBook(View v) {
		showSimplePopUp(book.getTitle());
	}
	
	private void showSimplePopUp(String title) {	
		final BookStorage db = new BookStorage(this);
		
		 AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);

        helpBuilder.setTitle("Confirmation");
        helpBuilder.setMessage("Are you sure you want to remove " + title + "?");

		 helpBuilder.setNegativeButton("Cancel",
				 new DialogInterface.OnClickListener() {
			 
			 public void onClick(DialogInterface dialog, int which) {
				 //Just close dialog box
			 }
		 });
		 helpBuilder.setPositiveButton("Confirm",
		   new DialogInterface.OnClickListener() {
			 
			 @SuppressLint("NewApi") public void onClick(DialogInterface dialog, int which) {
				 db.deleteBook(book);
				 finish();
			 }	
		 });
		 AlertDialog helpDialog = helpBuilder.create();
		 helpDialog.show();
	}

}
