package suis4j.driver;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import net.opengis.csw.v_2_0_2.CapabilitiesType;
import net.opengis.csw.v_2_0_2.ElementSetNameType;
import net.opengis.csw.v_2_0_2.GetRecordsResponseType;
import net.opengis.csw.v_2_0_2.GetRecordsType;
import net.opengis.csw.v_2_0_2.QueryConstraintType;
import net.opengis.csw.v_2_0_2.QueryType;
import net.opengis.csw.v_2_0_2.ResultType;
import net.opengis.filter.v_1_1_0.BinaryComparisonOpType;
import net.opengis.filter.v_1_1_0.BinaryLogicOpType;
import net.opengis.filter.v_1_1_0.FilterType;
import net.opengis.filter.v_1_1_0.LiteralType;
import net.opengis.filter.v_1_1_0.PropertyIsLikeType;
import net.opengis.filter.v_1_1_0.PropertyNameType;
import net.opengis.ows.v_1_0_0.Operation;
import net.opengis.ows.v_1_0_0.RequestMethodType;
import net.opengis.wms.v_1_3_0.HTTP;

/**
*Class CSWUtils.java
*@author Ziheng Sun
*@time Jun 16, 2016 5:19:25 PM
*Original aim is to support GeoBrain CSW and GeOnAS.
*/
public class CSWUtils {
	
	static Logger logger = Logger.getLogger(CSWUtils.class);
	
	static net.opengis.csw.v_2_0_2.ObjectFactory cswof = new net.opengis.csw.v_2_0_2.ObjectFactory();
	
	static net.opengis.filter.v_1_1_0.ObjectFactory of = new net.opengis.filter.v_1_1_0.ObjectFactory();
	
	static net.opengis.gml.v_3_1_1.ObjectFactory gmlof = new net.opengis.gml.v_3_1_1.ObjectFactory();
	
	static Marshaller csw_jaxbmarshaller;
	
	static Unmarshaller csw_jaxbUnmarshaller;
	
	static{
		
		JAXBContext csw_jaxbContext = null;
		
		try {
			
			csw_jaxbContext = JAXBContext.newInstance(net.opengis.csw.v_2_0_2.ObjectFactory.class, net.opengis.filter.v_1_1_0.ObjectFactory.class, net.opengis.gml.v_3_1_1.ObjectFactory.class);
			
			csw_jaxbUnmarshaller = csw_jaxbContext.createUnmarshaller();
			
			csw_jaxbmarshaller = csw_jaxbContext.createMarshaller();
			
		} catch (JAXBException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	public static URL getEndpoint(CapabilitiesType ct) throws MalformedURLException{

		List<Operation> opers = ct.getOperationsMetadata().getOperation();
		
		String exeurl = null;
		
		for(int i=0; i<opers.size(); i++){
			
			Operation oper = opers.get(i);
			
			if("GetRecords".equals(oper.getName())){
				
				exeurl = oper.getDCP().get(0).getHTTP().getGetOrPost().get(0).getValue().getHref();
				
			}
			
		}
		
		return new URL(exeurl);
		
	}
	
	public static CapabilitiesType parseCapabilityStr(String content){
		
		CapabilitiesType ca = null;
		
		try {
			
			ca = JAXB.unmarshal(new StringReader(content), CapabilitiesType.class);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			throw new RuntimeException(e.getLocalizedMessage());
			
		}
		
		return ca;
		
	}
	
	public static CapabilitiesType parseCapability(String url) throws Exception{
		
		String ca = HttpUtils.doGet(url);
		
		return parseCapabilityStr(ca);
		
	}
	
	
	public static void testLAITSCSW2(){
		
		String req = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><csw:GetRecords xmlns=\"http://www.opengis.net/cat/csw\" xmlns:csw=\"http://www.opengis.net/cat/csw\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:gml=\"http://www.opengis.net/gml\" version=\"2.0\" outputFormat=\"text/xml; charset=UTF-8\" outputSchema=\"http://www.opengis.net/cat/csw\" startPosition=\"1\" maxRecords=\"5\" requestId=\"GSTSCSF_ReqID_201661611928674_4\"><csw:FederationSearch CatalogServiceIDs=\"GMU_LAITS,NASA_ECHO\"/><csw:Query typeNames=\"DataGranule\">    <csw:ElementSetName>full</csw:ElementSetName><csw:ElementName>/DataGranule/</csw:ElementName>        <csw:Constraint version=\"1.0.0\">                <ogc:Filter>                    <ogc:And>           <ogc:BBOX>                                       <ogc:PropertyName>/DataGranule/BBOX</ogc:PropertyName>                                  <gml:Box srsName=\"EPSG:4326\">                                                <gml:coordinates>-122.335674957591,37.454539 -121.469275,37.904725</gml:coordinates>                </gml:Box>                               </ogc:BBOX>                             <ogc:PropertyIsGreaterThanOrEqualTo>                                    <ogc:PropertyName>/DataGranule/beginDateTime</ogc:PropertyName>                                      <ogc:Literal>2016-06-01 11:08:41</ogc:Literal>              </ogc:PropertyIsGreaterThanOrEqualTo>                            <ogc:PropertyIsLessThanOrEqualTo>                                       <ogc:PropertyName>/DataGranule/endDateTime</ogc:PropertyName>                                        <ogc:Literal>2016-06-08 11:08:41</ogc:Literal>                          </ogc:PropertyIsLessThanOrEqualTo>                           <ogc:PropertyIsEqualTo>                                 <ogc:PropertyName>/DataGranule/platformShortName</ogc:PropertyName>                                  <ogc:Literal>Aqua</ogc:Literal>                         </ogc:PropertyIsEqualTo>                    <ogc:PropertyIsEqualTo>                                  <ogc:PropertyName>/DataGranule/sensorShortName</ogc:PropertyName>                                   <ogc:Literal>MODIS</ogc:Literal>                         </ogc:PropertyIsEqualTo>                                <ogc:PropertyIsEqualTo>                     <ogc:PropertyName>/DataGranule/dataSetId</ogc:PropertyName>                                      <ogc:Literal>MODIS/Aqua Calibrated Radiances 5-Min L1B Swath 1km V005</ogc:Literal>                          </ogc:PropertyIsEqualTo>                        </ogc:And>              </ogc:Filter>   </csw:Constraint></csw:Query></csw:GetRecords>";
		
		try {
			
//			System.out.print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\nRequest is: " + t.formatXML(req));
			
			String resp = HttpUtils.doPost("http://cube.csiss.gmu.edu/LAITSCSW2/discovery", req);
			
//			System.out.print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\nResponse is: " + t.formatXML(resp));
		
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
	}
	/**
	 * 
	 * @param keywords
	 * @param startposition
	 * @param maxrecords
	 * @return
	 */
	public static String getRequestStaticXML(String keywords, int startposition, int maxrecords){
		
		StringBuffer req = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		req.append("<ns4:GetRecords xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:ns4=\"http://www.opengis.net/cat/csw/2.0.2\" xmlns=\"http://www.opengis.net/ogc\" xmlns:ns2=\"http://www.opengis.net/gml\" xmlns:ns3=\"http://www.w3.org/1999/xlink\" xmlns:ns5=\"http://www.opengis.net/ows\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ns6=\"http://purl.org/dc/elements/1.1/\" xmlns:ns7=\"http://purl.org/dc/terms/\" xmlns:ns8=\"http://www.w3.org/2001/SMIL20/\" xmlns:ns9=\"http://www.w3.org/2001/SMIL20/Language\" maxRecords=\"").append(maxrecords).append("\" outputSchema=\"http://www.isotc211.org/2005/gmd\" resultType=\"results\" service=\"CSW\" startPosition=\"").append(startposition).append("\" version=\"2.0.2\">");
		req.append("		<ns4:ElementSetName>full</ns4:ElementSetName>");
		req.append("		<ns4:Query xmlns:ns10=\"http://www.isotc211.org/2005/gmd\" typeNames=\"ns10:MD_Metadata\">");
		req.append("		<ns4:Constraint version=\"1.1.0\">");
		req.append("			<Filter>");
		
		/***************************************************/
		
		/**Boudning Box**/
		
		req.append("			<BBOX>");
		req.append("			    <PropertyName>ows:BoundingBox</PropertyName>");
		req.append("			    <gml:Envelope>");
		req.append("			      <gml:lowerCorner>47 -5</gml:lowerCorner>");
		req.append("			      <gml:upperCorner>55 20</gml:upperCorner>");
		req.append("			    </gml:Envelope>");
		req.append("			</BBOX>");
		
		/***************************************************/
		
		/**Keywords Filter**/
		
		/**Example 1**/
		
		req.append("			<PropertyIsLike matchCase=\"false\" wildCard=\"%\" singleChar=\"_\" escapeChar=\"\\\">");
		req.append("			         <PropertyName>").append("csw:AnyText").append("</PropertyName>");
		req.append("			         <Literal>%").append("").append("%</Literal>");
		req.append("			</PropertyIsLike>");
		
		/**Example 2**/
		
		req.append("				<And>");
		req.append("					<PropertyIsEqualTo>");
		req.append("						<PropertyName>").append("keyword").append("</PropertyName>");
		req.append("						<Literal>").append(keywords).append("</Literal>");
		req.append("					</PropertyIsEqualTo>");
		req.append("				</And>");
		
		/***************************************************/
		
		req.append("			 </Filter>");
		req.append("		</ns4:Constraint>");
		req.append("		</ns4:Query>");
		req.append("</ns4:GetRecords>");
		return req.toString();
		
	}
	
	/**
	 * Get request getrecordstype object
	 * @param keywords
	 * @param category_mode
	 * @param categories
	 * @param social_mode
	 * @param societal_benefit_areas
	 * @param geoss_core_mode
	 * @param geoss_data_core_list
	 * @return
	 */
	public static JAXBElement<GetRecordsType> getRequestObject(String keywords, int startposition, int maxrecords){
		
		GetRecordsType getrecordsreq = cswof.createGetRecordsType();
		
		QueryType query = cswof.createQueryType();
		
		QueryConstraintType constraint = cswof.createQueryConstraintType();
		
		constraint.setVersion("1.1.0");
		
//		FilterType filter = of.createFilterType();
//		
//		//create a And component to list all the constraints
//		BinaryLogicOpType and = of.createBinaryLogicOpType();
//		
//		//text like filter
//		if(!BaseTool.isNull(keywords)&&!BaseTool.isNull(keywords.trim())){
//			PropertyIsLikeType islike = of.createPropertyIsLikeType();
//			islike.setWildCard("%");
//			islike.setSingleChar("_");
//			islike.setEscapeChar("\\");
//			PropertyNameType pname = of.createPropertyNameType();
//			pname.getContent().add("AnyText");
//			LiteralType ltype = of.createLiteralType();
//			ltype.getContent().add("%"+keywords+"%");
//			islike.setPropertyName(pname);
//			islike.setLiteral(ltype);
//			JAXBElement<PropertyIsLikeType> value = of.createPropertyIsLike(islike);
//			and.getOps().add(value);
//		}
//		
//		//compose the request object
//		JAXBElement<BinaryLogicOpType> andfilter = of.createAnd(and);
//		
//		filter.setLogicOps(andfilter);
//		
//		constraint.setFilter(filter);
//		
		query.setConstraint(constraint);
		
//		ElementSetNameType est = cswof.createElementSetNameType();
//		
//		est.setValue("");
//		
//		query.setElementSetName(value);
		
		query.getTypeNames().add(new QName("http://www.isotc211.org/2005/gmd","MD_Metadata"));
		
		JAXBElement<QueryType> queryele = cswof.createQuery(query);
		
		getrecordsreq.setAbstractQuery(queryele);
		
		getrecordsreq.setOutputSchema("http://www.isotc211.org/2005/gmd");
		
		getrecordsreq.setResultType(ResultType.RESULTS);
		
		getrecordsreq.setStartPosition(BigInteger.valueOf(startposition));
		
		getrecordsreq.setMaxRecords(BigInteger.valueOf(maxrecords));
		//wrap the object into a JAXBElement
		
		JAXBElement<GetRecordsType> wrappedobj = cswof.createGetRecords(getrecordsreq);
		
		return wrappedobj;
	}
	
	/**
	 * 
	 * @param ca
	 * @return
	 */
	public static GetRecordsResponseType getRecords(CapabilitiesType ca){
		
		String grtreq = getRequestStaticXML("ground water", 1, 50);
		
//		logger.info(grtreq.getName().getLocalPart());
//		
//		String reqxml = grtreq.getValue().toString();
		
		logger.info(grtreq);
		
		//get getrecords url
		
		List<JAXBElement<RequestMethodType>> rmts = ca.getOperationsMetadata().getOperation().get(0).getDCP().get(0).getHTTP().getGetOrPost();
		
		//do post
		
		//Get the endpoint URL
		
		String posturl = null;
		
		for(int i=0; i<rmts.size();i++){
			
			JAXBElement<RequestMethodType> jele = rmts.get(i);
			
			RequestMethodType rmt = jele.getValue();
			
			logger.info(jele.getScope().getName());
			
			logger.info(rmt.getClass().getName());
			
			if(rmt.getConstraint().size()>0){
				
				logger.info(rmt.getConstraint().get(0).getName());
				
				logger.info(rmt.getConstraint().get(0).getValue().get(0));
				
				if(rmt.getConstraint().get(0).getValue().get(0).equals("XML")){
					
					posturl = rmt.getHref();
					
				}
				
			}
			
		}
		
		if(posturl ==null){
			
			throw new RuntimeException("Fail to get an endpoint URL to send the GetRecords request to");
			
		}
		
		//Construct a GetRecords request

		
		GetRecordsResponseType grrt = null;
		
		try {
			
//			String req = toXML(grtreq);
			
			logger.info(grtreq);
			
			String resp = HttpUtils.doPost(posturl, grtreq);
			
			JAXBElement<GetRecordsResponseType> grrte = parseCSWSearchResults(resp);
			
			
			logger.info(toXML(grrte));
			

			grrt = grrte.getValue();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		
		
		
		return grrt;
		
	}
	
	/**
	 * Parse search results
	 * @param respxml
	 * @return
	 */
	public static JAXBElement<GetRecordsResponseType> parseCSWSearchResults(String respxml){
		
		JAXBElement<GetRecordsResponseType> resp =null;
		
		try {
			
			logger.info(respxml);
			
			StringReader reader = new StringReader(respxml.trim());
			
			JAXBElement<GetRecordsResponseType> results = (JAXBElement<GetRecordsResponseType>) csw_jaxbUnmarshaller.unmarshal(reader);
			
			resp = results;
			//			logger.info(resp.getRequestId()); //null
			
			logger.info("GetRecords response parsing is completed.");
			
		} catch (JAXBException e) {
			
			  e.printStackTrace();
			  
		}
		
		return resp;
	}
	
	/**
	 * 
	 * @param reqobj
	 * @return
	 */
	
	public static String toXML(JAXBElement<?> reqobj){
		
		StringWriter sw = new StringWriter();
		
		try {
			
			csw_jaxbmarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			csw_jaxbmarshaller.marshal(reqobj, sw);
		
		} catch (JAXBException e) {
			
			e.printStackTrace();
			
		}
		
//		String finalreqxml = BaseTool.formatXML(sw.toString()).trim();
		
		return sw.toString().trim();
		
	}
	
	/**
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception{
		
//		testLAITSCSW2();
		
		CapabilitiesType ca = CSWUtils.parseCapability("http://tb12.essi-lab.eu/pubsub-csw/services/cswiso?service=CSW&version=2.0.2&request=GetCapabilities");
		
//		CapabilitiesType ca = CSWUtils.parseCapability("http://www3.csiss.gmu.edu/cnrcsw.xml");
		
		GetRecordsResponseType resp = getRecords(ca);
		
		logger.info("Result Number: " + resp.getSearchResults().getNumberOfRecordsMatched());
		
	}
	
}
