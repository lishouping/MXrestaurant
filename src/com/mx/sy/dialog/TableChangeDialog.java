package com.mx.sy.dialog;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mx.sy.R;
import com.mx.sy.activity.LoginActivity;
import com.mx.sy.adapter.TableTypeAdapter;
import com.mx.sy.api.ApiConfig;
import com.mx.sy.base.BaseActivity;

/**
 * @author lishouping
 * 
 */
public class TableChangeDialog extends BaseActivity {
	public static int TABLE_CLASS = 102;

	private ListView lv_chooseclass;
	private List<HashMap<String, String>> dateList;
	private TableTypeAdapter tableTypeAdapter;
	String curr_table_id;

	private SharedPreferences preferences;

	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initParms(Bundle parms) {
		// TODO Auto-generated method stub
		curr_table_id = parms.getString("curr_table_id");

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
		return R.layout.dialog_changetable;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		lv_chooseclass = $(R.id.lv_changetable);
	}

	@Override
	protected void initdata() {
		// TODO Auto-generated method stub
		dateList = new ArrayList<HashMap<String, String>>();
		tableTypeAdapter = new TableTypeAdapter(getApplicationContext(),
				dateList, R.layout.item_classselect);
		getTableInfo();

		lv_chooseclass.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String new_table_id = dateList.get(arg2).get("table_id");
				changeTable(new_table_id);
			}
		});
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doBusiness(Context mContext) {
		// TODO Auto-generated method stub

	}

	// 查询分区(包含桌台) /tableservice/getTableInfo
	public void getTableInfo() {
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
						String CODE = jsonObject.getString("CODE");
						if (CODE.equals("1000")) {
							JSONArray jsonArray = new JSONArray(jsonObject
									.getString("DATA"));
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject object = jsonArray.getJSONObject(i);
								String area_id = object.getString("area_id");
								String area_name = object
										.getString("area_name");
								JSONArray array = new JSONArray(object
										.getString("table_list"));
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("area_id", area_id);
								map.put("area_name", area_name);
								map.put("tableinfo", array);
								for (int j = 0; j < array.length(); j++) {
									JSONObject object2 = array.getJSONObject(j);
									String table_name = object2
											.getString("table_name");// 餐桌名
									String table_status = object2
											.getString("table_status");// 餐桌状态
									String table_id = object2
											.getString("table_id");
									HashMap<String, String> map4 = new HashMap<String, String>();
									map4.put("tableusename", table_name);
									map4.put("table_status", table_status);
									map4.put("table_id", table_id);
									dateList.add(map4);
								}
							}
							lv_chooseclass.setAdapter(tableTypeAdapter);
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
	
	public void changeTable(String new_table_id) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.CHANGETABLES;
		RequestParams params = new RequestParams();
		params.put("curr_table_id", curr_table_id);
		params.put("new_table_id", new_table_id);
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
							Toast.makeText(getApplicationContext(), jsonObject.getString("MESSAGE"), Toast.LENGTH_SHORT).show();
							Intent intent = new Intent();
							setResult(TABLE_CLASS, intent);
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
}
