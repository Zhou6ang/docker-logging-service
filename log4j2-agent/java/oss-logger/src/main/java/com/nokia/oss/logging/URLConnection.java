package com.nokia.oss.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.time.LocalDateTime;


public class URLConnection {

//	private static Logger logger = LogManager.(URLConnection.class);
	
	public static void main(String[] args) throws IOException {
		passingToES("http://10.69.183.10:9222/a/b", null);
	}
	public static void passingToES(String url,String data) throws IOException {
		
		HttpURLConnection urlconn = (HttpURLConnection)new URL(url).openConnection(new Proxy(Type.HTTP, new InetSocketAddress("10.144.1.10", 8080)));
//		HttpURLConnection urlconn = (HttpURLConnection)new URL(url).openConnection();
		urlconn.setDoOutput(true);
		urlconn.setDoInput(true);
		urlconn.setRequestMethod("POST");
		
		/**
		 * Simulator IE/Chrome sending httprequest.
		 */
//		urlconn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36");
//		urlconn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp;q=0.8");
		
		/**
		 * indicate that response message was encoding with gzip or others.
		 */
//		urlconn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
		urlconn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//		urlconn.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
//		urlconn.setRequestProperty("DNT", "1");
//		urlconn.setRequestProperty("Upgrade-Insecure-Requests", "1");
		
		OutputStream output = urlconn.getOutputStream();
		output.write(data.getBytes());
		output.close();
		
		/**
		 * optional to invoke urlconn.connect()
		 */
//		urlconn.connect();
		
		/**
		 * Print out all of Header for HTTP Response message.
		 */
		
//			urlconn.getHeaderFields().forEach((a,b) -> {
//				System.out.println(a+":"+b);
//			});
		System.out.println(LocalDateTime.now()+" "+urlconn.getResponseCode()+" "+urlconn.getResponseMessage());
		
		urlconn.disconnect();

	}
}
