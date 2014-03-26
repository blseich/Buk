package com.example.buk;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.fasterxml.jackson.core.*;
import com.google.api.services.books.Books;
import com.google.api.services.books.Books.Builder;

public class BookHelper {
	
	public BookHelper() {
		JsonFactory jsonFactory = new JacksonFactory();
		Builder bookBuilder = new Books.Builder(new NetHttpTransport(), jsonFactory, null);
		Books books = bookBuilder.build();
		System.out.println(books);
	}
}
