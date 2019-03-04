package suis4j.driver;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlCursor.TokenType;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlProject;

import suis4j.profile.Message;
import suis4j.profile.Operation;
import suis4j.profile.Parameter;

/**
*Class WSDL2SUNIS.java
*@author Ziheng Sun
*@time Dec 8, 2017 10:23:03 AM
*/
public class SOAPDriver extends AbstractDriver {
	
	WsdlInterface iface;
	
	Map<String, String[]> templatemap; //operation to request & response xml
	
	Logger log = Logger.getLogger(SOAPDriver.class);
	
	protected SOAPDriver(){
		
		templatemap  = new HashMap();
		
		operlist = new ArrayList();
		
	}
	
	public WsdlInterface getIface() {
		return iface;
	}

	public void setIface(WsdlInterface iface) {
		this.iface = iface;
	}
	
	@Override
	public Message decodeSUIS(Object rawmsg) {
		
		return null;
	}
	
	@Override
	public PayLoad encodeReq(Message msg) {
		
		//get the template xml
		
		String inputtemplate = templatemap.get(current_operation)[0];
		
		String req = null;
		
		 try {
			 
			 XmlObject xmlObjExpected = XmlObject.Factory.parse(inputtemplate);
			 
			 XmlCursor cursor = xmlObjExpected.newCursor();
			 
//			 <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:vec="http://Vector_XYOffset.grass.ws.laits.gmu.edu">
//			   <soapenv:Header/>
//			   <soapenv:Body>
//			      <vec:moveElement>
//			         <vec:sourceURL>?</vec:sourceURL>
//			         <vec:xoffset>?</vec:xoffset>
//			         <vec:yoffset>?</vec:yoffset>
//			      </vec:moveElement>
//			   </soapenv:Body>
//			</soapenv:Envelope>
			 
//			 String nsText = "declare namespace soapenv = 'http://schemas.xmlsoap.org/soap/envelope/'; ";
//			 String pathText = "$this/soapenv:Envelope/soapenv:Body";
//			 String queryText = nsText + pathText;
//			 
//			 cursor = cursor.execQuery(queryText);
			 
			 while(!cursor.toNextToken().isNone()){
				 
				 switch (cursor.currentTokenType().intValue())
				 {
					 case TokenType.INT_START:
						 
						 if("?".equals(cursor.getTextValue())){
							 
							 QName qn = cursor.getName();
							 
							 Parameter p = msg.get(qn.getLocalPart(), qn.getNamespaceURI());
							 
							 cursor.setTextValue(String.valueOf(p.getValue()));
							 
							 log.debug("set value: " + cursor.getTextValue());
							 
						 }
						 
						 break;
						 
					 case TokenType.INT_ATTR:
						 
						 if("?".equals(cursor.getTextValue())){
							 
							 QName qn = cursor.getName();
							 Parameter p = msg.get(qn.getLocalPart(), qn.getNamespaceURI());
							 cursor.setTextValue(String.valueOf(p.getValue()));
							 log.debug("set value: " + cursor.getTextValue());
							 
						 }
						 
						 break;
						 
				 }
				 
			 }
			 
			 cursor.dispose();
			 
			 req = xmlObjExpected.xmlText();
			 
			 log.debug("combinated request xml: " + xmlObjExpected.xmlText());
			 
		} catch (XmlException e) {
			 
			 e.printStackTrace();
			 
			 throw new RuntimeException("failed to get xml exception. " + e.getLocalizedMessage());
			 
		}
		
		return new PayLoad.Builder().content(req).build();
	}

	@Override
	public void send(PayLoad req) {
		
		try {
			
			System.out.println(req.getContent());
			
			String resp = HttpUtils.SOAP(String.valueOf(req.getContent()), this.current_operation, iface.getEndpoints()[0]);
			
			System.out.println(resp);
			
			response = new PayLoad.Builder().content(resp).build();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	@Override
	public PayLoad receive() {
		
		return response;
		
	}
	
	@Override
	public Message decodeResp(PayLoad resp) {
		
		String respxml = String.valueOf(resp.getContent()).trim();
		
		Operation o = getOperation(this.current_operation);
		
		boolean getoutput = false;
		
		 try {
			 
			 XmlObject resp_xmlObj = XmlObject.Factory.parse(respxml);
			 
			 XmlCursor resp_cursor = resp_xmlObj.newCursor();
			 
//			 String nsText = "declare namespace soapenv = 'http://schemas.xmlsoap.org/soap/envelope/'; ";
//			 String pathText = "$this/soapenv:Envelope/soapenv:Body";
//			 String queryText = nsText + pathText;
//			 
//			 cursor = cursor.execQuery(queryText);
			 
			 while(!resp_cursor.toNextToken().isNone()){
				 
				 switch (resp_cursor.currentTokenType().intValue())
				 {
					 case TokenType.INT_START:
						 
						 QName qn = resp_cursor.getName();
						 
						 if(o.getOutput().get(qn.getLocalPart())!=null){
							 
							 Parameter p = o.getOutput().get(qn.getLocalPart(), qn.getNamespaceURI());
							 
							 p.setValue(resp_cursor.getTextValue());
							 
							 getoutput = true;
							 
						 }
						 
						 break;
						 
					 case TokenType.INT_ATTR:
						 
						 qn = resp_cursor.getName();
						 
						 if(o.getOutput().get(qn.getLocalPart())!=null){
							 
							 Parameter p = o.getOutput().get(qn.getLocalPart(), qn.getNamespaceURI());
							 
							 p.setValue(resp_cursor.getTextValue());
							 
							 getoutput = true;
							 
						 }
						 
						 break;
						 
				 }
				 
			 }
			 
			 resp_cursor.dispose();
			 
			 if(!getoutput){
				 
				 o.getOutput().setError(respxml);
				 
			 }
			 
		} catch (XmlException e) {
			 
			 e.printStackTrace();
			 
			 throw new RuntimeException("failed to get xml exception. " + e.getLocalizedMessage());
			 
		}
		
		return o.getOutput();
	}

	@Override
	public Object encodeSUIS(Message msg) {
		
		msg.getParameter_list();
		
		PayLoad load = new PayLoad.Builder()
				.content("")
				.build();
		
		return load;
	}
	
	/**
	 * Parse the parameters from the generated request/response XML
	 * @param xml
	 * @return
	 */
	private List<Parameter> parseParams(String xml) {
		 
		 List<Parameter> paramlist = new ArrayList();
		 
		 try {
			 
			 XmlObject xmlObjExpected = XmlObject.Factory.parse(xml);
			 
			 XmlCursor cursor = xmlObjExpected.newCursor();
			 
//			 <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:vec="http://Vector_XYOffset.grass.ws.laits.gmu.edu">
//			   <soapenv:Header/>
//			   <soapenv:Body>
//			      <vec:moveElement>
//			         <vec:sourceURL>?</vec:sourceURL>
//			         <vec:xoffset>?</vec:xoffset>
//			         <vec:yoffset>?</vec:yoffset>
//			      </vec:moveElement>
//			   </soapenv:Body>
//			</soapenv:Envelope>
			 
//			 String nsText = "declare namespace soapenv = 'http://schemas.xmlsoap.org/soap/envelope/'; ";
//			 String pathText = "$this/soapenv:Envelope/soapenv:Body";
//			 String queryText = nsText + pathText;
//			 
//			 cursor = cursor.execQuery(queryText);
//			 
//			 log.info("Body: " + cursor.xmlText());
			 
			 while(!cursor.toNextToken().isNone()){
				 
				 switch (cursor.currentTokenType().intValue())
				 {
					 case TokenType.INT_START:
						 
						 if("?".equals(cursor.getTextValue())){
							 QName qn = cursor.getName();
							 Parameter p = new Parameter.Builder()
									 .name(qn.getLocalPart())
									 .namespace(qn.getNamespaceURI())
									 .build();
							 
							 paramlist.add(p);
							 
						 }
						 
						 break;
						 
					 case TokenType.INT_ATTR:

						 if("?".equals(cursor.getTextValue())){
							 
							 QName qn = cursor.getName();
							 
							 Parameter p = new Parameter.Builder()
									 .name(qn.getLocalPart())
									 .namespace(qn.getNamespaceURI())
									 .build();
							 
							 paramlist.add(p);
							 
						 }
						 
						 break;
				 
				 }
				 
			 }
			 
			 cursor.dispose();
			 
		} catch (XmlException e) {
			 
			 e.printStackTrace();
			 
			 throw new RuntimeException("failed to get xml exception. " + e.getLocalizedMessage());
			 
		}
		
		return paramlist;
		
	}
	

	@Override
	public List<Operation> digest() {
		
		operlist = new ArrayList();
		
		for(int i=0;i<iface.getAllOperations().length;i++){
        	
        	com.eviware.soapui.model.iface.Operation oper = iface.getAllOperations()[i];
        	
//        	MessagePart mp = oper.getDefaultRequestParts()[0];
        	
        	String[] templates = new String[2];
        	
        	String req = oper.createRequest( false );
        	
        	templates[0] = req;
        	 
//        	log.info("Request String Content: " + req);
        	
        	//get parameters from the generated request
        	
        	String resp = oper.createResponse(false);
        	
//        	log.info("Response String Content: " + resp);
        	
        	templates[1] = resp;
        	
        	templatemap.put(oper.getName(), templates);
        	
        	//get parameters from the generated response
        	
        	//create an operation object
        	
        	List<Parameter> inparams = parseParams(req);
        	
        	Message in = new Message.Builder()
        			
        			.params(inparams)
        			
        			.build();
        	
        	List<Parameter> outparams= parseParams(resp);
        	
        	Message out = new Message.Builder()
        			
        			.params(outparams)
        			
        			.build();
        	
        	Operation o = new Operation.Builder()
        			
        			.name(oper.getName())
        			
        			.description(oper.getDescription())
        			
        			.input(in)
        			
        			.output(out)
        			
        			.build();
        	
        	operlist.add(o);
        	
        }
		
		connect();
		
		return operlist;
		
	}
	
	public static class Builder extends AbstractDriver.Builder{
		
		SOAPDriver driver = new SOAPDriver();
		
		WsdlProject project = null;
		
		@Override
		public Builder parse(String descfile) {
			
			try{
				
				String wsdluri = descfile;
				
//				String wsdluri = "http://cube.csiss.gmu.edu/axis2/services/GMU_SOAP_WCS_Service?wsdl";
				
				if(project==null){
					
					project = new WsdlProject();
					
				}
		        
		        WsdlInterface iface = WsdlInterfaceFactory.importWsdl(project, wsdluri, false)[0];
		        
		        driver.setIface(iface);
		        
			}catch(Exception e){
				
				e.printStackTrace();
				
				throw new RuntimeException("Fail to parse WSDL " + e.getLocalizedMessage());
				
			}
			
			return this;
			
		}

		@Override
		public Builder access_endpoint(URL url) {
			
			driver.setAccess_endpoint(url);
			
			return this;
			
		}

		@Override
		public Builder desc_endpoint(URL url) {
			
			driver.setDesc_endpoint(url);
			
			return this;
			
		}
		
		@Override
		public AbstractDriver build() {
			
			return driver;
			
		}

		
		
	}

	@Override
	public void initParams(Operation o) {
		// TODO Auto-generated method stub
		
	}
	
}
