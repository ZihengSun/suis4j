package suis4j.driver;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import net.opengis.wcs.v_2_0.CapabilitiesType;
import net.opengis.wcs.v_2_0.ContentsType;
import net.opengis.wcs.v_2_0.CoverageDescriptionsType;
import net.opengis.wcs.v_2_0.CoverageSummaryType;
import net.opengis.wcs.v_2_0.DescribeCoverageType;
import net.opengis.wcs.v_2_0.GetCoverageType;
import net.opengis.wcs.v_2_0.ObjectFactory;
import net.opengis.wps.v_1_0_0.Execute;

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
	 */
	public static CapabilitiesType parseCapabilities(String url){
		CapabilitiesType ca =  null;
		try {
			String capabilitydoc = HttpUtils.doGet(url);
			JAXBContext jaxbContext = null;
			ca = JAXB.unmarshal(new StringReader(capabilitydoc), CapabilitiesType.class);
//				System.out.print(ca.getServiceProvider().getProviderName());
//				getCapabilitiesResponse = WMSUtils.getWMSGetCapabilitiesResponse(prefix, cont);
//				theLogger.info(getCapabilitiesResponse);
//				ca = WMSUtils.parseWMSCapabilityResponse(getCapabilitiesResponse);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Fail to parse the capabilities document." + e.getLocalizedMessage());
		}
		return ca;
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
			m.marshal(cdt, writer);
	
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
	public static CoverageDescriptionsType parseCoverageDescriptions(String url){
		
		CoverageDescriptionsType cdt = null;
		
		try{
			
			theLogger.info("D URL: " + url);
			
			String descriptiondoc = HttpUtils.doGet(url);
			
			JAXBContext jaxbContext = null;
			
			cdt = JAXB.unmarshal(new StringReader(descriptiondoc), CoverageDescriptionsType.class);
			
		}catch(Exception e){
			
			e.printStackTrace();
			
			throw new RuntimeException("Fail to parse the coverage descriptions. " + e.getLocalizedMessage());
			
		}
		
		return cdt;
		
	}
	
	/**
	 * Create a getcoverage request
	 * @param coverageid
	 * @return
	 */
	public static GetCoverageType createAGetCoverageRequest(String coverageid){
		ObjectFactory of = new ObjectFactory();
		GetCoverageType gct = of.createGetCoverageType();
		gct.setCoverageId(coverageid);
		return gct;
	}
	
	
	/**
	 * Create a describe coverage request
	 * @param coverageid
	 * @return
	 */
	public static DescribeCoverageType createADescribeCoverageRequest(String coverageid){
		ObjectFactory of = new ObjectFactory();
		DescribeCoverageType dct = of.createDescribeCoverageType();
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
		CapabilitiesType cat = WCSUtils.parseCapabilities(wcsurl);
//		CapabilitiesType cat = WCSUtils.parseCapabilities_SOAP(wcsurl);
		theLogger.info("There are total "+cat.getContents().getCoverageSummary().size() + " coverages in this WCS.");
		
		
		/**
		 * test describe coverage
		 */
//		DescribeCoverageType dct = WCSUtils.createADescribeCoverageRequest("xyz");
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
