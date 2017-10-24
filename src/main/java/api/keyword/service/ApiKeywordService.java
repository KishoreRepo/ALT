package api.keyword.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import api.keyword.controller.ApiKeywordController;
import excel.runner.Driver;
import testcase.RequestBase;
import testcase.RequestData;

public class ApiKeywordService 
{
	public static void setParameter(List<RequestData> requestDataList, String key, String value)
	{
		RequestData newDataSet = new RequestData();
		newDataSet.setRequestDataInputType("parameter");
		newDataSet.setRequestDataInputKey(key);
		newDataSet.setRequestDataInputValue(value);
		requestDataList.add(newDataSet);
	}
	
	public static void setHeader(List<RequestData> requestDataList, String key, String value, String headerType)
	{
		RequestData newDataSet = new RequestData();
		newDataSet.setRequestDataInputType(headerType);
		newDataSet.setRequestDataInputKey(key);
		newDataSet.setRequestDataInputValue(value);
		requestDataList.add(newDataSet);
	}
	
	public static void setRequestMedia(List<RequestData> requestDataList, String key, String value)
	{
		RequestData newDataSet = new RequestData();
		newDataSet.setRequestDataInputType("media");
		newDataSet.setRequestDataInputKey(key);
		newDataSet.setRequestDataInputValue(value);
		requestDataList.add(newDataSet);
	}
	
	public static void setRequestMediaFromFile(List<RequestData> requestDataList, String key, String value, RequestBase requestBase) throws IOException
	{
		StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(new FileReader(value));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1)
        {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();

		RequestData newDataSet = new RequestData();
		newDataSet.setRequestDataInputType("media");
		newDataSet.setRequestDataInputKey("requestinput");
		newDataSet.setRequestDataInputValue(fileData.toString());
		requestDataList.add(newDataSet);
		
		ApiNonKeywordService.identifyRequestContentType(fileData.toString(), requestBase);
	}
	
	public static void setUrl(RequestBase requestBase, String inputValue)
	{
		requestBase.setRequestBaseUrl(inputValue);
	}
	
	public static void setServiceUrl(RequestBase requestBase, String inputValue)
	{
		requestBase.setRequestServiceUrl(inputValue);
	}
	
	public static void setMethod(RequestBase requestBase, String key, String inputValue)
	{
		requestBase.setRequestMethod(inputValue);
	}
	
	public static void requestSubmission(RequestBase request, List<RequestData> requestDataList) throws ClientProtocolException, IOException, JSONException
	{
		
		StringBuffer stringBuffer =  new StringBuffer();
		
		switch(request.getRequestMethod())
		{
			case "get":
				ApiKeywordService.setGetEndPoint(request,requestDataList);
				HttpGet requestHttpGet = new HttpGet(request.getRequestEndPoint());
				printRequestDump(requestHttpGet,null,requestDataList,request);
				stringBuffer = postRequest(request,null,requestHttpGet,requestDataList,"get");
				setParamters(request, stringBuffer,request.getResponse());
				break;
				
			case "post":
				ApiKeywordService.setPostEndPoint(request,requestDataList);
				HttpPost requestHttpPost = new HttpPost(request.getRequestEndPoint());
				requestHttpPost.setEntity(new UrlEncodedFormEntity(setParameterForPostRequest(requestDataList)));
				
				if(request.getRequestContentType()!=null && !request.getRequestContentType().equals(""))
					requestHttpPost.setEntity(ApiKeywordService.setRequestMedia(request,requestDataList));
				
				printRequestDump(null, requestHttpPost,requestDataList,request);
				stringBuffer = postRequest(request,requestHttpPost,null,requestDataList,"post");
				setParamters(request, stringBuffer,request.getResponse());
				break;
			
		}
		request.setResponseContent(stringBuffer);
		printResponseDump(stringBuffer, request.getResponse());
		System.out.println("-----------------------------------");
		System.out.println("Validations dump:");
		System.out.println("-----------------------------------");
	}
	
	private static StringBuffer postRequest(RequestBase request, HttpPost requestHttpPost, HttpGet requestHttpGet, List<RequestData> requestDataList, String requestType) throws ClientProtocolException, IOException
	{
		
		if(requestType.equalsIgnoreCase("post"))
		{
			HttpClient client = HttpClientBuilder.create().build();
			request.setResponse(client.execute(setHeadersForPost(requestHttpPost,requestDataList,request)));
		}
		else if(requestType.equalsIgnoreCase("get"))
		{
			HttpClient client = HttpClientBuilder.create().build();
			request.setResponse(client.execute(setHeadersForGet(requestHttpGet,requestDataList,request)));
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getResponse().getEntity().getContent()));
		String line = "";
		StringBuffer stringBuffer = new StringBuffer();
		while ((line = reader.readLine()) != null) 
		{
			stringBuffer.append(line);
		}
		
		return stringBuffer;
	}
	
	private static void setGetEndPoint(RequestBase request, List<RequestData> requestDataList)
	{
		String parameters = "?";
		
		for(RequestData requestDataIterator : requestDataList)
		{
			if(requestDataIterator.getRequestDataInputType().equalsIgnoreCase("parameter"))
			{
				parameters = parameters.concat(requestDataIterator.getRequestDataInputKey());
				parameters = parameters.concat("=");
				parameters = parameters.concat(requestDataIterator.getRequestDataInputValue());
				parameters= parameters.concat("&");
			}
		}
		
		request.setRequestEndPoint(request.getRequestBaseUrl()+request.getRequestServiceUrl()+parameters);
	}
	
	private static HttpGet setHeadersForGet(HttpGet requestHttpGet, List<RequestData> requestDataList, RequestBase requestBase)
	{
		for(RequestData requestDataIterator : requestDataList)
		{
			if(requestDataIterator.getRequestDataInputType().equalsIgnoreCase("header"))
			{
				requestHttpGet.addHeader(requestDataIterator.getRequestDataInputKey(), requestDataIterator.getRequestDataInputValue());
			}
			else if(requestDataIterator.getRequestDataInputType().equalsIgnoreCase(ApiKeywordController.BASIC_AUTHENTICATION_HEADER))
			{
				requestHttpGet.addHeader(BasicScheme.authenticate(
						new UsernamePasswordCredentials(requestBase.getAuthenicationUser(), requestBase.getAuthenicationPassword())
						, "UTF-8", false));
			}
		}
		return requestHttpGet;
	}
	
	private static HttpPost setHeadersForPost(HttpPost requestHttp, List<RequestData> requestDataList, RequestBase requestBase)
	{
		for(RequestData requestDataIterator : requestDataList)
		{
			if(requestDataIterator.getRequestDataInputType().equalsIgnoreCase("header"))
			{
				requestHttp.addHeader(requestDataIterator.getRequestDataInputKey(), requestDataIterator.getRequestDataInputValue());
			}
			else if(requestDataIterator.getRequestDataInputType().equalsIgnoreCase(ApiKeywordController.BASIC_AUTHENTICATION_HEADER))
			{
				requestHttp.addHeader(BasicScheme.authenticate(
						new UsernamePasswordCredentials(requestBase.getAuthenicationUser(), requestBase.getAuthenicationPassword())
						, "UTF-8", false));
			}
		}
		return requestHttp;
	}
	
	private static void setPostEndPoint(RequestBase request, List<RequestData> requestDataList)
	{
		request.setRequestEndPoint(request.getRequestBaseUrl()+request.getRequestServiceUrl());
	}
	
	private static List<NameValuePair> setParameterForPostRequest(List<RequestData> requestDataList)
	{
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		
		for(RequestData requestDataIterator : requestDataList)
		{
			if(requestDataIterator.getRequestDataInputType().equalsIgnoreCase("parameter"))
			{
				nameValuePairs.add(new BasicNameValuePair(requestDataIterator.getRequestDataInputKey(), requestDataIterator.getRequestDataInputValue()));
			}
		}
		return nameValuePairs;
	}
	
	private static StringEntity setRequestMedia(RequestBase request, List<RequestData> requestDataList) throws UnsupportedEncodingException
	{

		StringEntity input = new StringEntity("");
		
		for(RequestData requestDataIterator : requestDataList)
		{
			if(requestDataIterator.getRequestDataInputType().equalsIgnoreCase("media"))
			{
				input = new StringEntity(requestDataIterator.getRequestDataInputValue());
				input.setContentType(request.getRequestContentType());
				
			}
		}
		return input;
	}

	public static void setResponseContentType(RequestBase request)
	{
		if(request.getResponse().getEntity().getContentType()!=null)
		{
			if(request.getResponse().getEntity().getContentType().toString().toLowerCase().contains("xml"))
			{
				request.setResponseContentType("XML");
			}
			else if(request.getResponse().getEntity().getContentType().toString().toLowerCase().contains("json"))
			{
				request.setResponseContentType("JSON");
			}
		}
	}
	
	static void setRequestContentType(RequestBase request, String inputValue)
	{
		if(inputValue.toLowerCase().contains("xml"))
		{
			request.setRequestContentType("application/xml");
		}
		else if(inputValue.contains("json"))
		{
			request.setRequestContentType("application/json");
		}
	}
	
		
	public static void verifyResponseStatus(RequestBase request, String value) throws InvalidAttributesException
	{
		if(!Integer.toString(request.getResponse().getStatusLine().getStatusCode()).equals(value))
		{
			throw new InvalidAttributesException("validation failed, Expected: " + request.getResponse().getStatusLine().getStatusCode() + " , actual: " + value );
		}
		
		System.out.println("Response Status:"+request.getResponse().getStatusLine().getStatusCode());
	}
	
	public static void verifyParamterCount(RequestBase request, String value) throws InvalidAttributesException
	{
		if(!Integer.toString(RequestBase.responseParameters.size()).equals(value))
		{
			throw new InvalidAttributesException("validation failed, Expected: " + value + " , actual: " + RequestBase.responseParameters.size() );
		}
		System.out.println("Parameters identified:"+RequestBase.responseParameters.size());
	}
	
	public static void verifyDataType(String value) throws InvalidAttributesException
	{
		String[] dataType =  value.split(",");
		String key=dataType[0];
				
		if(value.equalsIgnoreCase("number"))
		{
			if(!StringUtils.isNumeric(RequestBase.responseParameters.get(key).replaceAll("\\.", "").replaceAll(",", "")))
					throw new InvalidAttributesException("validation failed, Expected: " + dataType[1] + " datatype and actual : " +  RequestBase.responseParameters.get(key));
		}
		else if(value.equalsIgnoreCase("string"))
		{
			if(StringUtils.isNumeric(RequestBase.responseParameters.get(key).replaceAll("\\.", "").replaceAll(",", "")))
				throw new InvalidAttributesException("validation failed, Expected: " + dataType[1] + " datatype and actual : " + RequestBase.responseParameters.get(key));
		}
		
		System.out.println("Data Type validation: " + key + "--" + dataType[1]);
		
	}
	
	public static void verifyText(String actualValue, String expectedValue) throws InvalidAttributesException
	{
		if(!actualValue.equalsIgnoreCase(expectedValue))
			throw new InvalidAttributesException("validation failed, Actual: " + actualValue + " , Expected: " + expectedValue );
	
		System.out.println("Application value, " + actualValue + " : " +expectedValue);

	}
	
	private static void setParamters(RequestBase request, StringBuffer response, HttpResponse httpResponse) throws JSONException
	{
		setResponseContentType(request);
		if(request.getResponseContentType()!=null && response!=null && !response.toString().equals(""))
		{
			if(request.getResponseContentType().equalsIgnoreCase("xml"))
			{
				XML.setParamtersForXML(request, response, httpResponse);
			}
			else if(request.getResponseContentType().equalsIgnoreCase("json"))
			{
				Json.setParamtersForJson(request, response, httpResponse);
			}
		}
	}
	
	public static void verifyResponseContentType(RequestBase request, String value) throws InvalidAttributesException
	{
		if(!request.getResponse().getEntity().getContentType().toString().contains(value))
		{
			throw new InvalidAttributesException("validation failed, Expected: " + request.getResponse().getEntity().getContentType().toString() + " , actual: " + value );
		}
		
		System.out.println("Content type identified:"+request.getResponse().getEntity().getContentType().toString());
	}
	
	private static void printRequestDump(HttpGet requestHttpGet, HttpPost requestHttpPost, List<RequestData> requestDataList, RequestBase request)
	{
		System.out.println("-----------------------------------");
		System.out.println("Request dump:");
		System.out.println("-----------------------------------");
		if(requestHttpPost!=null)
		{
			Driver.LOGGER.setDescription(requestHttpPost.toString());
			System.out.println(requestHttpPost);			
		}
		else
		{
			Driver.LOGGER.setDescription(requestHttpGet.toString());
			System.out.println(requestHttpGet);
		}
	
		for(RequestData requestData:requestDataList)
		{
			System.out.println(requestData.getRequestDataInputType() + "- (" + requestData.getRequestDataInputKey() + ":" + requestData.getRequestDataInputValue()+")");
		}
		
		//System.out.println(request.toString());
	}
	
	private static void printResponseDump(StringBuffer response, HttpResponse httpResponse)
	{
		System.out.println("-----------------------------------");
		System.out.println("Response dump:");
		System.out.println("-----------------------------------");
		
		if(httpResponse.toString()!=null & !httpResponse.toString().equals(""))
		{
			System.out.println("HTTP Response: " +httpResponse.toString());
		}
		
		if(response.toString()!=null & !response.toString().equals(""))
		{
			System.out.println("Response ::" +response.toString());
			
			//ApiTest.LOGGER.setDescription(ApiTest.LOGGER.getDescription() + "                         Response:                      " + response.toString());
			
			for (String key : RequestBase.responseParameters.keySet()) 
			{
				System.out.println("Parameter - (" + key + ":" +RequestBase.responseParameters.get(key) + ")");
			}
		}
	}
	
}
