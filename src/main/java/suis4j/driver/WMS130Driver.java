package suis4j.driver;

import java.util.ArrayList;
import java.util.List;

import net.opengis.wms.v_1_3_0.WMSCapabilities;
import suis4j.profile.DataType;
import suis4j.profile.Message;
import suis4j.profile.Operation;
import suis4j.profile.Parameter;

/**
*Class WMS130Driver.java
*@author Ziheng Sun
*@time Mar 30, 2018 12:19:45 PM
*/
public class WMS130Driver extends OGCDriver{

	@Override
	public PayLoad encodeReq(Message msg) {

		Object content = null;
		
		if("GetCapabilities".equals(this.getCurrent_operation())){
			
			//do nothing
			
		}else if("GetMap".equals(this.getCurrent_operation())){
			
			content = "layers=" + msg.getValueAsString("layers") + "&bbox=" + msg.getValueAsString("bbox")
				+ "&width=" + msg.getValueAsString("width") + "&height=" + msg.getValueAsString("height")
				+ "&crs=" + msg.getValueAsString("crs") + "&format=" + msg.getValueAsString("format");
			
		}else{
			
			throw new RuntimeException("No such operation.");
			
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
				
			}else if("GetMap".equals(oper.getName())){
				
				oper.getOutput().get("map").setValue((String)resp.getContent());
				
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
			
			outparams.add(new Parameter.Builder().name("dataurl")
					.description("wms getmap url")
					.minoccurs(0).maxoccurs(1).build());
			
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
			
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
		
		return super.digest();
		
	}

	
	
}
