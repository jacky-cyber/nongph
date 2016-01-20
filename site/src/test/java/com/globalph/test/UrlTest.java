package com.globalph.test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class UrlTest {
	
	public static void main(String[] args) throws UnsupportedEncodingException{
		System.out.println(URLEncoder.encode("黄桃", "utf-8"));
		System.out.println(Charset.defaultCharset());  
		System.out.println(System.getProperties().get("file.encoding"));
	}
}
