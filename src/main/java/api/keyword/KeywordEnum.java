package api.keyword;

public enum KeywordEnum 
{
	BASEURL("BASEURL"),
	SERVICE("service"),
	REQUESTMETHOD("METHOD"),
	REQUESTHEADER("REQUESTHEADER"), 
	REQUESTPARAMETER("REQUESTPARAMETER"),
	REQUESTSUBMISSION("submit"),
	REQUESTMEDIA("requestmedia"),
	REQUESTMEDIAFILE("requestmediafile"),
	SETCONTENTTYPE("REQUESTCONTENTTYPE"),
	REPONSEFORMAT("RESPONSE"),
	VERIFYSTATUS("VERIFYSTATUS"),
	VERIFYRESPONSETYPE("VERIFYRESPONSETYPE"),
	VERIFYPARAMETERCOUNT("VERIFYPARAMETERCOUNT"),
	VERIFYDATATYPE("VERIFYDATATYPE"),
	REQUEST_CONTENT("REQUESTCONTENT"),
	VERIFYTEXT("VERIFYTEXT"),
	PREMPTIVEAUTHENTICATION("PREMPTIVEAUTHENTICATION"),
	OPENAPP("LAUNCHURL"),
	INPUTTEXT("INPUT"),
	CLICK("CLICK"),
	VERIFYVALUE("VERIFYVALUE"),
	SELECTFROMLIST("SELECTFROMLIST"),
	STORE("STORE"),
	ALERTOK("ALERTOK"),
	SELECTCHECKBOX("SELECTCHECKBOX"),
	WAIT("WAIT"),
	IFCLICK("IFCLICK");
	
	private String value;
	
	KeywordEnum(String value)
	{
		this.value = value;
	}
	
	 private String getKeyword() 
	 {
		 return value;
	 }
	 
	 public static KeywordEnum getKeywordValue(String input)
	 {
		 for (KeywordEnum keyword : KeywordEnum.values()) 
		 {
			 if(keyword.getKeyword().equalsIgnoreCase(input))
			 {
				 return keyword;
			 }
		 }
		 
		 throw new IllegalArgumentException("Keyword provided is not valid: " + input);
	 }
}
