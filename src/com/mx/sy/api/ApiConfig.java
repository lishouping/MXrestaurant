package com.mx.sy.api;


//定义API 接口等调用
public class ApiConfig {
	
	public static String API_URL = "http://47.92.66.33:8080/heygay";
	
	public static String getAbsoluteApiUrl(String partUrl) {
		String url = partUrl;
		if (!partUrl.startsWith("http:") && !partUrl.startsWith("https:")) {
			url = String.format(API_URL, partUrl);
		}
		return url;
	}
	
	// 接口详细地址的调用
	// 用户登录
	public static String USERLOGIN_URL = "/userservice/login";
	// 查询菜品分类(包含菜品)
	public static String SELECTCATEGORY_URL = "/goods/selectCategory";
	// 查询分区(包含桌台)
	public static String GETTABLEINFO_URL = "/tableservice/getTableInfo";
	// 查询购物车
	public static String GETSHOPPINGCAR_URL = "/cart/getCart";
	// 添加购物车
	public static String ADDSHOPPINGCAR_URL = "/cart/addCart";
	// 清空购物车
	public static String DELETECAR_URL = "/cart/deleteCart";
	// 移除购物车
	public static String REMOVECAR_URL = "/cart/removeCart";
	// 查询桌台未结账订单
	public static String GETORDER_URL = "/order/getOrder";
	// 服务员提交订单 
	public static String SAVEORDER_URL = "/order/saveOrder";
	// 结账
	public static String CHECK_URL = "/order/check";
	// 取消订单
	public static String CANCELORDER_URL = "/order/cancleOrder";
}
