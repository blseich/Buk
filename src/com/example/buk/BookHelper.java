package com.example.buk;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.widget.Toast;

public class BookHelper {
	
	final static String URL = "https://www.googleapis.com/shopping/search";
	String upc = "/v1/public/products?country=US&q=691464717759&restrictBy=gtin=691464717759";
	String key = "&key=AIzaSyB6V5zkZ_quWmpOmCxJ-nI6_6gl6M_fE-4";
	
	private HttpClient client;
	private JSONObject json;
	
	public BookHelper() {
			client = new DefaultHttpClient();
			new Read().execute("items");
	}
	
	public JSONObject products(String upc)  throws ClientProtocolException, IOException, JSONException {     
	    StringBuilder url = new StringBuilder(URL); 
	    url.append(upc);
	    url.append(key);
	    HttpGet get = new HttpGet(url.toString());     
	    HttpResponse r = client.execute(get);   
	    int status = r.getStatusLine().getStatusCode(); 

	    if (status == 200) {

	        HttpEntity e = r.getEntity();         
	        String data = EntityUtils.toString(e);         
	        JSONObject timeline = new JSONObject(data); 

	        return timeline;     } 
	    else {         
	    	System.out.println("ERROR");
	        return null;     } 
	    }  

	public class Read extends AsyncTask<String, Integer, String> {      
	    @Override     
	    protected String doInBackground(String... params) {         
	        // TODO Auto-generated method stub         
	        try {             

	                 json = products(upc);

	            return json.getString(params[0]);         
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
	        return null;     
	    }  

	@Override 
	protected void onPostExecute(String result){     
	System.out.println(result); } 


	} 
}

