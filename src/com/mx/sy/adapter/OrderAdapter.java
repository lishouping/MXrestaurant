package com.mx.sy.adapter;

import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
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
import com.mx.sy.activity.FoodCustomActivity;
import com.mx.sy.activity.LoginActivity;
import com.mx.sy.activity.OrderConductActivity;
import com.mx.sy.activity.PayImagesActivity;
import com.mx.sy.activity.PrintActivity;
import com.mx.sy.api.ApiConfig;
import com.mx.sy.base.CommonBaseAdapter;
import com.mx.sy.base.CommonViewHolder;
import com.mx.sy.dialog.PrintOrderDialog;
import com.mx.sy.dialog.SweetAlertDialog;
import com.mx.sy.utils.CommonUtils;
import com.mx.sy.utils.SendMessage;

/**
 * <p>
 * Title: OrderAdapter<／p>
 * <p>
 * Description: <／p>
 * <p>
 * Company: LTGames<／p>
 * 
 * @author lishp
 * @date 2017年4月2日
 */
public class OrderAdapter extends CommonBaseAdapter<HashMap<String, String>> {
	private Context context;
	private List<HashMap<String, String>> dateList;
	private int itemID;
	private SharedPreferences preferences;

	public OrderAdapter(Context context, List<HashMap<String, String>> datas,
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
		if (itemID == R.layout.item_order_untreated) {

			holder.setText(R.id.tv_order_num, "订单编号:" + bean.get("order_num"));
			holder.setText(R.id.tv_table_num, "桌号:" + bean.get("table_name"));
			holder.setText(R.id.tv_havemeals,
					"用餐人数:" + bean.get("people_count"));
			holder.setText(R.id.tv_push_time,
					"创建时间:" + CommonUtils.getStrTime(bean.get("order_time")));

			holder.getView(R.id.lv_placeorder).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							new SweetAlertDialog(context,
									SweetAlertDialog.NORMAL_TYPE)
									.setTitleText("确定要提交订单吗？")
									// .setContentText("Won't be able to recover this file!")
									.setCancelText("取消")
									.setConfirmText("确定")
									.showCancelButton(true)
									.setConfirmClickListener(
											new SweetAlertDialog.OnSweetClickListener() {
												@Override
												public void onClick(
														SweetAlertDialog sDialog) {
													sDialog.cancel();
													submitOrder(
															bean.get("order_id"),
															holder.getPosition());
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
		} else if (itemID == R.layout.item_order_havingdinner) {

			holder.setText(R.id.tv_order_num, "订单编号:" + bean.get("order_num"));
			holder.setText(R.id.tv_table_num, "桌号:" + bean.get("table_name"));
			holder.setText(R.id.tv_havemeals,
					"用餐人数:" + bean.get("people_count"));
			holder.setText(R.id.tv_order_time,
					"创建时间:" + CommonUtils.getStrTime(bean.get("order_time")));
			holder.setText(R.id.tv_havingdinner, "服务人员:" + bean.get("name"));

			holder.getView(R.id.lv_addfood).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(context,
									FoodCustomActivity.class);
							intent.putExtra("table_id", bean.get("table_id"));
							intent.putExtra("table_name",
									bean.get("table_name"));
							context.startActivity(intent);
						}
					});
			// 结账
			holder.getView(R.id.lv_placeorder).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							check(bean.get("order_id"), holder.getPosition());
						}
					});

		} else if (itemID == R.layout.item_order_com) {
			holder.setText(R.id.tv_order_num, "订单编号:" + bean.get("order_num"));
			holder.setText(R.id.tv_table_num, "桌号:" + bean.get("table_name"));
			holder.setText(R.id.tv_havemeals,
					"用餐人数:" + bean.get("people_count"));
			holder.setText(R.id.tv_order_time,
					"创建时间:" + CommonUtils.getStrTime(bean.get("order_time")));
			holder.setText(R.id.tv_havingdinner, "服务人员:" + bean.get("name"));
			holder.getView(R.id.lv_printorder).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							new SweetAlertDialog(context,
									SweetAlertDialog.NORMAL_TYPE)
									.setTitleText("选择打印方式!")
									// .setContentText("Won't be able to recover this file!")
									.setCancelText("网络")
									.setConfirmText("蓝牙")
									.showCancelButton(true)
									.setConfirmClickListener(
											new SweetAlertDialog.OnSweetClickListener() {
												@Override
												public void onClick(
														SweetAlertDialog sDialog) {
													sDialog.cancel();
													Intent intent = new Intent(context,
															PrintActivity.class);
													intent.putExtra("order_num", bean.get("order_num"));
													context.startActivity(intent);
												}
											})
									.setCancelClickListener(
											new SweetAlertDialog.OnSweetClickListener() {
												@Override
												public void onClick(
														SweetAlertDialog sDialog) {
													sDialog.cancel();
													getPrintContentByOrder(bean
															.get("order_id"), 0);
												}
											}).show();
						}
					});
		}
	}

	// 服务员确认顾客订单
	public void submitOrder(String order_id, final int pos) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.CONFIRMORDER;
		RequestParams params = new RequestParams();
		params.put("waiter_id", preferences.getString("business_id", ""));
		params.put("order_id", order_id);
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
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
							dateList.remove(pos);
							notifyDataSetChanged();
						} else {
							Toast.makeText(context,
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(context, "服务器异常", Toast.LENGTH_SHORT)
								.show();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "服务器异常", Toast.LENGTH_LONG).show();
			}
		});
	}

	// 结账/order/check
	public void check(String orderid, final int pos) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.CHECK_URL;
		RequestParams params = new RequestParams();
		params.put("order_id", orderid);
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
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
							dateList.remove(pos);
							notifyDataSetChanged();
							Intent intent = new Intent(context,
									PayImagesActivity.class);
							context.startActivity(intent);
						} else {
							Toast.makeText(context,
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(context, "服务器异常", Toast.LENGTH_SHORT)
								.show();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "服务器异常", Toast.LENGTH_LONG).show();
			}
		});
	}

	// 根据订单ID查询打印内容
	public void getPrintContentByOrder(final String order_id, final int select) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.PRINTBYORDER;
		RequestParams params = new RequestParams();
		params.put("order_id", order_id);
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
							String DATA = jsonObject.getString("DATA");
							if (select == 0) {
								Intent intent = new Intent(
										context,
										PrintOrderDialog.class);
								intent.putExtra("DATA", DATA);
								context.startActivity(intent);
							} else if (select == 1) {
								Intent intent = new Intent(context,
										PrintActivity.class);
								intent.putExtra("DATA", DATA);
								context.startActivity(intent);
							}
						} else {
							Toast.makeText(context,
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(context, "服务器异常", Toast.LENGTH_SHORT)
								.show();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "服务器异常",
						Toast.LENGTH_LONG).show();
			}
		});
	}

}
