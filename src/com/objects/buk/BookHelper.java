package com.objects.buk;

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
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;


//Book helper class structures an api call and retrieves search results.
public class BookHelper {
	
	//Structure the url for an api call
	final static String URL = "https://www.googleapis.com/books/v1/volumes";
	String upc = "/zyTCAlFPjgYC";
	String key = "?key=AIzaSyBkpidsGG3l5t_r5-TF3jXrby-nbnJ72WM";
	
	//open object necessary to make api call and retrieve results
	private HttpClient client;
	private JSONObject json;	
	private Book searchResult = null;
	
	public BookHelper() {
			client = new DefaultHttpClient();
	}
	
	//Places the keywords into the search field of the api call, executes a search, and returns a book object
	//populate with the results of this search
	public Book searchForBook(String keywords) {
		upc = "?q=" + keywords.replace(" ", "+").replaceAll("[/]", "\\/");
		key = "&key=AIzaSyBkpidsGG3l5t_r5-TF3jXrby-nbnJ72WM";
		fetchBook(upc);
		//new Read().execute("1");
		return searchResult;
	}
	
	//Makes the actual api call
	public JSONObject products(String upc)  throws ClientProtocolException, IOException, JSONException {     
	    StringBuilder url = new StringBuilder(URL); 
	    url.append(upc);
	    url.append(key);
	    String fullURL = url.toString();
	    HttpGet get = new HttpGet(fullURL);     
	    HttpResponse r = client.execute(get);   
	    int status = r.getStatusLine().getStatusCode();
	    JSONObject timeline = null;
	    
	    //If the call was made successfully return the JSONObject produced
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
    
		//Function called outside of this class to execute a keyword search
	     public void fetchBook(String upc){ 
	    	JSONObject volumeInfo = new JSONObject();
	    	JSONObject saleInfo = new JSONObject();
	    	try {             
	        	
	        	//Retrieve JSON Object
	            json = products(upc);
	            //If no objects are within the jsonobject the search returned no results
	            if (json.getString("totalItems").equals("0")){
	            	searchResult = new Book(0, "NO RESULTS FOUND", "");
	            	searchResult.setDescription(" ");
	            	searchResult.setPrice(" ");
	            	searchResult.setImgUrl(" ");
	            } 
	            //Else retrieve information from the first returned item and place the information
	            //into a new book object
	            else {
		            json = json.getJSONArray("items").getJSONObject(0);
		            
		            //Parse separate JSON Objects
		            volumeInfo = json.getJSONObject("volumeInfo");
		            saleInfo = json.getJSONObject("saleInfo");
		            //Parse JSON Objects into Book Object
		            searchResult = new Book(1, volumeInfo.getString("title"), volumeInfo.getJSONArray("authors").get(0).toString());
		            searchResult.setDescription(volumeInfo.getString("description"));
		            try {
		            	searchResult.setPrice(saleInfo.getJSONObject("listPrice").getString("amount"));
		            } catch (JSONException e) {
		            	searchResult.setPrice("Not For Sale");
		            }
		            try {
		            	searchResult.setImgUrl(volumeInfo.getJSONObject("imageLinks").getString("thumbnail"));
		            } 
		            //If there is no image url in the json object, set it to a static image url that says no image is available
		            catch (JSONException e) {
		            	searchResult.setImgUrl("https://cdn.dinafem.org/static/images/site/no-photo.jpg");
		            }
	            }
        	}catch (ClientProtocolException e) {             
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
	     
	     //Function to be called from outside the class
	     //Makes a network call to retrieve a drawable from the specified image url
	     public Drawable LoadImageFromWebOperations(String url) {
			    Drawable d;
	    	 	try {
			        InputStream is = (InputStream) new URL(url).getContent();
			        d = Drawable.createFromStream(is, "src name");
			    } catch (Exception e) {
			    	d = null;
			    }
			    return d;
			}

} 

