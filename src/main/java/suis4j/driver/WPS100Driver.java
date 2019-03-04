package suis4j.driver;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.opengis.wps.v_1_0_0.Execute;
import net.opengis.wps.v_1_0_0.ExecuteResponse;
import net.opengis.wps.v_1_0_0.InputDescriptionType;
import net.opengis.wps.v_1_0_0.OutputDataType;
import net.opengis.wps.v_1_0_0.OutputDescriptionType;
import net.opengis.wps.v_1_0_0.ProcessBriefType;
import net.opengis.wps.v_1_0_0.ProcessDescriptionType;
import net.opengis.wps.v_1_0_0.ProcessDescriptions;
import net.opengis.wps.v_1_0_0.WPSCapabilitiesType;
import suis4j.profile.Message;
import suis4j.profile.Operation;
import suis4j.profile.Parameter;

/**
*Class WPS100Driver.java
*@author Ziheng Sun
*@time Mar 30, 2018 12:24:03 PM
*/
public class WPS100Driver extends OGCDriver{

	@Override
	public PayLoad encodeReq(Message msg) {
		
		Object content = null;
		
		WPSCapabilitiesType wpscapa = (WPSCapabilitiesType) capa;
		
		ProcessDescriptionType pdt = (ProcessDescriptionType)processdescriptions.get(this.getCurrent_operation());
		
		Execute exe = WPSUtils.getExecuteRequest(wpscapa, pdt, msg.toKVPs());
		
		content = WPSUtils.turnExecuteReqToXML(exe); //turn the Execute object to xml
		
		return new PayLoad.Builder()
				.content(content)
				.build();
		
	}

	@Override
	public Message decodeResp(PayLoad resp) {
		
		Operation oper = this.getOperation(this.getCurrent_operation());
		
		Message respmsg = new Message.Builder().build();
		
		try{
			
			ExecuteResponse er = WPSUtils.parseExecuteResp((String)resp.getContent());
			
			if(er.getStatus().getProcessSucceeded()!=null){
				
				List<OutputDataType> odtlist = er.getProcessOutputs().getOutput();
				
				for(OutputDataType odt : odtlist){
					
					Parameter p = oper.getOutput().get(odt.getIdentifier().getValue());
					
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
					
//					params.add(p);
					
				}
				
//				respmsg.setParameter_list(params);
				
			}else{
				
				respmsg.setError((String)resp.getContent());
				
			}
			
		}catch(Exception e){
			
			respmsg.setError((String)resp.getContent());
			
		}
		
		respmsg = oper.getOutput();
		
		return respmsg;
		
	}

	@Override
	public List<Operation> digest() {

		operlist = new ArrayList();

		try{

			//add all the processes as operations
			
			WPSCapabilitiesType wct = WPSUtils.parseCapabilities(this.getDesc_endpoint().toString());
			
			capa = wct;
			
			this.setAccess_endpoint(new URL(WPSUtils.getExecuteEndpoint(wct)));
			
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
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}

		return super.digest();
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
	
	
}
