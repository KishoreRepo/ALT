package api.keyword.service;

import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import testcase.RequestBase;


public class Json 
{
	public static void setParamtersForJson(RequestBase request, StringBuffer response, HttpResponse httpResponse) throws JSONException
	{
	
		JSONObject obj = new JSONObject(response.toString());
		getValues(request,obj);
		
		/*Iterator<?> keys = obj.keys();
		
		while(keys.hasNext()) 
		{
			String key = (String)keys.next();
		    if (obj.get(key) instanceof JSONObject)
		    {
		    	JSONObject obj1 = obj.getJSONObject(key);
		    	getValues(request,obj1);
		    }
		    else if(obj.get(key) instanceof JSONArray)
		    {
		    	JSONArray arr = obj.getJSONArray(key);
		    	for (int i = 0; i < arr.length(); i++)
		    	{
			    	JSONArray jsonObject1 = (JSONArray) obj.get(key);
				    if(jsonObject1.get(i) instanceof JSONObject)
				    {
				    	getValues(request,(JSONObject)jsonObject1.get(i));				    	
				    }
			    	
		    	}
            }
		    else
		    	request.setResponseParameters(key, obj.get(key).toString());
		}*/
		
	}
	
	private static void getValues(RequestBase request, JSONObject obj) throws JSONException
	{
		Iterator<?> keys = obj.keys();

		while(keys.hasNext()) 
		{
			String key = (String)keys.next();
			request.setResponseParameters(key, obj.get(key).toString());
		    if (obj.get(key) instanceof JSONObject)
		    {
		    	request.setResponseParameters(key, obj.get(key).toString());
		    	getValues(request,obj.getJSONObject(key));
		    }
		    else if(obj.get(key) instanceof JSONArray)
		    {
		    	JSONArray arr = obj.getJSONArray(key);
		    	for (int i = 0; i < arr.length(); i++)
		    	{
		    		JSONArray jsonObject1 = (JSONArray) obj.get(key);
				    if(jsonObject1.get(i) instanceof JSONObject)
				    {
				    	request.setResponseParameters(key, jsonObject1.get(i).toString());
				    	getValues(request,(JSONObject)jsonObject1.get(i));			    	
				    }
				    else
				    	request.setResponseParameters(key, jsonObject1.get(i).toString());
		    	}
            }
		    //else
		    	//request.setResponseParameters(key, obj.get(key).toString());
		}
		
	}

}
