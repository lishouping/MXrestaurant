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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mx.sy.R;
import com.mx.sy.adapter.OrderConductAdapter;
import com.mx.sy.adapter.OrderSubmitAdapter;
import com.mx.sy.api.ApiConfig;
import com.mx.sy.base.BaseActivity;
import com.mx.sy.dialog.SweetAlertDialog;
import com.mx.sy.fragment.TableInfoFragment;
import com.mx.sy.utils.CommonUtils;
import com.tnktech.weight.TNKListView;

/**
 * @author lishouping 订单进行中页面 可以进行退菜，加菜 结账操作
 */
public class OrderConductActivity extends BaseActivity {
	public static OrderConductActivity inActivity;
	private LinearLayout ll_back;
	private TextView tv_title;

	private TextView tv_order_num, tv_table_num, tv_person_no, tv_service_time,
			tv_ordertotal_price,tv_jiucantype,tv_shangcaitype;
	private Button btn_jiezhang_order, btn_addfood_order;

	private TNKListView lv_order_dinner;
	private List<HashMap<String, String>> dateList;
	private OrderConductAdapter orderSubmitAdapter;
	
	private TextView tv_beizhu;

	private SharedPreferences preferences;

	private String table_id;
	private String table_name;

	private String order_id;
	private String ext_size_id;
	private String check_way;
	
	// 信息列表提示框  
    private AlertDialog alertDialog1;  

	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			TableInfoFragment.isrefresh = 1;
			break;
		case R.id.btn_jiezhang_order:
			showListAlertDialog();
			break;
		case R.id.btn_addfood_order:
			Intent intent = new Intent(getApplicationContext(),FoodCustomActivity.class);
			intent.putExtra("table_id",table_id);
			intent.putExtra("table_name", table_name);
			startActivity(intent);
			OrderDetailedActivity.isvisit = 1;
			FoodCustomActivity.isrefreshcar = true;
			FoodCustomActivity.addfood = true;
			break;
		default:
			break;
		}
	}
	
    
    public void showListAlertDialog(){  
        final String[] items = {"现金","微信","支付宝"};  
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);  
        alertBuilder.setTitle("请选择付款方式");  
        alertBuilder.setItems(items, new DialogInterface.OnClickListener() {  
            @Override  
            public void onClick(DialogInterface arg0, int index) {  
                alertDialog1.dismiss();  
                if (index==0) {
					check_way = "1";
				}else if (index==1) {
					check_way = "2";
				}else if (index==2) {
					check_way = "3";
				}
                check();
            }  
        });  
        alertDialog1 = alertBuilder.create();  
        alertDialog1.show();  
    }  

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
		return R.layout.activity_orderconduct;
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
		btn_jiezhang_order = $(R.id.btn_jiezhang_order);
		btn_addfood_order = $(R.id.btn_addfood_order);
		tv_beizhu = $(R.id.tv_beizhu);
		tv_jiucantype = $(R.id.tv_jiucantype);
		tv_shangcaitype = $(R.id.tv_shangcaitype);
		inActivity = this;
	}

	@Override
	protected void initdata() {
		// TODO Auto-generated method stub
		tv_title.setText("订单详情");
		dateList = new ArrayList<HashMap<String, String>>();
		orderSubmitAdapter = new OrderConductAdapter(getApplicationContext(),
				dateList, R.layout.item_order_foodlv);

		lv_order_dinner.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
					long arg3) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(
						OrderConductActivity.this);
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
									String cart_good_id = dateList.get(
											position).get("cart_good_id");
									updateGoodsIfUp(cart_good_id);
								} else {// 退菜
									LayoutInflater factory = LayoutInflater
											.from(OrderConductActivity.this);
									final View textEntryView = factory
											.inflate(
													R.layout.return_food_dialog,
													null);
									final EditText text_editnumbe = (EditText) textEntryView
											.findViewById(R.id.text_editnumbe);
									final EditText text_editprice = (EditText) textEntryView
											.findViewById(R.id.text_editprice);
									AlertDialog.Builder ad1 = new AlertDialog.Builder(
											OrderConductActivity.this);
									ad1.setTitle("退菜");
									ad1.setIcon(android.R.drawable.ic_dialog_info);
									ad1.setView(textEntryView);
									ad1.setPositiveButton(
											"保存",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int i) {
													String cart_goods_id = dateList
															.get(position)
															.get("cart_good_id");
													String number = text_editnumbe
															.getText()
															.toString();
													String price = text_editprice
															.getText()
															.toString();
													if (dateList.get(position).get("ext_size_id").equals("null")) {
														ext_size_id = null;
													}else {
														ext_size_id = dateList.get(position).get("ext_size_id");
													}
													returnGoods(
															cart_goods_id,
															number, price);
												}
											});
									ad1.setNegativeButton(
											"关闭",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int i) {

												}
											});
									ad1.show();// 显示对话框
								}
							}
						});
				builder.show();
			}
		});
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		ll_back.setOnClickListener(this);
		btn_jiezhang_order.setOnClickListener(this);
		btn_addfood_order.setOnClickListener(this);
	}

	@Override
	public void doBusiness(Context mContext) {
		// TODO Auto-generated method stub

		preferences = getSharedPreferences("userinfo",
				LoginActivity.MODE_PRIVATE);

		Intent intent = getIntent();
		table_id = intent.getStringExtra("table_id");
		table_name = intent.getStringExtra("table_name");
		
		

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (dateList.size()>0) {
			dateList.clear();
			orderSubmitAdapter.notifyDataSetChanged();
		}
		getOrder();
		super.onResume();
	}

	// 查询桌台未结账订单/order/getOrder
	public void getOrder() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.GETORDER_URL;
		RequestParams params = new RequestParams();
		params.put("table_id", table_id);
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
							
							String comments = object.getString("comments");
							if (comments.equals("null")) {
								tv_beizhu.setText("备注:无");
							}else {
								tv_beizhu.setText("备注:"+comments);
							}
							order_id = object.getString("order_id");
							String order_num = object.getString("order_num");	
							tv_order_num.setText("订单编号:" + order_num);
							JSONObject obh = new JSONObject(object
									.getString("table"));
							String table_name = obh.getString("table_name");
							tv_table_num.setText("桌号:" + table_name);
							String people_count = object.getString("people_count");
							tv_person_no.setText("用餐人数:" + people_count);
							String way = object.getString("way");
							if (way.equals("1")) {
								tv_jiucantype.setText("就餐方式:堂食");
							}else if (way.equals("2")) {
								tv_jiucantype.setText("就餐方式:打包");
							}
							String go_goods_way = object.getString("go_goods_way");
							if (go_goods_way.equals("1")) {
								tv_shangcaitype.setText("上菜方式:做好即上");
							}else if (go_goods_way.equals("2")) {
								tv_shangcaitype.setText("上菜方式:等待叫起");
							}
							String create_time = CommonUtils.getStrTime(obh
									.getString("create_time"));
							String order_time = object.getString("create_time");
							tv_service_time.setText("下单时间:" +CommonUtils.getStrTime(order_time) );
							JSONObject cartobj = new JSONObject(object
									.getString("cart"));
							String total_price = cartobj
									.getString("total_price");
							tv_ordertotal_price.setText(total_price
									+ "元");
							JSONArray jsonArray = cartobj
									.getJSONArray("goods_set");
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject object2 = jsonArray.getJSONObject(i);
								HashMap<String, String> map = new HashMap<String, String>();
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
								map.put("cart_good_id", object2.getString("cart_good_id"));
								map.put("if_up", object2.getString("if_up"));
								map.put("ext_size_id", object2.getString("ext_size_id"));
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
							finish();
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

	// 结账/order/check
	public void check() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.CHECK_URL;
		RequestParams params = new RequestParams();
		params.put("order_id", order_id);
		params.put("check_way",check_way);
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
							if (check_way.equals("1")) {
								Toast.makeText(getApplicationContext(),
										jsonObject.getString("MESSAGE"),
										Toast.LENGTH_SHORT).show();
								finish();
							}else {
								Toast.makeText(getApplicationContext(),
										jsonObject.getString("MESSAGE"),
										Toast.LENGTH_SHORT).show();
								finish();
								Intent intent = new Intent(getApplicationContext(),
										PayImagesActivity.class);
								startActivity(intent);
							}
						} else {
							if (check_way.equals("1")) {
								Toast.makeText(getApplicationContext(),
										jsonObject.getString("MESSAGE"),
										Toast.LENGTH_SHORT).show();
								finish();
							}else {
								Toast.makeText(getApplicationContext(),
										jsonObject.getString("MESSAGE"),
										Toast.LENGTH_SHORT).show();
								finish();
								Intent intent = new Intent(getApplicationContext(),
										PayImagesActivity.class);
								startActivity(intent);
							}
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
								if (dateList.size()>0) {
									dateList.clear();
									orderSubmitAdapter.notifyDataSetChanged();
								}
								getOrder();
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
		public void returnGoods(String cart_goods_id, String num, String price) {
			AsyncHttpClient client = new AsyncHttpClient();
			client.addHeader("key", preferences.getString("loginkey", ""));
			client.addHeader("id", preferences.getString("userid", ""));
			String url = ApiConfig.API_URL + ApiConfig.RETURNGOODS;
			RequestParams params = new RequestParams();
			params.put("cart_goods_id", cart_goods_id);
			params.put("num", num);
			params.put("price", price);
			params.put("ext_id", ext_size_id);
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
								if (dateList.size()>0) {
									dateList.clear();
									orderSubmitAdapter.notifyDataSetChanged();
								}
								getOrder();
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

	@Override    
    public boolean onKeyDown(int keyCode, KeyEvent event) {    
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {    
             System.out.println("按下了back键   onKeyDown()");     
             TableInfoFragment.isrefresh = 1;
             return super.onKeyDown(keyCode, event);    
        }else {    
        	return super.onKeyDown(keyCode, event);    
        }    
            
    }    
}
