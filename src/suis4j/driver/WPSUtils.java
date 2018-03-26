package suis4j.driver;

import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import net.opengis.ows.v_1_1_0.Operation;
import net.opengis.wps.v_1_0_0.ComplexDataCombinationsType;
import net.opengis.wps.v_1_0_0.ComplexDataDescriptionType;
import net.opengis.wps.v_1_0_0.ComplexDataType;
import net.opengis.wps.v_1_0_0.DataInputsType;
import net.opengis.wps.v_1_0_0.DataType;
import net.opengis.wps.v_1_0_0.DocumentOutputDefinitionType;
import net.opengis.wps.v_1_0_0.Execute;
import net.opengis.wps.v_1_0_0.ExecuteResponse;
import net.opengis.wps.v_1_0_0.InputDescriptionType;
import net.opengis.wps.v_1_0_0.InputReferenceType;
import net.opengis.wps.v_1_0_0.InputType;
import net.opengis.wps.v_1_0_0.LiteralDataType;
import net.opengis.wps.v_1_0_0.ObjectFactory;
import net.opengis.wps.v_1_0_0.OutputDescriptionType;
import net.opengis.wps.v_1_0_0.ProcessBriefType;
import net.opengis.wps.v_1_0_0.ProcessDescriptionType;
import net.opengis.wps.v_1_0_0.ProcessDescriptions;
import net.opengis.wps.v_1_0_0.ProcessOfferings;
import net.opengis.wps.v_1_0_0.ResponseDocumentType;
import net.opengis.wps.v_1_0_0.ResponseFormType;
import net.opengis.wps.v_1_0_0.WPSCapabilitiesType;
import net.opengis.wps.v_2_0.DataTransmissionModeType;
import net.opengis.wps.v_2_0.ExecuteRequestType;
import net.opengis.wps.v_2_0.ProcessOffering;
import net.opengis.wps.v_2_0.ProcessSummaryType;

/**
*Class WPSUtils.java
*@author Ziheng Sun
*@time May 17, 2016 10:29:12 AM
*Original aim is to support OGC Testbed 12.
*/
public class WPSUtils {
	
	/**
	 * Parse capabilities XML
	 * @param xml
	 * @return
	 */
	public static WPSCapabilitiesType parseCapabilitiesXML(String xml){
		WPSCapabilitiesType ca = JAXB.unmarshal(new StringReader(xml), WPSCapabilitiesType.class);
		return ca;
	}
	/**
	 * add by Z.S. on 9/28/2016
	 * @param xml
	 * @return
	 */
	public static net.opengis.wps.v_2_0.WPSCapabilitiesType parseCapabilitiesXML20(String xml){
		
		net.opengis.wps.v_2_0.WPSCapabilitiesType ca =  JAXB.unmarshal(new StringReader(xml), net.opengis.wps.v_2_0.WPSCapabilitiesType.class);
		
		return ca;
		
	}
	
	/**
	 * Get Process List
	 * @param cat
	 * @return
	 */
	public static List<ProcessBriefType> getProcessList(WPSCapabilitiesType cat){
		ProcessOfferings pos = cat.getProcessOfferings();
		return pos.getProcess();
	}
	/**
	 * Get ProcessSummary List
	 * @param cat
	 * @return
	 */
	public static List<ProcessSummaryType> getProcessList20(net.opengis.wps.v_2_0.WPSCapabilitiesType cat){
		List<ProcessSummaryType> pos = cat.getContents().getProcessSummary();
		return pos;
	}
	
	/**
	 * Get Process Description 2.0
	 * @param identifier
	 * @return
	 */
	public static net.opengis.wps.v_2_0.ProcessOfferings getProcessDescription20(String identifier, String describeprocessurl){
		
		if(!describeprocessurl.endsWith("?")){
			
			describeprocessurl += "?";
			
		}
		
		String dpreq = describeprocessurl + "service=WPS&version=2.0.0&request=DescribeProcess&identifier=" + identifier ;
		net.opengis.wps.v_2_0.ProcessOfferings ca = null;
		try {
			String resp = HttpUtils.doGet(dpreq);
			System.out.println(resp);
			ca = JAXB.unmarshal(new StringReader(resp.trim()), net.opengis.wps.v_2_0.ProcessOfferings.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Fail to get the process description. Request: " + dpreq);
		}
		
		return ca;
		
	}
	
	/**
	 * Get Process Description
	 * @param identifier
	 * @return
	 */
	public static ProcessDescriptions getProcessDescription(String identifier, String describeprocessurl){
		
		String dpreq = null;
		
		if(describeprocessurl.endsWith("?")){
			
			dpreq = describeprocessurl + "service=WPS&version=1.0.0&request=DescribeProcess&identifier=" + identifier ;
			
		}else{
			
			dpreq = describeprocessurl + "?service=WPS&version=1.0.0&request=DescribeProcess&identifier=" + identifier ;
			
		}
		
		ProcessDescriptions ca = null;
		try {
			String resp = HttpUtils.doGet(dpreq);
//			System.out.println(resp);
			ca = JAXB.unmarshal(new StringReader(resp.trim()), ProcessDescriptions.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Fail to get the process description. Request: " + dpreq);
		}
		
		return ca;
		
	}
	
	/**
	 * Parse WPS capabilities document
	 * @param url
	 * @return
	
	public static net.opengis.wps.v_2_0.WPSCapabilitiesType parseCapabilities20(String url){
		
		throw new UnsupportedOperationException("This version is still pending");
		
	}
	 */
   public static net.opengis.wps.v_2_0.WPSCapabilitiesType parseCapabilities20(String url){
		
	   net.opengis.wps.v_2_0.WPSCapabilitiesType ca =  null;
	   
	   String urlsub = url;
	   
		try {
			
			String capabilitydoc = HttpUtils.doGet(urlsub);
			
			ca = parseCapabilitiesXML20(capabilitydoc);
			
//				System.out.print(ca.getServiceProvider().getProviderName());
//				getCapabilitiesResponse = WMSUtils.getWMSGetCapabilitiesResponse(prefix, cont);
//				System.out.println(getCapabilitiesResponse);
//				ca = WMSUtils.parseWMSCapabilityResponse(getCapabilitiesResponse);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Fail to parse the capabilities document." + e.getLocalizedMessage());
		}
		return ca;
		
	}
   
   /**
    * add by Z.S. on 9/28/2016
    * @param jobid
    * @return
    */
   public static net.opengis.wps.v_2_0.GetResult createGetResultReq(String jobid){
	   
	   net.opengis.wps.v_2_0.ObjectFactory of = new net.opengis.wps.v_2_0.ObjectFactory();
	   
	   net.opengis.wps.v_2_0.GetResult getresultreq = of.createGetResult();
	   
	   getresultreq.setJobID(jobid);
	   
	   return getresultreq;
	   
   }
   /**
    *  add by Z.S. on 9/28/2016
    * @param getresultreq
    * @return
    */
   public static String turnGetResult2XML(net.opengis.wps.v_2_0.GetResult getresultreq){
	   
	   String theXML = null;
		try{
			// serialize to xml
			StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(net.opengis.wps.v_2_0.GetResult.class);            
			Marshaller m = context.createMarshaller();
			m.marshal(getresultreq, writer);
			// output string to console
			 theXML = writer.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return theXML;
	   
   }
   
   /**
    * created by Z.S. on 9/28/2016
    * @param resp
    * @return
    */
   public static net.opengis.wps.v_2_0.Result parseResultResponse(String resp){
	   
	   net.opengis.wps.v_2_0.Result ca =  null;
		try {
			
			ca =  JAXB.unmarshal(new StringReader(resp), net.opengis.wps.v_2_0.Result.class);
			
//				System.out.print(ca.getServiceProvider().getProviderName());
//				getCapabilitiesResponse = WMSUtils.getWMSGetCapabilitiesResponse(prefix, cont);
//				System.out.println(getCapabilitiesResponse);
//				ca = WMSUtils.parseWMSCapabilityResponse(getCapabilitiesResponse);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Fail to parse the capabilities document." + e.getLocalizedMessage());
		}
		return ca;
   }
	
	/**
	 * 
	 * @param ca
	 * @return
	 */
	public static String getExecuteEndpoint(WPSCapabilitiesType ca){
		
		List<Operation> opers = ca.getOperationsMetadata().getOperation();
		
		String exeurl = null;
		
		for(int i=0; i<opers.size(); i++){
			
			Operation oper = opers.get(i);
			
			if("Execute".equals(oper.getName())){
				
				exeurl = oper.getDCP().get(0).getHTTP().getGetOrPost().get(0).getValue().getHref();
				
			}
			
		}
		
		return exeurl;
		
	}
	
	/**
	 * Parsing WPS 1.0.0 capabilities
	 * @param url
	 * @return
	 */
	public static WPSCapabilitiesType parseCapabilities(String url){
		
		WPSCapabilitiesType ca =  null;
		
		try {
			
			String capabilitydoc = HttpUtils.doGet(url);
			
			ca = parseCapabilitiesXML(capabilitydoc);
			
//				System.out.print(ca.getServiceProvider().getProviderName());
//				getCapabilitiesResponse = WMSUtils.getWMSGetCapabilitiesResponse(prefix, cont);
//				System.out.println(getCapabilitiesResponse);
//				ca = WMSUtils.parseWMSCapabilityResponse(getCapabilitiesResponse);
		} catch (Exception e) {
			
			e.printStackTrace();
			
			throw new RuntimeException("Fail to parse the capabilities document." + e.getLocalizedMessage());
			
		}
		
		return ca;
		
	}
	/**
	 * Async Execute
	 * by Z.S. on 09/28/2016
	 * @param ca
	 * @param dt
	 * @param kvp
	 * @return
	 */
	public static Execute getExecuteRequestAsync(WPSCapabilitiesType ca, ProcessDescriptions dt, Map kvp) {
		
		ObjectFactory of = new ObjectFactory();
		
		Execute exe = of.createExecute();
		
		
		//set process identifier
		
		ProcessDescriptionType pdt = dt.getProcessDescription().get(0);
		
		exe.setIdentifier(pdt.getIdentifier());
		
		//ResponseFormType
		
		ResponseFormType rft = of.createResponseFormType();
		
		ResponseDocumentType rdt = of.createResponseDocumentType();
		
		List<OutputDescriptionType> odts = pdt.getProcessOutputs().getOutput();
		
		List<DocumentOutputDefinitionType> dodts = new ArrayList();
		
		//generate process outputs
		
		for(int i=0; i<odts.size(); i++){
			
			OutputDescriptionType odt = odts.get(i);
			
			DocumentOutputDefinitionType dodt = of.createDocumentOutputDefinitionType();
			
			dodt.setAbstract(odt.getAbstract());
			
			dodt.setIdentifier(odt.getIdentifier());
			
			dodt.setTitle(odt.getTitle());
			
			if(odt.getComplexOutput()!=null){
				
				ComplexDataCombinationsType cdct = odt.getComplexOutput().getSupported();
				
				ComplexDataDescriptionType cddt = cdct.getFormat().get(0);
				
				dodt.setMimeType(cddt.getMimeType());
				
			}

			
			dodts.add(dodt);
		}
		
		rdt.setOutput(dodts);
		
		//add input parameters
		
		DataInputsType di = of.createDataInputsType();
		
		List<InputDescriptionType> idtlist = pdt.getDataInputs().getInput();
		
		List<InputType> inputs = new ArrayList();
		
		for(int i=0; i<idtlist.size();i++){
			
			InputDescriptionType idt = idtlist.get(i);
			
			InputType it = of.createInputType();
			
			it.setIdentifier(idt.getIdentifier());
			
			it.setTitle(idt.getTitle());
			
			DataType newdt = of.createDataType();
			
			if(idt.getLiteralData() != null){
				
				LiteralDataType  ldt = of.createLiteralDataType();
				
				ldt.setValue((String)kvp.get(idt.getIdentifier().getValue()));
				
				newdt.setLiteralData(ldt);
				
				it.setData(newdt);
				
			}else if(idt.getComplexData()!=null){
				
				ComplexDataType cdt = of.createComplexDataType();
				
				ComplexDataDescriptionType cddt = idt.getComplexData().getSupported().getFormat().get(0);
				
				cdt.setEncoding(cddt.getEncoding());
				
				cdt.setMimeType(cddt.getMimeType());
				
				List<Serializable> conts = new ArrayList();
				
				conts.add((String)kvp.get(idt.getIdentifier().getValue()));
				
				cdt.setContent((List<Serializable>)conts);
				
				newdt.setComplexData(cdt);
				
				it.setData(newdt);
				
//				InputReferenceType irt = of.createInputReferenceType();
//				
//				irt.setHref((String)kvp.get(idt.getIdentifier().getValue()));
//				
//				irt.setMethod("GET");
//				
//				it.setReference(irt);
				
			}else {
				
				throw new RuntimeException("Unable to recognize the input type.");
				
			}
			
			inputs.add(it);
			
		}
		
		di.setInput(inputs);
		
		exe.setDataInputs(di);
		
//		rdt.setOutput(arg0);
		
		rft.setResponseDocument(rdt);
		
		exe.setResponseForm(rft);
		
		
		return exe;
		
	}
	/**
	 * For 2.0.0
	 * @param ca
	 * @param dt
	 * @param kvp
	 * @return
	 */
	public static net.opengis.wps.v_2_0.ExecuteRequestType getExecuteRequest20(net.opengis.wps.v_2_0.WPSCapabilitiesType ca, net.opengis.wps.v_2_0.ProcessOfferings dt, Map kvp, boolean isSync) {
		
		net.opengis.wps.v_2_0.ObjectFactory of = new net.opengis.wps.v_2_0.ObjectFactory();
		
//		<wps:Execute xmlns:wps="http://www.opengis.net/wps/2.0"
//			    xmlns:ows="http://www.opengis.net/ows/2.0" xmlns:xlink="http://www.w3.org/1999/xlink"
//			    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
//			    xsi:schemaLocation="http://www.opengis.net/wps/2.0 http://schemas.opengis.net/wps/2.0/wps.xsd"
//			    service="WPS" version="2.0.0" response="document" mode="sync">
//			    <ows:Identifier>testbed12.cmd.AsyncFacadeProcess</ows:Identifier>
//			    <wps:Input id="soap-request">
//			        <wps:Data mimeType="application/soap+xml"></wps:Data>
//			    </wps:Input>
//			    <wps:Input id="endpoint-url">
//			        <wps:Data>
//			            <wps:LiteralValue>https://polar.geodacenter.org:8443/wfs-soap</wps:LiteralValue>
//			        </wps:Data>
//			    </wps:Input>
//			    <wps:Output id="soap-response" transmission="value" mimeType="application/soap+xml" />
//			</wps:Execute>
		
		net.opengis.wps.v_2_0.ExecuteRequestType exe = of.createExecuteRequestType();
		
		//set process identifier
		
		ProcessOffering pdt = dt.getProcessOffering().get(0);
		
		exe.setIdentifier(pdt.getProcess().getIdentifier());
		
		exe.setResponse("document");
		
		if(isSync){

			exe.setMode("sync");
			
		}else{
			
			exe.setMode("async");
			
		}
		//ResponseFormType
		
//		ResponseFormType rft = of.createResponseFormType();
		
//		ResponseDocumentType rdt = of.createResponseDocumentType();
		
		List<net.opengis.wps.v_2_0.OutputDescriptionType> odts = pdt.getProcess().getOutput();
		
		List<net.opengis.wps.v_2_0.OutputDefinitionType> dodts = new ArrayList();
		
		//generate process outputs
		
		for(int i=0; i<odts.size(); i++){
			
			net.opengis.wps.v_2_0.OutputDescriptionType odt = odts.get(i);
			
			net.opengis.wps.v_2_0.OutputDefinitionType dodt = of.createOutputDefinitionType();
			
			dodt.setId(odt.getIdentifier().getValue());
			
			if(odt.getDataDescription().getValue() instanceof net.opengis.wps.v_2_0.ComplexDataType){
				
//				ComplexDataCombinationsType cdct = odt.getComplexOutput().getSupported();
//				
//				ComplexDataDescriptionType cddt = cdct.getFormat().get(0);
				
				net.opengis.wps.v_2_0.ComplexDataType cdt =  (net.opengis.wps.v_2_0.ComplexDataType) odt.getDataDescription().getValue();
				
				//TODO: this should be set to REFERENCE url or provide a option for users to specify
				
				dodt.setTransmission(DataTransmissionModeType.REFERENCE); 
				
				//TODO: use the default supported format
				
				net.opengis.wps.v_2_0.Format defaultformat = null;
				
				for(net.opengis.wps.v_2_0.Format f : cdt.getFormat()){
					
					if(f.isDefault()){
						
						defaultformat = f;
						
						break;
						
					}
					
				}
				
				//dodt.setMimeType(cdt.getFormat().get(1).getMimeType()); //base 64 - shape zip
				
				dodt.setMimeType(defaultformat.getMimeType()); //shape zip
				
				
			}
//			else if(odt.getDataDescription().getValue() instanceof net.opengis.wps.v_2_0.LiteralDataType){
//				
//				net.opengis.wps.v_2_0.LiteralDataType ldt = (net.opengis.wps.v_2_0.LiteralDataType) odt.getDataDescription().getValue();
//				
//				ldt.setLiteralDataDomain(arg0);
//				
//			}
			
//			dodt.setMimeType(odt.getMimeType());

			
			dodts.add(dodt);
		}
		
		exe.setOutput(dodts);
		
		//add input parameters
		
		List<net.opengis.wps.v_2_0.InputDescriptionType> idts = pdt.getProcess().getInput();
		
		List<net.opengis.wps.v_2_0.DataInputType> iodts = new ArrayList();
		
		for(int i=0; i<idts.size();i++){
			
			net.opengis.wps.v_2_0.InputDescriptionType idt = idts.get(i);
			
			net.opengis.wps.v_2_0.DataInputType it = of.createDataInputType();
			
			it.setId(idt.getIdentifier().getValue());
			
//			it.setTitle(idt.getTitle());
			
			net.opengis.wps.v_2_0.Data newdt = of.createData();
			
			if(idt.getDataDescription().getValue() instanceof net.opengis.wps.v_2_0.LiteralDataType){
				
				//TODO: this string thing is for temperal use, find out how to use the object
				
				if(kvp.get(idt.getIdentifier().getValue())==null){
					
					continue;
					
				}
				
				net.opengis.wps.v_2_0.LiteralValue ldt = of.createLiteralValue();
				
				ldt.setValue((String)kvp.get(idt.getIdentifier().getValue()));
				
				String literalvalue = "<ns4:LiteralValue>"+(String)kvp.get(idt.getIdentifier().getValue())+"</ns4:LiteralValue>";
				
				List<Serializable> contents = new ArrayList();
				
				contents.add(literalvalue);
				
				newdt.setContent(contents);
				
				it.setData(newdt);
				
			}else if(idt.getDataDescription().getValue() instanceof net.opengis.wps.v_2_0.ComplexDataType){
				
				//use reference to load the complex data
				
				net.opengis.wps.v_2_0.ReferenceType rt = of.createReferenceType();
				
				net.opengis.wps.v_2_0.ComplexDataType cdt = (net.opengis.wps.v_2_0.ComplexDataType)idt.getDataDescription().getValue();
				
				//String defaultformatmimetype = "application/x-zipped-shp"; //for temperal use TODO list
				
				//net.opengis.wps.v_2_0.Format format = cdt.getFormat().get(1); //base64 shape-zip
				
//				newdt.setEncoding(format.getEncoding());
//				
//				newdt.setMimeType(format.getMimeType());
				
//				rt.setMimeType(format.getMimeType());
				
				//get default format
				
				net.opengis.wps.v_2_0.Format defaultformat = null;
				
				for(net.opengis.wps.v_2_0.Format f : cdt.getFormat()){
					
					if(f.isDefault()){
						
						defaultformat = f;
						
						break;
						
					}
					
				}
				
				String dataurl = (String)kvp.get(idt.getIdentifier().getValue());
				
				if(dataurl.endsWith(".zip")&&!defaultformat.getMimeType().equals("application/x-zipped-shp")){
					
					rt.setMimeType("application/x-zipped-shp");
					
				}else{
					
					rt.setMimeType(defaultformat.getMimeType());
					
				}
				
				
				
				rt.setHref(dataurl);
				
//				List<Serializable> conts = new ArrayList();
//				
//				conts.add((String)kvp.get(idt.getIdentifier().getValue()));
//				
//				newdt.setContent((List<Serializable>)conts);
//				
//				it.setData(newdt);
				
				it.setReference(rt);
				
//				InputReferenceType irt = of.createInputReferenceType();
//				
//				irt.setHref((String)kvp.get(idt.getIdentifier().getValue()));
//				
//				irt.setMethod("GET");
//				
//				it.setReference(irt);
				
			}else {
				
				throw new RuntimeException("Unable to recognize the input type.");
				
			}
			
			iodts.add(it);
			
		}
		
		
		exe.setInput(iodts);
		
		
		
		return exe;
		
	}
	/**
	 * For version 1.0.0
	 * @param ca
	 * @param pdt
	 * @param kvp
	 * @return
	 */
	public static Execute getExecuteRequest(WPSCapabilitiesType ca, ProcessDescriptionType pdt, Map kvp) {
		
		ObjectFactory of = new ObjectFactory();
		
		Execute exe = of.createExecute();
		
		exe.setIdentifier(pdt.getIdentifier());
		
		//ResponseFormType
		
		ResponseFormType rft = of.createResponseFormType();
		
		ResponseDocumentType rdt = of.createResponseDocumentType();
		
		if(pdt.getProcessOutputs()!=null){
			
			List<OutputDescriptionType> odts = pdt.getProcessOutputs().getOutput();
			
			List<DocumentOutputDefinitionType> dodts = new ArrayList();
			
			//generate process outputs
			
			for(int i=0; i<odts.size(); i++){
				
				OutputDescriptionType odt = odts.get(i);
				
				DocumentOutputDefinitionType dodt = of.createDocumentOutputDefinitionType();
				
				dodt.setAbstract(odt.getAbstract());
				
				dodt.setIdentifier(odt.getIdentifier());
				
				dodt.setTitle(odt.getTitle());
				
				dodt.setAsReference(true); //default is reference
				
				if(odt.getComplexOutput()!=null){
					
					ComplexDataCombinationsType cdct = odt.getComplexOutput().getSupported();
					
					ComplexDataDescriptionType cddt = cdct.getFormat().get(0);
					
					dodt.setMimeType(cddt.getMimeType());
					
				}
				
				dodts.add(dodt);
			}
			
			rdt.setOutput(dodts);
			
		}
		
		//add input parameters
		
		DataInputsType di = of.createDataInputsType();
		
		List<InputType> inputs = new ArrayList();
		
		if(pdt.getDataInputs()!=null){
			
			List<InputDescriptionType> idtlist = pdt.getDataInputs().getInput();
			
			
			for(int i=0; i<idtlist.size();i++){
				
				InputDescriptionType idt = idtlist.get(i);
				
				InputType it = of.createInputType();
				
				it.setIdentifier(idt.getIdentifier());
				
				it.setTitle(idt.getTitle());
				
				DataType newdt = of.createDataType();
				
				if(idt.getLiteralData() != null){
					
					LiteralDataType  ldt = of.createLiteralDataType();
					
					ldt.setValue((String)kvp.get(idt.getIdentifier().getValue()));
					
					newdt.setLiteralData(ldt);
					
					it.setData(newdt);
					
				}else if(idt.getComplexData()!=null){
					
					String datavalue = (String)kvp.get(idt.getIdentifier().getValue());
					
					if(datavalue.toLowerCase().startsWith("http")){
						
						InputReferenceType irt = of.createInputReferenceType();
						
						irt.setHref((String)kvp.get(idt.getIdentifier().getValue()));
						
						irt.setMethod("GET");
						
						irt.setSchema("http://schemas.opengis.net/gml/3.1.1/base/feature.xsd"); //default as GMLv3
						
						it.setReference(irt);
						
					}else{
						
						ComplexDataType cdt = of.createComplexDataType();
						
						ComplexDataDescriptionType cddt = idt.getComplexData().getSupported().getFormat().get(0);
						
						cdt.setEncoding(cddt.getEncoding());
						
						cdt.setMimeType(cddt.getMimeType());
						
						List<Serializable> conts = new ArrayList();
						
						conts.add(datavalue);
						
						cdt.setContent((List<Serializable>)conts);
						
						newdt.setComplexData(cdt);
						
						it.setData(newdt);
						
					}
					
				}else {
					
					throw new RuntimeException("Unable to recognize the input type.");
					
				}
				
				inputs.add(it);
				
			}
			
		}
		
		
		di.setInput(inputs);
		
		exe.setDataInputs(di);
		
//		rdt.setOutput(arg0);
		
		rft.setResponseDocument(rdt);
		
		exe.setResponseForm(rft);
		
		
		return exe;
		
	}
	/**
	 * For 1.0.0
	 * @param ca
	 * @param dt
	 * @param kvp
	 * @return
	 */
	public static Execute getExecuteRequest(WPSCapabilitiesType ca, ProcessDescriptions dt, Map kvp) {
		
		return getExecuteRequest(ca, dt.getProcessDescription().get(0), kvp);
		
	}
	
	/**
	 * 
	 * @param resp
	 * @return
	 */
	public static ExecuteResponse parseExecuteResp(String resp){
		
		ExecuteResponse respobj = JAXB.unmarshal(new StringReader(resp), ExecuteResponse.class);
		
		return respobj;
	}
	/**
	 * Parse the execute response
	 * @param resp
	 * @return
	 */
	public static net.opengis.wps.v_2_0.Result parseExecuteResp20(String resp){
		
		net.opengis.wps.v_2_0.Result respobj = JAXB.unmarshal(new StringReader(resp), net.opengis.wps.v_2_0.Result.class);
		
		return respobj;
	}
	/**
	 * Parse the status information
	 * @param resp
	 * @return
	 */
	public static net.opengis.wps.v_2_0.StatusInfo parseExecuteResp20_Status(String resp){
		
		net.opengis.wps.v_2_0.StatusInfo respobj = JAXB.unmarshal(new StringReader(resp), net.opengis.wps.v_2_0.StatusInfo.class);
		
		return respobj;
	}
	
	/**
	 * 
	 * @param jobid
	 * @return
	 */
	public static net.opengis.wps.v_2_0.GetStatus createGetStatusRequest(String jobid){
		
		net.opengis.wps.v_2_0.ObjectFactory of = new net.opengis.wps.v_2_0.ObjectFactory();
		
		net.opengis.wps.v_2_0.GetStatus statusreq = of.createGetStatus();
		
		statusreq.setJobID(jobid);
		
		return statusreq;
		
	}
	
	/**
	 * Turn GetStatus to XML
	 * @param getstatus
	 * @return
	 */
	public static String turnGetStatus2XML(net.opengis.wps.v_2_0.GetStatus getstatus){
		String theXML = null;
		try{
			// serialize to xml
			StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(net.opengis.wps.v_2_0.GetStatus.class);            
			Marshaller m = context.createMarshaller();
			m.marshal(getstatus, writer);
			// output string to console
			 theXML = writer.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return theXML;
	}
	
	/**
	 * For 1.0.0
	 * @param exe
	 * @return
	 */
	public static String turnExecuteReqToXML(Execute exe){
		String theXML = null;
		try{
			// serialise to xml
			StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(Execute.class);            
			Marshaller m = context.createMarshaller();
			m.marshal(exe, writer);
	
			// output string to console
			 theXML = writer.toString();
//			System.out.println(theXML);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return theXML;
		
	}
	/**
	 * For 2.0.0
	 * @param exe
	 * @return
	 */
	public static String turnExecuteReqToXML20(ExecuteRequestType exe){
		String theXML = null;
		try{
			// serialise to xml
			StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(ExecuteRequestType.class);            
			Marshaller m = context.createMarshaller();
			net.opengis.wps.v_2_0.ObjectFactory of = new net.opengis.wps.v_2_0.ObjectFactory();
			
			m.marshal(of.createExecute(exe), writer);
	
			// output string to console
			 theXML = writer.toString();
//			System.out.println(theXML);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return theXML;
		
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		/**
		 * 
		 * For WPS 1.0.0
		 * 
		 */
		
//		try {
//			
//			//test GetCapabilities
//			
//			WPSCapabilitiesType ca = WPSUtils.parseCapabilities("http://ows.dev.52north.org:8080/wps/WebProcessingService?service=WPS&version=1.0.0&request=GetCapabilities");
//			
//			System.out.println(ca.getVersion());
//			
//			//test DescribeProcess
//			
//			ProcessDescriptions dt = WPSUtils.getProcessDescription("testbed12.fo.MergeFeaturesByAttribute", "http://ows.dev.52north.org:8080/wps/WebProcessingService?");
//			
//			System.out.println("DescriptionType output: " + dt.getProcessDescription().get(0).getIdentifier().getValue());
//			
//			//test ExecuteProcess
//			
//			Map kvp = new HashMap();
//			
//			kvp.put("classfield", "FTYPE");
//			
//			kvp.put("distance", "0.005");
//			
//			//kvp.put("data", "www3.csiss.gmu.edu/National_Hydrography_Dataset_(NHD)_Points_(High_Resolution).zip");
//			
//			kvp.put("data", BaseTool.encodeBase64("E:\\TESTDATA\\OGCTestbed12\\National_Hydrography_Dataset_(NHD)_Points_(High_Resolution).zip"));
//			
//			Execute exe = WPSUtils.getExecuteRequest(ca, dt, kvp);
//			
//			//turn the exe to XML
//			
//			String xml = turnExecuteReqToXML(exe);
//			
////			xml = BaseTool.formatXML(xml);
//			
//			BaseTool.writeString2File(xml, "E:\\TESTDATA\\OGCTestbed12\\wpsreq.xml");
//			
//			
////			String exeurl = WPSUtils.getExecuteEndpoint(ca);
//			
//			String exeurl = "http://ows.dev.52north.org:8080/wps/WebProcessingService";
//			
//			String resp = MyHttpUtils.doPost2(exeurl, xml);
//			
//			ExecuteResponse er = WPSUtils.parseExecuteResp(resp);
//			
//			System.out.println(er.getStatus().getProcessSucceeded());
//			
//			String outbase64 = (String)er.getProcessOutputs().getOutput().get(0).getData().getComplexData().getContent().get(0);
//						
//			BaseTool.decode(outbase64, "E:\\TESTDATA\\OGCTestbed12\\testoutput.zip");
//			
//			System.out.println("WPS output has been written into a Shapefile zip.");
//			
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//			
//		}
		
		/**
		 * 
		 * For WPS 2.0.0
		 * 
		 */
//		try {
//			
//			//test GetCapabilities
//			
//			net.opengis.wps.v_2_0.WPSCapabilitiesType ca = WPSUtils.parseCapabilities20("http://ows.dev.52north.org:8080/wps/WebProcessingService?service=WPS&version=2.0.0&request=GetCapabilities");
//			
//			System.out.println(ca.getVersion());
//			
//			//test DescribeProcess
//			
//			net.opengis.wps.v_2_0.ProcessOfferings dt = WPSUtils.getProcessDescription20("testbed12.fo.MergeFeaturesByAttribute", "http://ows.dev.52north.org:8080/wps/WebProcessingService?");
//			
//			System.out.println("DescriptionType output: " + dt.getProcessOffering().get(0).getProcess().getIdentifier().getValue());
//			
//			//test ExecuteProcess
//			
//			Map kvp = new HashMap();
//			
//			kvp.put("classfield", "FTYPE");
//			
//			kvp.put("distance", "0.005");
//			
//			kvp.put("data", "http://www3.csiss.gmu.edu/National_Hydrography_Dataset_(NHD)_Points_(High_Resolution).zip");
//			
////			kvp.put("data", BaseTool.encodeBase64("E:\\TESTDATA\\OGCTestbed12\\National_Hydrography_Dataset_(NHD)_Points_(High_Resolution).zip"));
//			
//			ExecuteRequestType exe = WPSUtils.getExecuteRequest20(ca, dt, kvp, true);
//			
//			//turn the exe to XML
//			
//			String xml = turnExecuteReqToXML20(exe);
//			
//			xml = xml.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
//			
////			xml = BaseTool.formatXML(xml);
//			
////			BaseTool.writeString2File(xml, "E:\\TESTDATA\\OGCTestbed12\\wpsreq3.xml");
//			
//			
//			
////			String exeurl = WPSUtils.getExecuteEndpoint(ca);
//			
//			String exeurl = "http://ows.dev.52north.org:8080/wps/WebProcessingService";
//			
//			String resp = HttpUtils.doPost(exeurl, xml);
//			
//			net.opengis.wps.v_2_0.Result er = WPSUtils.parseExecuteResp20(resp);
//			
//			System.out.println(er.getJobID());
//			
//			String outputurl = er.getOutput().get(0).getReference().getHref();
//			
////			String outbase64 = (String)er.getOutput().get(0).getData().getContent().get(0);
////						
////			BaseTool.decode(outbase64, "E:\\TESTDATA\\OGCTestbed12\\testoutput.zip");
//			
//			System.out.println("WPS output has been written into a Shapefile zip." + outputurl);
//			
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//			
//		}
		
		// for status check
		
//		net.opengis.wps.v_2_0.GetStatus getstatusreq = WPSUtils.createGetStatusRequest("7bed8502-bb8b-4910-8016-f912492d515c");
//		
//		String getstatusxml = WPSUtils.turnGetStatus2XML(getstatusreq);
//		
//		System.out.println(getstatusxml);
		
		//for getresult
		
//		net.opengis.wps.v_2_0.GetResult getresult = WPSUtils.createGetResultReq("7bed8502-bb8b-4910-8016-f912492d515c");
//		
//		String getresultxml = WPSUtils.turnGetResult2XML(getresult);
//		
//		System.out.println(getresultxml);
		
		/**
		 * 
		 * For WPS 2.0.0-REST
		 * 
		 */
		
		
		/**
		 * 
		 * For WPS 2.0.0-SOAP
		 * 
		 */
		
		
		
	}

}
