package com.codeandcoder.finalguide.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public class JSONParser extends Activity {
	static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    public static String user_id;
    public static int limit;
    public static ArrayList<String> subid= new ArrayList<String>(); 
    public static ArrayList<String> quizid= new ArrayList<String>(); 
    public static ArrayList<String> chapterid= new ArrayList<String>(); 
    
   // public static boolean status=false;
    public static String server_url="http://api.retailigence.com/v2.0/";
  
   /* String setCourseImageUrl(String img_name, int size)
    {
    	if(size==0)
    		return server_url+"ic.php?src="+img_name;
    	else
    		return server_url+"ic.php?src="+img_name+"&w="+size;
    }*/
    public String openConnection(String url, String methods)
    {
    	 try
         {
    		 if(methods=="POST"){
             HttpClient httpclient = new DefaultHttpClient();
             HttpPost httppost = new HttpPost(server_url+url);
             /*if(loginKey())
 			{
 				httppost.addHeader("PHP_AUTH_USER", setValue());
 			}*/
             HttpResponse response = httpclient.execute(httppost); 
             HttpEntity entity = response.getEntity();
             is = entity.getContent();
            // Log.e("log_tag", "connection success ");
    		 }
    		 else
    		 {
    			 DefaultHttpClient httpClient = new DefaultHttpClient();
                 HttpGet httpGet = new HttpGet(server_url+url);
                /* if(loginKey())
     			{
     				httpGet.addHeader("PHP_AUTH_USER", setValue());
     			}*/
                 HttpResponse httpResponse = httpClient.execute(httpGet);
                 HttpEntity httpEntity = httpResponse.getEntity();
                 is = httpEntity.getContent();
                 
                 //Log.e("log_tag", "connection success ");
    		 }
         }
     catch(Exception e)
         {
           //  Log.e("log_tag", "Error in http connection "+e.toString());
         }
     
    	 
    	  try{
              BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
              StringBuilder sb = new StringBuilder();
              String line = null;
              while ((line = reader.readLine()) != null) 
              {
                      sb.append(line + "\n");
                     
              }
              is.close();
              reader.close();
              json=sb.toString();
            
          }
          catch(Exception e)
          {
            //Log.e("log_tag", "Error converting result "+e.toString());
          }
    	  return json;
    }
    
 /*   
    public String checkLogin(String url,String methods)

    {
    	 try
         {
    		 DefaultHttpClient httpClient = new DefaultHttpClient();
             HttpGet httpGet = new HttpGet(server_url+url);
             HttpResponse httpResponse = httpClient.execute(httpGet);
             HttpEntity httpEntity = httpResponse.getEntity();
             is = httpEntity.getContent();
             
             Log.e("log_tag", "connection success ");
    		
         }
     catch(Exception e)
         {
             Log.e("log_tag", "Error in http connection "+e.toString());
         }
     
    	 
    	  try{
              BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
              StringBuilder sb = new StringBuilder();
              String line = null;
              while ((line = reader.readLine()) != null) 
              {
                      sb.append(line + "\n");
                     
              }
              is.close();
              reader.close();
              json=sb.toString();
            
          }
          catch(Exception e)
          {
             Log.e("log_tag", "Error converting result "+e.toString());
          }
    	  return json;
    }*/
	
    public String openConnection(String url, String methods, ArrayList<NameValuePair> params )
    {
    	 try
         {
    		 if(methods=="POST"){
             HttpClient httpclient = new DefaultHttpClient();
            // Toast.makeText(getBaseContext(),"Connecting",Toast.LENGTH_SHORT).show();
             HttpPost httppost = new HttpPost(server_url+url);
            // Toast.makeText(getBaseContext(),"Connecting string",Toast.LENGTH_SHORT).show();
 			
             /*if(loginKey())
 			{
 				   httppost.addHeader("PHP_AUTH_USER", setValue());
 			}*/
             httppost.setEntity(new UrlEncodedFormEntity(params));
          
             HttpResponse response = httpclient.execute(httppost); 
           
             HttpEntity entity = response.getEntity();
             is = entity.getContent();
          
             
           // Toast.makeText(getBaseContext(),"Connecting success",Toast.LENGTH_SHORT).show();

           //  Log.e("log_tag", "connection success ");
    		 }
    		 else
    		 {
    			
    			 DefaultHttpClient httpClient = new DefaultHttpClient();
                 String paramString = URLEncodedUtils.format(params, "utf-8");
                 url += "?" + paramString;
               
                 HttpGet httpGet = new HttpGet(server_url+url);
                 Log.d("kkkkkkkkkkkkkkkkkkk",server_url+url);
                 HttpResponse httpResponse = httpClient.execute(httpGet);
               
                 HttpEntity httpEntity = httpResponse.getEntity();
                 is = httpEntity.getContent();
               
    		 }
         }
     catch(Exception e)
         {
            // Log.e("log_tag", "Error in http connection "+e.toString());
         }
     
    	 
    	  try{
              BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            
              StringBuilder sb = new StringBuilder();
              String line = null;
         
              while ((line = reader.readLine()) != null) 
              {
                      sb.append(line + "\n");
                     
              }
              is.close();
        
              reader.close();
        
              json=sb.toString();
            
          }
          catch(Exception e)
          {
             //Log.e("log_tag", "Error converting result "+e.toString());
          }
    
    	  return json;
    }
    public boolean openConnection(String url, String methods, JSONArray json )
    {
    	 try
         {
    		 if(methods=="POST"){
             HttpClient httpclient = new DefaultHttpClient();
             Toast.makeText(getBaseContext(),
						"Connecting",Toast.LENGTH_SHORT).show();
             HttpPost httppost = new HttpPost(server_url+url);
             Toast.makeText(getBaseContext(),
						"Connecting string",Toast.LENGTH_SHORT).show();
 			
             /*if(loginKey())
 			{
 				   httppost.addHeader("PHP_AUTH_USER", setValue());
 			}*/
             //httppost.setHeader("json",json.toString());
             httppost.getParams().setParameter("jsonpost",json);
             HttpResponse response = httpclient.execute(httppost); 
             HttpEntity entity = response.getEntity();
             is = entity.getContent();
             
             Toast.makeText(getBaseContext(),
						"Connecting success",Toast.LENGTH_SHORT).show();

             Log.e("log_tag", "connection success ");
    		 }
    		
         }
     catch(Exception e)
         {
            Log.e("log_tag", "Error in http connection "+e.toString());
         }
     
    	 return true;
    }
  public boolean checkStatus(String result)
    {
    	try{
    		JSONObject jObject=new JSONObject(result);
    		
        String ch = jObject.getString("status");
        if(ch.equalsIgnoreCase("true"))
        	{
            return true;}
        else
        	return false;
    	}
    	  catch(JSONException e)
          {
                  Log.e("log_tag", "Error parsing data "+e.toString());
          }
          return false;
    }
  
  
  public boolean checkStatus1(String result)
  {
  	try{
  		JSONArray jArray=new JSONArray(result);
  		JSONObject JObject = jArray.getJSONObject(0);
        String ch = JObject.getString("status");
	      if(ch.equalsIgnoreCase("true"))
	      	{Log.d("1111",ch.toString());
	          return true;}
	      else
	      	return false;
  		}
  	  catch(JSONException e)
        {
                Log.e("log_tag1", "Error parsing data "+e.toString());
        }
        return false;
  }
    public boolean isEmailValid(String email) { 
        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
               isValid = true;
        }
        return isValid;
    }
    String path=System.getenv("EXTERNAL_STORAGE");
	   
    /*public boolean isLogin()
    {
    	
    	File f=new File(path+"/config.txt");
    	if(f.exists()){
    		String s=readFromFile();
    	if(s!="")
    		{
    			String str=checkLogin("is-user/"+s,"GET");
    			try{
    				JSONObject oj=new JSONObject(str);
    				if(oj.getString("status").equalsIgnoreCase("true"))
    				{ 
    					if(oj.getString("message").equalsIgnoreCase("ACCOUNT_INACTIVE"))
    					{
    						Intent i=new Intent(getBaseContext(),InactiveUser.class);
    						writeToFile("");
    						finish();
    						startActivity(i);
    					}
    					return true;
    				}
    			}catch(Exception e)
    			{ Log.e("Error",e.toString()); }
    		}
    	}
     return false;
    }
    public boolean loginKey()
    {
    	File f=new File(path+"/config.txt");
    	if(f.exists()){
    		if(readFromFile()!="")
    		return true;
    	}
    	return false;
    }
    
    public String setValue()
    {
    	return readFromFile();
    }
    public void logout()
    {
    	writeToFile("");
    }*/
   public void isSet(String key)
    {
    	writeToFile(key);    	
    }
    private void writeToFile(String data) {
        try {
        	  	    File myFile = new File(path+"/config.txt");  
                    myFile.createNewFile();  
                    FileOutputStream fOut = new FileOutputStream(myFile);  
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);  
                    myOutWriter.append(data);  
                    myOutWriter.close();  
                    fOut.close();  
                      
         } 
         catch (FileNotFoundException e) {e.printStackTrace();}  
         catch (IOException e) {
           // Log.e("Exception", "File write failed: " + e.toString());
        } 
    }
	
    
   /* private String readFromFile() {
    	
   	 
        String aDataRow = "";  
        String aBuffer = "";  
        try {  
            File myFile = new File(path+"/config.txt");  
            FileInputStream fIn = new FileInputStream(myFile);  
            BufferedReader myReader = new BufferedReader(  
                    new InputStreamReader(fIn));  
              
            while ((aDataRow = myReader.readLine()) != null) {  
                aBuffer = aDataRow.trim() ;  
            }  
            myReader.close();  
              
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
  	return aBuffer.trim();

  }*/
}
