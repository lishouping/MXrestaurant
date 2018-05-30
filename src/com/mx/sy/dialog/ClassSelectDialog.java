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
import com.mx.sy.fragment.TableInfoFragment;

/**
 * @author lishouping
 * 
 */
public class ClassSelectDialog extends BaseActivity {
	public static int TABLE_STATE = 100;
	public static int TABLE_CLASS = 101;

	private ListView lv_chooseclass;
	private List<HashMap<String, String>> dateList;
	private TableTypeAdapter tableTypeAdapter;
	String classType;

	private SharedPreferences preferences;

	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initParms(Bundle parms) {
		// TODO Auto-generated method stub
		classType = parms.getString("selectType");

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
		return R.layout.dialog_clsaaselect;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		lv_chooseclass = $(R.id.lv_chooseclass);
	}

	@Override
	protected void initdata() {
		// TODO Auto-generated method stub
		dateList = new ArrayList<HashMap<String, String>>();
		tableTypeAdapter = new TableTypeAdapter(getApplicationContext(),
				dateList, R.layout.item_classselect);
		if (classType.equals("100")) {
			HashMap<String, String> map0 = new HashMap<String, String>();
			map0.put("tableuseid", "0");
			map0.put("tableusename", "全部");
			dateList.add(map0);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("tableuseid", "0");
			map.put("tableusename", "空闲");
			dateList.add(map);
			HashMap<String, String> map1 = new HashMap<String, String>();
			map1.put("tableuseid", "1");
			map1.put("tableusename", "正在用餐");
			dateList.add(map1);
			HashMap<String, String> map2 = new HashMap<String, String>();
			map2.put("tableuseid", "2");
			map2.put("tableusename", "预定");
			dateList.add(map2);
			HashMap<String, String> map3 = new HashMap<String, String>();
			map3.put("tableuseid", "3");
			map3.put("tableusename", "占用");
			dateList.add(map3);
			HashMap<String, String> map4 = new HashMap<String, String>();
			map4.put("tableuseid", "4");
			map4.put("tableusename", "其他");
			dateList.add(map4);
			lv_chooseclass.setAdapter(tableTypeAdapter);
		} else {
			getTableInfo();
		}

		lv_chooseclass.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (classType.equals("100")) {
					Intent intent = new Intent();
					intent.putExtra("className",
							dateList.get(arg2).get("tableusename"));
					setResult(TABLE_STATE, intent);
					finish();
					TableInfoFragment.isrefresh = 10;
				} else if (classType.equals("101")) {
					Intent intent = new Intent();
					intent.putExtra("className",
							dateList.get(arg2).get("tableusename"));
					setResult(TABLE_CLASS, intent);
					finish();
					TableInfoFragment.isrefresh = 10;
				}
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
							
							HashMap<String, String> map5 = new HashMap<String, String>();
							map5.put("tableuseid", "");
							map5.put("tableusename", "全部");
							dateList.add(map5);
							
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject object = jsonArray.getJSONObject(i);
								String area_id = object.getString("area_id");
								String area_name = object
										.getString("area_name");

								HashMap<String, String> map4 = new HashMap<String, String>();
								map4.put("tableuseid", area_id);
								map4.put("tableusename", area_name);
								dateList.add(map4);
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
}
