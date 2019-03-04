package suis4j.driver;

import java.io.File;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import net.java.dev.wadl._2009._02.Application;
import net.java.dev.wadl._2009._02.Method;
import net.java.dev.wadl._2009._02.Param;
import net.java.dev.wadl._2009._02.Representation;
import net.java.dev.wadl._2009._02.Resource;
import net.java.dev.wadl._2009._02.Resources;
import net.java.dev.wadl._2009._02.Response;
import suis4j.profile.DataType;
import suis4j.profile.Message;
import suis4j.profile.Operation;
import suis4j.profile.Parameter;

/**
*Class REST2SUNIS.java
*@author Ziheng Sun
*@time Dec 8, 2017 10:23:31 AM
*/
public class RESTDriver extends AbstractDriver {
	
	Application application = null;
	
	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	@Override
	public Message decodeSUIS(Object rawmsg) {
		
		return null;
	}

	@Override
	public PayLoad encodeReq(Message msg) {
		
		//the payload of REST service is the KVP
		
		StringBuffer kvps = new StringBuffer();
		
		int num = 0;
		
		for(int i=0; i<msg.getParameter_list().size(); i++){
			
			if(msg.getParameter_list().get(i).getValue()!=null){
				
				if(num!=0)kvps.append("&");
				
				num++;
				
				kvps.append(msg.getParameter_list().get(i).getName())
					.append("=")
					.append(msg.getParameter_list().get(i).getValue());
				
			}
			
		}
		
		PayLoad p = new PayLoad.Builder()
				.content(kvps.toString())
				.build();
		
		return p;
		
	}

	@Override
	public void send(PayLoad req) {
		
		Operation o = this.getOperation(this.getCurrent_operation());
		
		try {
			
			String targeturl = o.getName()+"?" + String.valueOf(req.getContent());
			
			System.out.println("target url: " + targeturl);
			
			String resp = HttpUtils.doGet( targeturl );
			
			response = new PayLoad.Builder()
					.content(resp)
					.build();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	@Override
	public PayLoad receive() {
		
		return response;
		
	}

	@Override
	public Message decodeResp(PayLoad resp) {
		
		//default return is application/xml. other types of returns are not guaranteed. 
		
		Operation o = this.getOperation(this.getCurrent_operation());
		
		o.getOutput().get("return").setValue(resp.getContent()); //REST service has only one output in SUIS profile
		
		return o.getOutput();
		
	}
	
	@Override
	public Object encodeSUIS(Message msg) {
		
		
		
		return null;
		
	}
	
	/**
	 * Find method by name
	 * @param name
	 * @return
	 */
	public Method findmethod(String name){
		
		List types = application.getResourceTypeOrMethodOrRepresentation();
		
		Method m = null;
		
		if(name.startsWith("#")){
			
			name = name.substring(1);
			
		}
		
		for(Object type: types){
			
			if(type instanceof Method){
				
				if(((Method)type).getId().equals(name)){
					
					m = (Method) type;
					
					break;
					
				}
				
			}
			
		}
		
		return m;
		
	}
	
	/**
	 * Find representation by name
	 * @param name
	 * @return
	 */
	public Representation findrepresentation(String name){
		
		List types = application.getResourceTypeOrMethodOrRepresentation();
		
		Representation r = null;
		
		for(Object type: types){
			
			if(type instanceof Method){
				
				if(((Representation)type).getId().equals(name)){
					
					r = (Representation) type;
					
					break;
					
				}
				
			}
			
		}
		
		return r;
		
	}

	@Override
	public List<Operation> digest() {
		
		List<Resources> resources =  application.getResources();
		
		operlist = new ArrayList();
		
		//each resource corresponds to an operation
		
		for(Resources rs : resources){
			
			for(Resource r : rs.getResource()){
				
				//create input message - Right now, only for query parameters. Path parameters are not supported.
				
				List<Parameter> inparams = new ArrayList();
				
				Method them = (Method)r.getMethodOrResource().get(0); //get the method name
				
				Method m = null;
				
				if(them.getHref()!=null){
					
					m = findmethod(them.getHref()); //find the method type by name
					
				}else{
					
					m = them;
					
				}
				
				if(m.getRequest()!=null){
					
					for(int i=0; i<m.getRequest().getParam().size(); i++){
						
						Param p = m.getRequest().getParam().get(i);
						
						p.getStyle(); //query or path
						
						p.getOption(); //the option values of the parameter
						
						int minoccurs = 0;
						
						if(p.isRequired()){
							
							minoccurs = 1;
							
						}else{
							
							minoccurs = 0;
							
						}
						
						DataType dt = DataType.STRING;
						
						if(p.getType().getLocalPart().equals("string")){
							
							dt = DataType.STRING;
							
						}else if(p.getType().getLocalPart().equals("boolean")){
							
							dt = DataType.BOOL;
							
						}else if(p.getType().getLocalPart().equals("float")
								||p.getType().getLocalPart().equals("int")
								||p.getType().getLocalPart().equals("double")){
							
							dt = DataType.NUMBER;
							
						}
						
						Parameter newp = new Parameter.Builder()
								.name(p.getName())
								.type(dt)
								.minoccurs(minoccurs)
								.build();
						
						inparams.add(newp);
						
					}
					
				}
				
				Message inm = new Message.Builder().params(inparams).build();
				
				//create output message
				
				List<Parameter> outparams = new ArrayList();
				
				if(m.getResponse()!=null && m.getResponse().size()!=0){
					
					Response resp = m.getResponse().get(0);
					
//					List<Representation> repres = resp.getRepresentation(); //only one output with many possibilities in the result format
					
					Parameter outp = new Parameter.Builder()
							.name("return")
							.minoccurs(1)
							.maxoccurs(1) //only one output is expected
//							.type(t) //uncertain types of data may be returned. a feature of REST
							.build();
					
					outparams.add(outp);
					
				}
				
				Message outm = new Message.Builder().params(outparams).build();
				
				String baseurl = rs.getBase().replace("service.iris.edu",
						"service.iris.edu/irisws");
				
				Operation o = new Operation.Builder()
						.name(baseurl+"/"+r.getPath()) //the entire name should be the complete URI address of the resource
						.input(inm)
						.output(outm)
//						.description(r.getDoc().get(0).getTitle())
						.build();
				
				operlist.add(o);
				
			}
			
		}
		
		connect();
		
		return operlist;
	}

	@Override
	public void initParams(Operation o) {
		//nothing
		
	}
	
	public static class Builder extends AbstractDriver.Builder {
		
		RESTDriver driver = new RESTDriver();
		
		@Override
		public Builder parse(String descfile) {
			
			//parse the WADL
			
			try{
				
				String xml = HttpUtils.parseURL(descfile).asXML();
				
				JAXBContext jaxbContext = JAXBContext.newInstance(Application.class);
				
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

				StringReader reader = new StringReader(xml);
				
				Application application = (Application) unmarshaller.unmarshal(reader);
				
				driver.setApplication(application);
				
				driver.setDesc_endpoint(new URL(descfile));
				
			}catch(Exception e){
				
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
