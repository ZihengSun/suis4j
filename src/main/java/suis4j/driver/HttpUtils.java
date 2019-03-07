package suis4j.driver;

import java.net.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import java.io.*;

public class HttpUtils
{
	
	public static String TEMPORARY_PATH = System.getProperty("java.io.tmpdir") + File.separator;
	
	public static int TIMEOUT = 60000000; //waiting time = 1000 minutes = 16 hours
	
	/**
	 * Parse file from URL
	 * @param url
	 * @return
	 */
	public static Document parseURL(String url) 
    {
		
		URL myURL;
	       
        SAXReader reader = new SAXReader();
        Document document = null;
		try {

		    myURL = new URL(url);
			document = reader.read(myURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return document;
    }
	
	/**
	 * 
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
	    Map<String, String> query_pairs = new LinkedHashMap<String, String>();
	    String query = url.getQuery();
	    String[] pairs = query.split("&");
	    for (String pair : pairs) {
	        int idx = pair.indexOf("=");
	        query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8").toLowerCase(), 
	        		URLDecoder.decode(pair.substring(idx + 1), "UTF-8").toLowerCase()); //everything is lowcased
	    }
	    return query_pairs;
	}
	
	
	public static String turnListToString(List arrays){
		
		StringBuffer str = new StringBuffer();
		
		for(String s: (List<String>)arrays){
			
			str.append(s).append(",");
			
		}
		
		return str.toString();
		
		
	}
	
	
	/**
	 * Save the returned file from a post request
	 * @param url
	 * @param postContent
	 * @return
	 * @throws Exception
	 */
	public static void doPostFile(String url, String postContent, String filepath)throws Exception{

		URL u = new URL(url);
		
		// Open the connection and prepare to POST
		URLConnection uc = u.openConnection();
		
		HttpURLConnection huc = (HttpURLConnection)uc;

		huc.setRequestProperty("User-Agent", "Apache-HttpClient/4.1.1");
		
		huc.setRequestMethod("POST");
		
		huc.setRequestProperty("Content-Type", "application/xml");
		
		huc.setDoOutput(true);
		
		huc.setDoInput(true);
		
		huc.setAllowUserInteraction(false);
		
		DataOutputStream dstream = new DataOutputStream(huc.getOutputStream());
		
		// POST it
		dstream.writeBytes(postContent);
		
		dstream.close();
		
		// Read Response
		InputStream in = null;
		
		BufferedReader r = null;
		
        if(huc.getResponseCode()==200){
        	
        	in = huc.getInputStream();
        	
        }else{
        	
        	in = huc.getErrorStream();
        	
        }
        
//        r = new BufferedReader(new InputStreamReader(in));
//        
//        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filepath)));
//		
//		String line;
//		
//		while ((line = r.readLine())!=null){
//			
////			buf.append(line);
//			bw.write(line);
//			
//		}
		
		OutputStream os = new FileOutputStream(filepath);

		byte[] b = new byte[2048];
		int length;

		while ((length = in.read(b)) != -1) {
			os.write(b, 0, length);
		}
		
		in.close();
		
		os.close();
		
	}
	
	public static String doPost(String url, String postContent) throws Exception {
		
		URL u = new URL(url);
		
		// Open the connection and prepare to POST
		URLConnection uc = u.openConnection();
		
		HttpURLConnection huc = (HttpURLConnection)uc;

		huc.setRequestProperty("User-Agent", "Apache-HttpClient/4.1.1");
		
		huc.setRequestMethod("POST");
		
		huc.setRequestProperty("Content-Type", "application/xml");
		
		huc.setDoOutput(true);
		
		huc.setDoInput(true);
		
		huc.setAllowUserInteraction(false);
		
		DataOutputStream dstream = new DataOutputStream(huc.getOutputStream());
		
		// POST it
		dstream.writeBytes(postContent);
		
		dstream.close();
		
		// Read Response
		InputStream in = null;
		
		BufferedReader r = null;
		
        if(huc.getResponseCode()==200){
        	
        	in = huc.getInputStream();
        	
        }else{
        	
        	in = huc.getErrorStream();
        	
        }
        
        r = new BufferedReader(new InputStreamReader(in));
		
		StringBuffer buf = new StringBuffer();
		
		String line;
		
		while ((line = r.readLine())!=null)
			buf.append(line);
		
		in.close();
		
		return buf.toString();
		
	}
	/**
	 * SOAP
	 * @param param
	 * @param operationname
	 * @param input_url
	 * @return
	 */
	static public  String SOAP(String param,String operationname,String input_url){
        String result = "";
        try {
                URL url = new URL(input_url);	      
                HttpURLConnection con =(HttpURLConnection)url.openConnection();
                con.setDoOutput(true); 
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "text/xml");
                con.setRequestProperty("User-Agent","suis4j");
                con.setRequestProperty( "SOAPAction","\""+operationname+"\"");
                
                con.setRequestProperty("Cache-Control", "no-cache");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setConnectTimeout(TIMEOUT); 
                PrintWriter xmlOut = new PrintWriter(con.getOutputStream());
                xmlOut.write(param);   
                xmlOut.flush();
                
                BufferedReader response = null;
                if(con.getResponseCode()==200){
                	response = new BufferedReader(new InputStreamReader(con.getInputStream())); 
                }else{
                	response = new BufferedReader(new InputStreamReader(con.getErrorStream())); 
                }
                
                String line;
                while((line = response.readLine())!=null){
                    result += "\n" + line;
                }
        } catch (Exception e) {
                System.err.println(new StringBuffer().append("Cann't invoke the service '").append(input_url)
                                .append("' successfully as the following reasons. ").append(e.getLocalizedMessage()));
		    e.printStackTrace();
        }
        return result;
	}
	
	/**
	 * Save the returned file from a post request
	 * @param url
	 * @param postContent
	 * @return
	 * @throws Exception
	 */
	public static void doGETFile(String url, String filepath)throws Exception{

//		long start = System.currentTimeMillis();
		
		URL u = new URL(url);
		
		// Open the connection and prepare to POST
		URLConnection uc = u.openConnection();
		
		HttpURLConnection huc = (HttpURLConnection)uc;

		huc.setRequestProperty("User-Agent", "Apache-HttpClient/4.1.1");
		
		huc.setRequestMethod("GET");
		
		huc.setDoOutput(true);
		
		huc.setDoInput(true);
		
		huc.setAllowUserInteraction(false);
		
		// Read Response
		InputStream in = null;
		
		BufferedReader r = null;
		
        if(huc.getResponseCode()==200){
        	
        	in = huc.getInputStream();
        	
        }else{
        	
        	in = huc.getErrorStream();
        	
        }
        
//        r = new BufferedReader(new InputStreamReader(in));
        
        OutputStream os = new FileOutputStream(filepath);

		byte[] b = new byte[2048];
		int length;

		while ((length = in.read(b)) != -1) {
			os.write(b, 0, length);
		}
        
//        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filepath)));
//		
////        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        byte[] buf = new byte[1024];
//        int n = 0;
//        while (-1!=(n=in.read(buf)))
//        {
//        	bw.write(buf, 0, n);
//        }
		
		in.close();
		
		os.close();
		
//		long end = System.currentTimeMillis();
		
//		double downloadcost = end - start;
		
//		System.out.println("data transfer time cost: " + downloadcost + " ms");
		
	}
	
	public static String doGet(String url) throws Exception
	{
		URL u = new URL(url);

		// Open the connection and prepare to POST
		URLConnection uc = u.openConnection();
		HttpURLConnection huc = (HttpURLConnection)uc;
		huc.setDoOutput(false);
		huc.setDoInput(true);
		huc.setAllowUserInteraction(false);

		// Read Response
		InputStream in = huc.getInputStream();

		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		StringBuffer buf = new StringBuffer();
		String line;
		while ((line = r.readLine())!=null)
			buf.append(line);

		in.close();

		return buf.toString();
	}
	
	public static String doGetWithCookies(String url, String cookie_str) throws Exception
	{
		URL u = new URL(url);

		// Open the connection and prepare to POST
		URLConnection uc = u.openConnection();
		HttpURLConnection huc = (HttpURLConnection)uc;
		huc.setDoOutput(false);
		huc.setDoInput(true);
		huc.setAllowUserInteraction(false);
		huc.setRequestProperty("Cookie", cookie_str);

		// Read Response
		InputStream in = huc.getInputStream();

		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		StringBuffer buf = new StringBuffer();
		String line;
		while ((line = r.readLine())!=null)
			buf.append(line);

		in.close();

		return buf.toString();
	}
	
	public static void main(String[] args) throws Exception{
		
		String req = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><DescribeCoverage service=\"WCS\" version=\"2.0.0\" xmlns:ns2=\"http://www.opengis.net/ows/2.0\" xmlns=\"http://www.opengis.net/wcs/2.0\" xmlns:ns4=\"http://www.opengis.net/gml/3.2\" xmlns:ns3=\"http://www.w3.org/1999/xlink\" xmlns:ns5=\"http://www.opengis.net/gmlcov/1.0\" xmlns:ns6=\"http://www.opengis.net/swe/2.0\"><CoverageId>atmosphere__METOP-B_GOME-2_L3_TROPOSPHERIC_O3_MIXINGRATIO_STD</CoverageId></DescribeCoverage>";
		
		String resp = HttpUtils.doPost("https://geoservice.dlr.de/eoc/atmosphere/wcs", req);
		
//		System.out.println(resp);
		
		
	}
	
}
