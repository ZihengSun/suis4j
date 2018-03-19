package suis4j.driver;

import java.net.*;
import java.io.*;

public class HttpUtils
{
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
