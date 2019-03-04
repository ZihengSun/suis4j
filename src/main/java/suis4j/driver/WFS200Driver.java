package suis4j.driver;

import java.util.ArrayList;
import java.util.List;

import net.opengis.wfs.v_2_0.WFSCapabilitiesType;
import suis4j.profile.DataType;
import suis4j.profile.Message;
import suis4j.profile.Operation;
import suis4j.profile.Parameter;

/**
*Class WFS200Driver.java
*@author Ziheng Sun
*@time Mar 30, 2018 12:14:15 PM
*/
public class WFS200Driver extends OGCDriver{

	@Override
	public PayLoad encodeReq(Message msg) {
		
		Object content = null;
		
		if("GetFeature".equals(this.getCurrent_operation())){
			
			content = msg.getValueAsString("query");
			
		}else if("DescribeFeatureType".equals(this.getCurrent_operation())){
			
//			DescribeFeatureTypeType dftt = WFSUtils.createADescribeFeatureTypeRequest(msg.getValueAsString("typeName"));
			
//			content = WFSUtils.turnDescribeFeatureTypeTypeToXML(dftt);
			
			content = "typeNames=" + msg.getValueAsString("typeName");
			
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
			
			if("GetCapabilities".equals(oper.getName())){
				
				//do nothing
				
			}else if("GetFeature".equals(oper.getName())){
				
				oper.getOutput().get("featureCollection").setValue((String)resp.getContent());
				
			}else if("DescribeFeatureType".equals(oper.getName())){
				
				oper.getOutput().get("xmlSchema").setValue((String)resp.getContent());
				
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
		
		try{

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
			
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
		
		return super.digest();

	}

	
	
}
