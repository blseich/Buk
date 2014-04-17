package com.example.buk;

import android.annotation.TargetApi;
import android.app.Activity;
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
import android.view.View.OnClickListener;
import android.widget.Button;
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
		Intent intent = getIntent();
		
		Bundle b = intent.getExtras();
		//If parameters were passed by the main menu activity this means
		//that a book has been scanned and that information should be loaded into
		//the respective fields
		if(b != null && b.get("ISBN") != null){
			EditText keywordSearch = (EditText)findViewById(R.id.keywordSearch);
			keywordSearch.setText(b.get("ISBN").toString());
			//executes a search based on the ISBN number provided from the scanner
			this.executeSearch(null);
		}
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
	
	//Function to run when the exectue search button is clicked
	public void executeSearch(View view){
		class GetBook extends AsyncTask<String, Integer, String> {
			private Book book = null;
			private String keywords;
			Drawable imgFromUrl;
			
			public GetBook(String search) {
				keywords = search;			
			}
			
			//Execute a search for a book based on the parameters in the search bar
			@Override
			protected String doInBackground(String... params) {
				BookHelper bookHelper = new BookHelper();
				book = bookHelper.searchForBook(keywords);
				if (book != null) {
					imgFromUrl = bookHelper.LoadImageFromWebOperations(book.getImgUrl());
				}
				return null;
			}
			

			//Once the network call has finished, populate all fields with results from the search
			@Override 
			protected void onPostExecute(String result){    
				ScrollView searchResultViewer = (ScrollView)findViewById(R.id.searchResult);
				TextView title = (TextView)findViewById(R.id.title);
				TextView author = (TextView)findViewById(R.id.author);
				TextView description = (TextView)findViewById(R.id.description);
				TextView price = (TextView)findViewById(R.id.price);
				ImageView img = (ImageView)findViewById(R.id.image);
				Button btn = (Button)findViewById(R.id.addBookToListButton);
				
				title.setText(book.getTitle());
				author.setText(book.getAuthor());
				description.setText(book.getDescription());
				price.setText(book.getPrice());
				
				Drawable dr = imgFromUrl;
				if (dr == null) {
					dr = getResources().getDrawable(R.drawable.no_image);
				}
				Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
				Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, dr.getIntrinsicWidth()*5, dr.getIntrinsicHeight()*5, true));
				
				img.setImageDrawable(d);
				searchResultViewer.setVisibility(View.VISIBLE);
				
				//If there were no results found, do not show the add book button
				if(book.getTitle().equals("NO RESULTS FOUND")) {
					btn.setVisibility(View.INVISIBLE);
				} 
				//If there were search results, show the add book button and add an onclick
				//listener to start the select list to add book to activity
				else {
					btn.setVisibility(View.VISIBLE);
					btn.setOnClickListener(new OnClickListener() {
						
						public void onClick(View v) {
							Bundle b = new Bundle();
							Intent intent = new Intent(getApplicationContext(), AfterScanListPicker.class);
							b.putString("title", book.getTitle());
							b.putString("author", book.getAuthor());
							b.putString("description", book.getDescription());
							b.putString("price",book.getPrice());
							b.putString("imgUrl", book.getImgUrl());
							intent.putExtras(b);
							startActivity(intent);		
						}
					});
				}
				
			} 	
		}
		
		EditText searchBox = (EditText)findViewById(R.id.keywordSearch);
		String search = searchBox.getText().toString();
		//Only executes search if the search box is populated
		if (search.length() > 0) {
			new GetBook(search).execute("1");
		}		
	}

	
}

