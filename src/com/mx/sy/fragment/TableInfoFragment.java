package com.mx.sy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mx.sy.R;
import com.mx.sy.activity.FoodCustomActivity;
import com.mx.sy.activity.OrderConductActivity;
import com.mx.sy.adapter.TablesAdapter;
import com.mx.sy.api.ApiConfig;
import com.mx.sy.base.BaseFragment;
import com.mx.sy.common.PullToRefreshView;
import com.mx.sy.common.PullToRefreshView.OnFooterRefreshListener;
import com.mx.sy.common.PullToRefreshView.OnHeaderRefreshListener;
import com.mx.sy.dialog.ClassSelectDialog;
import com.mx.sy.dialog.SweetAlertDialog;

/**
 * @author Administrator 桌台信息
 */
public class TableInfoFragment extends BaseFragment implements
		OnHeaderRefreshListener, OnFooterRefreshListener, OnClickListener {
	public static int isrefresh = 0;
	public static int TABLE_STATE = 100;
	public static int TABLE_CLASS = 101;

	private GridView gri_tables;
	private List<HashMap<String, String>> dateList;
	private TablesAdapter tablesAdapter;

	private Button btn_table_state, btn_table_class;

	PullToRefreshView mPullToRefreshView;

	private SharedPreferences preferences;

	// 装在位置List
	private List<HashMap<String, Object>> daList = new ArrayList<HashMap<String, Object>>();

	@Override
	protected int setLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_tableinfo;
	}

	@Override
	protected void loadDate() {
		// TODO Auto-generated method stub
		super.loadDate();
		mPullToRefreshView = (PullToRefreshView) getActivity().findViewById(
				R.id.pullrefresh_table);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
	}

	@Override
	protected void initData(Bundle arguments) {
		// TODO Auto-generated method stub
		super.initData(arguments);

		preferences = getActivity().getSharedPreferences("userinfo",
				getActivity().MODE_PRIVATE);
		
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (isrefresh==1) {
			if (dateList.size()==0) {
			}else {
				dateList.clear();
				tablesAdapter.notifyDataSetChanged();
			}
			showDilog("加载中");
			getTableInfo();
			isrefresh = 0;
		}
		super.onResume();
	}
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		gri_tables = findViewById(R.id.gri_tables);
		btn_table_state = findViewById(R.id.btn_table_state);
		btn_table_state.setOnClickListener(this);
		btn_table_class = findViewById(R.id.btn_table_class);
		btn_table_class.setOnClickListener(this);

		super.initView();
	}

	@Override
	protected void onLazyLoad() {
		// TODO Auto-generated method stub
		super.onLazyLoad();
		dateList = new ArrayList<HashMap<String, String>>();
		tablesAdapter = new TablesAdapter(getActivity(), dateList,
				R.layout.item_tables);
		gri_tables.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				Intent intent = new Intent();
				if (dateList.get(position).get("table_status").equals("0")) {//未使用
					intent.setClass(getActivity(), FoodCustomActivity.class);
					intent.putExtra("table_id", dateList.get(position).get("table_id"));
					intent.putExtra("table_name", dateList.get(position).get("table_name"));
					startActivity(intent);
				}else if (dateList.get(position).get("table_status").equals("1")) {//正常使用中
					intent.setClass(getActivity(), OrderConductActivity.class);
					intent.putExtra("table_id", dateList.get(position).get("table_id"));
					intent.putExtra("table_name", dateList.get(position).get("table_name"));
					startActivity(intent);
				}else if (dateList.get(position).get("table_status").equals("2")) {//顾客预订
					new SweetAlertDialog(getActivity(),
							SweetAlertDialog.NORMAL_TYPE)
							.setTitleText("预订信息")
						    .setContentText("")
							.setCancelText("取消")
							.setConfirmText("确定")
							.showCancelButton(true)
							.setConfirmClickListener(
									new SweetAlertDialog.OnSweetClickListener() {
										@Override
										public void onClick(
												SweetAlertDialog sweetAlertDialog) {
											sweetAlertDialog.cancel();
										}
									})
							.setCancelClickListener(
									new SweetAlertDialog.OnSweetClickListener() {
										@Override
										public void onClick(
												SweetAlertDialog sweetAlertDialog) {
											sweetAlertDialog.cancel();
										}
									}).show();
				}
			}
		});
		if (dateList.size()==0) {
		}else {
			dateList.clear();
		}
		showDilog("加载中");
		getTableInfo();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		mPullToRefreshView.postDelayed(new Runnable() {
			@Override
			public void run() {
				// 下拉刷新
				mPullToRefreshView.onHeaderRefreshComplete();
				dateList.clear();
				getTableInfo();
				btn_table_state.setText("餐桌状态");
				btn_table_class.setText("餐桌位置");
			}
		}, 1000);
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		mPullToRefreshView.postDelayed(new Runnable() {
			@Override
			public void run() {
				// 上滑加载
				mPullToRefreshView.onFooterRefreshComplete();
				dateList.clear();
				getTableInfo();
			}
		}, 1000);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_table_state:
			Intent intent = new Intent(getActivity(), ClassSelectDialog.class);
			intent.putExtra("selectType", "100");// 餐桌状态
			startActivityForResult(intent, TABLE_STATE);
			break;
		case R.id.btn_table_class:
			Intent intent1 = new Intent(getActivity(), ClassSelectDialog.class);
			intent1.putExtra("selectType", "101");// 餐桌位置
			startActivityForResult(intent1, TABLE_CLASS);
			break;

		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == TABLE_STATE) {
			btn_table_state.setText(data.getStringExtra("className"));
			
			String className = data.getStringExtra("className");
			if (className.equals("未使用")) {
				setTableStatesInfo("0");
			}else if (className.equals("使用中")) {
				setTableStatesInfo("1");
			}else if (className.equals("预定中")) {
				setTableStatesInfo("2");
			}else if (className.equals("占用中")) {
				setTableStatesInfo("3");
			}else if (className.equals("其他")) {
				setTableStatesInfo("4");
			}
		} else if (resultCode == TABLE_CLASS) {
			btn_table_class.setText(data.getStringExtra("className"));
			setTableAirInfo(data.getStringExtra("className"));
		}
	}

	// 查询分区(包含桌台) /tableservice/getTableInfo 从接口获得
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
								daList.add(map);
								for (int j = 0; j < array.length(); j++) {
									JSONObject object2 = array.getJSONObject(j);
									String table_name = object2
											.getString("table_name");// 餐桌名
									String table_status = object2
											.getString("table_status");// 餐桌状态
									String table_id = object2
											.getString("table_id");
									HashMap<String, String> map4 = new HashMap<String, String>();
									map4.put("table_name", table_name);
									map4.put("table_status", table_status);
									map4.put("table_id", table_id);
									dateList.add(map4);
								}
							}
							gri_tables.setAdapter(tablesAdapter);
							dissmissDilog();
						} else {
							Toast.makeText(getActivity(),
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(getActivity(), "服务器异常",
								Toast.LENGTH_SHORT).show();
						dissmissDilog();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "服务器异常", Toast.LENGTH_LONG)
						.show();
			}
		});
	}

	// 根据餐桌状态进行选择
	private void setTableStatesInfo(String className) {
		dateList.clear();
		tablesAdapter.notifyDataSetChanged();
		for (int i = 0; i < daList.size(); i++) {
			try {
				String area_id = daList.get(i).get("area_id").toString();
				String area_name = daList.get(i).get("area_name").toString();
				JSONArray array = (JSONArray) daList.get(i).get("tableinfo");

				for (int j = 0; j < array.length(); j++) {
					JSONObject object2 = array.getJSONObject(j);
					String table_name = object2.getString("table_name");// 餐桌名
					String table_status = object2.getString("table_status");// 餐桌状态
					String table_id = object2.getString("table_id");
					if (className.equals(table_status)) {
						HashMap<String, String> map4 = new HashMap<String, String>();
						map4.put("table_name", table_name);
						map4.put("table_status", table_status);
						map4.put("table_id", table_id);
						dateList.add(map4);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		gri_tables.setAdapter(tablesAdapter);
	}

	// 根据餐桌分区进行选择
	@SuppressLint("NewApi")
	private void setTableAirInfo(String className) {
		dateList.clear();
		tablesAdapter.notifyDataSetChanged();
		for (int i = 0; i < daList.size(); i++) {
			try {
				String area_id = daList.get(i).get("area_id").toString();
				String area_name = daList.get(i).get("area_name").toString();
				JSONArray array = (JSONArray) daList.get(i).get("tableinfo");
				if (className.equals(area_name)) {
					for (int j = 0; j < array.length(); j++) {
						JSONObject object2 = array.getJSONObject(j);
						String table_name = object2.getString("table_name");// 餐桌名
						String table_status = object2.getString("table_status");// 餐桌状态
						String table_id = object2.getString("table_id");
						HashMap<String, String> map4 = new HashMap<String, String>();
						map4.put("table_name", table_name);
						map4.put("table_status", table_status);
						map4.put("table_id", table_id);
						dateList.add(map4);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		gri_tables.setAdapter(tablesAdapter);
	}
}
