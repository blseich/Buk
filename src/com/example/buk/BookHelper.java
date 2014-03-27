package com.example.buk;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.objects.buk.Book;

public class BookHelper {
	
	final static String URL = "https://www.googleapis.com/books/v1/volumes";
	String upc = "/zyTCAlFPjgYC";
	String key = "?key=AIzaSyBkpidsGG3l5t_r5-TF3jXrby-nbnJ72WM";
	
	private HttpClient client;
	private JSONObject json;	
	private Book searchResult = null;
	
	public BookHelper() {
			client = new DefaultHttpClient();
	}
	
	public Book searchForBook(String keywords) {
		upc = "?q=" + keywords.replace(" ", "+").replaceAll("[/]", "\\/");
		key = "&key=AIzaSyBkpidsGG3l5t_r5-TF3jXrby-nbnJ72WM";
		fetchBook(upc);
		//new Read().execute("1");
		return searchResult;
	}
	
	public JSONObject products(String upc)  throws ClientProtocolException, IOException, JSONException {     
	    StringBuilder url = new StringBuilder(URL); 
	    url.append(upc);
	    url.append(key);
	    String fullURL = url.toString();
	    HttpGet get = new HttpGet(fullURL);     
	    HttpResponse r = client.execute(get);   
	    int status = r.getStatusLine().getStatusCode();
	    JSONObject timeline = null;

	    if (status == 200) {
	    	
	        HttpEntity e = r.getEntity();         
	        String data = EntityUtils.toString(e);         
	        timeline = new JSONObject(data);
	    } 
	    else {         
	    	System.out.println("ERROR");
	        }
	    return timeline;   
	    }  
    
	     public void fetchBook(String upc){ 
	    	String result = null;
	    	JSONObject volumeInfo = new JSONObject();
	    	JSONObject saleInfo = new JSONObject();
	    	JSONArray industryIdentifiers = new JSONArray();
	    	JSONArray authors = new JSONArray();
	        try {             
	        	
	        	//Retrieve JSON Object
	            json = products(upc);
	            json = json.getJSONArray("items").getJSONObject(0);
	            
	            //Parse separate JSON Objects
	            volumeInfo = json.getJSONObject("volumeInfo");
	            saleInfo = json.getJSONObject("saleInfo");
	            industryIdentifiers = volumeInfo.getJSONArray("industryIdentifiers");
	            
	            //Parse JSON Objects into Book Object
	            searchResult = new Book(1, volumeInfo.getString("title"), industryIdentifiers.getJSONObject(0).getString("identifier"), volumeInfo.getJSONArray("authors").get(0).toString());
	            searchResult.setDescription(volumeInfo.getString("description"));
	            try {
	            	searchResult.setPrice(saleInfo.getJSONObject("listPrice").getString("amount"));
	            } catch (JSONException e) {
	            	searchResult.setPrice("Not For Sale");
	            }
	            try {
	            	searchResult.setImgUrl(volumeInfo.getJSONObject("imageLinks").getString("thumbnail"));
	            } catch (JSONException e) {
	            	searchResult.setImgUrl("https://cdn.dinafem.org/static/images/site/no-photo.jpg");
	            }
	            //Result is set as SUCCESS causes crash if any exceptions pop up
	            result = "SUCCESS";
	            } catch (ClientProtocolException e) {             
	                // TODO Auto-generated catch block             
	                e.printStackTrace();        
	            } catch (IOException e) {             
	                // TODO Auto-generated catch block             
	                e.printStackTrace();         
	            } catch (JSONException e) {             
	                // TODO Auto-generated catch block            
	                e.printStackTrace();         
	            }      
	    }  
	     
	     public Drawable LoadImageFromWebOperations(String url) {
			    try {
			        InputStream is = (InputStream) new URL(url).getContent();
			        Drawable d = Drawable.createFromStream(is, "src name");
			        return d;
			    } catch (Exception e) {
			        return null;
			    }
			}

} 

