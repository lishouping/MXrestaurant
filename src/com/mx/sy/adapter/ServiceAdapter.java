package com.mx.sy.adapter;

import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mx.sy.R;
import com.mx.sy.activity.LoginActivity;
import com.mx.sy.activity.MainActivity;
import com.mx.sy.api.ApiConfig;
import com.mx.sy.base.CommonBaseAdapter;
import com.mx.sy.base.CommonViewHolder;
import com.mx.sy.dialog.SweetAlertDialog;
import com.mx.sy.utils.CommonUtils;
import com.mx.sy.utils.SendMessage;

// 服务列表adapter
public class ServiceAdapter extends CommonBaseAdapter<HashMap<String, String>> {
	private Context context;
	private List<HashMap<String, String>> dateList;
	private int itemID;
	private SharedPreferences preferences;
	public ServiceAdapter(Context context, List<HashMap<String, String>> datas,
			int itemID) {
		super(context, datas, itemID);
		// TODO Auto-generated constructor stub
		this.dateList = datas;
		this.context = context;
		this.itemID = itemID;

		preferences = context.getSharedPreferences("userinfo",
				LoginActivity.MODE_PRIVATE);
	}

	@Override
	public void convert(final CommonViewHolder holder,
			final HashMap<String, String> bean) {
		// TODO Auto-generated method stub
		if (itemID == R.layout.item_servicemanage) {
			

			holder.setText(R.id.tv_table_num, "桌号:" + bean.get("table_name"));
			holder.setText(R.id.tv_service_time,
					"创建时间:" + CommonUtils.getStrTime(bean.get("create_time")));
			holder.setText(R.id.tv_service_name,
					"服务内容:" + bean.get("service_content"));
			String status = bean.get("status");
			if (status.equals("0")) {
				holder.setText(R.id.tv_service_state, "状态:未处理");
			} else if (status.equals("1")) {
				holder.setText(R.id.tv_service_state, "状态:已处理");
			} else if (status.equals("2")) {
				holder.setText(R.id.tv_service_state, "状态:已取消");
			}
			holder.getView(R.id.lin_manage).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							new SweetAlertDialog(context,
									SweetAlertDialog.NORMAL_TYPE)
									.setTitleText("确定要处理以下服务吗?")
								    .setContentText(bean.get("service_content"))
									.setCancelText("取消")
									.setConfirmText("确定")
									.showCancelButton(true)
									.setConfirmClickListener(
											new SweetAlertDialog.OnSweetClickListener() {
												@Override
												public void onClick(
														SweetAlertDialog sweetAlertDialog) {
													todoSertice(holder,bean.get("service_id"));
													sweetAlertDialog.cancel();
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
						}
					});
		} else if (itemID == R.layout.item_serviceprocess) {
			holder.setText(R.id.tv_table_num, "桌号:" + bean.get("table_name"));
			holder.setText(R.id.tv_service_time,
					"创建时间:" + CommonUtils.getStrTime(bean.get("create_time")));
			holder.setText(R.id.tv_proservice_time,
					"服务时间:" + CommonUtils.getStrTime(bean.get("receive_time")));
			holder.setText(R.id.tv_service_name,
					"服务内容:" + bean.get("service_content"));
			holder.setText(R.id.tv_service_us, "服务人:" + bean.get("name"));
			String status = bean.get("status");
			if (status.equals("0")) {
				holder.setText(R.id.tv_service_state, "状态:未处理");
			} else if (status.equals("1")) {
				holder.setText(R.id.tv_service_state, "状态:已处理");
			} else if (status.equals("2")) {
				holder.setText(R.id.tv_service_state, "状态:已取消");
			}
		}
	}

	// 获取Service
	public void todoSertice(final CommonViewHolder holder,final String service_id) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.TODOSERVICE;
		RequestParams params = new RequestParams();
		params.put("service_id", service_id);
		params.put("waiter_id", preferences.getString("business_id", ""));
		params.put("status", "1");
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
							Toast.makeText(context,
									"处理成功",
									Toast.LENGTH_SHORT).show();
							dateList.remove(holder.getPosition());
							notifyDataSetChanged();
							Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
							context.sendBroadcast(msgIntent);
						} else {
							Toast.makeText(context,
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(context, "服务器异常",
								Toast.LENGTH_SHORT).show();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "服务器异常", Toast.LENGTH_LONG)
						.show();
			}
		});
	}
}
