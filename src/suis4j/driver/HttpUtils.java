package suis4j.driver;

import java.net.*;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import java.io.*;

public class HttpUtils
{
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
	
	
	public static String doPost(String url, String postContent) throws Exception {
		
		URL u = new URL(url);
		
		// Open the connection and prepare to POST
		URLConnection uc = u.openConnection();
		
		HttpURLConnection huc = (HttpURLConnection)uc;
		
		huc.setDoOutput(true);
		
		huc.setDoInput(true);
		
		huc.setAllowUserInteraction(false);
		
		DataOutputStream dstream = new DataOutputStream(huc.getOutputStream());
		
		// POST it
		dstream.writeBytes(postContent);
		
		dstream.close();
		
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
                con.setConnectTimeout(600000);
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
}
