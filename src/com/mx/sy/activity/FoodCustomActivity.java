package com.mx.sy.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IInterface;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mx.sy.R;
import com.mx.sy.adapter.DishesClassAdapter;
import com.mx.sy.adapter.DishesNameAdapter;
import com.mx.sy.adapter.ShoppingCarAdapter;
import com.mx.sy.api.ApiConfig;
import com.mx.sy.base.BaseActivity;
import com.mx.sy.utils.SendMessage;

/**
 * @author lishouping 点餐页面
 */
public class FoodCustomActivity extends BaseActivity implements SendMessage {
	public static boolean addfood = false;
	public static boolean isrefreshcar = false;
	public static FoodCustomActivity inActivity;
	private LinearLayout ll_back;
	private TextView tv_title;

	private ListView lv_dishesclass;
	private ListView lv_dishesname;

	private List<HashMap<String, Object>> disclassList;
	private DishesClassAdapter dishesClassAdapter;

	private List<HashMap<String, String>> disNameList;
	private DishesNameAdapter dishesNameAdapter;

	private Button btn_price_dis, btn_place_order;

	private ImageView imgshopingcar;// 购物车按钮
	private FrameLayout fram_shopingcar;

	private ListView lv_shcar;
	private List<HashMap<String, String>> shopcarList;
	private ShoppingCarAdapter shoppingCarAdapter;

	private TextView tv_tableinfo_number, tv_shopingcar_totalprice;

	private LinearLayout lin_delshpingcar;
	private boolean shopviewstate = false;

	private SharedPreferences preferences;

	String table_id;
	String table_name;
	String good_id;
	String cart_id;
	String ext_id;

	JSONObject intentJsonObject = null;

	private final Timer timer = new Timer();
	private TimerTask task;
	private String objectintent;

	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			timer.cancel();
			isrefreshcar = false;
			addfood = false;
			break;
		case R.id.btn_price_dis:

			break;
		case R.id.btn_place_order:
			if (intentJsonObject == null) {
				Toast.makeText(getApplicationContext(), "您还没有点餐",
						Toast.LENGTH_SHORT).show();
			} else {
				Intent intent = new Intent(getApplicationContext(),
						OrderSubmitActivity.class);
				intent.putExtra("cart_id", cart_id);
				intent.putExtra("table_id", table_id);
				intent.putExtra("table_name", table_name);
				intent.putExtra("objectintent", objectintent + "");
				startActivity(intent);
			}
			break;
		case R.id.imgshopingcar:
			if (intentJsonObject == null) {
				Toast.makeText(getApplicationContext(), "您还没有点餐",
						Toast.LENGTH_SHORT).show();
			} else {
				if (shopviewstate == false) {
					fram_shopingcar.setVisibility(View.VISIBLE);
					shopviewstate = true;
					getCart();
				} else {
					fram_shopingcar.setVisibility(View.GONE);
					shopviewstate = false;
				}
			}

			break;
		case R.id.lin_delshpingcar:
			deleteCart();
			fram_shopingcar.setVisibility(View.GONE);
			shopviewstate = false;
			getCart();
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
		return R.layout.activity_foodcustom;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		ll_back = $(R.id.ll_back);
		tv_title = $(R.id.tv_title);
		lv_dishesclass = $(R.id.lv_dishesclass);

		lv_dishesname = $(R.id.lv_dishesname);

		btn_price_dis = $(R.id.btn_price_dis);
		btn_place_order = $(R.id.btn_place_order);

		imgshopingcar = $(R.id.imgshopingcar);
		fram_shopingcar = $(R.id.fram_shopingcar);

		tv_tableinfo_number = $(R.id.tv_tableinfo_number);
		tv_shopingcar_totalprice = $(R.id.tv_shopingcar_totalprice);

		lin_delshpingcar = $(R.id.lin_delshpingcar);

		lv_shcar = $(R.id.lv_shcar);

		inActivity = this;
	}

	@Override
	protected void initdata() {
		// TODO Auto-generated method stub

		preferences = getSharedPreferences("userinfo",
				LoginActivity.MODE_PRIVATE);

		tv_title.setText("点餐");

		disclassList = new ArrayList<HashMap<String, Object>>();
		dishesClassAdapter = new DishesClassAdapter(getApplicationContext(),
				disclassList, R.layout.item_classselect);
		lv_dishesclass.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				setTableGoodsInfo(
						disclassList.get(position).get("category_name") + "", 1);
				dishesClassAdapter.setSelectedPosition(position);
				dishesClassAdapter.notifyDataSetChanged();
			}
		});

		disNameList = new ArrayList<HashMap<String, String>>();
		dishesNameAdapter = new DishesNameAdapter(FoodCustomActivity.this,
				disNameList, R.layout.item_discname);

		shopcarList = new ArrayList<HashMap<String, String>>();
		shoppingCarAdapter = new ShoppingCarAdapter(FoodCustomActivity.this,
				shopcarList, R.layout.item_shoppingcar);

		task = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
			}
		};
		timer.schedule(task, 2000, 3000);
//		if (isrefreshcar == true) {
//			timer.schedule(task, 2000, 3000);
//		}
			
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			// 要做的事情
			getCart();
			super.handleMessage(msg);
		}
	};

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		ll_back.setOnClickListener(this);
		btn_price_dis.setOnClickListener(this);
		btn_place_order.setOnClickListener(this);
		imgshopingcar.setOnClickListener(this);
		lin_delshpingcar.setOnClickListener(this);
	}

	@Override
	public void doBusiness(Context mContext) {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		table_id = intent.getStringExtra("table_id");
		table_name = intent.getStringExtra("table_name");
	    objectintent = intent.getStringExtra("objectintent");
		
		
		showDilog("加载中");
		selectCategory();
		getCart();
	}

	// // 查询菜品分类(包含菜品)
	public void selectCategory() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.SELECTCATEGORY_URL + "/"
				+ preferences.getString("shop_id", "");
		RequestParams params = new RequestParams();
		params.put("shop_id", preferences.getString("shop_id", ""));
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				if (arg0 == 200) {
					try {
						String response = new String(arg2, "UTF-8");
						JSONObject jsonObject = new JSONObject(response);
						String CODE = jsonObject.getString("CODE");
						if (CODE.equals("1000")) {
							dissmissDilog();
							JSONArray jsonArray = new JSONArray(jsonObject
									.getString("DATA"));
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject object = jsonArray.getJSONObject(i);
								String category_id = object
										.getString("category_id");
								String category_name = object
										.getString("category_name");
								String category_status = object
										.getString("category_status");
								JSONArray array = new JSONArray(object
										.getString("goods_list"));
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("category_id", category_id);
								map.put("category_name", category_name);
								map.put("category_status", category_status);
								map.put("goods_list", array);
								disclassList.add(map);
							}
							lv_dishesclass.setAdapter(dishesClassAdapter);
							setTableGoodsInfo(
									disclassList.get(0).get("category_name")
											+ "", 0);
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
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	// 查询菜品
	private void setTableGoodsInfo(String className, int slepos) {
		if (slepos == 0) {
			for (int i = 0; i < disclassList.size(); i++) {
				try {
					String category_id = disclassList.get(i).get("category_id")
							.toString();
					String category_name = disclassList.get(i)
							.get("category_name").toString();
					String category_status = disclassList.get(i)
							.get("category_status").toString();
					JSONArray array = (JSONArray) disclassList.get(i).get(
							"goods_list");

					for (int j = 0; j < array.length(); j++) {
						JSONObject object2 = array.getJSONObject(j);
						String goods_name = object2.getString("goods_name");// 菜品名
						String pre_price = object2.getString("pre_price");// 单价
						String good_id = object2.getString("good_id");// 商品id
						if (category_name.equals(className)) {
							HashMap<String, String> map4 = new HashMap<String, String>();
							map4.put("goods_name", goods_name);
							map4.put("pre_price", pre_price);
							map4.put("good_id", good_id);
							if (object2.getString("good_exts_flag").equals("1")) {
								map4.put("good_exts_flag", object2.getString("good_exts_flag"));
								JSONArray array2 = new JSONArray(object2.getString("goods_exts_list"));
								map4.put("goods_exts_list", array2+"");
							}else {
								map4.put("good_exts_flag", "0");
							}
							disNameList.add(map4);
						}
					}
					lv_dishesname.setAdapter(dishesNameAdapter);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			disNameList.clear();
			for (int i = 0; i < disclassList.size(); i++) {
				try {
					String category_id = disclassList.get(i).get("category_id")
							.toString();
					String category_name = disclassList.get(i)
							.get("category_name").toString();
					String category_status = disclassList.get(i)
							.get("category_status").toString();
					JSONArray array = (JSONArray) disclassList.get(i).get(
							"goods_list");

					if (category_name.equals(className)) {
						for (int j = 0; j < array.length(); j++) {
							JSONObject object2 = array.getJSONObject(j);
							String goods_name = object2.getString("goods_name");// 菜品名
							String pre_price = object2.getString("pre_price");// 单价
							String good_id = object2.getString("good_id");// 商品id
							if (category_name.equals(className)) {
								HashMap<String, String> map4 = new HashMap<String, String>();
								map4.put("goods_name", goods_name);
								map4.put("pre_price", pre_price);
								map4.put("good_id", good_id);
								if (object2.getString("good_exts_flag").equals("1")) {
									map4.put("good_exts_flag", object2.getString("good_exts_flag"));
									JSONArray array2 = new JSONArray(object2.getString("goods_exts_list"));
									map4.put("goods_exts_list", array2+"");
								}else {
									map4.put("good_exts_flag", "0");
								}
								disNameList.add(map4);
							}
						}
						dishesNameAdapter.notifyDataSetChanged();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	// 查询购物车/cart/getCart
	public void getCart() {
		shopcarList.clear();
		shoppingCarAdapter.notifyDataSetChanged();
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
							intentJsonObject = object;
							cart_id = object.getString("cart_id");
							String total_num = object.getString("total_num");
							String total_price = object
									.getString("total_price");

							tv_shopingcar_totalprice.setText("￥" + total_price);
							tv_tableinfo_number.setText(total_num);

							JSONArray jsonArray = object
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
								map.put("ext_size_id", object2.getString("ext_size_id"));
								shopcarList.add(map);
							}
							lv_shcar.setAdapter(shoppingCarAdapter);
						} else {
							Toast.makeText(getApplicationContext(),
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						tv_tableinfo_number.setText("0");
						fram_shopingcar.setVisibility(View.GONE);
						shopviewstate = false;
						intentJsonObject = null;
						tv_shopingcar_totalprice.setText("￥0");
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

	// 添加购物车/cart/addCart
	public void addCart() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.ADDSHOPPINGCAR_URL;
		RequestParams params = new RequestParams();
		params.put("shop_id", preferences.getString("shop_id", ""));
		params.put("table_id", table_id);
		params.put("good_id", good_id);// 菜品id
		params.put("from", "2");// 来自
		params.put("ext_id", ext_id);
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
							getCart();
						} else if (CODE.equals("10000")) {
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

	// 移除商品从购物车/removeCart/
	public void removeCart() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.REMOVECAR_URL;
		RequestParams params = new RequestParams();
		params.put("cart_id", cart_id);
		params.put("good_id", good_id);// 菜品id
		params.put("ext_id", ext_id);
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

	// 清空购物车/cart/deleteCart
	public void deleteCart() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.DELETECAR_URL;
		RequestParams params = new RequestParams();
		params.put("cart_id", cart_id);
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
	public void SendMsg(int pos, Object object) {
		// TODO Auto-generated method stub
		int select = Integer.parseInt(object.toString());
		if (pos == 100) {
			good_id = disNameList.get(select).get("good_id");
			ext_id = null;
			addCart();
		} else if (pos == 101) {
			good_id = shopcarList.get(select).get("good_id");
			if (shopcarList.get(select).get("ext_size_id").equals("null")) {
				ext_id = null;
			}else {
				ext_id = shopcarList.get(select).get("ext_size_id");
			}
			removeCart();
		} else if (pos == 102) {
			good_id = shopcarList.get(select).get("good_id");
			if (shopcarList.get(select).get("ext_size_id").equals("null")) {
				ext_id = null;
			}else {
				ext_id = shopcarList.get(select).get("ext_size_id");
			}
			addCart();
		}else if (pos == 103) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), SelectExtsActivity.class);
			intent.putExtra("table_id", table_id);
			intent.putExtra("goods_exts_list", disNameList.get(select).get("goods_exts_list"));
			startActivityForResult(intent, 104);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==104) {
			getCart();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 按下BACK，同时没有重复
			Log.d(TAG, "onKeyDown()");
			isrefreshcar = false;
			timer.cancel();
			addfood = false;
		}

		return super.onKeyDown(keyCode, event);
	}
}
