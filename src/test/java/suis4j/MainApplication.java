package suis4j;

import org.apache.log4j.Logger;

import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlProject;

/**
*Class Main.java
*@author Ziheng Sun
*@time Jan 9, 2018 4:27:13 PM
*/
public class MainApplication {

	public Logger logger = Logger.getLogger(this.getClass());
	
	public void testWSDLDriver(){
		
		try{
			
			String wsdluri = "http://www3.csiss.gmu.edu/GeoprocessingWS/services/Vector_GetProjection?wsdl";
			
//			String wsdluri = "http://cube.csiss.gmu.edu/axis2/services/GMU_SOAP_WCS_Service?wsdl";
			
            WsdlProject project = new WsdlProject();
            
            WsdlInterface iface = WsdlInterfaceFactory.importWsdl(project, wsdluri, true)[0];
            
            for(int i=0;i<iface.getAllOperations().length;i++){
            	
            	com.eviware.soapui.model.iface.Operation oper = iface.getAllOperations()[i];
            	
            	System.out.println("Operation : " + oper.getName());
            	
            	System.out.println("has "+oper.getDefaultRequestParts().length + " requests");
            	
            	System.out.println("part: "+oper.getDefaultRequestParts()[0].getName());
            	
            	System.out.println("has : "+oper.getDefaultResponseParts().length + " responses");
            	
            	System.out.println("has "+oper.getDefaultRequestParts()[0].getName() + " requests");
            	
            	System.out.println("part: "+oper.getDefaultResponseParts()[0].getName());
            	
            	String req = oper.createRequest( false );
            	
            	System.out.println("Request String Content: " + req);
            	
            }
            
            project.release();
            
            
            
            // to print all operations supported by wsdl
            /*
             * for (Operation operation : iface.getOperationList()) {
             * WsdlOperation op = (WsdlOperation) operation;
             * System.out.println("OP:" + op.getName());
             * System.out.println(op.createRequest(true));
             * System.out.println("Response:");
             * System.out.println(op.createResponse(true)); }
             */

            // get valid operationName to perform on
//	            String operationName = "FetchRoomAllotment";
//	            WsdlOperation operation = iface.getOperationByName(operationName);
//	            // create a new empty request for that operation
//	            // replace ** with your credentials
//	            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
//	                    + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
//	                    + "  <soap:Header>\n" + "    <AuthenticationHeader xmlns=\"http://www.HotelTravel.com/\">\n"
//	                    + "      <Username>*****</Username>\n" + "      <Password>*****</Password>\n"
//	                    + "      <Hotelcode>KBILPL</Hotelcode>\n" + "    </AuthenticationHeader>\n" + "  </soap:Header>\n"
//	                    + "  <soap:Body>\n" + "    <FetchHotelAllotment xmlns=\"http://www.HotelTravel.com/\">\n"
//	                    + "      <xmlRequest>\n" + "         <Rooms>\n" + "            <Room Code=\"3\">\n"
//	                    + "            </Room>\n" + "        </Rooms>\n" + "      </xmlRequest>\n"
//	                    + "    </FetchHotelAllotment>\n" + "  </soap:Body>\n" + "</soap:Envelope>";
	//
//	            WsdlRequest request = operation.addNewRequest("name");
//	            request.setRequestContent(xml);
	//
//	            /*
//	             * request.setWssPasswordType("PasswordText");
//	             * request.setUsername(username); request.setPassword(password);
//	             */
//	            // request.setWssTimeToLive("10000");
	//
//	            WsdlSubmit<WsdlRequest> submit = request.submit(new WsdlSubmitContext(project), false);
//	            // wait for the response
//	            Response response = submit.getResponse();
//	            System.out.println("response ::\n" + response);
	//
//	            // print the response
//	            String content = response.getRequestContent();
//	            System.out.println("request content::\n" + content);
//	            System.out.println("response content::\n" + response.getContentAsString());
            	
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
		
	}
	
	public void testOGCDriver(){
		
		String ogccapabilitiesuri = "http://geoserver.itc.nl/cgi-bin/mapserv.exe?map=D:/Inetpub/mapserver/config.map&SERVICE=WMS&VERSION=1.3.0&REQUEST=GetCapabilities";
		
		
		
	}
	
	public void testRESTDriver(){
		
		String resturi = "";
		
		
		
	}
	
	
	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args){
		
		MainApplication ma = new MainApplication();
		
		ma.testWSDLDriver();
//		
//		ma.testOGCDriver();
//		
//		ma.testRESTDriver();
		
	}
	
}
