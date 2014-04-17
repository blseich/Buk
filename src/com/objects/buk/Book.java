package com.objects.buk;

//Creates a new class to store and retrieve book data
public class Book {
	final private int id;
	final private String title;
	final private String author;
	private String description;
	private String price;
	private String imgUrl;
	
	//set book information
	//id is required but not placed into database so when a new book is created id is set to 0
	public Book(int id, String bookName, String writer){
		this.id = id;
		this.title = bookName;
		this.author = writer;
		
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAuthor() {
		return author;
	}

	public String getTitle() {
		return title;
	}

	public int getId() {
		return id;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}


}
