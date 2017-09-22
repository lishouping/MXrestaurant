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
public class PrintOrderDialog extends BaseActivity {
	public static int TABLE_CLASS = 102;

	private ListView lv_chooseclass;
	private List<HashMap<String, String>> dateList;
	private TableTypeAdapter tableTypeAdapter;
	String DATA;

	private SharedPreferences preferences;

	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initParms(Bundle parms) {
		// TODO Auto-generated method stub
		DATA = parms.getString("DATA");

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
		return R.layout.dialog_printorder;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		lv_chooseclass = $(R.id.lv_drvierlist);
	}

	@Override
	protected void initdata() {
		// TODO Auto-generated method stub
		dateList = new ArrayList<HashMap<String, String>>();
		tableTypeAdapter = new TableTypeAdapter(getApplicationContext(),
				dateList, R.layout.item_classselect);
		
		Intent intent = getIntent();
	    DATA = intent.getStringExtra("DATA");
	    
		printerlist();

		lv_chooseclass.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String printer_no = dateList.get(arg2).get("printer_no");
				doPrinter(printer_no);
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

	// 查询打印机列表
	public void printerlist() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.PRINTLIST;
		RequestParams params = new RequestParams();
		params.put("shop_id", preferences.getString("shop_id", ""));
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
								String type_print = object
										.getString("type_print");
								String printer_no = object
										.getString("printer_no");
								String printer_name = object.getString("printer_name");
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("type_print", type_print);
								map.put("printer_no", printer_no);
								map.put("tableusename", printer_name);
								dateList.add(map);
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

	public void doPrinter(final String printer_no) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.DOPRINT;
		RequestParams params = new RequestParams();
		params.put("print_no", printer_no);
		params.put("content", DATA);
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
}
