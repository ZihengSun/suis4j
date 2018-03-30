package suis4j.driver;

import java.util.ArrayList;
import java.util.List;

import net.opengis.wcs.v_2_0.CapabilitiesType;
import net.opengis.wcs.v_2_0.CoverageDescriptionType;
import net.opengis.wcs.v_2_0.CoverageDescriptionsType;
import suis4j.profile.DataType;
import suis4j.profile.Message;
import suis4j.profile.Operation;
import suis4j.profile.Parameter;

/**
*Class WCS200Driver.java
*@author Ziheng Sun
*@time Mar 30, 2018 12:01:16 PM
*/
public class WCS200Driver extends OGCDriver{

	@Override
	public PayLoad encodeReq(Message msg) {
		
		Object content = null;
		
		if("GetCoverageList".equals(this.getCurrent_operation())){
			
			content = WCSUtils.turnGetCapabilitiesTypeToXML(
					WCSUtils.createAGetCapabilitiesRequest());
			
		}else if("DescribeCoverage".equals(this.getCurrent_operation())){

			content = WCSUtils.turnDescribeCoverageTypeToXML(
					WCSUtils.createADescribeCoverageRequest(
							msg.getValueAsString("coverageId"), this.getVersion()));
			
		}else if("GetCoverage".equals(this.getCurrent_operation())){
			
			content = WCSUtils.turnGetCoverageTypeToXML(
					WCSUtils.createAGetCoverageRequest(
							msg.getValueAsString("coverageId"), msg.getValueAsString("format"), this.getVersion()));
			
		}
		
		return new PayLoad.Builder()
				.content(content)
				.build();
	}
	
	@Override
	public Message decodeResp(PayLoad resp) {
		
		Operation oper = this.getOperation(this.getCurrent_operation());
		
		Message respmsg = new Message.Builder().build();
		
		try{
			
			if("GetCoverageList".equals(this.getCurrent_operation())){
				
				//list all the supported coverages
				
				CapabilitiesType ct = WCSUtils.parseCapabilities(String.valueOf(resp.getContent()));
				
				oper.getOutput().get("coveragelist").setValue(WCSUtils.getCoverageListString(ct));
				
			}else if("DescribeCoverage".equals(this.getCurrent_operation())){						
				
				CoverageDescriptionsType cdts = WCSUtils.parseCoverageDescriptions(String.valueOf(resp.getContent()));
				
				CoverageDescriptionType cdt = cdts.getCoverageDescription().get(0);
				
				oper.getOutput().get("coverageId").setValue(cdt.getCoverageId());
				
				oper.getOutput().get("metadata").setValue(WCSUtils.turnMetadataListToXML(cdt.getMetadata()));
				
				oper.getOutput().get("domainSet").setValue(WCSUtils.turnDomainSetTypeToXML(cdt.getDomainSet().getValue()));
				
				oper.getOutput().get("rangeType").setValue(WCSUtils.turnDataRecordPropertyTypeToXML(cdt.getRangeType()));
				
				oper.getOutput().get("service-Parameters").setValue(WCSUtils.turnServiceParametersToXML(cdt.getServiceParameters()));
				
			}else if("GetCoverage".equals(this.getCurrent_operation())){
				
				//save the coverage to a temporary file and give the file path back
				
				oper.getOutput().get("coverage").setValue((String)resp.getContent());
				
			}else{
				
				throw new RuntimeException("No such operation.");
				
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

		try {
			

			CapabilitiesType ct = WCSUtils.parseCapabilities(this.getDesc_endpoint().toString());
			
			capa = ct;
			
			this.setAccess_endpoint(WCSUtils.getEndpoint(ct));

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
					.input(new Message.Builder()
							.params(inparams)
							.build())
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
			
			inparams.add(new Parameter.Builder().name("format").minoccurs(0).maxoccurs(1).build());
			
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
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
		return super.digest();
	}

	
	
}
