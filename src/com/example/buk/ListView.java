package com.example.buk;

import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.objects.buk.BookList;
import com.objects.buk.BookStorage;

public class ListView extends Activity {

	int listId = 0;
	BookList bookList = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_view);
		Intent intent = getIntent();
		
		//retrieve listId from the parameters passed to the intent
		listId = intent.getIntExtra("listId", 0);
		
		//retrieve a list from the database using the listId provided to the intent
		BookStorage db = new BookStorage(this);
		bookList = db.getBookList(listId);
		
		//Set the title of the list in the view
		TextView listTitle = (TextView)findViewById(R.id.listTitle);
		listTitle.setText(bookList.getListTitle());
		
		//retrieve all books associated with this list from the database
		List<Book> allBooksInList = db.getAllBooksInBookList(listId);
		
		//retrieve the views needing to be populated from the layout
		ScrollView allBooks = (ScrollView)findViewById(R.id.allBooks);
		RelativeLayout allBooksContainer = (RelativeLayout)allBooks.findViewById(R.id.allBooksContainer);
		int previousId = 0;
		
		
		//Dynamically create all views for each book
		for(Book book : allBooksInList){
			RelativeLayout.LayoutParams params = 
					new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.MATCH_PARENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			RelativeLayout singleBook = new RelativeLayout(this);
			singleBook.setId(book.getId());
			
			//Set rule to place book views underneath one another
			if(previousId > 0){
				params.addRule(RelativeLayout.BELOW, previousId);
			} else {
				params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
			}

			//build the views that make up each individual book view
			ImageView thumbnail = buildThumbnail(book);
			TextView title = buildBookTitle(book);
			TextView author = buildBookAuthor(book);
			
			//Set the book view to open the book to view its detials once clicked upon
			singleBook.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					//Attach the specified bookId to the future intent
					Bundle b = new Bundle();
					Intent intent = new Intent(getApplicationContext(), BookView.class);
					int bookId = v.getId();
					b.putInt("bookId", bookId);
					
					//begin the intent to show book details
					intent.putExtras(b);
					startActivity(intent);
				}
			});
			
			//place all views into the single book view
			singleBook.addView(thumbnail);
			singleBook.addView(title);
			singleBook.addView(author);
			singleBook.setLayoutParams(params);
			
			//place the single book view into the collection view for all the books
			allBooksContainer.addView(singleBook);
			
			//update the previous id to set the next book view below the one just created
			previousId = book.getId();
		
		}
		
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	@Override
	public void onResume(){
		//reload the page each time the page resumes
		//this ensures books that were deleted no longer show up in this list view
		Bundle b = new Bundle();
		b.putInt("listId", listId);
		onCreate(b);
	}

	//Dynamically create the author field for each book view
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
	
	//Dynamically create the title field for each book view
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
	
	//Retrieve and create the thumbnail for each book view
	private ImageView buildThumbnail(Book book){
		//Asynchronous task used to make a network call and retrieve an image from url
		//provided by the api
		class GetThumbnail extends AsyncTask<String, Integer, String> {
			Drawable dr;
			int imageId = 0;
			String imgUrl;
			
			public GetThumbnail(String imgUrl, int imageId){
				this.imgUrl = imgUrl;
				this.imageId = imageId;
			}
			
			//Retrieve the image from the url specified
			@Override
			protected String doInBackground(String... params) {
				BookHelper bookHelper = new BookHelper();
				dr = bookHelper.LoadImageFromWebOperations(imgUrl);
				return null;
			}
			//Create the thumbnail drawable form the url
			@Override 
			protected void onPostExecute(String result){
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
	
	//the onclick function for the remove list button
	public void removeList(View V) {
		showSimplePopUp();
	}
	
	//shows a popup to confirm the removal of this list
	private void showSimplePopUp() {	
		final BookStorage db = new BookStorage(this);
		
		 AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);

        helpBuilder.setTitle("Confirmation");
        helpBuilder.setMessage("Are you sure you want to remove " + bookList.getListTitle() + "?");

		 helpBuilder.setNegativeButton("Cancel",
				 new DialogInterface.OnClickListener() {
			 
			 public void onClick(DialogInterface dialog, int which) {
				 //Just close dialog box
			 }
		 });
		 
		 //If the confirm button is clicked, remove the list from the database
		 helpBuilder.setPositiveButton("Confirm",
		   new DialogInterface.OnClickListener() {
			 
			 @SuppressLint("NewApi") public void onClick(DialogInterface dialog, int which) {
				 db.deleteBookList(bookList);
				 finish();
			 }	
		 });
		 AlertDialog helpDialog = helpBuilder.create();
		 helpDialog.show();
	}
	

}
