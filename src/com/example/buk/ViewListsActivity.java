package com.example.buk;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.objects.buk.BookList;
import com.objects.buk.BookStorage;

public class ViewListsActivity extends Activity {

	BookStorage db = new BookStorage(this);
	final Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_lists);
		
		//retrieve all book lists from the database
		List<BookList> allBookLists = db.getAllBookLists();
		int previousId = 0;
		ScrollView allListsView = (ScrollView)findViewById(R.id.scrollView);
		RelativeLayout container = (RelativeLayout)allListsView.findViewById(R.id.viewAllListsContainer);
		
		//Dynamically create all views for each list in the database
		for(BookList bookList : allBookLists){
			RelativeLayout.LayoutParams params = 
					new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.MATCH_PARENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			RelativeLayout singleList = new RelativeLayout(this);
			singleList.setId(bookList.getId());
			
			//Set rules to place each successive list below the current list
			if(previousId > 0){
				params.addRule(RelativeLayout.BELOW, previousId);
			} else {
				params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
			}
			
			//Set the title and number of books in the list for each list view
			TextView title = buildListTitle(bookList);
			TextView numBooks = buildNumBooks(bookList);
			
			//Set an onclick listener for each list view to take you to details
			//of that specific list
			singleList.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Bundle b = new Bundle();
					Intent intent = new Intent(getApplicationContext(), ListView.class);
					//pass a listId to the next intent to retrieve details about that list
					//from the database
					int listId = v.getId();
					b.putInt("listId", listId);
					intent.putExtras(b);
					startActivity(intent);
				}
			});
			
			//add all dynamically created views to this single list view
			singleList.addView(title);
			singleList.addView(numBooks);
			singleList.setLayoutParams(params);
			
			container.addView(singleList);
			
			//update previous id to set each successive list below the current list
			previousId = bookList.getId();
		
		}
	}
	
	//reload the page on resume so lists that were deleted do not show up on this page
	@Override
	public void onResume(){
		onCreate(null);
	}
	
	//Dynamically create the title for each list
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
	
	//Dynamically create the number of books for each list
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
		numBooks.setTextColor(getResources().getColor(R.color.forest_green));
		
		return numBooks;
	}
	
	//Add a list onclick function
	public void addList(View v){
		showSimplePopUp();
	}
	
	//show a popup in order to add a list
	private void showSimplePopUp() {	
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);

        helpBuilder.setTitle("Create A List");
        helpBuilder.setMessage("What would you like to name your list?");
        
        //field for enter a new title for a list
        final EditText nameInput = new EditText(this);
        helpBuilder.setView(nameInput);

		 helpBuilder.setNegativeButton("Cancel",
				 new DialogInterface.OnClickListener() {
			 
			 public void onClick(DialogInterface dialog, int which) {
				 //Just close dialog box
			 }
		 });
		 //on confirm add a new list with specified title to the database
		 helpBuilder.setPositiveButton("Confirm",
		   new DialogInterface.OnClickListener() {
			 
			 @SuppressLint("NewApi") public void onClick(DialogInterface dialog, int which) {
				 //only executes if a name of length greater than zero has been input to the field
				 if (nameInput.getText().toString().length() > 0) {
					 String listName = nameInput.getText().toString();
					 BookList list = new BookList(0, listName);
					 BookStorage db = new BookStorage(getApplicationContext());
					 db.addBookList(list);
					 finish();
					 startActivity(getIntent());
				}
			 }	
		 });
		 AlertDialog helpDialog = helpBuilder.create();
		 helpDialog.show();
	}
	
}
