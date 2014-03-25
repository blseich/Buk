package com.example.buk;

public class Book {
	public String title;
	public double isbn;
	public String author;

	//set book information
	public void setBookInfo(String bookName, double bookId, String writer){
		this.title = bookName;
		this.isbn = bookId;
		this.author = writer;
		
	}
	
	public void changeTitle(String newTitle)
	{
		this.title = newTitle;
	}
}
