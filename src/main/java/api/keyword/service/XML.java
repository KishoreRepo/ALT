package api.keyword.service;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import testcase.RequestBase;


public class XML 
{
	public static void setParamtersForXML(RequestBase request, StringBuffer response, HttpResponse httpResponse) throws JSONException
	{
	
		Document xml = convertStringToDocument(response.toString());
		
        Node user = xml.getFirstChild();
        subElements(request, user);

	}
	
    private static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(
                    xmlStr)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static void subElements(RequestBase request, Node child)
    {
        if(child.getChildNodes().getLength()>1)
        {
        	NodeList chilnodes = child.getChildNodes();
        	 for (int i = 0; i < chilnodes.getLength(); i++) {
        		 child = chilnodes.item(i);
             	subElements(request, child);
             	request.setResponseParameters(child.getNodeName(), child.getTextContent());
        	 }

        }
        else
        {
        	request.setResponseParameters(child.getNodeName(), child.getTextContent());
        }
    }
}
