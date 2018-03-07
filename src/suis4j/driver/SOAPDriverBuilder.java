package suis4j.driver;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.iface.MessagePart;

import suis4j.profile.Message;
import suis4j.profile.MessageBuilder;
import suis4j.profile.Operation;
import suis4j.profile.OperationBuilder;
import suis4j.profile.Parameter;
import suis4j.profile.ParameterBuilder;

/**
*Class SOAPDriverBuilder.java
*@author Ziheng Sun
*@time Feb 1, 2018 6:57:52 PM
*/
public class SOAPDriverBuilder extends AbstractDriverBuilder{
	
	SOAPDriver driver = new SOAPDriver();
	
	WsdlProject project = null;
	
	@Override
	public AbstractDriverBuilder parse(String descfile) {
		
		try{
			
			String wsdluri = descfile;
			
//			String wsdluri = "http://cube.csiss.gmu.edu/axis2/services/GMU_SOAP_WCS_Service?wsdl";
			
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
