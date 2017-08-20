package com.mx.sy.api;


//定义API 接口等调用
public class ApiConfig {
	
	public static String API_URL = "http://47.92.66.33:8080/heygay/";
	
	public static String getAbsoluteApiUrl(String partUrl) {
		String url = partUrl;
		if (!partUrl.startsWith("http:") && !partUrl.startsWith("https:")) {
			url = String.format(API_URL, partUrl);
		}
		return url;
	}
	
	// 接口详细地址的调用
	public static String USERLOGIN_URL = "userservice/login";
	
}
