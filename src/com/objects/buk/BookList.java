package com.objects.buk;

import java.util.ArrayList;

public class BookList {
	
	private ArrayList<Book> list;
	private String listTitle;
	
	public BookList(String titleForList) {
		setListTitle(titleForList);
		list = new ArrayList<Book>();
	}

	public String getListTitle() {
		return listTitle;
	}

	public void setListTitle(String listTitle) {
		this.listTitle = listTitle;
	}

	public ArrayList<Book> getList() {
		return list;
	}

	public void setList(ArrayList<Book> list) {
		this.list = list;
	}
	
	public void addBook(Book book) {
		list.add(book);
	}
	
}
