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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_lists);
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
