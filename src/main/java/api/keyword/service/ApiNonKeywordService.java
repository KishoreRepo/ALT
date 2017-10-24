package api.keyword.service;

import testcase.RequestBase;

public class ApiNonKeywordService 
{
	public static void identifyRequestContentType(String inputContent, RequestBase requestBase)
	{
		if(inputContent.startsWith("{"))
		{
			ApiKeywordService.setRequestContentType(requestBase,"json");
		}
		if(inputContent.startsWith("<"))
		{
			ApiKeywordService.setRequestContentType(requestBase,"xml");
		}
	}
}
