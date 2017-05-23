package com.mx.sy.api;


//定义API 接口等调用
public class ApiConfig {
	
	public static String API_URL = "http://test.mxmenu.cn/";
	
	public static String getAbsoluteApiUrl(String partUrl) {
		String url = partUrl;
		if (!partUrl.startsWith("http:") && !partUrl.startsWith("https:")) {
			url = String.format(API_URL, partUrl);
		}
		return url;
	}
	
	// 登录接口
	public static String userLoginUrl = "index.php?ctrl=member&action=shoploginin&datatype=json&from=app";
	
}
