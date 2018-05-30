package com.mx.sy.api;

//定义API 接口等调用
public class ApiConfig {

	public static String API_URL = "http://47.92.66.33:8080/heygay";

	public static String RESOURCE_URL = "http://www.heyguy.cn/images/";

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
	// 服务员订单列表(分页，每页10条)
	public static String ORDERLISTFORWRITER = "/order/getOrderListForWaiter";
	// 查询服务列表(分页,每页10条)
	public static String SELECTSERVICELIST = "/service/selectServiceListWaiter";
	// 服务员处理服务
	public static String TODOSERVICE = "/service/updateService";
	// 查询单个服务
	public static String SELECTONESERVICE = "/service/selectOneService";
	// 服务员确认顾客订单
	public static String CONFIRMORDER = "/order/confirmOrder";
	// 根据订单编号查询订单
	public static String GETORDERBYNO = "/order/getOneOrderBynNum";
	// 划菜
	public static String GOODSUPDATE = "/cart/updateGoodsIfUp";
	// 修改时价菜价格
	public static String UPDATEGOODSPRICE = "/cart/updateGoodsPrice";
	// 退菜
	public static String RETURNGOODS = "/cart/returnGoods";
	// 减菜
	public static String REMGOODS = "/cart/removeGoods";
	// 换桌
	public static String CHANGETABLES = "/tableservice/changeTable";
	// 打印
	public static String DOPRINT = "/printer/doPrinter";
	// 查询店铺信息
	public static String GETSHOPINFO = "/shop/getShopInfo/";
	// 查询打印机列表
	public static String PRINTLIST = "/printer/printerlist";
	// 根据订单ID查询打印内容
	public static String PRINTBYORDER = "/order/getPrintContentByOrder";
	// 修改订单信息(服务员确认订单之前)
	public static String UPDATEORDERINFO = "/order/updateOrderInfo";
	// 服务员提交建议
	public static String SAVESUGGEST = "/waiter/saveSuggest";
	// 服务员修改密码
	public static String UPDATEPASSWORD = "/waiter/updatePassword";
	// 查询店铺未处理订单及服务数量
	public static String GETNOREADNUMBER = "/order/getCountForOrderService";
	// 菜品销量统计按照菜品
	public static String GOODSSTATICS = "/statics/goodsStatics";
	// 菜品销量统计按照菜品销量
	public static String SHOPSTATIS = "/statics/shopStatics";
	// 服务数量统计
	public static String SERVICESTSTICS = "/statics/serviceStatics";
	// 更新
	public static String SERVICEUPDATE = "/app/apk";
	// 获取支付图片 二维码
	public static String PAYIMAGE = "/images";
}
