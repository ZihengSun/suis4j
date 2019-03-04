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
import net.opengis.wcs.v_1_0_0.CoverageDescription;
import net.opengis.wcs.v_1_0_0.CoverageOfferingBriefType;
import net.opengis.wcs.v_1_0_0.DCPTypeType;
import net.opengis.wcs.v_1_0_0.DCPTypeType.HTTP.Get;
import net.opengis.wcs.v_1_0_0.DCPTypeType.HTTP.Post;
import net.opengis.wcs.v_1_0_0.DescribeCoverage;
import net.opengis.wcs.v_1_0_0.GetCapabilities;
import net.opengis.wcs.v_1_0_0.GetCoverage;
import net.opengis.wcs.v_1_0_0.LonLatEnvelopeType;
import net.opengis.wcs.v_1_0_0.RangeSet;
import net.opengis.wcs.v_1_0_0.SupportedCRSsType;
import net.opengis.wcs.v_1_0_0.SupportedFormatsType;
import net.opengis.wcs.v_1_0_0.SupportedInterpolationsType;
import net.opengis.wcs.v_1_0_0.WCSCapabilitiesType;
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
	
	public static net.opengis.wcs.v_1_0_0.WCSCapabilitiesType parse100Capabilities(String url) throws Exception{
		
		String capabilitydoc = HttpUtils.doGet(url);
		
		return parse100CapabilitiesStr(capabilitydoc);
	}
	
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
	
	public static WCSCapabilitiesType parse100CapabilitiesStr(String content){
		
		WCSCapabilitiesType ca =  null;
		try {
			
			JAXBContext jaxbContext = null;
			
			ca = JAXB.unmarshal(new StringReader(content), WCSCapabilitiesType.class);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			throw new RuntimeException("Fail to parse the capabilities document." + e.getLocalizedMessage());
			
		}
		
		return ca;
		
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
	
	public static String turn100DomainSetTypeToXML(net.opengis.wcs.v_1_0_0.DomainSetType domainSet) {

		String theXML = null;
		try{
			// serialise to xml
			StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(net.opengis.wcs.v_1_0_0.DomainSetType.class);
			Marshaller m = context.createMarshaller();
			QName qName = new QName("http://www.opengis.net/wcs/1.0.0", "DomainSetType");
		    JAXBElement<net.opengis.wcs.v_1_0_0.DomainSetType> root = new JAXBElement<>(qName, net.opengis.wcs.v_1_0_0.DomainSetType.class, domainSet);
			m.marshal(root, writer);
			
			// output string to console
			theXML = writer.toString();
//			System.out.println(theXML);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return theXML;
	}
	
	public static String turn100SupportedCRSs(SupportedCRSsType supportedCRSs) {
		String theXML = null;
		try{
			// serialise to xml
			StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(net.opengis.wcs.v_1_0_0.SupportedCRSsType.class);
			Marshaller m = context.createMarshaller();
			QName qName = new QName("http://www.opengis.net/wcs/1.0.0", "SupportedCRSs");
		    JAXBElement<net.opengis.wcs.v_1_0_0.SupportedCRSsType> root = new JAXBElement<>(qName, net.opengis.wcs.v_1_0_0.SupportedCRSsType.class, supportedCRSs);
			m.marshal(root, writer);
			
			// output string to console
			theXML = writer.toString();
//			System.out.println(theXML);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return theXML;
	}
	

	public static String turn100SupportedInterpolations(SupportedInterpolationsType supportedInterpolations) {
		String theXML = null;
		try{
			// serialise to xml
			StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(net.opengis.wcs.v_1_0_0.SupportedInterpolationsType.class);
			Marshaller m = context.createMarshaller();
			QName qName = new QName("http://www.opengis.net/wcs/1.0.0", "SupportedInterpolations");
		    JAXBElement<net.opengis.wcs.v_1_0_0.SupportedInterpolationsType> root = new JAXBElement<>(qName, net.opengis.wcs.v_1_0_0.SupportedInterpolationsType.class, supportedInterpolations);
			m.marshal(root, writer);
			
			// output string to console
			theXML = writer.toString();
//			System.out.println(theXML);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return theXML;
	}
	
	public static Object turn100LonLatEnvelope(LonLatEnvelopeType lonLatEnvelope) {
		String theXML = null;
		try{
			// serialise to xml
			StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(net.opengis.wcs.v_1_0_0.LonLatEnvelopeType.class);
			Marshaller m = context.createMarshaller();
			QName qName = new QName("http://www.opengis.net/wcs/1.0.0", "RangeSet");
		    JAXBElement<net.opengis.wcs.v_1_0_0.LonLatEnvelopeType> root = new JAXBElement<>(qName, net.opengis.wcs.v_1_0_0.LonLatEnvelopeType.class, lonLatEnvelope);
			m.marshal(root, writer);
			
			// output string to console
			theXML = writer.toString();
//			System.out.println(theXML);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return theXML;
	}
	
	public static Object turn100SupportedFormats(SupportedFormatsType supportedFormats) {
		String theXML = null;
		try{
			// serialise to xml
			StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(net.opengis.wcs.v_1_0_0.SupportedFormatsType.class);
			Marshaller m = context.createMarshaller();
			QName qName = new QName("http://www.opengis.net/wcs/1.0.0", "SupportedFormats");
		    JAXBElement<net.opengis.wcs.v_1_0_0.SupportedFormatsType> root = new JAXBElement<>(qName, net.opengis.wcs.v_1_0_0.SupportedFormatsType.class, supportedFormats);
			m.marshal(root, writer);
			
			// output string to console
			theXML = writer.toString();
//			System.out.println(theXML);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return theXML;
	}
	
	public static Object turn100RangeSetTypeToXML(RangeSet rangeSet) {
		String theXML = null;
		try{
			// serialise to xml
			StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(net.opengis.wcs.v_1_0_0.RangeSet.class);
			Marshaller m = context.createMarshaller();
			QName qName = new QName("http://www.opengis.net/wcs/1.0.0", "RangeSet");
		    JAXBElement<net.opengis.wcs.v_1_0_0.RangeSet> root = new JAXBElement<>(qName, net.opengis.wcs.v_1_0_0.RangeSet.class, rangeSet);
			m.marshal(root, writer);
			
			// output string to console
			theXML = writer.toString();
//			System.out.println(theXML);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return theXML;
	}

	
	public static String turn100DescribeCoverageTypeToXML(DescribeCoverage createA100DescribeCoverageRequest) {

		String theXML = null;
		try{
			// serialise to xml
			StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(DescribeCoverage.class);
			Marshaller m = context.createMarshaller();
			QName qName = new QName("http://www.opengis.net/wcs/1.0.0", "DescribeCoverage");
		    JAXBElement<DescribeCoverage> root = new JAXBElement<>(qName, DescribeCoverage.class, createA100DescribeCoverageRequest);
			m.marshal(root, writer);
			
			// output string to console
			theXML = writer.toString();
//			System.out.println(theXML);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return theXML;
	}
	
	public static String turn100GetCapabilitiesTypeToXML(GetCapabilities createA100GetCapabilitiesRequest) {
		
		String theXML = null;
		try{
			// serialise to xml
			StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(GetCapabilities.class);
			Marshaller m = context.createMarshaller();
			QName qName = new QName("http://www.opengis.net/wcs/1.0.0", "GetCapabilities");
		    JAXBElement<GetCapabilities> root = new JAXBElement<>(qName, GetCapabilities.class, createA100GetCapabilitiesRequest);
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
	
	public static net.opengis.wcs.v_1_0_0.CoverageDescription parse100CoverageDescriptions(String returnxml) {
		
		CoverageDescription cdt = null;
		
		try{
			
			JAXBContext jaxbContext = null;
			
			cdt = JAXB.unmarshal(new StringReader(returnxml), CoverageDescription.class);
			
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
	

	public static GetCapabilities createA100GetCapabilitiesRequest() {

		net.opengis.wcs.v_1_0_0.ObjectFactory of = new net.opengis.wcs.v_1_0_0.ObjectFactory();
		
		GetCapabilities gct = of.createGetCapabilities(); 
		
		return gct;
	}
	
	/**
	 * Create a getcoverage request
	 * @param coverageid
	 * @return
	 */
	public static GetCoverageType createAGetCoverageRequest(String coverageid, String format, String version){
		ObjectFactory of = new ObjectFactory();
		GetCoverageType gct = of.createGetCoverageType();
		gct.setVersion(version);
		gct.setCoverageId(coverageid);
		gct.setFormat(format);
		return gct;
	}
	
	public static String createA100GetCoverageRequest(String coverageid, String format, String version,
			String crs, String response_crs, String bbox, String time, String parameter, String width, String height, String depth, 
			String resx, String resy, String resz, String interpolation, String exceptions) {
		
		StringBuffer kvps = new StringBuffer();
		
		kvps.append("coverage=").append(coverageid)
			.append("&format=").append(format)
			.append("&crs=").append(crs);
		
		if(!"".equals(response_crs))
			kvps.append("&response_crs=").append(response_crs);
		
		kvps.append("&bbox=").append(bbox);
		
		if(!"".equals(time))kvps.append("&time=").append(time);
		
		if(!"".equals(parameter))kvps.append("&parameter=").append(parameter);
		
		kvps.append("&width=").append(width);
		
		kvps.append("&height=").append(height);
			
		if(!"".equals(depth))	kvps.append("&depth=").append(depth);
		
		if(!"".equals(resx)) kvps.append("&resx=").append(resx);
		
		if(!"".equals(resy)) kvps.append("&resy=").append(resy);
		
		if(!"".equals(resz)) kvps.append("&resz=").append(resz);
		
		if(!"".equals(interpolation)) kvps.append("&interpolation=").append(interpolation);
		
		if(!"".equals(exceptions)) kvps.append("&exceptions=").append(exceptions);
			
		return kvps.toString();
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
	
	public static DescribeCoverage createA100DescribeCoverageRequest(String valueAsString, String version) {
		
		net.opengis.wcs.v_1_0_0.ObjectFactory of = new net.opengis.wcs.v_1_0_0.ObjectFactory();
		
		DescribeCoverage dct = of.createDescribeCoverage();
		
		List cids = new ArrayList();
		
		cids.add(valueAsString);
		
		dct.setCoverage(cids);
		
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
	
	public static URL get100Endpoint(WCSCapabilitiesType ct) throws MalformedURLException {
		
		String exeurl = null;
		
		Object o = ct.getCapability().getRequest().getGetCoverage()
				.getDCPType().get(0).getHTTP().getGetOrPost().get(0);
		
		if(o instanceof Get){
			
			exeurl = ((Get)o).getOnlineResource().getHref();
			
		}else if(o instanceof Post){
			
			exeurl = ((Post)o).getOnlineResource().getHref();
			
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
	
	public static String get100CoverageListString(WCSCapabilitiesType ca){
		
		List<CoverageOfferingBriefType> coverages = ca.getContentMetadata().getCoverageOfferingBrief();
		
		StringBuffer capalist = new StringBuffer();
		
		for(int i=0;i<coverages.size();i++){
		
			CoverageOfferingBriefType cst = coverages.get(i);
			
			capalist.append(cst.getWcsName()).append("\n");
			
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
	
	public static void main(String[] args) throws Exception{
//		String wcsurl = "http://cube.csiss.gmu.edu/cgi-bin/gbwcs-dem.cgi?service=wcs&request=getcapabilities&version=1.0.0";
//		String wcsurl = "http://ows9.csiss.gmu.edu/cgi-bin/WCS20-r?service=wcs&request=getcapabilities&version=2.0.0";
//		String wcsurl = "https://tb12.cubewerx.com/a045/cubeserv?DATASTORE=Satellite_Soil_Moisture&SERVICE=WCS&REQUEST=GetCapabilities";
		String wcsurl = "http://129.174.131.10/cgi-bin/mapserv?SRS=EPSG:102004&LAYERS=drought.2017.289&MAP=/media/gisiv01/mapfiles/drought/16days/2017/drought.2017.289.map&SERVICE=WCS&VERSION=1.0.0&REQUEST=GetCapabilities"; //1.0.0
//		String wcsurl = "http://www3.csiss.gmu.edu/axis2swa/services/GMU_SOAP_WCS_Service.GMU_SOAP_WCS_ServiceHttpSoap12Endpoint/";
//		theLogger.info(wcsurl);
		/**
		 * test parsing capability
		 */
//		CapabilitiesType cat = WCSUtils.parseCapabilities(wcsurl);
////		CapabilitiesType cat = WCSUtils.parseCapabilities_SOAP(wcsurl);
//		theLogger.info("There are total "+cat.getContents().getCoverageSummary().size() + " coverages in this WCS.");
		
		WCSCapabilitiesType wct = WCSUtils.parse100Capabilities(wcsurl);
		
		theLogger.info(WCSUtils.get100Endpoint(wct));
		
		WCSUtils.get100CoverageListString(wct);
		
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
