package com.objects.buk;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;

public class Book {
	final private String id;
	final private String title;
	final private String isbn;
	final private String author;
	private String description;
	private String price;
	private JSONObject imgUrls;
	private Drawable thumbnail;
	
	//set book information
	public Book(String id, String bookName, String bookId, String writer){
		this.id = id;
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getUrl(String size) {
		String result = "http://www.smokyfrontier.com/content/images/no-image-available.jpg";
		if (size.equals("thumbnail") || size.equals("small")
				|| size.equals("medium") || size.equals("large")
				|| size.equals("extraLarge")){
			try {
				result = imgUrls.getString(size);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public void setImgUrls(JSONObject imgUrls) {
		this.imgUrls = imgUrls;
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

	public Drawable getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Drawable thumbnail) {
		this.thumbnail = thumbnail;
	}

}
