package com.example.buk;

import java.util.List;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.objects.buk.Book;
import com.objects.buk.BookHelper;
import com.objects.buk.BookStorage;

public class ListView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_view);
		Intent intent = getIntent();
		int listId = intent.getIntExtra("listId", 0);
		BookStorage db = new BookStorage(this);
		List<Book> allBooksInList = db.getAllBooksInBookList(listId);
		ScrollView allBooks = (ScrollView)findViewById(R.id.allBooks);
		RelativeLayout allBooksContainer = (RelativeLayout)allBooks.findViewById(R.id.allBooksContainer);
		int previousId = 0;
		
		for(Book book : allBooksInList){
			RelativeLayout.LayoutParams params = 
					new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.MATCH_PARENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			RelativeLayout singleBook = new RelativeLayout(this);
			singleBook.setId(book.getId());
			
			if(previousId > 0){
				params.addRule(RelativeLayout.BELOW, previousId);
			} else {
				params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
			}

			ImageView thumbnail = buildThumbnail(book);
			TextView title = buildBookTitle(book);
			TextView author = buildBookAuthor(book);
			
//			singleBook.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					Bundle b = new Bundle();
//					Intent intent = new Intent(getApplicationContext(), ListView.class);
//					int listId = v.getId();
//					b.putInt("listId", listId);
//					intent.putExtras(b);
//					startActivity(intent);
//				}
//			});
			
			singleBook.addView(thumbnail);
			singleBook.addView(title);
			singleBook.addView(author);
			singleBook.setLayoutParams(params);
			
			allBooksContainer.addView(singleBook);
			previousId = book.getId();
		
		}
		
		// Show the Up button in the action bar.
		setupActionBar();
	}

	private TextView buildBookAuthor(Book book) {
		TextView bookAuthor = new TextView(this);
		RelativeLayout.LayoutParams params = 
				new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, book.getId()+2000);
		params.addRule(RelativeLayout.ALIGN_LEFT, book.getId()+2000);
		
		bookAuthor.setText(book.getAuthor());
		bookAuthor.setTextSize(15);
		bookAuthor.setLayoutParams(params);
		
		return bookAuthor;
	}
	
	private TextView buildBookTitle(Book book) {
		TextView bookTitle = new TextView(this);
		RelativeLayout.LayoutParams params = 
				new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.RIGHT_OF, book.getId()+1000);
		params.addRule(RelativeLayout.ALIGN_TOP, book.getId()+1000);
		
		bookTitle.setId(book.getId()+2000);
		bookTitle.setText(book.getTitle());
		bookTitle.setTextSize(30);
		bookTitle.setLayoutParams(params);
		
		
		return bookTitle;
	}
	
	private ImageView buildThumbnail(Book book){
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
				BookHelper bookHelper = new BookHelper();
				dr = bookHelper.LoadImageFromWebOperations(imgUrl);
				return null;
			}
			@Override 
			protected void onPostExecute(String result){
				Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
				Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, dr.getIntrinsicWidth()*2, dr.getIntrinsicHeight()*2, true));
				
				ImageView thumbnail = (ImageView)findViewById(imageId);
				thumbnail.setImageDrawable(dr);
			}
			
		}
		ImageView thumbnail = new ImageView(this);
		RelativeLayout.LayoutParams params = 
				new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		thumbnail.setId(book.getId() + 1000);
		new GetThumbnail(book.getImgUrl(), book.getId()+1000).execute("1");
		return thumbnail;
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
	
	public void showBook(View view){
		Intent intent = new Intent(this, BookView.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_view, menu);
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
	

}
