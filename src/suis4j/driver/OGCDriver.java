package suis4j.driver;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.opengis.wps.v_1_0_0.InputDescriptionType;
import net.opengis.wps.v_1_0_0.OutputDescriptionType;
import net.opengis.wps.v_1_0_0.ProcessBriefType;
import net.opengis.wps.v_1_0_0.ProcessDescriptionType;
import net.opengis.wps.v_1_0_0.ProcessDescriptions;
import net.opengis.wps.v_1_0_0.WPSCapabilitiesType;
import suis4j.profile.DataType;
import suis4j.profile.Message;
import suis4j.profile.Operation;
import suis4j.profile.Parameter;

/**
*Class OGCWPS2SUNIS.java
*@author Ziheng Sun
*@time Dec 8, 2017 10:23:52 AM
*/
public class OGCDriver extends AbstractDriver {
	
	String category = "wps";
	
	String version = "1.0.0";
	
	Object capa;
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public PayLoad encodeReq(Message msg) {
		
		if("wps".equals(category)){
			
			if(version.equals("1.0.0")){
				
				
				
			}else if(version.equals("2.0")||version.equals("2.0.0")){
				
				
			}
			
		}
		
		return new PayLoad.Builder()
				.content("")
				.build();
		
	}

	@Override
	public void send(PayLoad req) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PayLoad receive() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Message decodeResp(PayLoad resp) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Message decodeSUIS(Object rawmsg) {

		return null;
	}

	@Override
	public Object encodeSUIS(Message msg) {
		
		
		
		return null;
	}
	
	
	
	@Override
	public List<Operation> digest() {
		
		operlist = new ArrayList();
		
		if("wps".equals(category)){
			
			if(version.equals("1.0.0")){
				
				//add an operation for getProcessList
				
//				List<Parameter> params = new ArrayList();
//				
//				params.add(new Parameter.Builder()
//						.name("processlist")
//						.description("all the supported process by this wps")
//						.minoccurs(1)
//						.maxoccurs(1)
//						.type(DataType.STRING)
//						.build());
//				
//				Operation getcapaoper = new Operation.Builder()
//						.name("GetProcessList")
//						.input(new Message.Builder().build())  //no input parameter
//						.output(new Message.Builder()
//								.params(params)
//								.build())
//						.build();
//				
//				operlist.add(getcapaoper);
				
				//add an operation for GetProcessDescription
				
//			    List<Parameter> inparams = new ArrayList();
//				
//			    inparams.add(new Parameter.Builder()
//			    		.name("processId")
//			    		.description("the process identifier")
//			    		.type(DataType.STRING)
//			    		.build());
//			    
//			    List<Parameter> outparams = new ArrayList();
//			    
//			    outparams.add(new Parameter.Builder()
//			    		.name("processDescription")
//			    		.description("description of process")
//			    		.type(DataType.STRING)
//			    		.build());
//				
//				Operation describeprocessoper = new Operation.Builder()
//						.name("GetProcessDescription")
//						.input(new Message.Builder()
//								.params(inparams)
//								.build())
//						.output(new Message.Builder()
//								.params(outparams)
//								.build())
//						.build();
//				
//				operlist.add(describeprocessoper);
				
				//add all the processes as operations

				WPSCapabilitiesType wct = WPSUtils.parseCapabilities(this.getDesc_endpoint().toString());
				
				capa = wct;
				
				List<ProcessBriefType> processes = wct.getProcessOfferings().getProcess();
				
				operlist = new ArrayList();
				
				for(int i=0;i<processes.size();i++){
					
					ProcessBriefType p = processes.get(i);
					
					String pname = p.getIdentifier().getValue();
					
					System.out.println("processing: " + pname);
					
					//get the inputs and outputs
					
					ProcessDescriptions pds = WPSUtils.getProcessDescription(pname, WPSUtils.getExecuteEndpoint(wct));
					
					ProcessDescriptionType pdt = pds.getProcessDescription().get(0); 
					
					List<Parameter> paramlist = new ArrayList();
					
					if(pdt.getDataInputs()!=null){
						
						List<InputDescriptionType> idts = pdt.getDataInputs().getInput();
				    	
				    	for(int j=0; j<idts.size(); j++){
				    		
				    		InputDescriptionType idt = idts.get(j);
				    		
				    		int minn = idt.getMinOccurs().intValue();
				    		
				    		int maxn = idt.getMaxOccurs().intValue();
				    		
				    		idt.getComplexData();
				    		
				    		idt.getLiteralData();
				    		
				    		String desc = idt.getAbstract()!=null?idt.getAbstract().getValue():"";
				    		
							Parameter param = new Parameter.Builder()
									.name(idt.getIdentifier().getValue())
									.description(desc)
									.maxoccurs(maxn)
									.minoccurs(minn)
									.build();
							
							paramlist.add(param);
				    		
				    	}
				    	
					}
					
					Message inm = new Message.Builder()
							.params(paramlist)
							.build();
					
					paramlist = new ArrayList();
					
					if(pdt.getProcessOutputs()!=null){
						
						List<OutputDescriptionType> odts = pdt.getProcessOutputs().getOutput();
						
						for(OutputDescriptionType odt: odts){
							
							String desc = odt.getAbstract()!=null?odt.getAbstract().getValue():"";
							
							Parameter param = new Parameter.Builder()
									.name(odt.getIdentifier().getValue())
									.description(desc)
									.minoccurs(1)
									.maxoccurs(1)
									.build();
							
							paramlist.add(param);
							
						}
						
					}
					
					Message outm = new Message.Builder()
							.params(paramlist)
							.build();
					
					Operation o = new Operation.Builder()
							.name(pname)
							.input(inm)
							.output(outm)
							.build();
					
					operlist.add(o);
					
				}
				
			}else if(version.equals("2.0.0")||version.equals("2.0")){
				
				net.opengis.wps.v_2_0.WPSCapabilitiesType wct = WPSUtils.parseCapabilities20(this.getDesc_endpoint().toString());
				
				System.err.println("Not implemented yet.");
				
			}
			
		}else if("wcs".equals(category)){
			
			
		}else if("wfs".equals(category)){
			
			
		}else if("wms".equals(category)){
			
			
		}else if("csw".equals(category)){
			
			
		}else{
			
			throw new RuntimeException("The service category ["+category+"] is not supported yet.");
			
		}
		
		connect();
		
		return this.getOperlist();
	
	}
	
	public static class Builder extends AbstractDriver.Builder{

		OGCDriver driver = new OGCDriver();
		
		@Override
		public Builder parse(String descfile) {
			
			//Parse the GetCapabilities URL. First, know what type of service it is.
			
			try {
				
				Map<String, String> querykvp = HttpUtils.splitQuery(new URL(descfile));
				
				if(querykvp.get("service")==null){
					
					throw new RuntimeException("The link doesn't specify the service category.");
					
				}
				
				driver.setCategory(querykvp.get("service"));
				
				driver.setVersion(querykvp.get("version"));
				
				desc_endpoint(new URL(descfile));
				
			} catch (UnsupportedEncodingException e) {
				
				e.printStackTrace();
				
			} catch (MalformedURLException e) {
				
				e.printStackTrace();
				
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
		public Builder type(ServiceType type) {
			
			driver.servicetype = type;
			
			return this;
		}

		@Override
		public AbstractDriver build() {
			
			return driver;
			
		}
		
	}
	
}
