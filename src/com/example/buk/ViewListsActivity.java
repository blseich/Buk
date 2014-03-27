package com.example.buk;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.objects.buk.Book;
import com.objects.buk.BookList;

public class ViewListsActivity extends Activity {

	public List<BookList> shelf = new ArrayList<BookList>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_lists);

		BookList bookList1 = new BookList(1, "Book List 1");
		for (int i = 0; i < 5; i++) {
			Book temp = new Book(i, "title" + i, "isbn" + i, "author" + i);
			bookList1.addBook(temp);
		}
		BookList bookList2 = new BookList(2, "Book List 2");
		for (int i = 5; i < 10; i++) {
			Book temp = new Book(i, "title" + i, "isbn" + i, "author" + i);
			bookList2.addBook(temp);
		}
		BookList bookList3 = new BookList(3, "Book List 3");
		for (int i = 10; i < 15; i++) {
			Book temp = new Book(i, "title" + i, "isbn" + i, "author" + i);
			bookList3.addBook(temp);
		}
		shelf.add(bookList1);
		shelf.add(bookList2);
		shelf.add(bookList3);

	}

	public void showList(View view) {
		Intent intent = new Intent(this, ListView.class);
		startActivity(intent);
	}
	
	public void createList(View view) {
		Intent intent = new Intent(this, CreateList.class);
		startActivity(intent);
	}

}
