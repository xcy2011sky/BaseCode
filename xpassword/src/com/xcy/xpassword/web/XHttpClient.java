package com.xcy.xpassword.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class XHttpClient {

	private String TAG="XHttpClient"; 
	private String baseUrl=null;
	private List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
	private HttpClient httpClient;

	
	public XHttpClient(String uri)
	{
		 httpClient = new DefaultHttpClient();
		 this.baseUrl=uri;
	}
	  
	public String Get()
	{
		String result=null;
		 
		//params.add(new BasicNameValuePair("param1", "中国"));  
		//params.add(new BasicNameValuePair("param2", "value2"));  
		  
		//对参数编码  
		//String param = URLEncodedUtils.format(params, "UTF-8");  
	
		//将URL与参数拼接  
		//HttpGet getMethod = new HttpGet(baseUrl + "?" + param);  
		HttpGet getMethod = new HttpGet(baseUrl);
		try {  
		    HttpResponse response = httpClient.execute(getMethod); //发起GET请求  
		  
		    Log.i(TAG, "resCode = " + response.getStatusLine().getStatusCode()); //获取响应码  
		    result=EntityUtils.toString(response.getEntity(), "utf-8");//获取服务器响应内容  
		    Log.i(TAG, "result = " + result);
		    
		} catch (ClientProtocolException e) {  
		    // TODO Auto-generated catch block  
		    e.printStackTrace();  
		} catch (IOException e) {  
		    // TODO Auto-generated catch block  
		    e.printStackTrace();  
		}  
		
		return result;
	}
	public boolean Post(String... mparams)
	{
		boolean result=false;
		
		String jsonString=buildJSON(mparams);
		params = new LinkedList<BasicNameValuePair>();  
		
		params.add(new BasicNameValuePair("jsonstring", jsonString));  
		              
		try {  
		    HttpPost postMethod = new HttpPost(baseUrl);  
		    postMethod.setEntity(new UrlEncodedFormEntity(params, "utf-8")); //将参数填入POST Entity中  
		                  
		    HttpResponse response = httpClient.execute(postMethod); //执行POST方法  
		    Log.i(TAG, "resCode = " + response.getStatusLine().getStatusCode()); //获取响应码 
		    Log.i(TAG,EntityUtils.toString(response.getEntity(), "utf-8")); //获取响应内容 
		    
		    if(response.getStatusLine().getStatusCode()==200)
		    	return true;
	
		                  
		} catch (UnsupportedEncodingException e) {  
		    // TODO Auto-generated catch block  
		    e.printStackTrace();  
		} catch (ClientProtocolException e) {  
		    // TODO Auto-generated catch block  
		    e.printStackTrace();  
		} catch (IOException e) {  
		    // TODO Auto-generated catch block  
		    e.printStackTrace();  
		}  
		
		return result;
	}
	
	public String buildJSON(String ...params)
	{
		String jsonresult =null;
		
		if(params==null) return null;
		
		  JSONObject object = new JSONObject();//创建一个总的对象，这个对象对整个json串  
	        try {  
	            JSONArray jsonarray = new JSONArray();//json数组，里面包含的内容为pet的所有对象  
	            JSONObject jsonObj = new JSONObject();//pet对象，json形式  
	            jsonObj.put("name", params[0]);
	            jsonObj.put("feedback", params[1]);
	            jsonObj.put("device", params[2]);
	             
	            jsonarray.put(jsonObj);//向json数组里面添加pet对象  
	            object.put("app", jsonarray);//向总对象里面添加包含pet的数组  
	                        jsonresult = object.toString();//生成返回字符串  
	        } catch (JSONException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	        Log.i(TAG, "生成的json串为:"+jsonresult);  
	      
		
		return jsonresult;
	}
	
	 // 普通Json数据解析 
    private void parseJson(String strResult) { 
        try { 
            JSONObject jsonObj = new JSONObject(strResult).getJSONObject("xpassword"); 
             
            String method = jsonObj.getString("method"); 
            String mac= jsonObj.getString("mac"); 
          
        } catch (JSONException e) { 
            System.out.println("Json parse error"); 
            e.printStackTrace(); 
        } 
    } 
    //解析多个数据的Json
    private void parseJsonMulti(String strResult) { 
        try { 
            JSONArray jsonObjs = new JSONObject(strResult).getJSONArray("xpassword"); 
            String s = ""; 
            for(int i = 0; i < jsonObjs.length() ; i++){ 
                JSONObject jsonObj = ((JSONObject)jsonObjs.opt(i)) 
                .getJSONObject("xpassword"); 
                String method = jsonObj.getString("method"); 
                String mac= jsonObj.getString("mac"); 
            } 
           // tvJson.setText(s); 
        } catch (JSONException e) { 
            System.out.println("Jsons parse error !"); 
            e.printStackTrace(); 
        } 
    } 
	
}
