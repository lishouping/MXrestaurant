package com.mx.sy.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mx.sy.R;
import com.mx.sy.adapter.SalesStaticsAdapter;
import com.mx.sy.adapter.ServiceAdapter;
import com.mx.sy.api.ApiConfig;
import com.mx.sy.base.BaseActivity;

/**
* <p>Title: AboutUsActivity<／p>
* <p>Description:销售统计<／p>
* <p>Company: LTGames<／p> 
* @author lishp
* @date 2017年7月23日
 */
public class SalesStatisticsActivity extends BaseActivity{
	private LinearLayout ll_back,lin_food_static,lin_shop_static;
	private TextView tv_title;
	
	private TextView tv_shop_totalnum,tv_shop_totalprice;
	
	private LinearLayout lin_nomanage, lin_processed;
	private TextView tv_nomanage, tv_processed;
	private View viw_nomanage, viw_processed;
	
	private Button btn_month,btn_week,btn_day,btn_time,btn_start_time,btn_end_time;
	
	private int selectBtnFlag = 0;
	
	private SharedPreferences preferences;
	private List<HashMap<String, String>> dateList;
	private SalesStaticsAdapter salesStaticsAdapter;
	
	private ListView lv_food_static;
	
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
		case R.id.lin_nomanage:
			selectBtnFlag = 0;
			changeBtnBg(selectBtnFlag);
			break;
		case R.id.lin_processed:
			selectBtnFlag = 1;
			changeBtnBg(selectBtnFlag);
			break;
		case R.id.btn_month:
			time_flag = "3";
			selectgoodstatic();
			selectshopgoodstatic();
			break;
		case R.id.btn_week:
			time_flag = "2";
			selectgoodstatic();
			selectshopgoodstatic();
			break;
		case R.id.btn_day:
			time_flag = "1";
			selectgoodstatic();
			selectshopgoodstatic();
			break;
		case R.id.btn_time:
			time_flag = "3";
			selectgoodstatic();
			selectshopgoodstatic();
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
		
	}

	@Override
	public View bindView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_salesstatistics;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		ll_back = $(R.id.ll_back);
		tv_title = $(R.id.tv_title);
		lv_food_static = $(R.id.lv_food_static);
		lin_shop_static = $(R.id.lin_shop_static);
		
		lin_food_static = $(R.id.lin_food_static);
		
		lin_nomanage = $(R.id.lin_nomanage);
		lin_processed = $(R.id.lin_processed);
		
		tv_nomanage = $(R.id.tv_nomanage);
		tv_processed = $(R.id.tv_processed);
		
		viw_nomanage = $(R.id.viw_nomanage);
		viw_processed = $(R.id.viw_processed);
		
		tv_shop_totalnum = $(R.id.tv_shop_totalnum);
		tv_shop_totalprice = $(R.id.tv_shop_totalprice);
		
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
		tv_title.setText("销售统计");
		preferences = getSharedPreferences("userinfo",
				LoginActivity.MODE_PRIVATE);
		dateList = new ArrayList<HashMap<String,String>>();
		salesStaticsAdapter = new SalesStaticsAdapter(getApplicationContext(), dateList, R.layout.item_salesstatics);
		
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		ll_back.setOnClickListener(this);
		lin_processed.setOnClickListener(this);
		lin_nomanage.setOnClickListener(this);
		
		btn_month.setOnClickListener(this);
		btn_week.setOnClickListener(this);
		btn_day.setOnClickListener(this);
		btn_time.setOnClickListener(this);
		
		btn_start_time.setOnClickListener(this);
		btn_end_time.setOnClickListener(this);
	}
	private void changeBtnBg(int selectTag) {
		switch (selectTag) {
		case 0:
			tv_nomanage.setTextColor(Color.rgb(79, 145, 244));
			viw_nomanage.setBackgroundResource(R.color.main_bg_color);
			
			tv_processed.setTextColor(Color.rgb(0, 0, 0));
			viw_processed.setBackgroundResource(R.color.sweet_dialog_bg_color);
			
			lin_food_static.setVisibility(View.VISIBLE);
			lin_shop_static.setVisibility(View.GONE);
			break;
		case 1:
			tv_nomanage.setTextColor(Color.rgb(0, 0, 0));
			viw_nomanage.setBackgroundResource(R.color.sweet_dialog_bg_color);
			
			tv_processed.setTextColor(Color.rgb(79, 145, 244));
			viw_processed.setBackgroundResource(R.color.main_bg_color);
			
			lin_food_static.setVisibility(View.GONE);
			lin_shop_static.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}
	@Override
	public void doBusiness(Context mContext) {
		// TODO Auto-generated method stub
		selectgoodstatic();
		selectshopgoodstatic();
	}
	// 获取菜品销量统计
	public void selectgoodstatic() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.GOODSSTATICS;
		RequestParams params = new RequestParams();
		params.put("shop_id", preferences.getString("shop_id", ""));
		params.put("time_flag", time_flag);
		params.put("start_time",start_time);
		params.put("end_time", end_time);
		client.post(url, params,new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				if (arg0 == 200) {
					try {
						String response = new String(arg2, "UTF-8");
						JSONObject jsonObject = new JSONObject(response);
						String CODE = jsonObject.getString("CODE");
						if (CODE.equals("1000")) {
							dissmissDilog();
							JSONArray jsonArray = new JSONArray(jsonObject
									.getString("DATA"));
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject object = jsonArray.getJSONObject(i);
								String GOOD_NAME = object
										.getString("GOOD_NAME");
								String zs = object
										.getString("zs");
								String ze = object
										.getString("ze");
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("good_name", GOOD_NAME);
								map.put("good_zs", zs);//销售数量
								map.put("good_ze", ze);//销售金额
								dateList.add(map);
							}
							lv_food_static.setAdapter(salesStaticsAdapter);
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
	// 获取菜品销量统计
		public void selectshopgoodstatic() {
			AsyncHttpClient client = new AsyncHttpClient();
			client.addHeader("key", preferences.getString("loginkey", ""));
			client.addHeader("id", preferences.getString("userid", ""));
			String url = ApiConfig.API_URL + ApiConfig.SHOPSTATIS;
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
								dissmissDilog();
								JSONArray jsonArray = new JSONArray(jsonObject
										.getString("DATA"));
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject object = jsonArray.getJSONObject(i);
									String zs = object
											.getString("zs");
									String ze = object
											.getString("ze");
									
									tv_shop_totalnum.setText(zs);
									tv_shop_totalprice.setText(ze);
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
	                    	int man = month+1;
	                    	start_time = year+"-"+man+"-"+dayOfMonth;
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
		                    	int man = month+1;
		                    	end_time = year+"-"+man+"-"+dayOfMonth;
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
