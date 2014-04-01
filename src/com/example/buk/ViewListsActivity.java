package com.example.buk;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.objects.buk.BookList;
import com.objects.buk.BookStorage;

public class ViewListsActivity extends Activity {

	BookStorage db = new BookStorage(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_lists);
		
		List<BookList> allBookLists = db.getAllBookLists();
		int previousId = 0;
		ScrollView allListsView = (ScrollView)findViewById(R.id.scrollView);
		RelativeLayout container = (RelativeLayout)allListsView.findViewById(R.id.viewAllListsContainer);
		
		for(BookList bookList : allBookLists){
			RelativeLayout.LayoutParams params = 
					new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.MATCH_PARENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			RelativeLayout singleList = new RelativeLayout(this);
			singleList.setId(bookList.getId());
			
			if(previousId > 0){
				params.addRule(RelativeLayout.BELOW, previousId);
			} else {
				params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
			}
			
			TextView title = buildListTitle(bookList);
			TextView numBooks = buildNumBooks(bookList);
			CheckBox checkBox = buildCheckBox(bookList);
			
			singleList.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Bundle b = new Bundle();
					Intent intent = new Intent(getApplicationContext(), ListView.class);
					int listId = v.getId();
					b.putInt("listId", listId);
					intent.putExtras(b);
					startActivity(intent);
				}
			});
			
			singleList.addView(title);
			singleList.addView(numBooks);
			singleList.addView(checkBox);
			singleList.setLayoutParams(params);
			
			container.addView(singleList);
			previousId = bookList.getId();
		
		}
	}

	public void showList(View view) {
		Intent intent = new Intent(this, ListView.class);
		startActivity(intent);
	}
	
	public void createList(View view) {
		Intent intent = new Intent(this, CreateList.class);
		startActivity(intent);
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
	
	private CheckBox buildCheckBox(BookList bookList){
		CheckBox checkBox = new CheckBox(this);
		RelativeLayout.LayoutParams params = 
				new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		checkBox.setId(bookList.getId()+1000);
		checkBox.setLayoutParams(params);
		return checkBox;
	}
	
}
