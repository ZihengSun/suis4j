package suis4j.driver;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.opengis.wcs.v_2_0.CapabilitiesType;
import net.opengis.wps.v_1_0_0.Execute;
import net.opengis.wps.v_1_0_0.ExecuteResponse;
import net.opengis.wps.v_1_0_0.InputDescriptionType;
import net.opengis.wps.v_1_0_0.OutputDataType;
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
	
	Map<String, Object> processdescriptions; //operation name to process description
	
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
		
		Object content = null;
		
		if("wps".equals(category)){
			
			if(version.equals("1.0.0")){
				
				WPSCapabilitiesType wpscapa = (WPSCapabilitiesType) capa;
				
				ProcessDescriptionType pdt = (ProcessDescriptionType)processdescriptions.get(this.getCurrent_operation());
				
				Execute exe = WPSUtils.getExecuteRequest(wpscapa, pdt, msg.toKVPs());
				
				content = WPSUtils.turnExecuteReqToXML(exe); //turn the Execute object to xml
				
			}else if(version.equals("2.0")||version.equals("2.0.0")){
				
				
				
			}
			
		}else if("wcs".equals(category)){
			
			if(version.equals("2.0.0")){
				
				if("DescribeCoverage".equals(this.getCurrent_operation())){

					content = WCSUtils.turnDescribeCoverageTypeToXML(
							WCSUtils.createADescribeCoverageRequest(
									msg.getValueAsString("coverageId")));
					
				}else if("GetCoverage".equals(this.getCurrent_operation())){

					content = WCSUtils.createAGetCoverageRequest(msg.getValueAsString("coverageId")).toString();
					
				}
				
			}
			
		}
		
		return new PayLoad.Builder()
				.content(content)
				.build();
		
	}

	@Override
	public void send(PayLoad req) {
		
		try {
			
			System.out.println(">> "+(String)req.getContent());
			
			String resp = HttpUtils.doPost(this.getAccess_endpoint().toString(), (String)req.getContent());
			
			System.out.println(">> " + resp);
			
			response = new PayLoad.Builder()
					.content(resp)
					.build();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			response = new PayLoad.Builder()
					.content(e.getLocalizedMessage())
					.build();
			
		}
		
	}

	@Override
	public PayLoad receive() {
		
		return response;
		
	}

	@Override
	public Message decodeResp(PayLoad resp) {
		
		List<Parameter> params = new ArrayList();
		
		Message respmsg = new Message.Builder()
			.build();
		
		try{
			
			if("wps".equals(category)){
				
				if("1.0.0".equals(version)){
					
					ExecuteResponse er = WPSUtils.parseExecuteResp((String)resp.getContent());
					
					if(er.getStatus().getProcessSucceeded()!=null){
						
						List<OutputDataType> odtlist = er.getProcessOutputs().getOutput();
						
						for(OutputDataType odt : odtlist){
							
							Parameter p = new Parameter.Builder()
									.name(odt.getIdentifier().getValue())
									.build();
							
							if(odt.getReference()!=null){
								
								p.setValue(odt.getReference().getHref());
								
							}else{
								
								if(odt.getData().getLiteralData()!=null){
									
									p.setValue(odt.getData().getLiteralData().getValue());
									
								}else if(odt.getData().getComplexData()!=null){
									
									odt.getData().getComplexData().getMimeType();
									
									odt.getData().getComplexData().getContent();
									
								}else if(odt.getData().getBoundingBoxData()!=null){
									
									p.setValue(odt.getData().getBoundingBoxData().toString());
									
								}
								
							}
							
							params.add(p);
							
						}
						
						respmsg.setParameter_list(params);
						
					}else{
						
						respmsg.setError((String)resp.getContent());
						
					}
						
				}
				
			}
			
		}catch(Exception e){
			
			respmsg.setError((String)resp.getContent());
			
		}
		
		return respmsg;
		
	}
	
	@Override
	public Message decodeSUIS(Object rawmsg) {

		return null;
	}

	@Override
	public Object encodeSUIS(Message msg) {
		
		
		
		return null;
		
	}
	
	/**
	 * Initialize parameters
	 * @param o
	 * operation
	 */
	public void initParams(Operation o){
		
		if(o.getInput().getParameter_list()==null
				||o.getOutput().getParameter_list()==null){
			

			//get the inputs and outputs
			
			ProcessDescriptions pds = WPSUtils.getProcessDescription(o.getName(), this.getAccess_endpoint().toString());
			
			ProcessDescriptionType pdt = pds.getProcessDescription().get(0); 
			
			processdescriptions.put(o.getName(), pdt);
			
			List<Parameter> paramlist = new ArrayList();
			
			if(pdt.getDataInputs()!=null){
				
				List<InputDescriptionType> idts = pdt.getDataInputs().getInput();
		    	
		    	for(int j=0; j<idts.size(); j++){
		    		
		    		InputDescriptionType idt = idts.get(j);
		    		
		    		int minn = idt.getMinOccurs()==null?1:idt.getMinOccurs().intValue();
		    		
		    		int maxn = idt.getMaxOccurs()==null?1:idt.getMaxOccurs().intValue();
		    		
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
			
			o.setInput(inm);
			
			o.setOutput(outm);
			
		}
		
	}
	
	@Override
	public List<Operation> digest() {
		
		operlist = new ArrayList();
		
		if("wps".equals(category)){
			
			if(version==null||version.equals("1.0.0")){
				
				//add all the processes as operations
				
				WPSCapabilitiesType wct = WPSUtils.parseCapabilities(this.getDesc_endpoint().toString());
				
				capa = wct;
				
				try {
					
					this.setAccess_endpoint(new URL(WPSUtils.getExecuteEndpoint(wct)));
					
				} catch (MalformedURLException e) {
					
					e.printStackTrace();
					
				}
				
				List<ProcessBriefType> processes = wct.getProcessOfferings().getProcess();
				
				processdescriptions = new HashMap();
				
				for(int i=0;i<processes.size();i++){
					
					ProcessBriefType p = processes.get(i);
					
					String pname = p.getIdentifier().getValue();
					
					System.out.println("processing: " + pname);
					
					Operation o = new Operation.Builder()
							.name(pname)
							.build();
					
					operlist.add(o);
					
				}
				
			}else if(version.equals("2.0.0")||version.equals("2.0")){
				
				net.opengis.wps.v_2_0.WPSCapabilitiesType wct = WPSUtils.parseCapabilities20(this.getDesc_endpoint().toString());
				
				System.err.println("Not implemented yet.");
				
			}
			
		}else if("wcs".equals(category)){
			
			if(version==null||version.equals("2.0.0")){
				
				CapabilitiesType ct = WCSUtils.parseCapabilities(this.getDesc_endpoint().toString());
				
				capa = ct;

				//list coverages
				
				List<Parameter> inparams = new ArrayList();
				
				List<Parameter> outparams = new ArrayList();
				
				Parameter p = new Parameter.Builder()
						.name("coveragelist")
						.description("the list of coverages that this WCS hosts")
						.minoccurs(1)
						.maxoccurs(1)
						.type(DataType.STRING)
						.value(WCSUtils.getCoverageListString(ct))
						.build();
				
				outparams.add(p);
				
				Operation listcoverageoper = new Operation.Builder()
						.name("GetCoverageList")
						.output(new Message.Builder()
								.params(outparams)
								.build())
						.build();
				
				operlist.add(listcoverageoper);
				
				//describe coverage
				
				inparams = new ArrayList();
				
				inparams.add(new Parameter.Builder()
						.name("coverageId")
						.description("coverage identifier")
						.minoccurs(1)
						.type(DataType.STRING)
						.build());
				
				outparams = new ArrayList();
				
				outparams.add(new Parameter.Builder().name("coverageId").minoccurs(1).maxoccurs(1).build());
				
				outparams.add(new Parameter.Builder().name("coverage-Function").minoccurs(0).maxoccurs(1).build());
				
				outparams.add(new Parameter.Builder().name("metadata").minoccurs(0).maxoccurs(-1).build());
				
				outparams.add(new Parameter.Builder().name("domainSet").minoccurs(1).maxoccurs(1).build());
				
				outparams.add(new Parameter.Builder().name("rangeType").minoccurs(1).maxoccurs(1).build());
				
				outparams.add(new Parameter.Builder().name("service-Parameters").minoccurs(1).maxoccurs(1).build());
				
				Operation describecoverageoper = new Operation.Builder()
						.name("DescribeCoverage")
						.input(new Message.Builder()
								.params(inparams)
								.build())
						.output(new Message.Builder()
								.params(outparams)
								.build())
						.build();
				
				operlist.add(describecoverageoper);
				
				//get coverage
				
				inparams = new ArrayList();
				
				inparams.add(new Parameter.Builder().name("coverageId").minoccurs(1).maxoccurs(1).build());
				
				inparams.add(new Parameter.Builder().name("dimension-Subset").minoccurs(0).maxoccurs(-1).build());
				
				outparams = new ArrayList();
				
				outparams.add(new Parameter.Builder().name("coverage").minoccurs(1).maxoccurs(1).type(DataType.FILE).build());
				
				Operation getcoverageoper = new Operation.Builder()
						.name("GetCoverage")
						.input(new Message.Builder()
								.params(inparams)
								.build())
						.output(new Message.Builder()
								.params(outparams)
								.build())
						.build();
				
				operlist.add(getcoverageoper);
				
			}
			
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
		public AbstractDriver build() {
			
			return driver;
			
		}
		
	}
	
}
