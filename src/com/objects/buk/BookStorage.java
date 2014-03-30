package com.objects.buk;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookStorage extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 2;
	
	// Database Name
	private static final String DATABASE_NAME = "BOOK_STORAGE";
	
	//Book List Table Name
	private static final String BOOK_LIST_TABLE_NAME = "BOOKLIST";
	
	// Book List Table Column Names
	private static final String KEY_BOOK_LIST_ID = "BookListId";
	private static final String KEY_BOOK_LIST_NAME = "Name";
	
	// Book List Table Create SQLite Statement
	private static final String BOOK_LIST_TABLE_CREATE = 
			"CREATE TABLE " + BOOK_LIST_TABLE_NAME + " (" +
					KEY_BOOK_LIST_ID + " INTEGER PRIMARY KEY," +
					KEY_BOOK_LIST_NAME + " TEXT" + ")";
	
	// Book Table Name
	private static final String BOOK_TABLE_NAME = "BOOK";
	
	// Book Table Column Names
	private static final String KEY_BOOK_ID = "BookId";
	private static final String KEY_TITLE = "Title";
	private static final String KEY_AUTHOR = "Author";
	private static final String KEY_PRICE = "Price";
	private static final String KEY_DESCRIPTION = "Description";
	private static final String KEY_IMG_URL = "ImgUrl";
	
	private static final String BOOK_TABLE_CREATE =
			"CREATE TABLE " + BOOK_TABLE_NAME + " (" +
					KEY_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
					KEY_TITLE + " TEXT," + KEY_AUTHOR + " TEXT," +
					KEY_PRICE + " TEXT," + KEY_DESCRIPTION + " TEXT," +
					KEY_IMG_URL + " TEXT," +
					KEY_BOOK_LIST_ID + " INTEGER," +
					" FOREIGN KEY (" + KEY_BOOK_LIST_ID + ") REFERENCES "
					+ BOOK_LIST_TABLE_NAME + " (" + KEY_BOOK_LIST_ID + "))"; 
	
	public BookStorage (Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(BOOK_LIST_TABLE_CREATE);
		db.execSQL(BOOK_TABLE_CREATE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + BOOK_LIST_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + BOOK_TABLE_NAME);
		
		onCreate(db);
	}
	
	//Add new Book List
	public void addBookList(BookList bookList) {
		SQLiteDatabase db = this.getWritableDatabase();
		long newRowId;
		
		ContentValues values = new ContentValues();
		values.put(KEY_BOOK_LIST_NAME, bookList.getListTitle());
		newRowId = db.insert(BOOK_LIST_TABLE_NAME, null, values);
		
		if (newRowId > -1) {
			for (Book book : bookList.getList()) {
				ContentValues bookValues = new ContentValues();
				bookValues.put(KEY_TITLE, book.getTitle());
				bookValues.put(KEY_AUTHOR, book.getAuthor());
				bookValues.put(KEY_PRICE, book.getPrice());
				bookValues.put(KEY_DESCRIPTION, book.getDescription());
				bookValues.put(KEY_IMG_URL, book.getImgUrl());
				bookValues.put(KEY_BOOK_LIST_ID, newRowId);
				db.insert(BOOK_TABLE_NAME, null, bookValues);
			}
		}
		
		db.close();
	}
	
	//Get single Book List
	public BookList getBookList(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(BOOK_LIST_TABLE_NAME, 
				new String[]{ KEY_BOOK_LIST_ID, KEY_BOOK_LIST_NAME },
				KEY_BOOK_LIST_ID + "=?",
				new String[] { String.valueOf(id)},
				null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		BookList bookList = new BookList(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
		bookList.setList(getAllBooksInBookList(Integer.parseInt(cursor.getString(0))));
		return bookList;
	}
	
	//Get All Book Lists
	public List<BookList> getAllBookLists(){
		List<BookList> allBookLists = new ArrayList<BookList>();
		//Select All Query
		String selectQuery = "SELECT * FROM " + BOOK_LIST_TABLE_NAME;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()) {
			do {
				BookList bookList = new BookList(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
				allBookLists.add(bookList);
			} while (cursor.moveToNext());
		}
		
		return allBookLists;
	}
	
	//Update Book List
	public int updateBookList(BookList bookList){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_BOOK_LIST_NAME, bookList.getListTitle());
		
		return db.update(BOOK_LIST_TABLE_NAME, values, KEY_BOOK_LIST_ID + " = ?",
				new String[] {String.valueOf(bookList.getId())});
	}
	
	//Delete Book List
	public void deleteBookList(BookList bookList){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(BOOK_LIST_TABLE_NAME, KEY_BOOK_LIST_ID + " = ?",
				new String[] {String.valueOf(bookList.getId())});
		db.close();
	}
	
	//Get all Books in a particular List
	public ArrayList<Book> getAllBooksInBookList(int bookListId) {
		ArrayList<Book> bookList = new ArrayList<Book>();
		SQLiteDatabase db = this.getWritableDatabase();
		
		String selectQuery = 
				"SELECT * FROM " + BOOK_TABLE_NAME +
				" WHERE " + KEY_BOOK_LIST_ID + " = " + Integer.toString(bookListId);
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do {
				Book book = new Book(
						Integer.parseInt(cursor.getString(0)),
						cursor.getString(cursor.getColumnIndex(KEY_TITLE)), 
						cursor.getString(cursor.getColumnIndex(KEY_AUTHOR)));
				book.setPrice(cursor.getString(cursor.getColumnIndex(KEY_PRICE)));
				book.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
				book.setImgUrl(cursor.getString(cursor.getColumnIndex(KEY_IMG_URL)));
				bookList.add(book);
			} while (cursor.moveToNext());
		}
		
		return bookList;
	}
	
	public void addABook(Book book, long listId) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(KEY_TITLE, book.getTitle());
		values.put(KEY_AUTHOR, book.getAuthor());
		values.put(KEY_PRICE, book.getPrice());
		values.put(KEY_DESCRIPTION, book.getDescription());
		values.put(KEY_IMG_URL, book.getImgUrl());
		values.put(KEY_BOOK_LIST_ID, listId);
		db.insert(BOOK_TABLE_NAME, null, values);
	}
	
	public Book getABook(int bookId) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(BOOK_TABLE_NAME, 
				new String[]{ KEY_BOOK_ID, KEY_TITLE, KEY_AUTHOR, KEY_PRICE, KEY_DESCRIPTION, KEY_IMG_URL },
				KEY_BOOK_ID + "=?",
				new String[] { String.valueOf(bookId)},
				null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		Book book = new Book(
				Integer.parseInt(cursor.getString(cursor.getColumnIndex("KEY_BOOK_ID"))),
				cursor.getString(cursor.getColumnIndex("KEY_TITLE")),
				cursor.getString(cursor.getColumnIndex("KEY_AUTHOR")));
		book.setDescription(cursor.getString(cursor.getColumnIndex("KEY_DESCRIPTION")));
		book.setImgUrl(cursor.getString(cursor.getColumnIndex("KEY_IMG_URL")));
		book.setPrice(cursor.getString(cursor.getColumnIndex("KEY_PRICE")));
		return book;
	}
	
	public void deleteBook(Book book){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(BOOK_TABLE_NAME, KEY_BOOK_ID + " = ?",
				new String[] {String.valueOf(book.getId())});
		db.close();
	}
	
	//Update Book List
	public int updateBook(Book book, int listId){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, book.getTitle());
		values.put(KEY_AUTHOR, book.getAuthor());
		values.put(KEY_PRICE, book.getPrice());
		values.put(KEY_DESCRIPTION, book.getDescription());
		values.put(KEY_IMG_URL, book.getImgUrl());
		values.put(KEY_BOOK_LIST_ID, listId);
		
		return db.update(BOOK_LIST_TABLE_NAME, values, KEY_BOOK_LIST_ID + " = ?",
				new String[] {String.valueOf(book.getId())});
	}
	
}
