package com.mx.sy.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
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
import com.mx.sy.fragment.TableInfoFragment;
import com.mx.sy.utils.CommonUtils;
import com.tnktech.weight.TNKListView;

/**
 * @author lishouping 订单进行中页面 可以进行退菜，加菜 结账操作
 */
public class OrderConductActivity extends BaseActivity {
	private LinearLayout ll_back;
	private TextView tv_title;

	private TextView tv_order_num, tv_table_num, tv_person_no, tv_service_time,
			tv_ordertotal_price;
	private Button btn_jiezhang_order, btn_addfood_order;

	private TNKListView lv_order_dinner;
	private List<HashMap<String, String>> dateList;
	private OrderSubmitAdapter orderSubmitAdapter;

	private SharedPreferences preferences;

	private String table_id;
	private String table_name;

	private String order_id;

	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			TableInfoFragment.isrefresh = 1;
			break;
		case R.id.btn_jiezhang_order:

			check();

			break;
		case R.id.btn_addfood_order:
			Intent intent = new Intent(getApplicationContext(),FoodCustomActivity.class);
			intent.putExtra("table_id",table_id);
			intent.putExtra("table_name", table_name);
			startActivity(intent);
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
						new SweetAlertDialog(OrderConductActivity.this,
								SweetAlertDialog.NORMAL_TYPE)
								.setTitleText("确定要退此菜品吗？")
								// .setContentText("Won't be able to recover this file!")
								.setCancelText("取消")
								.setConfirmText("确定")
								.showCancelButton(true)
								.setConfirmClickListener(
										new SweetAlertDialog.OnSweetClickListener() {
											@Override
											public void onClick(
													SweetAlertDialog sDialog) {
												dateList.remove(position);
												orderSubmitAdapter
														.notifyDataSetChanged();
												sDialog.cancel();
											}
										})
								.setCancelClickListener(
										new SweetAlertDialog.OnSweetClickListener() {
											@Override
											public void onClick(
													SweetAlertDialog sDialog) {
												sDialog.cancel();
											}
										}).show();

						return true;
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

		getOrder();

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
							order_id = object.getString("order_id");
							String order_num = object.getString("order_num");
							tv_order_num.setText("订单编号:" + order_num);
							JSONObject obh = new JSONObject(object
									.getString("table"));
							String table_name = obh.getString("table_name");
							tv_table_num.setText("桌号:" + table_name);
							String people_count = obh.getString("people_count");
							tv_person_no.setText("用餐人数:" + people_count);
							String create_time = CommonUtils.getStrTime(obh
									.getString("create_time"));
							tv_service_time.setText("下单时间:" + create_time);
							JSONObject cartobj = new JSONObject(object
									.getString("cart"));
							String total_price = cartobj
									.getString("total_price");
							tv_ordertotal_price.setText("总计:" + total_price
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
							TableInfoFragment.isrefresh = 1;
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
