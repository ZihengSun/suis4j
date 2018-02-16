package suis4j.driver;

import java.net.URL;

import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlProject;

/**
*Class SOAPDriverBuilder.java
*@author Ziheng Sun
*@time Feb 1, 2018 6:57:52 PM
*/
public class SOAPDriverBuilder extends AbstractDriverBuilder{
	
	SOAPDriver driver = new SOAPDriver();
	
	static WsdlProject project = null;

	@Override
	public AbstractDriverBuilder parse(String descfile) {
		
		try{
			
			String wsdluri = descfile;
			
//			String wsdluri = "http://cube.csiss.gmu.edu/axis2/services/GMU_SOAP_WCS_Service?wsdl";
			
			if(project==null){
				
				project = new WsdlProject();
				
			}
	        
	        WsdlInterface iface = WsdlInterfaceFactory.importWsdl(project, wsdluri, false)[0];
	        
	        
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
	        
	        iface.release();
	        
//	        project.saveAs("");
	        
	        project.release();
	        
	        
	        
		}catch(Exception e){
			
			e.printStackTrace();
			
			throw new RuntimeException("Fail to parse WSDL " + e.getLocalizedMessage());
			
		}
		
		return this;
	}

	@Override
	public AbstractDriverBuilder access_endpoint(URL url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractDriverBuilder desc_endpoint(URL url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractDriverBuilder type(ServiceType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractDriver build() {
		
		return driver;
	}

	
	
}
