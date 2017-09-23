package com.mx.sy.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
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
import com.mx.sy.adapter.ReserveAdapter;
import com.mx.sy.adapter.TableTypeAdapter;
import com.mx.sy.api.ApiConfig;
import com.mx.sy.base.BaseActivity;
import com.mx.sy.utils.SendMessage;

/**
 * @author lishouping
 * 
 */
public class ReserveDialog extends BaseActivity implements SendMessage{
	public static int TABLE_CLASS = 102;

	private ListView lv_chooseclass;
	private List<HashMap<String, String>> dateList;
	private ReserveAdapter reserveAdapter;
	String book_list;

	private SharedPreferences preferences;

	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub

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
		return R.layout.dialog_reserve;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		lv_chooseclass = $(R.id.lv_reserve);
	}

	@Override
	protected void initdata() {
		// TODO Auto-generated method stub
		dateList = new ArrayList<HashMap<String, String>>();
		reserveAdapter = new ReserveAdapter(ReserveDialog.this,
				dateList, R.layout.item_table_reserve);
		
		Intent intent = getIntent();
		book_list = intent.getStringExtra("book_list");
		
		try {
			JSONArray jsonArray = new JSONArray(book_list);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String use_time = jsonObject.getString("use_time");
				String people_num = jsonObject.getString("people_num");
				String name = jsonObject.getString("name");
				String phone = jsonObject.getString("phone");
				String table_name = jsonObject.getString("table_name");
				String table_id = jsonObject.getString("table_id");
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("use_time", use_time);
				map.put("people_num", people_num);
				map.put("name", name);
				map.put("phone", phone);
				map.put("table_name", table_name);
				map.put("table_id", table_id);
				dateList.add(map);
			}
			lv_chooseclass.setAdapter(reserveAdapter);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	    
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doBusiness(Context mContext) {
		// TODO Auto-generated method stub

	}

	@Override
	public void SendMsg(int pos, Object object) {
		// TODO Auto-generated method stub
		if (object.equals("1")) {
			finish();
		}
	}
}
