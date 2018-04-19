package suis4j.driver;

import java.util.ArrayList;
import java.util.List;

import net.opengis.wcs.v_1_0_0.CoverageDescription;
import net.opengis.wcs.v_1_0_0.CoverageOfferingType;
import net.opengis.wcs.v_1_0_0.WCSCapabilitiesType;
import suis4j.profile.DataType;
import suis4j.profile.Message;
import suis4j.profile.Operation;
import suis4j.profile.Parameter;

/**
*Class WCS100.java
*@author Ziheng Sun
*@time Apr 17, 2018 8:46:58 PM
*/
public class WCS100Driver extends OGCDriver {

	@Override
	public PayLoad encodeReq(Message msg) {

		Object content = null;
		
		if("GetCoverageList".equals(this.getCurrent_operation())){
			
			content = WCSUtils.turn100GetCapabilitiesTypeToXML(
					WCSUtils.createA100GetCapabilitiesRequest());
			
		}else if("DescribeCoverage".equals(this.getCurrent_operation())){

			content = WCSUtils.turn100DescribeCoverageTypeToXML(
					WCSUtils.createA100DescribeCoverageRequest(
							msg.getValueAsString("coverageId"), this.getVersion()));
			
		}else if("GetCoverage".equals(this.getCurrent_operation())){
			
			content = WCSUtils.createA100GetCoverageRequest(
							msg.getValueAsString("coverage"), 
							msg.getValueAsString("format"), 
							this.getVersion(),
							msg.getValueAsString("crs"),
							msg.getValueAsString("response_crs"),
							msg.getValueAsString("bbox"),
							msg.getValueAsString("time"),
							msg.getValueAsString("parameter"),
							msg.getValueAsString("width"),
							msg.getValueAsString("height"),
							msg.getValueAsString("depth"),
							msg.getValueAsString("resx"),
							msg.getValueAsString("resy"),
							msg.getValueAsString("resz"),
							msg.getValueAsString("interpolation"),
							msg.getValueAsString("exceptions")
					);
			
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
				
				WCSCapabilitiesType ct = WCSUtils.parse100Capabilities(String.valueOf(resp.getContent()));
				
				oper.getOutput().get("coveragelist").setValue(WCSUtils.get100CoverageListString(ct));
				
			}else if("DescribeCoverage".equals(this.getCurrent_operation())){
				
				CoverageDescription cdts = WCSUtils.parse100CoverageDescriptions(String.valueOf(resp.getContent()));
				
				CoverageOfferingType co = cdts.getCoverageOffering().get(0);
				
				oper.getOutput().get("coverage").setValue(co.getWcsName());
				
				oper.getOutput().get("description").setValue(co.getDescription().getValue());
				
				oper.getOutput().get("label").setValue(co.getLabel());
				
				oper.getOutput().get("lonlatEnvolope").setValue(WCSUtils.turn100LonLatEnvelope(co.getLonLatEnvelope()));
				
				oper.getOutput().get("keywords").setValue(HttpUtils.turnListToString(co.getKeywords()));
				
				oper.getOutput().get("domainSet").setValue(WCSUtils.turn100DomainSetTypeToXML(co.getDomainSet()));
				
				oper.getOutput().get("rangeSet").setValue(WCSUtils.turn100RangeSetTypeToXML(co.getRangeSet()));
				
				oper.getOutput().get("supportedCRSs").setValue(WCSUtils.turn100SupportedCRSs(co.getSupportedCRSs()));

				oper.getOutput().get("supportedFormats").setValue(WCSUtils.turn100SupportedFormats(co.getSupportedFormats()));
				
				oper.getOutput().get("supportedInterpolations").setValue(WCSUtils.turn100SupportedInterpolations(co.getSupportedInterpolations()));
				
			}else if("GetCoverage".equals(this.getCurrent_operation())){
				
				//save the coverage to a temporary file and give the file path back
				
				oper.getOutput().get("coverage").setValue((String)resp.getContent());
				
				oper.getOutput().get("dataurl").setValue(this.dataurl);
				
				
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
			
			WCSCapabilitiesType ct = WCSUtils.parse100Capabilities(this.getDesc_endpoint().toString());
			
			capa = ct;
			
			this.setAccess_endpoint(WCSUtils.get100Endpoint(ct));

			//list coverages
			
			List<Parameter> inparams = new ArrayList();
			
			List<Parameter> outparams = new ArrayList();
			
			Parameter p = new Parameter.Builder()
					.name("coveragelist")
					.description("the list of coverages that this WCS hosts")
					.minoccurs(1)
					.maxoccurs(1)
					.type(DataType.STRING)
					.value(WCSUtils.get100CoverageListString(ct))
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
					.name("coverage")
					.description("coverage identifier")
					.minoccurs(1)
					.type(DataType.STRING)
					.build());
			
			outparams = new ArrayList();
			
			outparams.add(new Parameter.Builder().name("coverage").minoccurs(1).maxoccurs(1).build());
			
			outparams.add(new Parameter.Builder().name("description").minoccurs(0).maxoccurs(1).build());
			
			outparams.add(new Parameter.Builder().name("label").minoccurs(0).maxoccurs(-1).build());
			
			outparams.add(new Parameter.Builder().name("lonlatEnvolope").minoccurs(0).maxoccurs(-1).build());
			
			outparams.add(new Parameter.Builder().name("keywords").minoccurs(0).maxoccurs(-1).build());
			
			outparams.add(new Parameter.Builder().name("domainSet").minoccurs(1).maxoccurs(1).build());
			
			outparams.add(new Parameter.Builder().name("rangeSet").minoccurs(1).maxoccurs(1).build());

			outparams.add(new Parameter.Builder().name("supportedFormats").minoccurs(0).maxoccurs(-1).build());
			
			outparams.add(new Parameter.Builder().name("supportedCRSs").minoccurs(1).maxoccurs(1).build());
			
			outparams.add(new Parameter.Builder().name("supportedInterpolations").minoccurs(1).maxoccurs(1).build());
			
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
			
			inparams.add(new Parameter.Builder().name("coverage").minoccurs(1).maxoccurs(1).build());
			
			inparams.add(new Parameter.Builder().name("crs").minoccurs(1).maxoccurs(1).build());
			
			inparams.add(new Parameter.Builder().name("format").minoccurs(1).maxoccurs(1).build());
			
			inparams.add(new Parameter.Builder().name("response_crs").minoccurs(0).maxoccurs(1).build());
			
			inparams.add(new Parameter.Builder().name("bbox").minoccurs(1).maxoccurs(1).build());
			
			inparams.add(new Parameter.Builder().name("time").minoccurs(0).maxoccurs(1).build());
			
			inparams.add(new Parameter.Builder().name("parameter").minoccurs(0).maxoccurs(1).build());
			
			inparams.add(new Parameter.Builder().name("width").minoccurs(1).maxoccurs(1).build());
			
			inparams.add(new Parameter.Builder().name("height").minoccurs(1).maxoccurs(1).build());
			
			inparams.add(new Parameter.Builder().name("depth").minoccurs(0).maxoccurs(1).build());
			
			inparams.add(new Parameter.Builder().name("resx").minoccurs(0).maxoccurs(1).build());
			
			inparams.add(new Parameter.Builder().name("resy").minoccurs(0).maxoccurs(1).build());
			
			inparams.add(new Parameter.Builder().name("resz").minoccurs(0).maxoccurs(1).build());
			
			inparams.add(new Parameter.Builder().name("interpolation").minoccurs(0).maxoccurs(1).build());
			
			inparams.add(new Parameter.Builder().name("exceptions").minoccurs(0).maxoccurs(1).build());
			
			outparams = new ArrayList();
			
			outparams.add(new Parameter.Builder().name("coverage").minoccurs(1).maxoccurs(1).type(DataType.FILE).build());
			
			//this variable should appear in the ouputs of GetCoverage, GetMap, GetFeature
			outparams.add(new Parameter.Builder().name("dataurl").minoccurs(0).maxoccurs(1).type(DataType.FILE).build());
			
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
