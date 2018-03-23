package edu.gmu.csiss.onas.ogc;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import edu.gmu.csiss.onas.util.BaseTool;
import edu.gmu.csiss.onas.util.MyHttpUtils;
import edu.gmu.csiss.onas.util.SOAPUtil;
import net.opengis.wcs.v_2_0.CoverageDescriptionsType;
import net.opengis.wfs.v_2_0.DescribeFeatureTypeType;
import net.opengis.wfs.v_2_0.FeatureTypeListType;
import net.opengis.wfs.v_2_0.FeatureTypeType;
import net.opengis.wfs.v_2_0.WFSCapabilitiesType;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.ReferencingFactoryFinder;
/**
 * 
 * updated and tested by Ziheng Sun on 3/16/2016
 * 
 * updated by Ziheng Sun on 4/29/2016 - to make the plan clear
 * 
 * This class is for WFS 2.0. Pay attention to this. WFS 1.0 will be supported in another class.
 * 
 * @author Ziheng Sun
 * 
 */
public class WFSUtils {
	
	private static Logger theLogger = Logger.getLogger(WFSUtils.class);
	
	public static final String supported_version = "2.0";
	
	private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	private static SOAPUtil client = new SOAPUtil();
	
	/**
	 * Parse WFS capabilities document
	 * created by Ziheng Sun on 4/29/2016
	 * @param capability_doc_url
	 * @return
	 */
	public static WFSCapabilitiesType parseCapabilities(String capability_doc_url){
		WFSCapabilitiesType wct = null;
		try {
			String capa = MyHttpUtils.doGet(capability_doc_url);
			JAXBContext jaxbContext = null;
			wct = JAXB.unmarshal(new StringReader(capa), WFSCapabilitiesType.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getLocalizedMessage());
		}
		return wct;
	}
	
	/**
	 * Parse WFS capabilities document
	 * created by Ziheng Sun on 4/29/2016
	 * @param capability_doc_url
	 * @return
	 */
	public static WFSCapabilitiesType parseCapabilities_SOAP(String soap_capability_doc_url){
		WFSCapabilitiesType wct = null;
		try {			
			//client = new SOAPUtil();
	        client.setEndpoint(soap_capability_doc_url);
	        client.setSoapmessage("<soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:ns=\"http://www.opengis.net/wfs/2.0\" xmlns:ns1=\"http://www.opengis.net/ows/2.0\">\n" +
												"   <soapenv:Header/>\n" +
												"   <soapenv:Body>\n" +
												"      <ns:GetCapabilities version=\"2.0.0\" service=\"WFS\">\n" +
												"      </ns:GetCapabilities>\n" +
												"   </soapenv:Body>\n" +
												"</soapenv:Envelope>");
	        client.send2();
	        
	        String soapresp = client.getRespmessage();
	        
	        theLogger.info(soapresp);
	        
	        SAXReader reader = new SAXReader();
	        
	        
	        Map uris = new HashMap();
	        uris.put("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
	        uris.put("wfs", "http://www.opengis.net/wfs/2.0");
//	        XPath xpath = doc.createXPath("//soapenv:Envelop/soapenv:Body/wfs:WFS_Capabilities");
//	        xpath.setNamespaceURIs(uris);
//	        List<Node> nodes = xpath.selectNodes(doc);
	        
	        reader.getDocumentFactory().setXPathNamespaceURIs(uris);
	        
	        Document doc = reader.read(new StringReader(soapresp));
	        
	        //theLogger.info("Body Content XML:" + body);
	        String capa = doc.selectSingleNode("//soapenv:Envelope/soapenv:Body/wfs:WFS_Capabilities").asXML();
	        
	        theLogger.info(capa);
	        
	        capa = capa.replace("<Name>", "<Name   xmlns:testbed12=\"http://polar.geodacenter.org/testbed12\">");
	        
	        //String capa = MyHttpUtils.doGet(body);
			JAXBContext jaxbContext = null;
			wct = JAXB.unmarshal(new StringReader(capa), WFSCapabilitiesType.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getLocalizedMessage());
		}
		return wct;
	}
	
	/**
	 * Parse WFS capabilities document
	 * created by Ziheng Sun on 4/29/2016
	 * @param capability_doc_url
	 * @return
	 */
	public static WFSCapabilitiesType parseCapabilities_SOAP_Security(String soap_capability_doc_url){
		WFSCapabilitiesType wct = null;
		try {			
			//SOAPUtil client = new SOAPUtil();
	        client.setEndpoint(soap_capability_doc_url);
	        String msg = "<soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:ns=\"http://www.opengis.net/wfs/2.0\" xmlns:ns1=\"http://www.opengis.net/ows/2.0\">\n" +
					"<soapenv:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"><wsa:To>https://tb12.secure-dimensions.com/soap/services/ows_proxy</wsa:To><wsa:Action>http://www.opengis.net/wfs/requests#GetCapabilities</wsa:Action><wsa:MessageID>urn:uuid:ae6c2f16-c6a1-1e51-226b-002522163135</wsa:MessageID><wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" soapenv:mustUnderstand=\"1\" xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\"><wsse:BinarySecurityToken EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" wsu:Id=\"CertID-ae6d196c-c6a1-1e51-226c\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\">MIIDDDCCAfSgAwIBAgIQM6YEf7FVYx/tZyEXgVComTANBgkqhkiG9w0BAQUFADAwMQ4wDAYDVQQKDAVPQVNJUzEeMBwGA1UEAwwVT0FTSVMgSW50ZXJvcCBUZXN0IENBMB4XDTA1MDMxOTAwMDAwMFoXDTE4MDMxOTIzNTk1OVowQjEOMAwGA1UECgwFT0FTSVMxIDAeBgNVBAsMF09BU0lTIEludGVyb3AgVGVzdCBDZXJ0MQ4wDAYDVQQDDAVBbGljZTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAoqi99By1VYo0aHrkKCNT4DkIgPL/SgahbeKdGhrbu3K2XG7arfD9tqIBIKMfrX4Gp90NJa85AV1yiNsEyvq+mUnMpNcKnLXLOjkTmMCqDYbbkehJlXPnaWLzve+mW0pJdPxtf3rbD4PS/cBQIvtpjmrDAU8VsZKT8DN5Kyz+EZsCAwEAAaOBkzCBkDAJBgNVHRMEAjAAMDMGA1UdHwQsMCowKKImhiRodHRwOi8vaW50ZXJvcC5iYnRlc3QubmV0L2NybC9jYS5jcmwwDgYDVR0PAQH/BAQDAgSwMB0GA1UdDgQWBBQK4l0TUHZ1QV3V2QtlLNDm+PoxiDAfBgNVHSMEGDAWgBTAnSj8wes1oR3WqqqgHBpNwkkPDzANBgkqhkiG9w0BAQUFAAOCAQEABTqpOpvW+6yrLXyUlP2xJbEkohXHI5OWwKWleOb9hlkhWntUalfcFOJAgUyH30TTpHldzx1+vK2LPzhoUFKYHE1IyQvokBN2JjFO64BQukCKnZhldLRPxGhfkTdxQgdf5rCK/wh3xVsZCNTfuMNmlAM6lOAg8QduDah3WFZpEA0s2nwQaCNQTNMjJC8tav1CBr6+E5FAmwPXP7pJxn9Fw9OXRyqbRA4v2y7YpbGkG2GI9UvOHw6SGvf4FRSthMMO35YbpikGsLix3vAsXWWi4rwfVOYzQK0OFPNi9RMCUdSH06m9uLWckiCxjos0FQODZE9l4ATGy9s9hNVwryOJTw==</wsse:BinarySecurityToken><wsse:UsernameToken xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" wsu:Id=\"SigID-ae6d8e9c-c6a1-1e51-226d\"><wsse:Username>Alice</wsse:Username><wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">+43faXkM59Wvc/1EqkhqFYGJwOs=</wsse:Password><wsse:Nonce>p3aGzO+yN4I8oTehOJTY3ZrIcYsM6XJ8</wsse:Nonce><wsu:Created>2016-01-29T16:02:22.307Z</wsu:Created></wsse:UsernameToken><ds:Signature xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" Id=\"SigID-ae6db5ac-c6a1-1e51-226e\"><ds:SignedInfo><ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></ds:CanonicalizationMethod><ds:SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"></ds:SignatureMethod><ds:Reference URI=\"#SigID-ae6d8e9c-c6a1-1e51-226d\"><ds:Transforms><ds:Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"></ds:DigestMethod><ds:DigestValue>sk06OjMEwRt9U0PZYPTR3CVycL4=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>bcI24Ds8MvM7dvgAC3WeO+uv9zKAq6yB3qdI02t8I1h7W8GVBrPYs4f7a9JdELtV0UsA1NUOp4nbPwtPUdw+nVURHQUHr+mZ879HZuzD3daW5Z3kTk9i1+6ceCbZh/K/SRCFOQvDpKRC74W5iTkik7TBALgPl7sJxVt7nY6GBhs=</ds:SignatureValue><ds:KeyInfo><wsse:SecurityTokenReference><wsse:Reference URI=\"#CertID-ae6d196c-c6a1-1e51-226c\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\"></wsse:Reference></wsse:SecurityTokenReference></ds:KeyInfo></ds:Signature></wsse:Security></soapenv:Header>" +
					"   <soapenv:Body>\n" +
					"      <ns:GetCapabilities version=\"2.0.0\" service=\"WFS\">\n" +
					"      </ns:GetCapabilities>\n" +
					"   </soapenv:Body>\n" +
					"</soapenv:Envelope>";
	        theLogger.info("SOAP Security Request: " + msg);
	        client.setSoapmessage(msg);
	        client.send2();
	        
	        String soapresp = client.getRespmessage();
	        
	        theLogger.info(soapresp);
	        
	        SAXReader reader = new SAXReader();
	        
	        
	        Map uris = new HashMap();
	        uris.put("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
	        uris.put("wfs", "http://www.opengis.net/wfs/2.0");
//	        XPath xpath = doc.createXPath("//soapenv:Envelop/soapenv:Body/wfs:WFS_Capabilities");
//	        xpath.setNamespaceURIs(uris);
//	        List<Node> nodes = xpath.selectNodes(doc);
	        
	        reader.getDocumentFactory().setXPathNamespaceURIs(uris);
	        
	        soapresp = soapresp.replace("soapenv:mustUnderstand=\"1\"/>", "/>").replace("cw:","cw-");
	        Document doc = reader.read(new StringReader(soapresp));
	        
	        //theLogger.info("Body Content XML:" + body);
	        String capa = doc.selectSingleNode("//soapenv:Envelope/soapenv:Body/wfs:WFS_Capabilities").asXML();
	        
	        theLogger.info(capa);
	        
	        
	        theLogger.info(capa);
	        //String capa = MyHttpUtils.doGet(body);
			JAXBContext jaxbContext = null;
			wct = JAXB.unmarshal(new StringReader(capa), WFSCapabilitiesType.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getLocalizedMessage());
		}
		return wct;
	}

	public static WFSCapabilitiesType parseCapabilities_SOAP_Security_BAK(String soap_capability_doc_url){
		WFSCapabilitiesType wct = null;
		try {			
			//SOAPUtil client = new SOAPUtil();
	        client.setEndpoint(soap_capability_doc_url);
	        client.setSoapmessage("<soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:ns=\"http://www.opengis.net/wfs/2.0\" xmlns:ns1=\"http://www.opengis.net/ows/2.0\">\n" +
	        									"<soapenv:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"><wsa:To>https://tb12.secure-dimensions.com/soap/services/ows_proxy</wsa:To><wsa:Action>http://www.opengis.net/wfs/requests#GetCapabilities</wsa:Action><wsa:MessageID>urn:uuid:ae6c2f16-c6a1-1e51-226b-002522163135</wsa:MessageID><wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" soapenv:mustUnderstand=\"1\"><wsse:BinarySecurityToken EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" wsu:Id=\"CertID-ae6d196c-c6a1-1e51-226c\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\">MIIDDDCCAfSgAwIBAgIQM6YEf7FVYx/tZyEXgVComTANBgkqhkiG9w0BAQUFADAwMQ4wDAYDVQQKDAVPQVNJUzEeMBwGA1UEAwwVT0FTSVMgSW50ZXJvcCBUZXN0IENBMB4XDTA1MDMxOTAwMDAwMFoXDTE4MDMxOTIzNTk1OVowQjEOMAwGA1UECgwFT0FTSVMxIDAeBgNVBAsMF09BU0lTIEludGVyb3AgVGVzdCBDZXJ0MQ4wDAYDVQQDDAVBbGljZTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAoqi99By1VYo0aHrkKCNT4DkIgPL/SgahbeKdGhrbu3K2XG7arfD9tqIBIKMfrX4Gp90NJa85AV1yiNsEyvq+mUnMpNcKnLXLOjkTmMCqDYbbkehJlXPnaWLzve+mW0pJdPxtf3rbD4PS/cBQIvtpjmrDAU8VsZKT8DN5Kyz+EZsCAwEAAaOBkzCBkDAJBgNVHRMEAjAAMDMGA1UdHwQsMCowKKImhiRodHRwOi8vaW50ZXJvcC5iYnRlc3QubmV0L2NybC9jYS5jcmwwDgYDVR0PAQH/BAQDAgSwMB0GA1UdDgQWBBQK4l0TUHZ1QV3V2QtlLNDm+PoxiDAfBgNVHSMEGDAWgBTAnSj8wes1oR3WqqqgHBpNwkkPDzANBgkqhkiG9w0BAQUFAAOCAQEABTqpOpvW+6yrLXyUlP2xJbEkohXHI5OWwKWleOb9hlkhWntUalfcFOJAgUyH30TTpHldzx1+vK2LPzhoUFKYHE1IyQvokBN2JjFO64BQukCKnZhldLRPxGhfkTdxQgdf5rCK/wh3xVsZCNTfuMNmlAM6lOAg8QduDah3WFZpEA0s2nwQaCNQTNMjJC8tav1CBr6+E5FAmwPXP7pJxn9Fw9OXRyqbRA4v2y7YpbGkG2GI9UvOHw6SGvf4FRSthMMO35YbpikGsLix3vAsXWWi4rwfVOYzQK0OFPNi9RMCUdSH06m9uLWckiCxjos0FQODZE9l4ATGy9s9hNVwryOJTw==</wsse:BinarySecurityToken><wsse:UsernameToken xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" wsu:Id=\"SigID-ae6d8e9c-c6a1-1e51-226d\"><wsse:Username>Alice</wsse:Username><wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">+43faXkM59Wvc/1EqkhqFYGJwOs=</wsse:Password><wsse:Nonce>p3aGzO+yN4I8oTehOJTY3ZrIcYsM6XJ8</wsse:Nonce><wsu:Created>2016-01-29T16:02:22.307Z</wsu:Created></wsse:UsernameToken><ds:Signature xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" Id=\"SigID-ae6db5ac-c6a1-1e51-226e\"><ds:SignedInfo><ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></ds:CanonicalizationMethod><ds:SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"></ds:SignatureMethod><ds:Reference URI=\"#SigID-ae6d8e9c-c6a1-1e51-226d\"><ds:Transforms><ds:Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"></ds:DigestMethod><ds:DigestValue>sk06OjMEwRt9U0PZYPTR3CVycL4=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>bcI24Ds8MvM7dvgAC3WeO+uv9zKAq6yB3qdI02t8I1h7W8GVBrPYs4f7a9JdELtV0UsA1NUOp4nbPwtPUdw+nVURHQUHr+mZ879HZuzD3daW5Z3kTk9i1+6ceCbZh/K/SRCFOQvDpKRC74W5iTkik7TBALgPl7sJxVt7nY6GBhs=</ds:SignatureValue><ds:KeyInfo><wsse:SecurityTokenReference><wsse:Reference URI=\"#CertID-ae6d196c-c6a1-1e51-226c\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\"></wsse:Reference></wsse:SecurityTokenReference></ds:KeyInfo></ds:Signature></wsse:Security></soapenv:Header>" +
												"   <soapenv:Body>\n" +
												"      <ns:GetCapabilities version=\"2.0.0\" service=\"WFS\">\n" +
												"      </ns:GetCapabilities>\n" +
												"   </soapenv:Body>\n" +
												"</soapenv:Envelope>");
	        client.send2();
	        String soapresp = client.getRespmessage();
	        String body = SOAPUtil.nodeToString(client.getResponse().getSOAPBody().getFirstChild());
	        theLogger.info("Body Content XML:" + body);
	        String capa = body;
	        //String capa = MyHttpUtils.doGet(body);
			JAXBContext jaxbContext = null;
			wct = JAXB.unmarshal(new StringReader(capa), WFSCapabilitiesType.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getLocalizedMessage());
		}
		return wct;
	}

	
	
	/**
	 * Get Feature List of WFS
	 * @param wct
	 * @return
	 */
	public static List<FeatureTypeType> getFeatureList(WFSCapabilitiesType wct ){
		List<FeatureTypeType> ftts = wct.getFeatureTypeList().getFeatureType();
		return ftts;
	}
	/**
	 * create a describefeaturetype request
	 * @param type
	 * @return
	 */
	public static DescribeFeatureTypeType createADescribeFeatureTypeRequest(String type){
		
		
		return null;
	}
	/**
	 * Parse describefeaturetype response
	 * @param xml
	 * @return
	 */
	public static FeatureTypeListType parseDescribeFeatureTypeResponse(String xml){
		
		
		return null;
	}
	

	/**
	 * @param wfsurl
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public static Vector<GeoFeature> getWFSFeatures20(String wfsurl, BBOX box) throws Exception {
		
		theLogger.info("BBox " + box.crs + box.maxx + ":" + box.minx);
		
		Vector<GeoFeature> featureVec = new Vector<GeoFeature>(); 
		wfsurl = wfsurl.trim();
		
		if(wfsurl.endsWith("?")||wfsurl.endsWith("&"))
    		wfsurl += "service=WFS&version=2.0.0&request=GetCapabilities";    		
    	else
    		wfsurl += "?service=WFS&version=2.0.0&request=GetCapabilities";
		
		String wfsstr = wfsurl;
		
		theLogger.info("WFS URL: " + wfsurl);
    	
//		Map connectionParameters = new HashMap();
		
//		connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", wfsurl);
		
		//updated by Z.S. on 8/11/2016
//		DataStore dstore = DataStoreFinder.getDataStore(connectionParameters);		
//		DataStore dstore = (new WFSDataStoreFactory()).createDataStore(connectionParameters);
		
//		String typeNames[] = dstore.getTypeNames();		
		
//		if(typeNames == null)
		
//			return null;
		
		String reqcrs[] = box.crs.split(":");
		
		CoordinateReferenceSystem reqepsg = ReferencingFactoryFinder.getCRSAuthorityFactory(reqcrs[0], null).createCoordinateReferenceSystem(reqcrs[1]);
		
		BoundingBox reqbound;
		//updated by Z.S. on 8/12/2016
		if(box.crs.equalsIgnoreCase("EPSG:4326"))
			reqbound = new ReferencedEnvelope(box.miny, box.maxy, box.minx, box.maxx, reqepsg);
		else
			reqbound = new ReferencedEnvelope(box.minx, box.maxx, box.miny, box.maxy, reqepsg);
		
		System.out.println("out bbox is " +box.toString());
		
		System.out.println("Required bbox is " +reqbound);
		
		System.out.println("Interscted WFS Layers are listed as");
		
		WFSCapabilitiesType wct = WFSUtils.parseCapabilities(wfsstr);
		
		FeatureTypeListType ftlt = wct.getFeatureTypeList();
		
		for(int i=0; i<ftlt.getFeatureType().size(); i++){			
			QName qn = ftlt.getFeatureType().get(i).getName();
		
			System.out.println("===============================\n" + qn.getPrefix()+ ":" + qn.getLocalPart());
			
			//FeatureSource<SimpleFeatureType, SimpleFeature> fsource = dstore.getFeatureSource(tname);
			
			//ReferencedEnvelope databounds = fsource.getBounds();					
			
			//BoundingBox datatarnsbounds = databounds.toBounds(reqepsg);												
			
			//if(datatarnsbounds != null) {	
				
				//System.out.println( "src Bounds:"+ databounds);
			
				//System.out.println( "src Trans Bounds:"+ datatarnsbounds);
				
				//System.out.println( "req Bounds:"+ reqbound);
				
				//if(!datatarnsbounds.intersects(reqbound)){
//				if(!(datatarnsbounds.intersects(reqbound)||datatarnsbounds.contains(reqbound)||reqbound.contains(datatarnsbounds))) {
				
					//System.out.println("Not intersected " + tname);
					
					//continue;
					
				
				
				GeoFeature feature = new GeoFeature();			
				
				feature.setName(qn.getPrefix()+ ":" + qn.getLocalPart());
				
				feature.setTitle(ftlt.getFeatureType().get(i).getTitle().get(0).getValue());
				
				//feature.setDescription(tname);
				
				//feature.setWfsUrl(wfsstr);
				
				BBOX fbox;
				
				if(box.crs.equalsIgnoreCase("EPSG:4326"))
				
					fbox = new BBOX(box.crs, reqbound.getMinY(), reqbound.getMinX(), reqbound.getMaxY(), reqbound.getMaxX());
				
				else
				
					fbox = new BBOX(box.crs, reqbound.getMinX(), reqbound.getMinY(), reqbound.getMaxX(), reqbound.getMaxY());
				
				System.out.println(ftlt.getFeatureType().get(i).getName().toString() + "--" +fbox.toString());
				
				feature.addBBOX(fbox);			
				
				featureVec.add(feature);
				
			}
			
		
		if(featureVec.size() > 0)
			
			return featureVec;
		
		else
			
			return null;
		
	
		
		
	}
	/**
	 * @param wfsurl
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public static Vector<GeoFeature> getWFSFeatures(String wfsurl, BBOX box) throws Exception {
		Vector<GeoFeature> featureVec = new Vector<GeoFeature>(); 
		wfsurl = wfsurl.trim();
		String wfsstr = wfsurl;
    	if(wfsurl.endsWith("?"))
    		wfsurl += "service=WFS&version=1.0.0&request=GetCapabilities";
    	else
    		wfsurl += "?service=WFS&version=1.0.0&request=GetCapabilities";
		
    	System.out.println("WFS URL: " + wfsurl);
    	
		Map connectionParameters = new HashMap();
		
		connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", wfsurl);
		
		//updated by Z.S. on 8/11/2016
//		DataStore dstore = DataStoreFinder.getDataStore(connectionParameters);		
		DataStore dstore = (new WFSDataStoreFactory()).createDataStore(connectionParameters);
		
		String typeNames[] = dstore.getTypeNames();		
		
		if(typeNames == null)
		
			return null;
		
		String reqcrs[] = box.crs.split(":");
		
		CoordinateReferenceSystem reqepsg = ReferencingFactoryFinder.getCRSAuthorityFactory(reqcrs[0], null).createCoordinateReferenceSystem(reqcrs[1]);
		
		BoundingBox reqbound;
		//updated by Z.S. on 8/12/2016
		if(box.crs.equalsIgnoreCase("EPSG:4326"))
			reqbound = new ReferencedEnvelope(box.miny, box.maxy, box.minx, box.maxx, reqepsg);
		else
			reqbound = new ReferencedEnvelope(box.minx, box.maxx, box.miny, box.maxy, reqepsg);
		
		System.out.println("out bbox is " +box.toString());
		
		System.out.println("Required bbox is " +reqbound);
		
		System.out.println("Interscted WFS Layers are listed as");
		
		for(String tname: typeNames) {			
		
			System.out.println("===============================\n" + tname);
			
			FeatureSource<SimpleFeatureType, SimpleFeature> fsource = dstore.getFeatureSource(tname);
			
			ReferencedEnvelope databounds = fsource.getBounds();					
			
			BoundingBox datatarnsbounds = databounds.toBounds(reqepsg);												
			
			if(datatarnsbounds != null) {	
				
				System.out.println( "src Bounds:"+ databounds);
			
				System.out.println( "src Trans Bounds:"+ datatarnsbounds);
				
				System.out.println( "req Bounds:"+ reqbound);
				
				if(!datatarnsbounds.intersects(reqbound)){
//				if(!(datatarnsbounds.intersects(reqbound)||datatarnsbounds.contains(reqbound)||reqbound.contains(datatarnsbounds))) {
				
					System.out.println("Not intersected " + tname);
					
					continue;
					
				}
				
				GeoFeature feature = new GeoFeature();			
				
				feature.setName(tname);
				
				feature.setTitle(tname);
				
				feature.setDescription(tname);
				
				feature.setWfsUrl(wfsstr);
				
				BBOX fbox;
				
				if(box.crs.equalsIgnoreCase("EPSG:4326"))
				
					fbox = new BBOX(box.crs, datatarnsbounds.getMinY(), datatarnsbounds.getMinX(), datatarnsbounds.getMaxY(), datatarnsbounds.getMaxX());
				
				else
				
					fbox = new BBOX(box.crs, datatarnsbounds.getMinX(), datatarnsbounds.getMinY(), datatarnsbounds.getMaxX(), datatarnsbounds.getMaxY());
				
				System.out.println(tname + "--" +fbox.toString());
				
				feature.addBBOX(fbox);			
				
				featureVec.add(feature);
				
			}
			
		}		
		if(featureVec.size() > 0)
			
			return featureVec;
		
		else
			
			return null;
		
	}

	/**
	 * @param wfsurl
	 * @param box
	 * @return
	 * @throws Exception
	 */
	public static Vector<GeoFeature> getWFSFeatures20_SOAP(String wfsurl, BBOX box) throws Exception {
		
		Vector<GeoFeature> featureVec = new Vector<GeoFeature>(); 	
		
		String wfsstr = wfsurl;
		
    	System.out.println("WFS URL: " + wfsurl);
    	
    	String reqcrs[] = box.crs.split(":");		
		
		CoordinateReferenceSystem reqepsg = ReferencingFactoryFinder.getCRSAuthorityFactory(reqcrs[0], null).createCoordinateReferenceSystem(reqcrs[1]);		
		BoundingBox reqbound;
		//updated by Z.S. on 8/12/2016
		if(box.crs.equalsIgnoreCase("EPSG:4326")) {
			reqbound = new ReferencedEnvelope(box.miny, box.maxy, box.minx, box.maxx, reqepsg);
		}			
		else {
			reqbound = new ReferencedEnvelope(box.minx, box.maxx, box.miny, box.maxy, reqepsg);
		}
			
		
		System.out.println("out bbox is " +box.toString());		
		System.out.println("Required bbox is " +reqbound);		
		System.out.println("Interscted WFS Layers are listed as");
		
		WFSCapabilitiesType wct = WFSUtils.parseCapabilities_SOAP(wfsstr);
		
		FeatureTypeListType ftlt = wct.getFeatureTypeList();
		
		
		for(int i=0; i<ftlt.getFeatureType().size(); i++){			
			
			QName qn = ftlt.getFeatureType().get(i).getName();
			
			GeoFeature feature = new GeoFeature();
			
			if(qn!=null){
				
				System.out.println("===============================\n" + qn.getPrefix()+ ":" + qn.getLocalPart());
				
				feature.setName(qn.getPrefix()+ ":" + qn.getLocalPart());				
				
			}
			feature.setTitle(ftlt.getFeatureType().get(i).getTitle().get(0).getValue());
				
			//feature.setDescription(tname);				
			//feature.setWfsUrl(wfsstr);
				
			BBOX fbox;				
			if(box.crs.equalsIgnoreCase("EPSG:4326")) {
				fbox = new BBOX(box.crs, reqbound.getMinY(), reqbound.getMinX(), reqbound.getMaxY(), reqbound.getMaxX());
			}	
			else {
				fbox = new BBOX(box.crs, reqbound.getMinX(), reqbound.getMinY(), reqbound.getMaxX(), reqbound.getMaxY());
			}
				
//			System.out.println(ftlt.getFeatureType().get(i).getName().toString() + "--" +fbox.toString());			
			feature.addBBOX(fbox);							
			featureVec.add(feature);				
		}			
		
		if(featureVec.size() > 0) {
			return featureVec;
		}		
		else {
			return null;
		}		
	}
	
	public static Vector<GeoFeature> getWFSFeatures20_SOAP_Security(String wfsurl, BBOX box) throws Exception {
		
		Vector<GeoFeature> featureVec = new Vector<GeoFeature>(); 	
		
		String wfsstr = wfsurl;
		
    	System.out.println("WFS URL: " + wfsurl);
    	
    	String reqcrs[] = box.crs.split(":");		
		
		CoordinateReferenceSystem reqepsg = ReferencingFactoryFinder.getCRSAuthorityFactory(reqcrs[0], null).createCoordinateReferenceSystem(reqcrs[1]);		
		BoundingBox reqbound;
		//updated by Z.S. on 8/12/2016
		if(box.crs.equalsIgnoreCase("EPSG:4326")) {
			reqbound = new ReferencedEnvelope(box.miny, box.maxy, box.minx, box.maxx, reqepsg);
		}			
		else {
			reqbound = new ReferencedEnvelope(box.minx, box.maxx, box.miny, box.maxy, reqepsg);
		}
			
		
		System.out.println("out bbox is " +box.toString());		
		System.out.println("Required bbox is " +reqbound);		
		System.out.println("Interscted WFS Layers are listed as");
		
		WFSCapabilitiesType wct = WFSUtils.parseCapabilities_SOAP_Security(wfsstr);
		
		FeatureTypeListType ftlt = wct.getFeatureTypeList();
		
		
		for(int i=0; i<ftlt.getFeatureType().size(); i++){			
			
			QName qn = ftlt.getFeatureType().get(i).getName();
			
			GeoFeature feature = new GeoFeature();
			
			if(qn!=null){
				
				System.out.println("===============================\n" + qn.getPrefix() + qn.getLocalPart().replace("cw-","cw:"));
				
				feature.setName(qn.getPrefix()+ qn.getLocalPart().replace("cw-","cw:"));				
				
			}
			feature.setTitle(ftlt.getFeatureType().get(i).getTitle().get(0).getValue());
				
			//feature.setDescription(tname);				
			//feature.setWfsUrl(wfsstr);
				
			BBOX fbox;				
			if(box.crs.equalsIgnoreCase("EPSG:4326")) {
				fbox = new BBOX(box.crs, reqbound.getMinY(), reqbound.getMinX(), reqbound.getMaxY(), reqbound.getMaxX());
			}	
			else {
				fbox = new BBOX(box.crs, reqbound.getMinX(), reqbound.getMinY(), reqbound.getMaxX(), reqbound.getMaxY());
			}
				
//			System.out.println(ftlt.getFeatureType().get(i).getName().toString() + "--" +fbox.toString());			
			feature.addBBOX(fbox);							
			featureVec.add(feature);				
		}			
		
		if(featureVec.size() > 0) {
			return featureVec;
		}		
		else {
			return null;
		}			
	}
	
	/**
	 * @param wfsurl
	 */
	public static String getGMLFeatureByName_SOAP(String featurename, String url){
		
		String returnedgml =  null;
		
		try {			
			
			//SOAPUtil client = new SOAPUtil();
			
	        client.setEndpoint(url);
	        client.setSoapmessage("<soap:Envelope\r\n    xmlns:soap='http://www.w3.org/2003/05/soap-envelope' >\r\n    <soap:Header/>\r\n    <soap:Body>\r\n        <wfs:GetFeature service='WFS' version='2.0.0'\r\n            xmlns:wfs='http://www.opengis.net/wfs/2.0'\r\n            xmlns:fes='http://www.opengis.net/fes/2.0'\r\n            xmlns:gml='http://www.opengis.net/gml/3.2'\r\n            xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='http://schemas.opengis.net/wfs/2.0/wfs.xsd http://schemas.opengis.net/gml/3.2.1/gml.xsd http://schemas.opengis.net/filter/2.0/filterAll.xsd' outputFormat='application/gml+xml; version=3.2' count='3' >\r\n            <wfs:Query typeNames='"+featurename+"'></wfs:Query>\r\n        </wfs:GetFeature>\r\n    </soap:Body>\r\n</soap:Envelope>");
	        
	        client.send2();
	        
	        String soapresp = client.getRespmessage();
	        
	        theLogger.info(soapresp);
	        
	        SAXReader reader = new SAXReader();
	        
	        
	        Map uris = new HashMap();
	        uris.put("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
	        uris.put("wfs", "http://www.opengis.net/wfs/2.0");
//	        XPath xpath = doc.createXPath("//soapenv:Envelop/soapenv:Body/wfs:WFS_Capabilities");
//	        xpath.setNamespaceURIs(uris);
//	        List<Node> nodes = xpath.selectNodes(doc);
	        
	        reader.getDocumentFactory().setXPathNamespaceURIs(uris);
	        
	        Document doc = reader.read(new StringReader(soapresp));
	        
	        //theLogger.info("Body Content XML:" + body);
	        returnedgml = doc.selectSingleNode("//soapenv:Envelope/soapenv:Body/wfs:FeatureCollection").asXML();
	        
	        theLogger.info(returnedgml);
	        
	        //capa = capa.replace("<Name>", "<Name   xmlns:testbed12=\"http://polar.geodacenter.org/testbed12\">");
	        
	        //String capa = MyHttpUtils.doGet(body);
//			JAXBContext jaxbContext = null;
//			wct = JAXB.unmarshal(new StringReader(returnedgml), WFSCapabilitiesType.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getLocalizedMessage());
		}
		
		return returnedgml;
	}
	/**
	 * @param wfsurl
	 */
	public static String getGMLFeatureByName_SOAP_Security(String featurename, String url){
		
		String returnedgml =  null;
		
		try {			
			//SOAPUtil client = new SOAPUtil();
	        client.setEndpoint(url);
	        
	        String msg = "<soap:Envelope\r\n    xmlns:soap='http://www.w3.org/2003/05/soap-envelope' >\r\n    	        <soap:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"><wsa:To>https://tb12.secure-dimensions.com/soap/services/ows_proxy</wsa:To><wsa:Action>http://www.opengis.net/wfs/requests#GetCapabilities</wsa:Action><wsa:MessageID>urn:uuid:ae6c2f16-c6a1-1e51-226b-002522163135</wsa:MessageID><wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" soapenv:mustUnderstand=\"1\" xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\"><wsse:BinarySecurityToken EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" wsu:Id=\"CertID-ae6d196c-c6a1-1e51-226c\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\">MIIDDDCCAfSgAwIBAgIQM6YEf7FVYx/tZyEXgVComTANBgkqhkiG9w0BAQUFADAwMQ4wDAYDVQQKDAVPQVNJUzEeMBwGA1UEAwwVT0FTSVMgSW50ZXJvcCBUZXN0IENBMB4XDTA1MDMxOTAwMDAwMFoXDTE4MDMxOTIzNTk1OVowQjEOMAwGA1UECgwFT0FTSVMxIDAeBgNVBAsMF09BU0lTIEludGVyb3AgVGVzdCBDZXJ0MQ4wDAYDVQQDDAVBbGljZTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAoqi99By1VYo0aHrkKCNT4DkIgPL/SgahbeKdGhrbu3K2XG7arfD9tqIBIKMfrX4Gp90NJa85AV1yiNsEyvq+mUnMpNcKnLXLOjkTmMCqDYbbkehJlXPnaWLzve+mW0pJdPxtf3rbD4PS/cBQIvtpjmrDAU8VsZKT8DN5Kyz+EZsCAwEAAaOBkzCBkDAJBgNVHRMEAjAAMDMGA1UdHwQsMCowKKImhiRodHRwOi8vaW50ZXJvcC5iYnRlc3QubmV0L2NybC9jYS5jcmwwDgYDVR0PAQH/BAQDAgSwMB0GA1UdDgQWBBQK4l0TUHZ1QV3V2QtlLNDm+PoxiDAfBgNVHSMEGDAWgBTAnSj8wes1oR3WqqqgHBpNwkkPDzANBgkqhkiG9w0BAQUFAAOCAQEABTqpOpvW+6yrLXyUlP2xJbEkohXHI5OWwKWleOb9hlkhWntUalfcFOJAgUyH30TTpHldzx1+vK2LPzhoUFKYHE1IyQvokBN2JjFO64BQukCKnZhldLRPxGhfkTdxQgdf5rCK/wh3xVsZCNTfuMNmlAM6lOAg8QduDah3WFZpEA0s2nwQaCNQTNMjJC8tav1CBr6+E5FAmwPXP7pJxn9Fw9OXRyqbRA4v2y7YpbGkG2GI9UvOHw6SGvf4FRSthMMO35YbpikGsLix3vAsXWWi4rwfVOYzQK0OFPNi9RMCUdSH06m9uLWckiCxjos0FQODZE9l4ATGy9s9hNVwryOJTw==</wsse:BinarySecurityToken><wsse:UsernameToken xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" wsu:Id=\"SigID-ae6d8e9c-c6a1-1e51-226d\"><wsse:Username>Alice</wsse:Username><wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">+43faXkM59Wvc/1EqkhqFYGJwOs=</wsse:Password><wsse:Nonce>p3aGzO+yN4I8oTehOJTY3ZrIcYsM6XJ8</wsse:Nonce><wsu:Created>2016-01-29T16:02:22.307Z</wsu:Created></wsse:UsernameToken><ds:Signature xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" Id=\"SigID-ae6db5ac-c6a1-1e51-226e\"><ds:SignedInfo><ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></ds:CanonicalizationMethod><ds:SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"></ds:SignatureMethod><ds:Reference URI=\"#SigID-ae6d8e9c-c6a1-1e51-226d\"><ds:Transforms><ds:Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"></ds:DigestMethod><ds:DigestValue>sk06OjMEwRt9U0PZYPTR3CVycL4=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>bcI24Ds8MvM7dvgAC3WeO+uv9zKAq6yB3qdI02t8I1h7W8GVBrPYs4f7a9JdELtV0UsA1NUOp4nbPwtPUdw+nVURHQUHr+mZ879HZuzD3daW5Z3kTk9i1+6ceCbZh/K/SRCFOQvDpKRC74W5iTkik7TBALgPl7sJxVt7nY6GBhs=</ds:SignatureValue><ds:KeyInfo><wsse:SecurityTokenReference><wsse:Reference URI=\"#CertID-ae6d196c-c6a1-1e51-226c\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3\"></wsse:Reference></wsse:SecurityTokenReference></ds:KeyInfo></ds:Signature></wsse:Security></soap:Header>\r\n    <soap:Body>\r\n        <wfs:GetFeature service='WFS' version='2.0.0'\r\n            xmlns:wfs='http://www.opengis.net/wfs/2.0'\r\n            xmlns:fes='http://www.opengis.net/fes/2.0'\r\n            xmlns:gml='http://www.opengis.net/gml/3.2'\r\n            xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='http://schemas.opengis.net/wfs/2.0/wfs.xsd http://schemas.opengis.net/gml/3.2.1/gml.xsd http://schemas.opengis.net/filter/2.0/filterAll.xsd' outputFormat='application/gml+xml; version=2.1' count='3' >\r\n            <wfs:Query typeNames='"+featurename+"'></wfs:Query>\r\n        </wfs:GetFeature>\r\n    </soap:Body>\r\n</soap:Envelope>";

	        theLogger.info(msg);
	        
	        client.setSoapmessage(msg);
			
	        client.send3();
	        
	        String soapresp = client.getRespmessage();
	        
	        soapresp = soapresp.replaceAll("&", "&amp;");
	        
	        theLogger.info(soapresp);
	        
	        SAXReader reader = new SAXReader();
	        
	        Map uris = new HashMap();
	        
	        uris.put("soapenv", "http://www.w3.org/2003/05/soap-envelope");
	        
	        uris.put("wfs", "http://www.opengis.net/wfs/2.0");
	        
//	        XPath xpath = doc.createXPath("//soapenv:Envelop/soapenv:Body/wfs:WFS_Capabilities");
//	        xpath.setNamespaceURIs(uris);
//	        List<Node> nodes = xpath.selectNodes(doc);
	        
	        reader.getDocumentFactory().setXPathNamespaceURIs(uris);
	        
	        Document doc = reader.read(new StringReader(soapresp));
	        
	        //theLogger.info("Body Content XML:" + body);
	        returnedgml = doc.selectSingleNode("//soapenv:Envelope/soapenv:Body/wfs:FeatureCollection").asXML();
	        
	        theLogger.info(returnedgml);
	        
	        //capa = capa.replace("<Name>", "<Name   xmlns:testbed12=\"http://polar.geodacenter.org/testbed12\">");
	        
	        //String capa = MyHttpUtils.doGet(body);
//			JAXBContext jaxbContext = null;
//			wct = JAXB.unmarshal(new StringReader(returnedgml), WFSCapabilitiesType.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getLocalizedMessage());
		}
		
		return returnedgml;
	}
	
    public static String cacheGML(String gmlurl){
    	
    	String fileurl = null;
    	
    	try {
			
			String filename = UUID.randomUUID().toString() + ".gml";
			
			String filepath = BaseTool.get_tempfilepath() + filename;
			
			fileurl = BaseTool.get_tempfileurl() + filename;
			
			theLogger.info(filepath);
			
			BaseTool.downloadFromUrl2(new URL(gmlurl),  filepath);
			
		} catch (Exception e) {

			e.printStackTrace();
			
		} 

		return fileurl;
		
    }
	
	/**
	 * 
	 * @param featurename
	 * @param url 
	 * @return
	 */
	public static String cacheFeatureSOAP(String featurename, String url){
		
		//remove the namespace prefix in the feature name - ASU's requirement
		
		int l = -1;
		
		if((l = featurename.indexOf(":"))!=-1){
			
			featurename = featurename.substring(l+1);
			
		}
		
		String gml = WFSUtils.getGMLFeatureByName_SOAP(featurename, url);
		
		//TODO: save GML to a place and give a URL - chen zhang
		
		try {
			
			String filename = UUID.randomUUID().toString() + ".gml";
			
			String filepath = BaseTool.get_tempfilepath() + filename;
			
			String fileurl = BaseTool.get_tempfileurl() + filename;
			
			theLogger.info(filepath);
			
			BaseTool.writeString2File(gml, filepath);
			
			return fileurl;
			
		} catch (Exception e) {

			e.printStackTrace();
			
		} 
		
		String cachedurl = null;
		
		return cachedurl;
		
	}
	
	/**
	 * 
	 * @param featurename
	 * @param url 
	 * @return
	 */
	public static String cacheFeatureSOAPSecurity(String featurename, String url){
		
		//remove the namespace prefix in the feature name - ASU's requirement
		
//		int l = -1;
//		
//		if((l = featurename.indexOf(":"))!=-1){
//			
//			featurename = featurename.substring(l+1);
//			
//		}
		
		String gml = WFSUtils.getGMLFeatureByName_SOAP_Security(featurename, url);
		
		//TODO: save GML to a place and give a URL - chen zhang
		
		try {
			
			String filename = UUID.randomUUID().toString() + ".gml";
			
			String filepath = BaseTool.get_tempfilepath() + filename;
			
			String fileurl = BaseTool.get_tempfileurl() + filename;
			
			theLogger.info(filepath);
			
			BaseTool.writeString2File(gml, filepath);
			
			return fileurl;
			
		} catch (Exception e) {

			e.printStackTrace();
			
		} 
		
		String cachedurl = null;
		
		return cachedurl;
		
	}
	

	
	public static void main(String[] args) {
		
		/**
		 * 
		 * test conventional WFS 2.0
		 * 
		 */
		
////		String wfsurl = "http://ws.csiss.gmu.edu:8080/geoserver/wfs";
//		
//		String wfsurl = "http://cube.csiss.gmu.edu/geoserver/topp/ows";
//		
//		//String wfscapaurl = "http://cube.csiss.gmu.edu/geoserver/topp/ows?service=wfs&request=getcapabilities&version=2.0.0";
//		
//		String wfscapaurl = "http://59.120.223.164:443/geoserver/ows?service=wfs&version=2.0.0&request=GetCapabilities";
//		
//		WFSCapabilitiesType wct = WFSUtils.parseCapabilities(wfscapaurl);
//		
//		FeatureTypeListType ftlt = wct.getFeatureTypeList();
//		
//		System.out.println("Feature type nubmer : " + ftlt.getFeatureType().size());
//	//	BBOX box = new BBOX("EPSG:4326", -77.527282, 38.934311, -76.887893, 39.353648);	
////		BBOX box = new BBOX("EPSG:32618", 280940.92757638777, 4312524.259656975, 337335.10634472733, 4357722.154561454);
////		try {		
////			Vector<GeoFeature> gfVec2 = getWFSFeatures(wfsurl, box);
////			System.out.println("--------------------------------");
////			System.out.println("gfVec2.size()=" + gfVec2.size());
////			for(GeoFeature gf:gfVec2) {
////				System.out.println("Name=" + gf.getName());
////			}
////		} catch(Exception e) {
////			e.printStackTrace();
////		}
		
		/**
		 * 
		 *  Test SOAP WFS 2.0
		 * 
		 */
//	//	String wfsurl = "http://ws.csiss.gmu.edu:8080/geoserver/wfs";
//		
//		String wfsurl = "http://polar.geodacenter.org/services/ows/wfs/soap/1.2";
//		
//		//String wfscapaurl = "http://cube.csiss.gmu.edu/geoserver/topp/ows?service=wfs&request=getcapabilities&version=2.0.0";
//		
//		//String wfscapaurl = "http://59.120.223.164:443/geoserver/ows?service=wfs&version=2.0.0&request=GetCapabilities";
//		
////		BBOX bb = new BBOX("EPSG:4326", -122.5754909, 37.3862364, -121.861508, 38.0220317);
////		
////		try {
////			
////			Vector<GeoFeature> featurevec = WFSUtils.getWFSFeatures20_SOAP(wfsurl, bb);
////			
////
//////			/**
//////			 * 
//////			 * Test ASU WFS SOAP Capabilities
//////			 * 
//////			 */
//////			WFSUtils.getWFSFeatures20("http://localhost:8080/capa.xml", bb);
////			
////		} catch (Exception e) {
////
////			e.printStackTrace();
////			
////		}						
//		
//		//test cache feature
//		
//		WFSUtils.cacheFeatureSOAP("landuse", wfsurl);
//		
		
		//WFSUtils.parseCapabilities_SOAP_Security("https://tb12.secure-dimensions.com/soap/services");
		
		WFSUtils.cacheFeatureSOAPSecurity("cw:Elev_Contour", "https://tb12.secure-dimensions.com/soap/services");
	}	
	
}
