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
import com.mx.sy.utils.CommonUtils;
import com.tnktech.weight.TNKListView;

/**
 * @author lishouping 订单修改
 */
public class OrderUpdateActivity extends BaseActivity {
	private LinearLayout ll_back, lin_jiucantype, lin_shangcaitype;
	private TextView tv_title;


	private TextView tv_table_num, tv_order_num, tv_jiucantype,
			tv_shangcaitype,tv_service_time;

	private EditText edit_bzi, edit_peoplenum;

	private SharedPreferences preferences;

	private Button btn_sub_order;
	
	private String way = "1";
	private String go_goods_way = "1";
	
	private String order_num;

	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		case R.id.btn_sub_order:
			if (edit_peoplenum.getText().toString().equals("")) {
				Toast.makeText(getApplicationContext(), "请输入用餐人数", Toast.LENGTH_SHORT).show();
			}else {
				showDilog("加载中");
				submitOrder();
			}
			break;
		case R.id.lin_jiucantype:
		AlertDialog.Builder builder = new AlertDialog.Builder(
				OrderUpdateActivity.this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("请选择");
		// 指定下拉列表的显示数据
		final String[] cities = { "堂食", "打包" };
		// 设置一个下拉的列表选择项
		builder.setItems(cities,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						if (which == 0) {
							tv_jiucantype.setText("堂食");
							way = "1";
						} else {
							tv_jiucantype.setText("打包");
							way = "2";
						}
					}
				});
		builder.show();
			break;
		case R.id.lin_shangcaitype:
			AlertDialog.Builder builder1 = new AlertDialog.Builder(
					OrderUpdateActivity.this);
			builder1.setIcon(R.drawable.ic_launcher);
			builder1.setTitle("请选择");
			// 指定下拉列表的显示数据
			final String[] cities1 = { "做好即上", "等待叫起" };
			// 设置一个下拉的列表选择项
			builder1.setItems(cities1,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							if (which == 0) {
								tv_shangcaitype.setText("做好即上");
								go_goods_way = "1";
							} else {
								tv_shangcaitype.setText("等待叫起");
								go_goods_way = "2";
							}
						}
					});
			builder1.show();
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
		return R.layout.activity_orderupdate;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		ll_back = $(R.id.ll_back);
		tv_title = $(R.id.tv_title);
		btn_sub_order = $(R.id.btn_sub_order);
		tv_table_num = $(R.id.tv_table_num);
		tv_order_num = $(R.id.tv_order_num);
		edit_bzi = $(R.id.edit_bzi);
		edit_peoplenum = $(R.id.edit_peoplenum);
		lin_jiucantype = $(R.id.lin_jiucantype);
		lin_shangcaitype = $(R.id.lin_shangcaitype);
		tv_jiucantype = $(R.id.tv_jiucantype);
		tv_shangcaitype = $(R.id.tv_shangcaitype);
		tv_service_time = $(R.id.tv_service_time);
	}

	@Override
	protected void initdata() {
		// TODO Auto-generated method stub
		tv_title.setText("订单详情");
		preferences = getSharedPreferences("userinfo",
				LoginActivity.MODE_PRIVATE);

	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		ll_back.setOnClickListener(this);
		btn_sub_order.setOnClickListener(this);
		lin_jiucantype.setOnClickListener(this);
		lin_shangcaitype.setOnClickListener(this);
	}

	@Override
	public void doBusiness(Context mContext) {
		// TODO Auto-generated method stub
		try {
		Intent intent = getIntent();
		String obj = intent.getStringExtra("obj");
		JSONObject object = new JSONObject(obj);
		String comments = object.getString("comments");
		String way1 = object.getString("way");
		way = way1;
		String go_goods_way1 = object.getString("go_goods_way");
		go_goods_way = go_goods_way1;
	    order_num = object.getString("order_num");
		JSONObject tabobj = new JSONObject(object
				.getString("table"));
		String table_num = tabobj.getString("table_name");
		String order_time = object.getString("create_time");
		tv_table_num.setText("桌号" + table_num);
		tv_order_num.setText("订单编号:" + order_num);
		tv_service_time.setText("创建时间:"
				+ CommonUtils.getStrTime(order_time));
		String people_count = object
				.getString("people_count");
		if (comments.equals("null")) {
			edit_bzi.setText("无");
		}else {
			edit_bzi.setText(comments);
		}
		if (way1.equals("1")) {
			tv_jiucantype.setText("堂食");
		}else if (way1.equals("2")) {
			tv_jiucantype.setText("打包");
		}
		if (go_goods_way1.equals("1")) {
			tv_shangcaitype.setText("做好即上");
		}else if (go_goods_way1.equals("2")) {
			tv_shangcaitype.setText("等待叫起");
		}
		
		edit_peoplenum.setText(people_count);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 服务员提交订单/order/saveOrder
	public void submitOrder() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.UPDATEORDERINFO;
		RequestParams params = new RequestParams();
		params.put("order_num", order_num);
		params.put("comments", edit_bzi.getText().toString());
		params.put("people_count", edit_peoplenum.getText().toString());
		params.put("way", way);
		params.put("go_goods_way", go_goods_way);
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
