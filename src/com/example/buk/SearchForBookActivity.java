package com.example.buk;

import android.annotation.TargetApi;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.objects.buk.Book;
import com.objects.buk.BookHelper;

public class SearchForBookActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_for_book);
		// Show the Up button in the action bar.
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
		getMenuInflater().inflate(R.menu.search_for_book, menu);
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
	
	public void executeSearch(View view){
		class GetBook extends AsyncTask<String, Integer, String> {
			private Book book = null;
			private String keywords;
			Drawable imgFromUrl;
			
			public GetBook(String search) {
				keywords = search;			
			}
			
			public Book retrieveSearchResult() {
				return this.book;
			}
			
			@Override
			protected String doInBackground(String... params) {
				BookHelper bookHelper = new BookHelper();
				book = bookHelper.searchForBook(keywords);
				imgFromUrl = bookHelper.LoadImageFromWebOperations(book.getImgUrl());
				return null;
			}
			

			@Override 
			protected void onPostExecute(String result){    
				ScrollView searchResultViewer = (ScrollView)findViewById(R.id.searchResult);
				TextView title = (TextView)findViewById(R.id.title);
				TextView author = (TextView)findViewById(R.id.author);
				TextView description = (TextView)findViewById(R.id.description);
				TextView price = (TextView)findViewById(R.id.price);
				ImageView img = (ImageView)findViewById(R.id.image);
				
				title.setText(book.getTitle());
				author.setText(book.getAuthor());
				description.setText(book.getDescription());
				price.setText(book.getPrice());
				
				Drawable dr = imgFromUrl;
				Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
				Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, dr.getIntrinsicWidth()*5, dr.getIntrinsicHeight()*5, true));
				
				img.setImageDrawable(d);
				searchResultViewer.setVisibility(View.VISIBLE);
			} 
			
		}

		EditText searchBox = (EditText)findViewById(R.id.keywordSearch);
		String search = searchBox.getText().toString();
		if (search.length() > 0) {
			new GetBook(search).execute("1");
		}
		
	}
	


}

