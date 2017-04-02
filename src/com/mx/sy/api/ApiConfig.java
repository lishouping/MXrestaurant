package com.mx.sy.api;


//定义API 接口等调用
public class ApiConfig {
	
	public static String API_URL = "http://192.168.18.115:8080";
	
	public static String getAbsoluteApiUrl(String partUrl) {
		String url = partUrl;
		if (!partUrl.startsWith("http:") && !partUrl.startsWith("https:")) {
			url = String.format(API_URL, partUrl);
		}
		return url;
	}
	
}
