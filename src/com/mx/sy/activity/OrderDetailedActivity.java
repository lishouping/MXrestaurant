package com.mx.sy.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mx.sy.R;
import com.mx.sy.adapter.OrderSubmitAdapter;
import com.mx.sy.api.ApiConfig;
import com.mx.sy.base.BaseActivity;
import com.mx.sy.dialog.SweetAlertDialog;
import com.mx.sy.utils.CommonUtils;
import com.tnktech.weight.TNKListView;

/**
 * @author lishouping 订单未处理详情页面
 */
public class OrderDetailedActivity extends BaseActivity {
	private LinearLayout ll_back;
	private TextView tv_title;
	private TNKListView lv_order_dinner;
	private List<HashMap<String, String>> dateList;
	private OrderSubmitAdapter orderSubmitAdapter;

	private String detailedpage;

	private TextView tv_order_num, tv_table_num, tv_person_no, tv_service_time;

	// 未处理页面
	private Button btn_add_food;// 加菜
	private Button btn_cancel_order;// 取消订单
	private Button btn_sub_order;// 确认下单

	// 用餐中
	private Button btn_addfood_order;// 添加菜品
	private Button btn_jiezhang_order;// 结账

	// 已完成
	private Button btn_dayin_order;// 打印

	private TextView tv_ordertotal_price;

	String objs;

	private SharedPreferences preferences;
	private String order_id;

	private String table_id;
	private String table_name;
	private String order_num;

	@Override
	public void initParms(Bundle parms) {
		// TODO Auto-generated method stub

	}

	@Override
	public View bindView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub

		Intent intent = getIntent();
		order_num = intent.getStringExtra("order_num");
		detailedpage = intent.getStringExtra("detailedpage");
		objs = intent.getStringExtra("jsonobj");
		if (detailedpage.equals("1")) {// 未处理
			return R.layout.activity_orderuntreated;
		} else if (detailedpage.equals("2")) {// 正在用餐
			return R.layout.activity_orderconduct;
		} else if (detailedpage.equals("3")) {// 已完成
			return R.layout.activity_orderend;
		}
		return 0;

	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		ll_back = $(R.id.ll_back);
		tv_title = $(R.id.tv_title);
		lv_order_dinner = $(R.id.lv_order_dinner);
		tv_order_num = $(R.id.tv_order_num);
		tv_table_num = $(R.id.tv_table_num);
		tv_person_no = $(R.id.tv_person_no);
		tv_service_time = $(R.id.tv_service_time);
		tv_ordertotal_price = $(R.id.tv_ordertotal_price);

		if (detailedpage.equals("1")) {// 未处理
			btn_add_food = $(R.id.btn_add_food);
			btn_cancel_order = $(R.id.btn_cancel_order);
			btn_sub_order = $(R.id.btn_sub_order);
		} else if (detailedpage.equals("2")) {// 正在用餐
			btn_addfood_order = $(R.id.btn_addfood_order);
			btn_jiezhang_order = $(R.id.btn_jiezhang_order);
		} else if (detailedpage.equals("3")) {// 已完成
			btn_dayin_order = $(R.id.btn_dayin_order);
		}

		preferences = getSharedPreferences("userinfo",
				LoginActivity.MODE_PRIVATE);

	}

	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		case R.id.btn_add_food:
			Intent intent = new Intent(getApplicationContext(),
					FoodCustomActivity.class);
			intent.putExtra("table_id", table_id);
			intent.putExtra("table_name", table_name);
			startActivity(intent);
			break;
		case R.id.btn_addfood_order:
			Intent intent1 = new Intent(getApplicationContext(),
					FoodCustomActivity.class);
			intent1.putExtra("table_id", table_id);
			intent1.putExtra("table_name", table_name);
			startActivity(intent1);
			break;
		case R.id.btn_sub_order:
			new SweetAlertDialog(OrderDetailedActivity.this,
					SweetAlertDialog.NORMAL_TYPE)
					.setTitleText("确定要提交订单吗？")
					// .setContentText("Won't be able to recover this file!")
					.setCancelText("取消")
					.setConfirmText("确定")
					.showCancelButton(true)
					.setConfirmClickListener(
							new SweetAlertDialog.OnSweetClickListener() {
								@Override
								public void onClick(SweetAlertDialog sDialog) {
									sDialog.cancel();
									submitOrder();
								}
							})
					.setCancelClickListener(
							new SweetAlertDialog.OnSweetClickListener() {
								@Override
								public void onClick(SweetAlertDialog sDialog) {
									sDialog.cancel();
								}
							}).show();
			break;
		case R.id.btn_cancel_order:
			new SweetAlertDialog(OrderDetailedActivity.this,
					SweetAlertDialog.NORMAL_TYPE)
					.setTitleText("确定要取消订单吗？")
					// .setContentText("Won't be able to recover this file!")
					.setCancelText("取消")
					.setConfirmText("确定")
					.showCancelButton(true)
					.setConfirmClickListener(
							new SweetAlertDialog.OnSweetClickListener() {
								@Override
								public void onClick(SweetAlertDialog sDialog) {
									sDialog.cancel();
									cancleOrder();
								}
							})
					.setCancelClickListener(
							new SweetAlertDialog.OnSweetClickListener() {
								@Override
								public void onClick(SweetAlertDialog sDialog) {
									sDialog.cancel();
								}
							}).show();
			break;
		case R.id.btn_jiezhang_order:
			new SweetAlertDialog(OrderDetailedActivity.this,
					SweetAlertDialog.NORMAL_TYPE)
					.setTitleText("确定要结算订单吗？")
					// .setContentText("Won't be able to recover this file!")
					.setCancelText("取消")
					.setConfirmText("确定")
					.showCancelButton(true)
					.setConfirmClickListener(
							new SweetAlertDialog.OnSweetClickListener() {
								@Override
								public void onClick(SweetAlertDialog sDialog) {
									sDialog.cancel();
									check();
								}
							})
					.setCancelClickListener(
							new SweetAlertDialog.OnSweetClickListener() {
								@Override
								public void onClick(SweetAlertDialog sDialog) {
									sDialog.cancel();
								}
							}).show();
			break;
		case R.id.btn_dayin_order:
			new SweetAlertDialog(OrderDetailedActivity.this,
					SweetAlertDialog.NORMAL_TYPE)
					.setTitleText("确定要打印订单？")
					// .setContentText("Won't be able to recover this file!")
					.setCancelText("取消")
					.setConfirmText("确定")
					.showCancelButton(true)
					.setConfirmClickListener(
							new SweetAlertDialog.OnSweetClickListener() {
								@Override
								public void onClick(SweetAlertDialog sDialog) {
									sDialog.cancel();
									Intent intent = new Intent(
											getApplicationContext(),
											PrintActivity.class);
									startActivity(intent);
								}
							})
					.setCancelClickListener(
							new SweetAlertDialog.OnSweetClickListener() {
								@Override
								public void onClick(SweetAlertDialog sDialog) {
									sDialog.cancel();
								}
							}).show();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
	}

	@Override
	protected void initdata() {
		// TODO Auto-generated method stub
		tv_title.setText("订单详情");
		dateList = new ArrayList<HashMap<String, String>>();
		orderSubmitAdapter = new OrderSubmitAdapter(getApplicationContext(),
				dateList, R.layout.item_order_foodlv);
		lv_order_dinner.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

			}
		});
		lv_order_dinner
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, final int position, long id) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								OrderDetailedActivity.this);
						builder.setIcon(R.drawable.ic_launcher);
						builder.setTitle("选择一个操作");
						// 指定下拉列表的显示数据
						final String[] cities = { "划菜", "退菜" };
						// 设置一个下拉的列表选择项
						builder.setItems(cities,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (which == 0) {// 划菜
											String cart_good_id = dateList.get(position).get("cart_good_id");
											updateGoodsIfUp(cart_good_id);
										} else {// 退菜

										}
									}
								});
						builder.show();

						return true;
					}

				});
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		ll_back.setOnClickListener(this);
		if (detailedpage.equals("1")) {// 未处理
			btn_add_food.setOnClickListener(this);
			btn_sub_order.setOnClickListener(this);
			btn_cancel_order.setOnClickListener(this);
		} else if (detailedpage.equals("2")) {// 正在用餐
			btn_addfood_order.setOnClickListener(this);
			btn_jiezhang_order.setOnClickListener(this);
		} else if (detailedpage.equals("3")) {// 已完成
			btn_dayin_order.setOnClickListener(this);
		}
	}

	@Override
	public void doBusiness(Context mContext) {
		// TODO Auto-generated method stub
		getOrderDeatiledByOrderNum();
		// getOrderDeatiled();

	}

	public void getOrderDeatiledByOrderNum() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.GETORDERBYNO;
		RequestParams params = new RequestParams();
		params.put("order_num", order_num);
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				if (arg0 == 200) {
					try {
						String response = new String(arg2, "UTF-8");
						JSONObject jsonObject = new JSONObject(response);
						String CODE = jsonObject.getString("CODE");
						if (CODE.equals("1000")) {
							JSONObject object = new JSONObject(jsonObject
									.getString("DATA"));
							order_id = object.getString("order_id");
							table_id = object.getString("table_id");
							String order_num = object.getString("order_num");
							String order_time = null;
							JSONObject tabobj = new JSONObject(object
									.getString("table"));
							if (detailedpage.equals("1")) {
								order_time = object.getString("create_time");
							} else {
								order_time = object.getString("order_time");
							}
							JSONObject cartobj = new JSONObject(object
									.getString("cart"));

							table_name = tabobj.getString("table_name");
							String people_count = tabobj
									.getString("people_count");

							tv_order_num.setText("订单编号:" + order_num);
							tv_table_num.setText("桌号:" + table_name);
							tv_person_no.setText("用餐人数:" + people_count);
							tv_service_time.setText("创建时间:"
									+ CommonUtils.getStrTime(order_time));

							String total_price = cartobj
									.getString("total_price");
							tv_ordertotal_price.setText("总计:" + total_price
									+ "元");

							JSONArray jsonArray = cartobj
									.getJSONArray("goods_set");
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject object2 = jsonArray.getJSONObject(i);
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("cart_good_id",
										object2.getString("cart_good_id"));
								map.put("good_id", object2.getString("good_id"));
								map.put("pre_price",
										object2.getString("pre_price"));
								map.put("good_id", object2.getString("good_id"));
								map.put("good_name",
										object2.getString("good_name"));
								map.put("good_price",
										object2.getString("good_price"));
								map.put("good_num",
										object2.getString("good_num"));
								map.put("good_total_price",
										object2.getString("good_total_price"));
								map.put("if_up", object2.getString("if_up"));
								dateList.add(map);
							}
							lv_order_dinner.setAdapter(orderSubmitAdapter);

						} else {
							Toast.makeText(getApplicationContext(),
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "服务器异常",
						Toast.LENGTH_LONG).show();
			}
		});

	}

	// public void getOrderDeatiled(){
	// try {
	// JSONObject object = new JSONObject(objs);
	// order_id = object.getString("order_id");
	// table_id = object.getString("table_id");
	// String order_num = object.getString("order_num");
	// String order_time = null;
	// JSONObject writerobj = null;
	// String status = object.getString("status");
	// JSONObject tabobj = new JSONObject(object.getString("table"));
	// if (detailedpage.equals("1")) {
	// order_time= object.getString("create_time");
	// }else {
	// order_time= object.getString("order_time");
	// writerobj = new JSONObject(object.getString("waiter"));
	// String name = writerobj.getString("name");
	// }
	// JSONObject cartobj = new JSONObject(object.getString("cart"));
	//
	// table_name = tabobj.getString("table_name");
	// String people_count = tabobj.getString("people_count");
	//
	// tv_order_num.setText("订单编号:"+order_num);
	// tv_table_num.setText("桌号:"+table_name);
	// tv_person_no.setText("用餐人数:"+people_count);
	// tv_service_time.setText("创建时间:"+CommonUtils.getStrTime(order_time));
	//
	// String total_price = cartobj.getString("total_price");
	// tv_ordertotal_price.setText("总计:"+total_price+"元");
	//
	// JSONArray jsonArray = cartobj.getJSONArray("goods_set");
	// for (int i = 0; i < jsonArray.length(); i++) {
	// JSONObject object2 = jsonArray.getJSONObject(i);
	// HashMap<String, String> map = new HashMap<String, String>();
	// map.put("good_id", object2.getString("good_id"));
	// map.put("pre_price", object2.getString("pre_price"));
	// map.put("good_id", object2.getString("good_id"));
	// map.put("good_name", object2.getString("good_name"));
	// map.put("good_price", object2.getString("good_price"));
	// map.put("good_num", object2.getString("good_num"));
	// map.put("good_total_price",
	// object2.getString("good_total_price"));
	// dateList.add(map);
	// }
	// lv_order_dinner.setAdapter(orderSubmitAdapter);
	//
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	// 服务员确认顾客订单
	public void submitOrder() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.CONFIRMORDER;
		RequestParams params = new RequestParams();
		params.put("waiter_id", preferences.getString("business_id", ""));
		params.put("order_id", order_id);
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				if (arg0 == 200) {
					try {
						String response = new String(arg2, "UTF-8");
						JSONObject jsonObject = new JSONObject(response);

						String CODE = jsonObject.getString("CODE");
						if (CODE.equals("1000")) {
							Toast.makeText(getApplicationContext(),
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(),
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "服务器异常",
								Toast.LENGTH_SHORT).show();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "服务器异常",
						Toast.LENGTH_LONG).show();
			}
		});
	}

	// 结账/order/check
	public void check() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.CHECK_URL;
		RequestParams params = new RequestParams();
		params.put("order_id", order_id);
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				if (arg0 == 200) {
					try {
						String response = new String(arg2, "UTF-8");
						JSONObject jsonObject = new JSONObject(response);
						String CODE = jsonObject.getString("CODE");
						if (CODE.equals("1000")) {
							Toast.makeText(getApplicationContext(),
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(),
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "服务器异常",
								Toast.LENGTH_SHORT).show();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "服务器异常",
						Toast.LENGTH_LONG).show();
			}
		});
	}

	// 取消订单/order/cancleOrder
	public void cancleOrder() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.CANCELORDER_URL;
		RequestParams params = new RequestParams();
		params.put("order_id", order_id);
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				if (arg0 == 200) {
					try {
						String response = new String(arg2, "UTF-8");
						JSONObject jsonObject = new JSONObject(response);
						String CODE = jsonObject.getString("CODE");
						if (CODE.equals("1000")) {
							Toast.makeText(getApplicationContext(),
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(),
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "服务器异常",
								Toast.LENGTH_SHORT).show();
						dissmissDilog();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "服务器异常",
						Toast.LENGTH_LONG).show();
			}
		});
	}

	// 划菜
	public void updateGoodsIfUp(String cart_goods_id) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.GOODSUPDATE;
		RequestParams params = new RequestParams();
		params.put("cart_goods_id", cart_goods_id);
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				if (arg0 == 200) {
					try {
						String response = new String(arg2, "UTF-8");
						JSONObject jsonObject = new JSONObject(response);

						String CODE = jsonObject.getString("CODE");
						if (CODE.equals("1000")) {
							Toast.makeText(getApplicationContext(),
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(),
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "服务器异常",
								Toast.LENGTH_SHORT).show();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "服务器异常",
						Toast.LENGTH_LONG).show();
			}
		});
	}

	// 退菜
	public void returnGoods() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.RETURNGOODS;
		RequestParams params = new RequestParams();
		params.put("cart_goods_id", "");
		params.put("num", order_id);
		params.put("price", order_id);
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				if (arg0 == 200) {
					try {
						String response = new String(arg2, "UTF-8");
						JSONObject jsonObject = new JSONObject(response);

						String CODE = jsonObject.getString("CODE");
						if (CODE.equals("1000")) {
							Toast.makeText(getApplicationContext(),
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(),
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "服务器异常",
								Toast.LENGTH_SHORT).show();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "服务器异常",
						Toast.LENGTH_LONG).show();
			}
		});
	}
}
