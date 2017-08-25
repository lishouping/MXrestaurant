package com.mx.sy.activity;

import java.util.Set;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mx.sy.R;
import com.mx.sy.api.ApiConfig;
import com.mx.sy.base.BaseActivity;
import com.mx.sy.utils.CommonUtils;

/**
 * @author Administrator 登录页面
 */
public class LoginActivity extends BaseActivity {
	private EditText edit_user;
	private EditText edit_pass;
	private Button btn_login;
	private SharedPreferences preferences;

	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_login:
			if (isNull()) {
				// 调用登录方法
				userLogin();
			}
			// Intent intent = new
			// Intent(getApplicationContext(),MainActivity.class);
			// startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	public void initParms(Bundle parms) {
		// TODO Auto-generated method stub
		preferences = getSharedPreferences("userinfo",
				LoginActivity.MODE_PRIVATE);
	}

	@Override
	public View bindView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_login;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		edit_user = $(R.id.edit_user);
		edit_pass = $(R.id.edit_pass);
		btn_login = $(R.id.btn_login);
	}

	@Override
	protected void initdata() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		btn_login.setOnClickListener(this);
	}

	@Override
	public void doBusiness(Context mContext) {
		// TODO Auto-generated method stub

	}

	private Boolean isNull() {
		if (edit_user.getText().toString().trim().equals("")) {
			Toast.makeText(getApplicationContext(), "请输入用户名",
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (edit_pass.getText().toString().trim().equals("")) {
			Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else {
			return true;
		}
	}

	// // 查询菜品分类(包含菜品)
	public void selectCategory() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.SELECTCATEGORY_URL+"?shop_id=" + preferences.getString("shop_id", "");
		RequestParams params = new RequestParams();
		params.put("shop_id", preferences.getString("shop_id", ""));
		client.get(url,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						if (arg0 == 200) {
							try {
								String response = new String(arg2, "UTF-8");
								JSONObject jsonObject = new JSONObject(response);
								Toast.makeText(getApplicationContext(),
										jsonObject.toString(),
										Toast.LENGTH_LONG).show();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								Toast.makeText(getApplicationContext(),
										"服务器异常", Toast.LENGTH_SHORT).show();
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
	//查询分区(包含桌台) /tableservice/getTableInfo
	public void getTableInfo(){
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.GETTABLEINFO_URL;
		RequestParams params = new RequestParams();
		params.put("shopid", preferences.getString("shop_id", ""));
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				if (arg0 == 200) {
					try {
						String response = new String(arg2, "UTF-8");
						JSONObject jsonObject = new JSONObject(response);
						Toast.makeText(getApplicationContext(),
								jsonObject.toString(), Toast.LENGTH_LONG)
								.show();
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
				Toast.makeText(getApplicationContext(),
						"服务器异常", Toast.LENGTH_LONG)
						.show();
			}
		});
	}
	//查询购物车/cart/getCart
	public void getCart(){
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.GETSHOPPINGCAR_URL;
		RequestParams params = new RequestParams();
		params.put("shop_id", preferences.getString("shop_id", ""));
		params.put("table_id", "7");
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				if (arg0 == 200) {
					try {
						String response = new String(arg2, "UTF-8");
						JSONObject jsonObject = new JSONObject(response);
						Toast.makeText(getApplicationContext(),
								jsonObject.toString(), Toast.LENGTH_LONG)
								.show();
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
				Toast.makeText(getApplicationContext(),
						"服务器异常", Toast.LENGTH_LONG)
						.show();
			}
		});
	}
	//添加购物车/cart/addCart
		public void addCart(){
			AsyncHttpClient client = new AsyncHttpClient();
			client.addHeader("key", preferences.getString("loginkey", ""));
			client.addHeader("id", preferences.getString("userid", ""));
			String url = ApiConfig.API_URL + ApiConfig.ADDSHOPPINGCAR_URL;
			RequestParams params = new RequestParams();
			params.put("shop_id", preferences.getString("shop_id", ""));
			params.put("table_id", "7");
			params.put("good_id", "1");//菜品id
			params.put("from", "2");//来自
			client.post(url, params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					// TODO Auto-generated method stub
					if (arg0 == 200) {
						try {
							String response = new String(arg2, "UTF-8");
							JSONObject jsonObject = new JSONObject(response);
							Toast.makeText(getApplicationContext(),
									jsonObject.toString(), Toast.LENGTH_LONG)
									.show();
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
					Toast.makeText(getApplicationContext(),
							"服务器异常", Toast.LENGTH_LONG)
							.show();
				}
			});
		}
		//移除商品从购物车/removeCart/
				public void removeCart(){
					AsyncHttpClient client = new AsyncHttpClient();
					client.addHeader("key", preferences.getString("loginkey", ""));
					client.addHeader("id", preferences.getString("userid", ""));
					String url = ApiConfig.API_URL + ApiConfig.REMOVECAR_URL;
					RequestParams params = new RequestParams();
					params.put("cart_id", "18");
					params.put("good_id", "1");//菜品id
					client.post(url, params, new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							// TODO Auto-generated method stub
							if (arg0 == 200) {
								try {
									String response = new String(arg2, "UTF-8");
									JSONObject jsonObject = new JSONObject(response);
									Toast.makeText(getApplicationContext(),
											jsonObject.toString(), Toast.LENGTH_LONG)
											.show();
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
							Toast.makeText(getApplicationContext(),
									"服务器异常", Toast.LENGTH_LONG)
									.show();
						}
					});
				}
				
		//清空购物车/cart/deleteCart
		public void deleteCart(){
					AsyncHttpClient client = new AsyncHttpClient();
					client.addHeader("key", preferences.getString("loginkey", ""));
					client.addHeader("id", preferences.getString("userid", ""));
					String url = ApiConfig.API_URL + ApiConfig.DELETECAR_URL;
					RequestParams params = new RequestParams();
					//params.put("shop_id", preferences.getString("shop_id", ""));
					params.put("cart_id", "17");
					client.post(url, params, new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							// TODO Auto-generated method stub
							if (arg0 == 200) {
								try {
									String response = new String(arg2, "UTF-8");
									JSONObject jsonObject = new JSONObject(response);
									Toast.makeText(getApplicationContext(),
											jsonObject.toString(), Toast.LENGTH_LONG)
											.show();
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
							Toast.makeText(getApplicationContext(),
									"服务器异常", Toast.LENGTH_LONG)
									.show();
						}
					});
	}
		//服务员提交订单/order/saveOrder
		public void submitOrder(){
					AsyncHttpClient client = new AsyncHttpClient();
					client.addHeader("key", preferences.getString("loginkey", ""));
					client.addHeader("id", preferences.getString("userid", ""));
					String url = ApiConfig.API_URL + ApiConfig.SAVEORDER_URL;
					RequestParams params = new RequestParams();
					params.put("cart_id", "19");
					client.post(url, params, new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							// TODO Auto-generated method stub
							if (arg0 == 200) {
								try {
									String response = new String(arg2, "UTF-8");
									JSONObject jsonObject = new JSONObject(response);
									Toast.makeText(getApplicationContext(),
											jsonObject.toString(), Toast.LENGTH_LONG)
											.show();
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
							Toast.makeText(getApplicationContext(),
									"服务器异常", Toast.LENGTH_LONG)
									.show();
						}
					});
	}
		//查询桌台未结账订单/order/getOrder
				public void getOrder(){
							AsyncHttpClient client = new AsyncHttpClient();
							client.addHeader("key", preferences.getString("loginkey", ""));
							client.addHeader("id", preferences.getString("userid", ""));
							String url = ApiConfig.API_URL + ApiConfig.GETORDER_URL;
							RequestParams params = new RequestParams();
							params.put("table_id", "7");
							client.post(url, params, new AsyncHttpResponseHandler() {

								@Override
								public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
									// TODO Auto-generated method stub
									if (arg0 == 200) {
										try {
											String response = new String(arg2, "UTF-8");
											JSONObject jsonObject = new JSONObject(response);
											Toast.makeText(getApplicationContext(),
													jsonObject.toString(), Toast.LENGTH_LONG)
													.show();
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
									Toast.makeText(getApplicationContext(),
											"服务器异常", Toast.LENGTH_LONG)
											.show();
								}
							});
			}
				
				//结账/order/check
				public void check(){
							AsyncHttpClient client = new AsyncHttpClient();
							client.addHeader("key", preferences.getString("loginkey", ""));
							client.addHeader("id", preferences.getString("userid", ""));
							String url = ApiConfig.API_URL + ApiConfig.CHECK_URL;
							RequestParams params = new RequestParams();
							params.put("order_id", "7");
							client.post(url, params, new AsyncHttpResponseHandler() {

								@Override
								public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
									// TODO Auto-generated method stub
									if (arg0 == 200) {
										try {
											String response = new String(arg2, "UTF-8");
											JSONObject jsonObject = new JSONObject(response);
											Toast.makeText(getApplicationContext(),
													jsonObject.toString(), Toast.LENGTH_LONG)
													.show();
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
									Toast.makeText(getApplicationContext(),
											"服务器异常", Toast.LENGTH_LONG)
											.show();
								}
							});
			}
				//取消订单/order/cancleOrder
				public void cancleOrder(){
							AsyncHttpClient client = new AsyncHttpClient();
							client.addHeader("key", preferences.getString("loginkey", ""));
							client.addHeader("id", preferences.getString("userid", ""));
							String url = ApiConfig.API_URL + ApiConfig.CANCELORDER_URL;
							RequestParams params = new RequestParams();
							params.put("order_id", "7");
							client.post(url, params, new AsyncHttpResponseHandler() {

								@Override
								public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
									// TODO Auto-generated method stub
									if (arg0 == 200) {
										try {
											String response = new String(arg2, "UTF-8");
											JSONObject jsonObject = new JSONObject(response);
											Toast.makeText(getApplicationContext(),
													jsonObject.toString(), Toast.LENGTH_LONG)
													.show();
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
									Toast.makeText(getApplicationContext(),
											"服务器异常", Toast.LENGTH_LONG)
											.show();
								}
							});
			}			
	public void interfaceOnclick(View view) {
		//submitOrder();
		getOrder();;
		//getCart();
	}

	private void userLogin() {
		showDilog("登录中");
		// 用户登录
		AsyncHttpClient client = new AsyncHttpClient();
		String url = ApiConfig.API_URL + ApiConfig.USERLOGIN_URL;
		RequestParams params = new RequestParams();
		// params.put("password",
		// CommonUtils.md5(edit_pass.getText().toString()));
		params.put("user_name", edit_user.getText().toString());
		params.put("password", edit_pass.getText().toString());
		params.put("type", "2");// Number 1:商户，2:管理员，3:个人用户，4:超级管理员
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 == 200) {
					try {
						String response = new String(arg2, "UTF-8");
						JSONObject jsonObject = new JSONObject(response);
						String CODE = jsonObject.getString("CODE");
						if (CODE.equals("1000")) {
							JSONObject object = new JSONObject(jsonObject
									.getString("DATA"));

							JSONObject object2 = new JSONObject(object
									.getString("waiter"));

							// JSONObject object3 = new
							// JSONObject(object2.getString("create_time"));
							String user_id = object.getString("user_id");
							String login_key = object.getString("login_key");
							String shop_id = object2.getString("shop_id");

							preferences.edit().putString("userid", user_id)
									.commit();
							preferences.edit().putString("loginkey", login_key)
									.commit();
							preferences.edit().putString("shop_id", shop_id)
									.commit();
							// Intent intent = new
							// Intent(getApplicationContext(),
							// MainActivity.class);
							// startActivity(intent);
							// finish();

							dissmissDilog();
						} else {
							btn_login.setClickable(true);
							Toast.makeText(getApplicationContext(),
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
							dissmissDilog();
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						btn_login.setClickable(true);
						Log.i("异常了", e + "");
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
				Log.i("出错了", arg3 + "");
				Toast.makeText(getApplicationContext(), "服务器异常",
						Toast.LENGTH_SHORT).show();
				dissmissDilog();
			}
		});
	}

}
