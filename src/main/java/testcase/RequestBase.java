package testcase;

import java.util.HashMap;

import org.apache.http.HttpResponse;

public class RequestBase 
{
	private String requestBaseUrl;
	private String requestServiceUrl;
	private String requestMethod;
	private String requestEndPoint;
	private String requestContentType;
	private String responseContentType;
	private HttpResponse response;
	private StringBuffer responseContent;
	private String authenticationType;
	private String authenicationUser;
	private String authenicationPassword;
	public static HashMap<String, String> responseParameters = new HashMap<String,String>();
	
	public String getRequestContentType() {
		return requestContentType;
	}
	public void setRequestContentType(String requestContentType) {
		this.requestContentType = requestContentType;
	}
	public String getRequestEndPoint() {
		return requestEndPoint;
	}
	public void setRequestEndPoint(String requestEndPoint) {
		this.requestEndPoint = requestEndPoint;
	}
	public String getRequestBaseUrl() {
		return requestBaseUrl;
	}
	public void setRequestBaseUrl(String requestBaseUrl) {
		this.requestBaseUrl = requestBaseUrl;
	}
	public String getRequestServiceUrl() {
		return requestServiceUrl;
	}
	public void setRequestServiceUrl(String requestServiceUrl) {
		this.requestServiceUrl = requestServiceUrl;
	}
	public String getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	public HttpResponse getResponse() {
		return response;
	}
	public void setResponse(HttpResponse response) {
		this.response = response;
	}
	public StringBuffer getResponseContent() {
		return responseContent;
	}
	public void setResponseContent(StringBuffer responseContent) {
		this.responseContent= new StringBuffer();
		this.responseContent = responseContent;
	}
	public String getResponseParameters(String key) {
		return RequestBase.responseParameters.get(key);
	}
	public void setResponseParameters(String key, String value) {
		if(RequestBase.responseParameters.get(key)!=null && !RequestBase.responseParameters.get(key).equals(""))
			RequestBase.responseParameters.remove(key);
			
		RequestBase.responseParameters.put(key, value);
	}
	public String getResponseContentType() {
		return responseContentType;
	}
	public void setResponseContentType(String responseContentType) {
		this.responseContentType = responseContentType;
	}
	public String getAuthenticationType() {
		return authenticationType;
	}
	public void setAuthenticationType(String authenticationType) {
		this.authenticationType = authenticationType;
	}
	public String getAuthenicationUser() {
		return authenicationUser;
	}
	public void setAuthenicationUser(String authenicationUser) {
		this.authenicationUser = authenicationUser;
	}
	public String getAuthenicationPassword() {
		return authenicationPassword;
	}
	public void setAuthenicationPassword(String authenicationPassword) {
		this.authenicationPassword = authenicationPassword;
	}

}
