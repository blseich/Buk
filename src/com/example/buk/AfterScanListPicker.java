package com.example.buk;

import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.objects.buk.Book;
import com.objects.buk.BookList;
import com.objects.buk.BookStorage;

public class AfterScanListPicker extends Activity {
	
	BookStorage db = new BookStorage(this);
	Book book;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_after_scan_list_picker);
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		
		//Creates a book object from the attributes passed to the activity
		book = new Book(0, b.getString("title"), b.getString("author"));
		book.setDescription(b.getString("description"));
		book.setPrice(b.getString("price"));
		book.setImgUrl(b.getString("imgUrl"));
		
		//Retrieves all book lists stored in the database
		List<BookList> allBookLists = db.getAllBookLists();
		
		int previousId = 0;
		
		//Access the contents of the select screen
		ScrollView selectListScroller = (ScrollView)findViewById(R.id.selectListScroller);
		RelativeLayout selectListContainer = (RelativeLayout)selectListScroller.findViewById(R.id.selectListContainer);
		
		//Dynamically creates all individual views for each list
		for(BookList bookList : allBookLists) {
			RelativeLayout.LayoutParams params = 
					new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.MATCH_PARENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			RelativeLayout singleList = new RelativeLayout(this);
			singleList.setId(bookList.getId());
			
			//Positions successive lists below previous ones
			if(previousId > 0){
				params.addRule(RelativeLayout.BELOW, previousId);
			} else {
				params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
			}
			

			TextView title = buildListTitle(bookList);
			TextView numBooks = buildNumBooks(bookList);
			
			//Add created views to a container for the single list
			singleList.addView(title);
			singleList.addView(numBooks);
			
			//sets an onclick listener to add the book to that specific list
			singleList.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					showSimplePopUp(book, v.getId());
				}
			});
			
			//Adds the view for this single list to the container
			singleList.setLayoutParams(params);
			selectListContainer.addView(singleList);
			
			previousId = bookList.getId();
			
		}
		
		
		
		setupActionBar();
	}
	
	private TextView buildListTitle(BookList bookList){
		TextView title = new TextView(this);
		RelativeLayout.LayoutParams params = 
				new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		
		title.setId(bookList.getId()+123);
		title.setText(bookList.getListTitle());
		title.setTextSize(30);
		title.setLayoutParams(params);
		
		
		return title;		
	}
	
	private TextView buildNumBooks(BookList bookList) {
		TextView numBooks = new TextView(this);
		RelativeLayout.LayoutParams params = 
				new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, bookList.getId()+123);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		
		numBooks.setText(db.getAllBooksInBookList(bookList.getId()).size() + " Total Books");
		numBooks.setTextSize(15);
		numBooks.setLayoutParams(params);
		
		return numBooks;
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
		getMenuInflater().inflate(R.menu.after_scan_list_picker, menu);
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
	
	//Shows a popup on the screen to confirm adding the book to a list
	private void showSimplePopUp(final Book book, final int listId) {	
		final BookStorage db = new BookStorage(this);
		final BookList bookList = db.getBookList(listId);
		
		//Object for the alert dialog
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		
		//Sets attributes of the alert dialog
        helpBuilder.setTitle("Confirmation");
        helpBuilder.setMessage("Add " + book.getTitle() + " to " + bookList.getListTitle() + "?");

		 helpBuilder.setNegativeButton("No",
				 new DialogInterface.OnClickListener() {
			 
			 public void onClick(DialogInterface dialog, int which) {
				 //Just close dialog box
			 }
		 });
		 
		 //Sets action to take if the user confirms to add the book
		 helpBuilder.setPositiveButton("Yes",
		   new DialogInterface.OnClickListener() {
			 
			 @SuppressLint("NewApi") public void onClick(DialogInterface dialog, int which) {
				 
				 //adds the book to the book list specified by the button
				 db.addABook(book, bookList.getId());
				 
				 //Takes user back to the main menu
				 Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
				 startActivity(intent);
			 }	
		 });
		 AlertDialog helpDialog = helpBuilder.create();
		 helpDialog.show();
	}
	
	
	
	
	

}
