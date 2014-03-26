package com.objects.buk;

import android.graphics.drawable.Drawable;

public class Book {
	final private String title;
	final private String isbn;
	final private String author;
	private String description;
	private float price;
	private Drawable img;
	
	//set book information
	public Book(String bookName, String bookId, String writer){
		this.title = bookName;
		this.isbn = bookId;
		this.author = writer;
		
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Drawable getImg() {
		return img;
	}

	public void setImg(Drawable img) {
		this.img = img;
	}

	public String getAuthor() {
		return author;
	}

	public String getTitle() {
		return title;
	}

	public String getIsbn() {
		return isbn;
	}

}
