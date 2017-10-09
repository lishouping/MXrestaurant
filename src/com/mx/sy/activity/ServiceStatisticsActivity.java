package com.mx.sy.activity;

import java.util.Calendar;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mx.sy.R;
import com.mx.sy.api.ApiConfig;
import com.mx.sy.base.BaseActivity;

/**
* <p>Title: AboutUsActivity<／p>
* <p>Description: 服务统计<／p>
* <p>Company: LTGames<／p> 
* @author lishp
* @date 2017年7月23日
 */
public class ServiceStatisticsActivity extends BaseActivity{
	private LinearLayout ll_back;
	private TextView tv_title;
	private SharedPreferences preferences;
	
	private TextView tv_service_num,tv_order_num,tv_jiezhangorder_num;
	
	private Button btn_month,btn_week,btn_day,btn_time,btn_start_time,btn_end_time;
	
	private String time_flag = "3"; 
	private String start_time = "";
	private String end_time = "";
	
	

	  private final static int DATE_DIALOG = 0;
	    private final static int TIME_DIALOG = 1;
	    private Calendar c = null;
	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		case R.id.btn_month:
			time_flag = "3";
			break;
		case R.id.btn_week:
			time_flag = "2";
			break;
		case R.id.btn_day:
			time_flag = "1";
			break;
		case R.id.btn_time:
			
			break;
		case R.id.btn_start_time:
			onCreateDialog(DATE_DIALOG).show();
			break;
		case R.id.btn_end_time:
			onCreateDialog(TIME_DIALOG).show();
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
		return R.layout.activity_servicestatistics;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		ll_back = $(R.id.ll_back);
		tv_title = $(R.id.tv_title);
		tv_service_num = $(R.id.tv_service_num);
		tv_order_num = $(R.id.tv_order_num);
		tv_jiezhangorder_num = $(R.id.tv_jiezhangorder_num);
		
		btn_month = $(R.id.btn_month);
		btn_week = $(R.id.btn_week);
		btn_day = $(R.id.btn_day);
		btn_time = $(R.id.btn_time);
		

		btn_start_time = $(R.id.btn_start_time);
		btn_end_time = $(R.id.btn_end_time);
	}

	@Override
	protected void initdata() {
		// TODO Auto-generated method stub
		tv_title.setText("服务统计");
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		ll_back.setOnClickListener(this);
		
		btn_month.setOnClickListener(this);
		btn_week.setOnClickListener(this);
		btn_day.setOnClickListener(this);
		btn_time.setOnClickListener(this);
		
		btn_start_time.setOnClickListener(this);
		btn_end_time.setOnClickListener(this);
	}

	@Override
	public void doBusiness(Context mContext) {
		// TODO Auto-generated method stub
		getServiceStatics();
	}
	
	// 服务数量统计
			public void getServiceStatics() {
				AsyncHttpClient client = new AsyncHttpClient();
				client.addHeader("key", preferences.getString("loginkey", ""));
				client.addHeader("id", preferences.getString("userid", ""));
				String url = ApiConfig.API_URL + ApiConfig.SERVICESTSTICS;
				RequestParams params = new RequestParams();
				params.put("shop_id", preferences.getString("shop_id", ""));
				params.put("time_flag", time_flag);
				params.put("start_time",start_time);
				params.put("end_time", end_time);
				client.post(url,params, new AsyncHttpResponseHandler() {

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
										JSONObject object = jsonArray.getJSONObject(0);
										String SERVICE_COUNT = object
												.getString("SERVICE_COUNT");
										JSONObject object1 = jsonArray.getJSONObject(1);
										String ORDER_COUNT = object1
												.getString("ORDER_COUNT");
										JSONObject object2 = jsonArray.getJSONObject(2);
										String ORDER_CHECK_COUNT = object2
												.getString("ORDER_CHECK_COUNT");
										
										tv_service_num.setText(SERVICE_COUNT);
										tv_order_num.setText(ORDER_COUNT);
										tv_jiezhangorder_num.setText(ORDER_CHECK_COUNT);
									}
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
								Toast.LENGTH_SHORT).show();
					}
				});
			}
			

			 /**
		     * 创建日期及时间选择对话框
		     */
		    @Override
		    protected Dialog onCreateDialog(int id) {
		        Dialog dialog = null;
		        switch (id) {
		        case DATE_DIALOG:
		            c = Calendar.getInstance();
		            dialog = new DatePickerDialog(
		                this,
		                new DatePickerDialog.OnDateSetListener() {
		                    public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
		                    	start_time = year+"-"+month+"-"+dayOfMonth;
		                    	btn_start_time.setText(start_time);
		                    }
		                }, 
		                c.get(Calendar.YEAR), // 传入年份
		                c.get(Calendar.MONTH), // 传入月份
		                c.get(Calendar.DAY_OF_MONTH) // 传入天数
		            );
		            break;
		        case TIME_DIALOG:
		        	 c = Calendar.getInstance();
			            dialog = new DatePickerDialog(
			                this,
			                new DatePickerDialog.OnDateSetListener() {
			                    public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
			                    	end_time = year+"-"+month+"-"+dayOfMonth;
			                    	btn_end_time.setText(end_time);
			                    }
			                }, 
			                c.get(Calendar.YEAR), // 传入年份
			                c.get(Calendar.MONTH), // 传入月份
			                c.get(Calendar.DAY_OF_MONTH) // 传入天数
			            );
		            break;
		        }
		        return dialog;
		    }

}
