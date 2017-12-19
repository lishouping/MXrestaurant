package com.mx.sy.activity;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mx.sy.R;
import com.mx.sy.adapter.ExtsAdapter;
import com.mx.sy.api.ApiConfig;
import com.mx.sy.base.BaseActivity;

/**
 * @author lishouping 规格选择
 */
public class SelectExtsActivity extends BaseActivity {
	private ListView lv_exts;
	private TextView tv_ext_total;
	private Button btn_add_extfood;
	private List<HashMap<String, Object>> dateList;
	private ExtsAdapter extsAdapter;
	private LinearLayout ll_back;
	private TextView tv_title;
	private String table_id;
	private SharedPreferences preferences;
	private String good_id;
	private String ext_id;

	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		case R.id.btn_add_extfood:
			addCart();
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
		return R.layout.activity_selectexts;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub

		lv_exts = $(R.id.lv_exts);
		tv_ext_total = $(R.id.tv_ext_total);
		btn_add_extfood = $(R.id.btn_add_extfood);
		btn_add_extfood.setOnClickListener(this);
		ll_back = $(R.id.ll_back);
		ll_back.setOnClickListener(this);
		tv_title = $(R.id.tv_title);
	}

	@Override
	protected void initdata() {
		// TODO Auto-generated method stub
		tv_title.setText("选择规格");
		preferences = getSharedPreferences("userinfo",
				LoginActivity.MODE_PRIVATE);
		dateList = new ArrayList<HashMap<String, Object>>();
		extsAdapter = new ExtsAdapter(getApplicationContext(), dateList,
				R.layout.item_classselect);
		Intent intent = getIntent();
		table_id = intent.getStringExtra("table_id");
		String goods_exts_list = intent.getStringExtra("goods_exts_list");
		try {
			JSONArray array = new JSONArray(goods_exts_list);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object2 = array.getJSONObject(i);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("ext_id", object2.getString("ext_id"));
				map.put("price", object2.getString("price"));
				map.put("good_id", object2.getString("good_id"));
				map.put("size", object2.getString("size"));
				dateList.add(map);
			}
			lv_exts.setAdapter(extsAdapter);
			good_id = dateList.get(0).get("good_id") + "";
			ext_id = dateList.get(0).get("ext_id")+"";
			tv_ext_total.setText("￥" + dateList.get(0).get("price"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lv_exts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				tv_ext_total.setText("￥" + dateList.get(position).get("price"));
				good_id = dateList.get(position).get("good_id") + "";
				ext_id = dateList.get(position).get("ext_id")+"";
				extsAdapter.setSelectedPosition(position);
				extsAdapter.notifyDataSetChanged();
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
						Toast.makeText(getApplicationContext(),
								jsonObject.getString("MESSAGE"),
								Toast.LENGTH_SHORT).show();
						setResult(104);
						finish();
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
