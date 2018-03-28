package suis4j.driver;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.opengis.wcs.v_2_0.CapabilitiesType;
import net.opengis.wcs.v_2_0.CoverageDescriptionType;
import net.opengis.wcs.v_2_0.CoverageDescriptionsType;
import net.opengis.wfs.v_2_0.DescribeFeatureTypeType;
import net.opengis.wfs.v_2_0.WFSCapabilitiesType;
import net.opengis.wms.v_1_3_0.WMSCapabilities;
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
	
	static String TEMPORARY_PATH = System.getProperty("java.io.tmpdir") + File.separator;
	
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
				
				System.err.println("This version of WPS is not supported yet.");
			}
			
		}else if("wcs".equals(category)){
			
			if(version.equals("2.0.0")){
				
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
				
			}
			
		}else if("wfs".equals(category)){
			
			if(version.equals("2.0.0")){
				
				if("GetFeature".equals(this.getCurrent_operation())){
					
					content = msg.getValueAsString("query");
					
				}else if("DescribeFeatureType".equals(this.getCurrent_operation())){
					
//					DescribeFeatureTypeType dftt = WFSUtils.createADescribeFeatureTypeRequest(msg.getValueAsString("typeName"));
					
//					content = WFSUtils.turnDescribeFeatureTypeTypeToXML(dftt);
					
					content = "typeNames=" + msg.getValueAsString("typeName");
					
				}
				
			}else{
				
				throw new RuntimeException("This version of WFS is not supported.");
				
			}
			
			
		}else if("wms".equals(category)){
			
			if(version==null||version.equals("1.3.0")){
				
				if("GetCapabilities".equals(this.getCurrent_operation())){
					
					//do nothing
					
				}else if("GetMap".equals(this.getCurrent_operation())){
					
					content = "layers=" + msg.getValueAsString("layers") + "&bbox=" + msg.getValueAsString("bbox")
						+ "&width=" + msg.getValueAsString("width") + "&height=" + msg.getValueAsString("height")
						+ "&crs=" + msg.getValueAsString("crs") + "&format=" + msg.getValueAsString("format");
					
				}else{
					
					throw new RuntimeException("No such operation.");
					
				}
				
			}
			
		}else{
			
			throw new RuntimeException("Encoder doesn't support.");
			
		}
		
		return new PayLoad.Builder()
				.content(content)
				.build();
		
	}

	@Override
	public void send(PayLoad req) {
		
		try {
			
			String resp = null;
			
			System.out.println(">> "+(String)req.getContent());
			
			if("GetFeature".equals(this.getCurrent_operation())
					||"DescribeFeatureType".equals(this.getCurrent_operation())
					||"GetMap".equals(this.getCurrent_operation())){
				
				String filename = "temp-" + UUID.randomUUID().toString();
				
				//doGetFile
				
				String url = null;
				
				if(this.getAccess_endpoint().toString().endsWith("?")){
					
					url = this.getAccess_endpoint().toString();
					
				}else if(this.getAccess_endpoint().toString().contains("?")){
					
					url = this.getAccess_endpoint().toString() + "&";
					
				}else {
					
					url = this.getAccess_endpoint().toString() + "?";
					
				}
				
				url += "service="+category+"&version=" + version + "&request="+ this.getCurrent_operation() +"&" + String.valueOf(req.getContent());
				
				HttpUtils.doGETFile(url, TEMPORARY_PATH + filename);
				
				resp = "file:" + TEMPORARY_PATH + filename;
				
			}else if("GetCoverage".equals(this.getCurrent_operation())){
				
				//download the file and save to a temporary file path
				
				String filename = "coverage-" + UUID.randomUUID().toString();
				
				//doPostFile
				HttpUtils.doPostFile(this.getAccess_endpoint().toString(), String.valueOf(req.getContent()), TEMPORARY_PATH + filename);
				
				resp = "file:" + TEMPORARY_PATH + filename;
				
			}else if("GetCapabilities".equals(this.getCurrent_operation())){
				
				//do nothing
				
				resp = this.getOperation(this.getCurrent_operation()).getOutput().getValueAsString("capabilities");
				
			}else{
				
				resp = HttpUtils.doPost(this.getAccess_endpoint().toString(), (String)req.getContent());
				
			}
			
			System.out.println(">> " + resp);
			
			response = new PayLoad.Builder().content(resp).build();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			response = new PayLoad.Builder().content(e.getLocalizedMessage()).build();
			
		}
		
	}

	@Override
	public PayLoad receive() {
		
		return response;
		
	}

	@Override
	public Message decodeResp(PayLoad resp) {
		
//		List<Parameter> params = new ArrayList();
		
		Operation oper = this.getOperation(this.getCurrent_operation());
		
		Message respmsg = new Message.Builder().build();
		
		try{
			
			if("wps".equals(category)){
				
				if("1.0.0".equals(version)){
					
					ExecuteResponse er = WPSUtils.parseExecuteResp((String)resp.getContent());
					
					if(er.getStatus().getProcessSucceeded()!=null){
						
						List<OutputDataType> odtlist = er.getProcessOutputs().getOutput();
						
						for(OutputDataType odt : odtlist){
							
//							Parameter p = new Parameter.Builder()
//									.name(odt.getIdentifier().getValue())
//									.build();
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
							
//							params.add(p);
							
						}
						
//						respmsg.setParameter_list(params);
						
					}else{
						
						respmsg.setError((String)resp.getContent());
						
					}
					
				}
				
			}else if("wcs".equals(category)){
				
				if("2.0.0".equals(version)){
					
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
					
				}
				
			}else if("wfs".equals(category)){
				
				if(version.equals("2.0.0")){
					
					if("GetCapabilities".equals(oper.getName())){
						
						//do nothing
						
					}else if("GetFeature".equals(oper.getName())){
						
						oper.getOutput().get("featureCollection").setValue((String)resp.getContent());
						
					}else if("DescribeFeatureType".equals(oper.getName())){
						
						oper.getOutput().get("xmlSchema").setValue((String)resp.getContent());
						
					}else{
						
						throw new RuntimeException("No such operation.");
						
					}
					
				}else{
					
					throw new RuntimeException("Decoder doesn't support this version of WFS.");
					
				}
				
			}else if("wms".equals(category)){
				
				if(version.equals("1.3.0")){
					
					if("GetCapabilities".equals(oper.getName())){
						
						//do nothing
						
					}else if("GetMap".equals(oper.getName())){
						
						oper.getOutput().get("map").setValue((String)resp.getContent());
						
					}else{
						
						throw new RuntimeException("No such operation.");
						
					}
					
				}
				
			}else{
				
				throw new RuntimeException("This OGC type is not supported.");
				
			}
			
		}catch(Exception e){
			
			respmsg.setError((String)resp.getContent());
			
		}
		
		respmsg = oper.getOutput();
		
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
	
	private void digestWPS200(){
		
		net.opengis.wps.v_2_0.WPSCapabilitiesType wct = WPSUtils.parseCapabilities20(this.getDesc_endpoint().toString());
		
		System.err.println("Not implemented yet.");
		
	}
	
	private void digestWPS100() throws MalformedURLException{
		
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
		
	}
	
	private void digestWCS200() throws Exception{
		
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
		
	}
	/**
	 * Digest WFS 2.0.0
	 * @throws Exception
	 */
	private void digestWFS200() throws Exception{
		
		String capacontent = HttpUtils.doGet(this.getDesc_endpoint().toString());
		
		WFSCapabilitiesType wca = WFSUtils.parseCapabilities(capacontent);
		
		capa = wca;
		
		this.setAccess_endpoint(WFSUtils.getEndpoint(wca));
		
		//list features
		
		List<Parameter> inparams = new ArrayList();
		
		List<Parameter> outparams = new ArrayList();
		
		outparams.add(new Parameter.Builder().name("capabilities")
				.description("capabilities of the wfs")
				.minoccurs(1)
				.maxoccurs(1)
				.type(DataType.STRING)
				.value(capacontent)
				.build());
		
		Operation getcapabilitiesoper = new Operation.Builder()
				.name("GetCapabilities")
				.input(new Message.Builder()
						.params(inparams)
						.build())
				.output(new Message.Builder()
						.params(outparams)
						.build())
				.build();
		
		operlist.add(getcapabilitiesoper);
		
		//DescribeFeatureType
		
		inparams = new ArrayList();
		
		inparams.add(new Parameter.Builder()
				.name("typeName")
				.description("a comma separated list of feature types to describe. If no value is specified, the complete application schema offered by the server shall be described.")
				.minoccurs(0)
				.maxoccurs(1)
				.type(DataType.STRING)
				.build());
		
		inparams.add(new Parameter.Builder()
				.name("outputFormat")
				.description("Shall support the value <application/gml+xml; version=3.2> indicating that a GML (see ISO19136:2007) application schema shall be generated. A server may support other values to which this Interantional Standard does not assign any meaning.")
				.minoccurs(0)
				.maxoccurs(1)
				.build());
		
		outparams = new ArrayList();
		
		outparams.add(new Parameter.Builder()
				.name("xmlSchema")
				.description("In the event that a DescribeFeatureType operation requests that feature types in multiple namespaces be described, the server shall generate the complete shcema for one of the requested namespaces and import the remaining namespaces. The WFS standard doesn't madate which naemspace's schema should be generated and which namespaces should be imported.")
				.minoccurs(1)
				.maxoccurs(1)
				.type(DataType.FILE)
				.build());
		
		Operation describefeaturetypeoper = new Operation.Builder()
				.name("DescribeFeatureType")
				.input(new Message.Builder()
						.params(inparams)
						.build())
				.output(new Message.Builder()
						.params(outparams)
						.build())
				.build();
		
		operlist.add(describefeaturetypeoper);
		
		//get feature
		
		inparams = new ArrayList();
		
		inparams.add(new Parameter.Builder().name("query")
				.description("A GetFeature request contains one or more query expressions. A query expression identifies a set of feature instances that shall be presented to a client in the response document. Query expressions in a GetFeature request shall be independent on each other and may be executed in any order.")
				.minoccurs(1).maxoccurs(-1).build());
		
		inparams.add(new Parameter.Builder().name("resolve").minoccurs(0).maxoccurs(1).build());
		
		inparams.add(new Parameter.Builder().name("resolveDepth").minoccurs(0).maxoccurs(1).build());
		
		inparams.add(new Parameter.Builder().name("resolveTimeout").minoccurs(0).maxoccurs(1).build());
		
		inparams.add(new Parameter.Builder().name("startIndex").minoccurs(0).maxoccurs(1).build());
		
		inparams.add(new Parameter.Builder().name("count").minoccurs(0).maxoccurs(1).build());
		
		inparams.add(new Parameter.Builder().name("outputFormat").minoccurs(0).maxoccurs(1).build());
		
		inparams.add(new Parameter.Builder().name("resultStyle").minoccurs(0).maxoccurs(1).build());
		
		//storedquery and adhocquery is not considered at present. Will be added soon. - Z.S. on 20180328
		
		outparams = new ArrayList();
		
		outparams.add(new Parameter.Builder().name("featureCollection").minoccurs(1).maxoccurs(1).type(DataType.FILE).build());
		
		Operation getfeatureoper = new Operation.Builder()
				.name("GetFeature")
				.input(new Message.Builder()
						.params(inparams)
						.build())
				.output(new Message.Builder()
						.params(outparams)
						.build())
				.build();
		
		operlist.add(getfeatureoper);
		
		//get property value
		
		inparams = new ArrayList();
		
		inparams.add(new Parameter.Builder().name("query")
				.description("A query expression identifies a set of feature instances whose property values shall be presented to a client in the response document.")
				.minoccurs(1).maxoccurs(-1).build());
		
		inparams.add(new Parameter.Builder().name("valueReference")
				.description("The valueRefernece parameter is an XPath expression that identifies a node, or child node of a property node of a feature whose value shall be retrieved from a server's data store and reported in the response document.")
				.minoccurs(1).maxoccurs(1).build());
		
		outparams = new ArrayList();
		
		outparams.add(new Parameter.Builder().name("valueCollection").minoccurs(1).maxoccurs(1).type(DataType.FILE).build());
		
		Operation getpropertyvalueoper = new Operation.Builder()
				.name("GetPropertyValue")
				.input(new Message.Builder()
						.params(inparams)
						.build())
				.output(new Message.Builder()
						.params(outparams)
						.build())
				.build();
		
		operlist.add(getpropertyvalueoper);
		
	}
	
	private void digestWMS130() throws Exception{
		
		String capacontent = HttpUtils.doGet(this.getDesc_endpoint().toString());
		
		WMSCapabilities wmc = WMSUtils.parseWMSCapabilityResponse(capacontent);
		
		capa = wmc;
		
		this.setAccess_endpoint(WMSUtils.getEndpoint(wmc));
		
		//GetCapabilities
		
		List<Parameter> inparams = new ArrayList();
		
		List<Parameter> outparams = new ArrayList();
		
		outparams.add(new Parameter.Builder().name("capabilities")
				.description("capabilities of the wms")
				.minoccurs(1).maxoccurs(1).type(DataType.STRING)	.value(capacontent).build());
		
		Operation getcapabilitiesoper = new Operation.Builder()
				.name("GetCapabilities")
				.input(new Message.Builder()
						.params(inparams)
						.build())
				.output(new Message.Builder()
						.params(outparams)
						.build())
				.build();
		
		operlist.add(getcapabilitiesoper);
		
		//GetMap
		
		inparams = new ArrayList();
		
		inparams.add(new Parameter.Builder().name("layers")
				.description("comma-separated list of one or more map layers")
				.minoccurs(1).maxoccurs(1).build());
		
		inparams.add(new Parameter.Builder().name("styles")
				.description("comma-separated list of one rendering style per requested layer")
				.minoccurs(1).maxoccurs(1).build());
		
		inparams.add(new Parameter.Builder().name("crs")
				.description("coordinate reference system")
				.minoccurs(1).maxoccurs(1).build());
		
		inparams.add(new Parameter.Builder().name("bbox")
				.description("bounding box corners (lower left, upper right) in CRS units")
				.minoccurs(1).maxoccurs(1).build());
		
		inparams.add(new Parameter.Builder().name("width")
				.description("width in pixels of map picture")
				.minoccurs(1).maxoccurs(1).build());
		
		inparams.add(new Parameter.Builder().name("height")
				.description("height in pixels of map picture")
				.minoccurs(1).maxoccurs(1).build());
		
		inparams.add(new Parameter.Builder().name("format")
				.description("output format of map")
				.minoccurs(1).maxoccurs(1).build());
		
		inparams.add(new Parameter.Builder()
				.name("transparent")
				.description("background transparency of map (default = FALSE)")
				.minoccurs(0).maxoccurs(1).build());
		
		inparams.add(new Parameter.Builder()
				.name("bgcolor")
				.description("hexadecimal red-gree-blue colour value for the background color (default=0xFFFFFF)")
				.minoccurs(0).maxoccurs(1).build());
		
		inparams.add(new Parameter.Builder()
				.name("exceptions")
				.description("the format in which exceptions are to be reported by the WMS (default=XML)")
				.minoccurs(0).maxoccurs(1).build());
		
		inparams.add(new Parameter.Builder()
				.name("elevation")
				.description("Elevation of layer desired")
				.minoccurs(0).maxoccurs(1).build());
		
		inparams.add(new Parameter.Builder()
				.name("time")
				.description("time value of layer desired")
				.minoccurs(0).maxoccurs(1).build());
		
		outparams = new ArrayList();
		
		outparams.add(new Parameter.Builder().name("map")
				.description("a map of the spatially referenced information layer requested, in the desired style, and having the specified coordiante reference system, bounding box, size, format and transparency")
				.minoccurs(1).maxoccurs(1).build());
		
		Operation getmapoper = new Operation.Builder()
				.name("GetMap")
				.input(new Message.Builder()
						.params(inparams)
						.build())
				.output(new Message.Builder()
						.params(outparams)
						.build())
				.build();
		
		operlist.add(getmapoper);
		
	}
	
	@Override
	public List<Operation> digest() {
		
		operlist = new ArrayList();

		try {
			
			if("wps".equals(category)){
				
				if(version==null||version.equals("1.0.0")){
					
					this.digestWPS100();
					
				}else if(version.equals("2.0.0")||version.equals("2.0")){
					
					this.digestWPS200();
					
				}
				
			}else if("wcs".equals(category)){
				
				if(version==null||version.equals("2.0.0")){
					
					this.digestWCS200();
					
				}
				
			}else if("wfs".equals(category)){
				
				if(version==null||version.equals("2.0.0")){
					
					this.digestWFS200();
					
				}else{
					
					throw new RuntimeException("This version of WFS is not supported yet.");
					
				}
				
			}else if("wms".equals(category)){
				
				if(version==null||version.equals("1.3.0")){
					
					this.digestWMS130();
					
				}else{
					
					throw new RuntimeException("This version of WMS is not supported yet.");
					
				}
				
			}else if("csw".equals(category)){
				
				throw new RuntimeException("Not supported");
				
			}else{
				
				throw new RuntimeException("The service category ["+category+"] is not supported yet.");
				
			}
			
			connect();

		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
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
