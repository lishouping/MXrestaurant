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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.mx.sy.api.ApiConfig;
import com.mx.sy.base.BaseActivity;
import com.tnktech.weight.TNKListView;

/**
 * @author lishouping 订单确认页面 可以进行删除菜品的操作
 */
public class OrderSubmitActivity extends BaseActivity {
	private LinearLayout ll_back;
	private TextView tv_title;
	private TNKListView lv_order_dinner;
	private List<HashMap<String, String>> dateList;
	private OrderConductAdapter orderSubmitAdapter;

	private Button btn_add_food;

	private String cart_id;
	private String table_id;
	private String table_name;

	private TextView tv_table_num, tv_subtotalprice;

	private SharedPreferences preferences;

	private Button btn_sub_order;

	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		case R.id.btn_sub_order:
			showDilog("加载中");
			submitOrder();
			break;
		case R.id.btn_add_food:
			finish();
			break;
		default:
			break;
		}
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
		return R.layout.activity_ordersubmit;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		ll_back = $(R.id.ll_back);
		tv_title = $(R.id.tv_title);
		lv_order_dinner = $(R.id.lv_order_dinner);
		btn_sub_order = $(R.id.btn_sub_order);
		tv_table_num = $(R.id.tv_table_num);
		tv_subtotalprice = $(R.id.tv_subtotalprice);
		btn_add_food = $(R.id.btn_add_food);
	}

	@Override
	protected void initdata() {
		// TODO Auto-generated method stub
		tv_title.setText("订单详情");
		preferences = getSharedPreferences("userinfo",
				LoginActivity.MODE_PRIVATE);

		dateList = new ArrayList<HashMap<String, String>>();
		orderSubmitAdapter = new OrderConductAdapter(getApplicationContext(),
				dateList, R.layout.item_order_foodlv);
		lv_order_dinner.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				 LayoutInflater factory = LayoutInflater.from(OrderSubmitActivity.this);   
			        final View textEntryView = factory.inflate(R.layout.create_user_dialog, null);   
			        final EditText editTextName = (EditText) textEntryView.findViewById(R.id.text_editprice);   
			        //final EditText editTextNumEditText = (EditText)textEntryView.findViewById(R.id.editTextNum);   
			        AlertDialog.Builder ad1 = new AlertDialog.Builder(OrderSubmitActivity.this);   
			        ad1.setTitle("时价菜品");   
			        ad1.setIcon(android.R.drawable.ic_dialog_info);   
			        ad1.setView(textEntryView);   
			        ad1.setPositiveButton("保存", new DialogInterface.OnClickListener() {   
			            public void onClick(DialogInterface dialog, int i) { 
			            	String cart_goods_id = dateList.get(arg2).get("cart_good_id");
			            	String price = editTextName.getText().toString();
			            	updateGoodsPrice(cart_goods_id,price);
			            }   
			        });   
			        ad1.setNegativeButton("关闭", new DialogInterface.OnClickListener() {   
			            public void onClick(DialogInterface dialog, int i) {   
			     
			            }   
			        });   
			        ad1.show();// 显示对话框   
				
				
			}
		});

	}
	
	

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		ll_back.setOnClickListener(this);
		btn_add_food.setOnClickListener(this);
		btn_sub_order.setOnClickListener(this);
	}

	@Override
	public void doBusiness(Context mContext) {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		cart_id = intent.getStringExtra("cart_id");
		table_id = intent.getStringExtra("table_id");
		table_name = intent.getStringExtra("table_name");
		tv_table_num.setText("桌号" + table_name);

		getCart();
	}

	// 服务员提交订单/order/saveOrder
	public void submitOrder() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.SAVEORDER_URL;
		RequestParams params = new RequestParams();
		params.put("cart_id", cart_id);
		params.put("waiter_id", preferences.getString("business_id", ""));
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				if (arg0 == 200) {
					try {
						String response = new String(arg2, "UTF-8");
						JSONObject jsonObject = new JSONObject(response);

						String CODE = jsonObject.getString("CODE");
						dissmissDilog();
						if (CODE.equals("1000")) {
							Toast.makeText(getApplicationContext(),
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
							OrderConductActivity.inActivity.finish();
							OrderDetailedActivity.initactivitActivity.finish();
							Intent intent = new Intent();
							intent.setClass(getApplicationContext(),
									OrderConductActivity.class);
							intent.putExtra("table_id", table_id);
							intent.putExtra("table_name", table_name);
							startActivity(intent);
							FoodCustomActivity.inActivity.finish();
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

	
	// 查询购物车/cart/getCart
		public void getCart() {
			if (dateList.size()>0) {
				orderSubmitAdapter.notifyDataSetChanged();
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.addHeader("key", preferences.getString("loginkey", ""));
			client.addHeader("id", preferences.getString("userid", ""));
			String url = ApiConfig.API_URL + ApiConfig.GETSHOPPINGCAR_URL;
			RequestParams params = new RequestParams();
			params.put("shop_id", preferences.getString("shop_id", ""));
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
								cart_id = object.getString("cart_id");
								String total_num = object.getString("total_num");
								String total_price = object.getString("total_price");
								tv_subtotalprice.setText("总计:" + total_price + "元");
								JSONArray jsonArray = object.getJSONArray("goods_set");
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject object2 = jsonArray.getJSONObject(i);
									HashMap<String, String> map = new HashMap<String, String>();
									map.put("good_id", object2.getString("good_id"));
									map.put("pre_price", object2.getString("pre_price"));
									map.put("good_id", object2.getString("good_id"));
									map.put("good_name", object2.getString("good_name"));
									map.put("good_price", object2.getString("good_price"));
									map.put("good_num", object2.getString("good_num"));
									map.put("good_total_price",
											object2.getString("good_total_price"));
									map.put("cart_good_id", object2.getString("cart_good_id"));
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

	// 时价菜品修改
	public void updateGoodsPrice(String cart_goods_id,String price) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.UPDATEGOODSPRICE;
		RequestParams params = new RequestParams();
		params.put("cart_goods_id", cart_goods_id);
		params.put("price", price);
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
							getCart();
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
