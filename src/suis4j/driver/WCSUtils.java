package suis4j.driver;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import net.opengis.gml.v_3_2_1.DomainSetType;
import net.opengis.gmlcov.v_1_0.Metadata;
import net.opengis.ows.v_2_0.Operation;
import net.opengis.swecommon.v_2_0.DataRecordPropertyType;
import net.opengis.wcs.v_2_0.CapabilitiesType;
import net.opengis.wcs.v_2_0.ContentsType;
import net.opengis.wcs.v_2_0.CoverageDescriptionsType;
import net.opengis.wcs.v_2_0.CoverageSummaryType;
import net.opengis.wcs.v_2_0.DescribeCoverageType;
import net.opengis.wcs.v_2_0.GetCapabilitiesType;
import net.opengis.wcs.v_2_0.GetCoverageType;
import net.opengis.wcs.v_2_0.ObjectFactory;
import net.opengis.wcs.v_2_0.ServiceParametersType;

/**
*Class WCSUtils.java
*WCS 2.0.0 client
*@author Ziheng Sun
*@time Apr 19, 2016 12:44:09 AM
*Original aim is to support GeOnAS.
*/
public class WCSUtils {
	
	private static Logger theLogger = Logger.getLogger(WCSUtils.class);
	
	/**
	 * Parse capabilities document
	 * @param url
	 * @return
	 * @throws Exception 
	 */
	public static CapabilitiesType parseCapabilities(String url) throws Exception{
		
		String capabilitydoc = HttpUtils.doGet(url);
		
		return parseCapabilitiesStr(capabilitydoc);
		
	}
	
	public static CapabilitiesType parseCapabilitiesStr(String content){
		
		CapabilitiesType ca =  null;
		try {
			
			JAXBContext jaxbContext = null;
			
			ca = JAXB.unmarshal(new StringReader(content), CapabilitiesType.class);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			throw new RuntimeException("Fail to parse the capabilities document." + e.getLocalizedMessage());
			
		}
		
		return ca;
		
	}
	/**
	 * Turn GetCoverage request to XML
	 * @param cdt
	 * GetCoverage request object
	 * @return
	 * GetCoverage XML request
	 */
	public static String turnGetCoverageTypeToXML(GetCoverageType cdt){
		String theXML = null;
		try{
			// serialise to xml
			StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(GetCoverageType.class);
			Marshaller m = context.createMarshaller();
			QName qName = new QName("http://www.opengis.net/wcs/2.0", "GetCoverage");
		    JAXBElement<GetCoverageType> root = new JAXBElement<>(qName, GetCoverageType.class, cdt);
			m.marshal(root, writer);
			
			// output string to console
			theXML = writer.toString();
//			System.out.println(theXML);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return theXML;
		
	}
	/**
	 * Turn coverage description to XML
	 * @param cdt
	 * @return
	 */
	public static String turnDescribeCoverageTypeToXML(DescribeCoverageType cdt){
		String theXML = null;
		try{
			// serialise to xml
			StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(DescribeCoverageType.class);
			Marshaller m = context.createMarshaller();
			QName qName = new QName("http://www.opengis.net/wcs/2.0", "DescribeCoverage");
		    JAXBElement<DescribeCoverageType> root = new JAXBElement<>(qName, DescribeCoverageType.class, cdt);
			m.marshal(root, writer);
			
			// output string to console
			theXML = writer.toString();
//			System.out.println(theXML);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return theXML;
		
	}
	
	/**
	 * Parse Coverage Descriptions
	 * @param url
	 * @return
	 */
	public static CoverageDescriptionsType parseCoverageDescriptions(String content){
		
		CoverageDescriptionsType cdt = null;
		
		try{
			
			JAXBContext jaxbContext = null;
			
			cdt = JAXB.unmarshal(new StringReader(content), CoverageDescriptionsType.class);
			
		}catch(Exception e){
			
			e.printStackTrace();
			
			throw new RuntimeException("Fail to parse the coverage descriptions. " + e.getLocalizedMessage());
			
		}
		
		return cdt;
		
	}
	
	public static String turnServiceParametersToXML(ServiceParametersType spt){

		String theXML = null;
		
		try{
			
			StringWriter writer = new StringWriter();
			
			JAXBContext context = JAXBContext.newInstance(ServiceParametersType.class);
			
			Marshaller m = context.createMarshaller();
			
			QName qName = new QName("http://www.opengis.net/gmlcov/1.0", "rangeType");
		    
			JAXBElement<ServiceParametersType> root = new JAXBElement<>(qName, ServiceParametersType.class, spt);
			
		    m.marshal(root, writer);
			
			theXML = writer.toString();
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
		
		return theXML;
		
		
	}
	
	public static String turnDataRecordPropertyTypeToXML(DataRecordPropertyType drpt){
		
		String theXML = null;
		
		try{
			
			StringWriter writer = new StringWriter();
			
			JAXBContext context = JAXBContext.newInstance(DataRecordPropertyType.class);
			
			Marshaller m = context.createMarshaller();
			
			QName qName = new QName("http://www.opengis.net/gmlcov/1.0", "rangeType");
		    
			JAXBElement<DataRecordPropertyType> root = new JAXBElement<>(qName, DataRecordPropertyType.class, drpt);
			
		    m.marshal(root, writer);
			
			theXML = writer.toString();
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
		
		return theXML;
		
	}  
	
	public static String turnDomainSetTypeToXML(DomainSetType ds){
		
		String theXML = null;
		
		try{
			
			StringWriter writer = new StringWriter();
			
			JAXBContext context = JAXBContext.newInstance(DomainSetType.class);
			
			Marshaller m = context.createMarshaller();
			
			QName qName = new QName("http://www.opengis.net/gml/3.2", "domainSet");
		    
			JAXBElement<DomainSetType> root = new JAXBElement<>(qName, DomainSetType.class, ds);
			
		    m.marshal(root, writer);
			
			theXML = writer.toString();
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
		
		return theXML;
	}
	
	public static String turnMetadataListToXML(List<Metadata> mds){

		StringBuffer theXML = new StringBuffer();
		
		try{
			
			// serialise to xml
			
			for(int i=0;i<mds.size();i++){
				
				StringWriter writer = new StringWriter();
				
				JAXBContext context = JAXBContext.newInstance(Metadata.class);
				
				Marshaller m = context.createMarshaller();
				
				QName qName = new QName("http://www.opengis.net/gmlcov/1.0", "metadata");
			    
				JAXBElement<Metadata> root = new JAXBElement<>(qName, Metadata.class, mds.get(i));
				
			    m.marshal(root, writer);
				
				// output string to console
				theXML.append(writer.toString()).append("\n");
				
			}
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
		
		return theXML.toString();
		
	}
	
	
	public static String turnGetCapabilitiesTypeToXML(GetCapabilitiesType gct){
		
		String theXML = null;
		
		try{
			
			// serialise to xml
			
			StringWriter writer = new StringWriter();
			
			JAXBContext context = JAXBContext.newInstance(GetCapabilitiesType.class);
			
			Marshaller m = context.createMarshaller();
			
			QName qName = new QName("http://www.opengis.net/wcs/2.0", "GetCapabilities");
		    
			JAXBElement<GetCapabilitiesType> root = new JAXBElement<>(qName, GetCapabilitiesType.class, gct);
			
		    m.marshal(root, writer);
			
			// output string to console
			theXML = writer.toString();
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
		
		return theXML;
		
	}
	
	/**
	 * Create a getcapabilities type
	 * @return
	 */
	public static GetCapabilitiesType createAGetCapabilitiesRequest(){
		
		ObjectFactory of = new ObjectFactory();
		
		GetCapabilitiesType gct = of.createGetCapabilitiesType(); 
		
		return gct;
		
	}
	
	/**
	 * Create a getcoverage request
	 * @param coverageid
	 * @return
	 */
	public static GetCoverageType createAGetCoverageRequest(String coverageid, String version){
		ObjectFactory of = new ObjectFactory();
		GetCoverageType gct = of.createGetCoverageType();
		gct.setVersion(version);
		gct.setCoverageId(coverageid);
		return gct;
	}
	
	
	/**
	 * Create a describe coverage request
	 * @param coverageid
	 * @return
	 */
	public static DescribeCoverageType createADescribeCoverageRequest(String coverageid, String version){
		ObjectFactory of = new ObjectFactory();
		DescribeCoverageType dct = of.createDescribeCoverageType();
		dct.setVersion(version);
		List cids = new ArrayList();
		cids.add(coverageid);
		dct.setCoverageId(cids);
		return dct;
	}
	/**
	 * get coverage description
	 * @param endurl
	 * @param dct
	 * @return
	 */
	public static CoverageDescriptionsType getCoverageDescription(String endurl, DescribeCoverageType dct){
		String req = dct.toString();
		theLogger.info(req);
		CoverageDescriptionsType cdt = null;
		try {
			String cds = HttpUtils.doPost(endurl, req);
			JAXBContext jaxbContext = null;
			cdt = JAXB.unmarshal(new StringReader(cds), CoverageDescriptionsType.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Fail to get coverage descriptions of Coverage: " + dct.getCoverageId().get(0) + ". "+ e.getLocalizedMessage());
		}
		return cdt;
	}
	
	/**
	 * Get access endpoint
	 * @param ct
	 * capabilitiestype
	 * @return
	 * URL
	 * @throws MalformedURLException 
	 */
	public static URL getEndpoint(CapabilitiesType ct) throws MalformedURLException {
		
		List<Operation> opers = ct.getOperationsMetadata().getOperation();
		
		String exeurl = null;
		
		for(int i=0; i<opers.size(); i++){
			
			Operation oper = opers.get(i);
			
			if("GetCoverage".equals(oper.getName())){
				
				exeurl = oper.getDCP().get(0).getHTTP().getGetOrPost().get(0).getValue().getHref();
				
			}
			
		}
		
		return new URL(exeurl);
		
	}
	
	/**
	 * Get the list of coverage
	 * @param ca
	 * capabilities object
	 * @return
	 * 
	 */
	public static String getCoverageListString(CapabilitiesType ca){
		
		ContentsType cts = ca.getContents();
		
		List<CoverageSummaryType> clist = cts.getCoverageSummary();
		
		StringBuffer capalist = new StringBuffer();
		
		for(int i=0;i<clist.size();i++){
		
			CoverageSummaryType cst = clist.get(i);
			
			capalist.append(cst.getCoverageId()).append("\n");
		}
		
		return capalist.toString();
		
	}
	
	
	
	/**
	 * Get the list of coverage
	 * @param ca
	 * capabilities object
	 * @return
	 * 
	 */
	public static List getCoverageList(CapabilitiesType ca){
		ContentsType cts = ca.getContents();
		List<CoverageSummaryType> clist = cts.getCoverageSummary();
		List cids = new ArrayList();
		for(int i=0;i<clist.size();i++){
			CoverageSummaryType cst = clist.get(i);
			cids.add(cst.getCoverageId());
		}
		return cids;
	}
	
	public static void main(String[] args){
//		String wcsurl = "http://cube.csiss.gmu.edu/cgi-bin/gbwcs-dem.cgi?service=wcs&request=getcapabilities&version=1.0.0";
		String wcsurl = "http://ows9.csiss.gmu.edu/cgi-bin/WCS20-r?service=wcs&request=getcapabilities&version=2.0.0";
//		String wcsurl = "https://tb12.cubewerx.com/a045/cubeserv?DATASTORE=Satellite_Soil_Moisture&SERVICE=WCS&REQUEST=GetCapabilities";
		
//		String wcsurl = "http://www3.csiss.gmu.edu/axis2swa/services/GMU_SOAP_WCS_Service.GMU_SOAP_WCS_ServiceHttpSoap12Endpoint/";
//		theLogger.info(wcsurl);
		/**
		 * test parsing capability
		 */
//		CapabilitiesType cat = WCSUtils.parseCapabilities(wcsurl);
////		CapabilitiesType cat = WCSUtils.parseCapabilities_SOAP(wcsurl);
//		theLogger.info("There are total "+cat.getContents().getCoverageSummary().size() + " coverages in this WCS.");
		
		
		/**
		 * test describe coverage
		 */
//		DescribeCoverageType dct = WCSUtils.createADescribeCoverageRequest("xyz", "2.0.0");
//		CoverageDescriptionsType cdt = WCSUtils.getCoverageDescription(wcsurl, dct);
		
		
		
		
		/**
		 * test get coverage
		 */
//		WCSUtils.getCoverage_SOAP(wcsurl, "GEOTIFF:\"/home/zsun/testfiles/data/2010_305_30H.tif\":Band", "image/GEOTIFF");
		
		/**
		 * test security
		 */
//		WCSUtils.parseCapabilities_Security("http://cube.csiss.gmu.edu/axis2secure/services/GMU_SOAP_WCS_Service.GMU_SOAP_WCS_ServiceHttpSoap12Endpoint/", "client", "apache");
		
		
	}

	
}
