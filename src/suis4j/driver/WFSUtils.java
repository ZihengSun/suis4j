package suis4j.driver;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

import net.opengis.wcs.v_2_0.CoverageDescriptionsType;
import net.opengis.wfs.v_2_0.DescribeFeatureTypeType;
import net.opengis.wfs.v_2_0.FeatureTypeListType;
import net.opengis.wfs.v_2_0.FeatureTypeType;
import net.opengis.wfs.v_2_0.WFSCapabilitiesType;

/**
 * 
 * updated and tested by Ziheng Sun on 3/16/2016
 * 
 * updated by Ziheng Sun on 4/29/2016 - to make the plan clear
 * 
 * This class is for WFS 2.0. Pay attention to this. WFS 1.0 will be supported in another class.
 * 
 * @author Ziheng Sun
 * 
 */
public class WFSUtils {
	
	private static Logger theLogger = Logger.getLogger(WFSUtils.class);
	
	public static final String supported_version = "2.0";
	
	private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	/**
	 * Parse WFS capabilities document
	 * created by Ziheng Sun on 4/29/2016
	 * @param capability_doc_url
	 * @return
	 */
	public static WFSCapabilitiesType parseCapabilities(String capability_doc_url){
		WFSCapabilitiesType wct = null;
		try {
			String capa = HttpUtils.doGet(capability_doc_url);
			JAXBContext jaxbContext = null;
			wct = JAXB.unmarshal(new StringReader(capa), WFSCapabilitiesType.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getLocalizedMessage());
		}
		return wct;
	}
	
	/**
	 * Get Feature List of WFS
	 * @param wct
	 * @return
	 */
	public static List<FeatureTypeType> getFeatureList(WFSCapabilitiesType wct ){
		List<FeatureTypeType> ftts = wct.getFeatureTypeList().getFeatureType();
		return ftts;
	}
	/**
	 * create a describefeaturetype request
	 * @param type
	 * @return
	 */
	public static DescribeFeatureTypeType createADescribeFeatureTypeRequest(String type){
		
		
		return null;
	}
	/**
	 * Parse describefeaturetype response
	 * @param xml
	 * @return
	 */
	public static FeatureTypeListType parseDescribeFeatureTypeResponse(String xml){
		
		
		return null;
	}
	

	/**
	 * @param wfsurl
	 * @param box
	 * @return
	 * @throws Exception
	 */
//	public static Vector<GeoFeature> getWFSFeatures20(String wfsurl, BBOX box) throws Exception {
//		
//		theLogger.info("BBox " + box.crs + box.maxx + ":" + box.minx);
//		
//		Vector<GeoFeature> featureVec = new Vector<GeoFeature>(); 
//		wfsurl = wfsurl.trim();
//		
//		if(wfsurl.endsWith("?")||wfsurl.endsWith("&"))
//    		wfsurl += "service=WFS&version=2.0.0&request=GetCapabilities";    		
//    	else
//    		wfsurl += "?service=WFS&version=2.0.0&request=GetCapabilities";
//		
//		String wfsstr = wfsurl;
//		
//		theLogger.info("WFS URL: " + wfsurl);
//    	
////		Map connectionParameters = new HashMap();
//		
////		connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", wfsurl);
//		
//		//updated by Z.S. on 8/11/2016
////		DataStore dstore = DataStoreFinder.getDataStore(connectionParameters);		
////		DataStore dstore = (new WFSDataStoreFactory()).createDataStore(connectionParameters);
//		
////		String typeNames[] = dstore.getTypeNames();		
//		
////		if(typeNames == null)
//		
////			return null;
//		
//		String reqcrs[] = box.crs.split(":");
//		
//		CoordinateReferenceSystem reqepsg = ReferencingFactoryFinder.getCRSAuthorityFactory(reqcrs[0], null).createCoordinateReferenceSystem(reqcrs[1]);
//		
//		BoundingBox reqbound;
//		//updated by Z.S. on 8/12/2016
//		if(box.crs.equalsIgnoreCase("EPSG:4326"))
//			reqbound = new ReferencedEnvelope(box.miny, box.maxy, box.minx, box.maxx, reqepsg);
//		else
//			reqbound = new ReferencedEnvelope(box.minx, box.maxx, box.miny, box.maxy, reqepsg);
//		
//		System.out.println("out bbox is " +box.toString());
//		
//		System.out.println("Required bbox is " +reqbound);
//		
//		System.out.println("Interscted WFS Layers are listed as");
//		
//		WFSCapabilitiesType wct = WFSUtils.parseCapabilities(wfsstr);
//		
//		FeatureTypeListType ftlt = wct.getFeatureTypeList();
//		
//		for(int i=0; i<ftlt.getFeatureType().size(); i++){			
//			QName qn = ftlt.getFeatureType().get(i).getName();
//		
//			System.out.println("===============================\n" + qn.getPrefix()+ ":" + qn.getLocalPart());
//			
//			//FeatureSource<SimpleFeatureType, SimpleFeature> fsource = dstore.getFeatureSource(tname);
//			
//			//ReferencedEnvelope databounds = fsource.getBounds();					
//			
//			//BoundingBox datatarnsbounds = databounds.toBounds(reqepsg);												
//			
//			//if(datatarnsbounds != null) {	
//				
//				//System.out.println( "src Bounds:"+ databounds);
//			
//				//System.out.println( "src Trans Bounds:"+ datatarnsbounds);
//				
//				//System.out.println( "req Bounds:"+ reqbound);
//				
//				//if(!datatarnsbounds.intersects(reqbound)){
////				if(!(datatarnsbounds.intersects(reqbound)||datatarnsbounds.contains(reqbound)||reqbound.contains(datatarnsbounds))) {
//				
//					//System.out.println("Not intersected " + tname);
//					
//					//continue;
//					
//				
//				
//				GeoFeature feature = new GeoFeature();			
//				
//				feature.setName(qn.getPrefix()+ ":" + qn.getLocalPart());
//				
//				feature.setTitle(ftlt.getFeatureType().get(i).getTitle().get(0).getValue());
//				
//				//feature.setDescription(tname);
//				
//				//feature.setWfsUrl(wfsstr);
//				
//				BBOX fbox;
//				
//				if(box.crs.equalsIgnoreCase("EPSG:4326"))
//				
//					fbox = new BBOX(box.crs, reqbound.getMinY(), reqbound.getMinX(), reqbound.getMaxY(), reqbound.getMaxX());
//				
//				else
//				
//					fbox = new BBOX(box.crs, reqbound.getMinX(), reqbound.getMinY(), reqbound.getMaxX(), reqbound.getMaxY());
//				
//				System.out.println(ftlt.getFeatureType().get(i).getName().toString() + "--" +fbox.toString());
//				
//				feature.addBBOX(fbox);			
//				
//				featureVec.add(feature);
//				
//			}
//			
//		
//		if(featureVec.size() > 0)
//			
//			return featureVec;
//		
//		else
//			
//			return null;
//		
//	
//		
//		
//	}
//	/**
//	 * @param wfsurl
//	 * @param box
//	 * @return
//	 * @throws Exception
//	 */
//	public static Vector<GeoFeature> getWFSFeatures(String wfsurl, BBOX box) throws Exception {
//		Vector<GeoFeature> featureVec = new Vector<GeoFeature>(); 
//		wfsurl = wfsurl.trim();
//		String wfsstr = wfsurl;
//    	if(wfsurl.endsWith("?"))
//    		wfsurl += "service=WFS&version=1.0.0&request=GetCapabilities";
//    	else
//    		wfsurl += "?service=WFS&version=1.0.0&request=GetCapabilities";
//		
//    	System.out.println("WFS URL: " + wfsurl);
//    	
//		Map connectionParameters = new HashMap();
//		
//		connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", wfsurl);
//		
//		//updated by Z.S. on 8/11/2016
////		DataStore dstore = DataStoreFinder.getDataStore(connectionParameters);		
//		DataStore dstore = (new WFSDataStoreFactory()).createDataStore(connectionParameters);
//		
//		String typeNames[] = dstore.getTypeNames();		
//		
//		if(typeNames == null)
//		
//			return null;
//		
//		String reqcrs[] = box.crs.split(":");
//		
//		CoordinateReferenceSystem reqepsg = ReferencingFactoryFinder.getCRSAuthorityFactory(reqcrs[0], null).createCoordinateReferenceSystem(reqcrs[1]);
//		
//		BoundingBox reqbound;
//		//updated by Z.S. on 8/12/2016
//		if(box.crs.equalsIgnoreCase("EPSG:4326"))
//			reqbound = new ReferencedEnvelope(box.miny, box.maxy, box.minx, box.maxx, reqepsg);
//		else
//			reqbound = new ReferencedEnvelope(box.minx, box.maxx, box.miny, box.maxy, reqepsg);
//		
//		System.out.println("out bbox is " +box.toString());
//		
//		System.out.println("Required bbox is " +reqbound);
//		
//		System.out.println("Interscted WFS Layers are listed as");
//		
//		for(String tname: typeNames) {			
//		
//			System.out.println("===============================\n" + tname);
//			
//			FeatureSource<SimpleFeatureType, SimpleFeature> fsource = dstore.getFeatureSource(tname);
//			
//			ReferencedEnvelope databounds = fsource.getBounds();					
//			
//			BoundingBox datatarnsbounds = databounds.toBounds(reqepsg);												
//			
//			if(datatarnsbounds != null) {	
//				
//				System.out.println( "src Bounds:"+ databounds);
//			
//				System.out.println( "src Trans Bounds:"+ datatarnsbounds);
//				
//				System.out.println( "req Bounds:"+ reqbound);
//				
//				if(!datatarnsbounds.intersects(reqbound)){
////				if(!(datatarnsbounds.intersects(reqbound)||datatarnsbounds.contains(reqbound)||reqbound.contains(datatarnsbounds))) {
//				
//					System.out.println("Not intersected " + tname);
//					
//					continue;
//					
//				}
//				
//				GeoFeature feature = new GeoFeature();			
//				
//				feature.setName(tname);
//				
//				feature.setTitle(tname);
//				
//				feature.setDescription(tname);
//				
//				feature.setWfsUrl(wfsstr);
//				
//				BBOX fbox;
//				
//				if(box.crs.equalsIgnoreCase("EPSG:4326"))
//				
//					fbox = new BBOX(box.crs, datatarnsbounds.getMinY(), datatarnsbounds.getMinX(), datatarnsbounds.getMaxY(), datatarnsbounds.getMaxX());
//				
//				else
//				
//					fbox = new BBOX(box.crs, datatarnsbounds.getMinX(), datatarnsbounds.getMinY(), datatarnsbounds.getMaxX(), datatarnsbounds.getMaxY());
//				
//				System.out.println(tname + "--" +fbox.toString());
//				
//				feature.addBBOX(fbox);			
//				
//				featureVec.add(feature);
//				
//			}
//			
//		}		
//		if(featureVec.size() > 0)
//			
//			return featureVec;
//		
//		else
//			
//			return null;
//		
//	}
//
//	/**
//	 * @param wfsurl
//	 * @param box
//	 * @return
//	 * @throws Exception
//	 */
//	public static Vector<GeoFeature> getWFSFeatures20_SOAP(String wfsurl, BBOX box) throws Exception {
//		
//		Vector<GeoFeature> featureVec = new Vector<GeoFeature>(); 	
//		
//		String wfsstr = wfsurl;
//		
//    	System.out.println("WFS URL: " + wfsurl);
//    	
//    	String reqcrs[] = box.crs.split(":");		
//		
//		CoordinateReferenceSystem reqepsg = ReferencingFactoryFinder.getCRSAuthorityFactory(reqcrs[0], null).createCoordinateReferenceSystem(reqcrs[1]);		
//		BoundingBox reqbound;
//		//updated by Z.S. on 8/12/2016
//		if(box.crs.equalsIgnoreCase("EPSG:4326")) {
//			reqbound = new ReferencedEnvelope(box.miny, box.maxy, box.minx, box.maxx, reqepsg);
//		}			
//		else {
//			reqbound = new ReferencedEnvelope(box.minx, box.maxx, box.miny, box.maxy, reqepsg);
//		}
//			
//		
//		System.out.println("out bbox is " +box.toString());		
//		System.out.println("Required bbox is " +reqbound);		
//		System.out.println("Interscted WFS Layers are listed as");
//		
//		WFSCapabilitiesType wct = WFSUtils.parseCapabilities_SOAP(wfsstr);
//		
//		FeatureTypeListType ftlt = wct.getFeatureTypeList();
//		
//		
//		for(int i=0; i<ftlt.getFeatureType().size(); i++){			
//			
//			QName qn = ftlt.getFeatureType().get(i).getName();
//			
//			GeoFeature feature = new GeoFeature();
//			
//			if(qn!=null){
//				
//				System.out.println("===============================\n" + qn.getPrefix()+ ":" + qn.getLocalPart());
//				
//				feature.setName(qn.getPrefix()+ ":" + qn.getLocalPart());				
//				
//			}
//			feature.setTitle(ftlt.getFeatureType().get(i).getTitle().get(0).getValue());
//				
//			//feature.setDescription(tname);				
//			//feature.setWfsUrl(wfsstr);
//				
//			BBOX fbox;				
//			if(box.crs.equalsIgnoreCase("EPSG:4326")) {
//				fbox = new BBOX(box.crs, reqbound.getMinY(), reqbound.getMinX(), reqbound.getMaxY(), reqbound.getMaxX());
//			}	
//			else {
//				fbox = new BBOX(box.crs, reqbound.getMinX(), reqbound.getMinY(), reqbound.getMaxX(), reqbound.getMaxY());
//			}
//				
////			System.out.println(ftlt.getFeatureType().get(i).getName().toString() + "--" +fbox.toString());			
//			feature.addBBOX(fbox);							
//			featureVec.add(feature);				
//		}			
//		
//		if(featureVec.size() > 0) {
//			return featureVec;
//		}		
//		else {
//			return null;
//		}		
//	}
//	
//	public static Vector<GeoFeature> getWFSFeatures20_SOAP_Security(String wfsurl, BBOX box) throws Exception {
//		
//		Vector<GeoFeature> featureVec = new Vector<GeoFeature>(); 	
//		
//		String wfsstr = wfsurl;
//		
//    	System.out.println("WFS URL: " + wfsurl);
//    	
//    	String reqcrs[] = box.crs.split(":");		
//		
//		CoordinateReferenceSystem reqepsg = ReferencingFactoryFinder.getCRSAuthorityFactory(reqcrs[0], null).createCoordinateReferenceSystem(reqcrs[1]);		
//		BoundingBox reqbound;
//		//updated by Z.S. on 8/12/2016
//		if(box.crs.equalsIgnoreCase("EPSG:4326")) {
//			reqbound = new ReferencedEnvelope(box.miny, box.maxy, box.minx, box.maxx, reqepsg);
//		}			
//		else {
//			reqbound = new ReferencedEnvelope(box.minx, box.maxx, box.miny, box.maxy, reqepsg);
//		}
//			
//		
//		System.out.println("out bbox is " +box.toString());		
//		System.out.println("Required bbox is " +reqbound);		
//		System.out.println("Interscted WFS Layers are listed as");
//		
//		WFSCapabilitiesType wct = WFSUtils.parseCapabilities_SOAP_Security(wfsstr);
//		
//		FeatureTypeListType ftlt = wct.getFeatureTypeList();
//		
//		
//		for(int i=0; i<ftlt.getFeatureType().size(); i++){			
//			
//			QName qn = ftlt.getFeatureType().get(i).getName();
//			
//			GeoFeature feature = new GeoFeature();
//			
//			if(qn!=null){
//				
//				System.out.println("===============================\n" + qn.getPrefix() + qn.getLocalPart().replace("cw-","cw:"));
//				
//				feature.setName(qn.getPrefix()+ qn.getLocalPart().replace("cw-","cw:"));				
//				
//			}
//			feature.setTitle(ftlt.getFeatureType().get(i).getTitle().get(0).getValue());
//				
//			//feature.setDescription(tname);				
//			//feature.setWfsUrl(wfsstr);
//				
//			BBOX fbox;				
//			if(box.crs.equalsIgnoreCase("EPSG:4326")) {
//				fbox = new BBOX(box.crs, reqbound.getMinY(), reqbound.getMinX(), reqbound.getMaxY(), reqbound.getMaxX());
//			}	
//			else {
//				fbox = new BBOX(box.crs, reqbound.getMinX(), reqbound.getMinY(), reqbound.getMaxX(), reqbound.getMaxY());
//			}
//				
////			System.out.println(ftlt.getFeatureType().get(i).getName().toString() + "--" +fbox.toString());			
//			feature.addBBOX(fbox);							
//			featureVec.add(feature);				
//		}			
//		
//		if(featureVec.size() > 0) {
//			return featureVec;
//		}		
//		else {
//			return null;
//		}			
//	}
	
//    public static String cacheGML(String gmlurl){
//    	
//    	String fileurl = null;
//    	
//    	try {
//			
//			String filename = UUID.randomUUID().toString() + ".gml";
//			
//			String filepath = BaseTool.get_tempfilepath() + filename;
//			
//			fileurl = BaseTool.get_tempfileurl() + filename;
//			
//			theLogger.info(filepath);
//			
//			BaseTool.downloadFromUrl2(new URL(gmlurl),  filepath);
//			
//		} catch (Exception e) {
//
//			e.printStackTrace();
//			
//		} 
//
//		return fileurl;
//		
//    }
	
	
	public static void main(String[] args) {
		
		/**
		 * 
		 * test conventional WFS 2.0
		 * 
		 */
		
////		String wfsurl = "http://ws.csiss.gmu.edu:8080/geoserver/wfs";
//		
//		String wfsurl = "http://cube.csiss.gmu.edu/geoserver/topp/ows";
//		
//		//String wfscapaurl = "http://cube.csiss.gmu.edu/geoserver/topp/ows?service=wfs&request=getcapabilities&version=2.0.0";
//		
//		String wfscapaurl = "http://59.120.223.164:443/geoserver/ows?service=wfs&version=2.0.0&request=GetCapabilities";
//		
//		WFSCapabilitiesType wct = WFSUtils.parseCapabilities(wfscapaurl);
//		
//		FeatureTypeListType ftlt = wct.getFeatureTypeList();
//		
//		System.out.println("Feature type nubmer : " + ftlt.getFeatureType().size());
//	//	BBOX box = new BBOX("EPSG:4326", -77.527282, 38.934311, -76.887893, 39.353648);	
////		BBOX box = new BBOX("EPSG:32618", 280940.92757638777, 4312524.259656975, 337335.10634472733, 4357722.154561454);
////		try {		
////			Vector<GeoFeature> gfVec2 = getWFSFeatures(wfsurl, box);
////			System.out.println("--------------------------------");
////			System.out.println("gfVec2.size()=" + gfVec2.size());
////			for(GeoFeature gf:gfVec2) {
////				System.out.println("Name=" + gf.getName());
////			}
////		} catch(Exception e) {
////			e.printStackTrace();
////		}
		
		/**
		 * 
		 *  Test SOAP WFS 2.0
		 * 
		 */
//	//	String wfsurl = "http://ws.csiss.gmu.edu:8080/geoserver/wfs";
//		
//		String wfsurl = "http://polar.geodacenter.org/services/ows/wfs/soap/1.2";
//		
//		//String wfscapaurl = "http://cube.csiss.gmu.edu/geoserver/topp/ows?service=wfs&request=getcapabilities&version=2.0.0";
//		
//		//String wfscapaurl = "http://59.120.223.164:443/geoserver/ows?service=wfs&version=2.0.0&request=GetCapabilities";
//		
////		BBOX bb = new BBOX("EPSG:4326", -122.5754909, 37.3862364, -121.861508, 38.0220317);
////		
////		try {
////			
////			Vector<GeoFeature> featurevec = WFSUtils.getWFSFeatures20_SOAP(wfsurl, bb);
////			
////
//////			/**
//////			 * 
//////			 * Test ASU WFS SOAP Capabilities
//////			 * 
//////			 */
//////			WFSUtils.getWFSFeatures20("http://localhost:8080/capa.xml", bb);
////			
////		} catch (Exception e) {
////
////			e.printStackTrace();
////			
////		}						
//		
//		//test cache feature
//		
//		WFSUtils.cacheFeatureSOAP("landuse", wfsurl);
//		
		
		//WFSUtils.parseCapabilities_SOAP_Security("https://tb12.secure-dimensions.com/soap/services");
		
//		WFSUtils.cacheFeatureSOAPSecurity("cw:Elev_Contour", "https://tb12.secure-dimensions.com/soap/services");
	}	
	
}
