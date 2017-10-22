package com.mx.sy.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mx.sy.R;
import com.mx.sy.api.ApiConfig;
import com.mx.sy.common.PullToRefreshView;
import com.mx.sy.fragment.MineFragment;
import com.mx.sy.fragment.OrderFragment;
import com.mx.sy.fragment.ServiceFragment;
import com.mx.sy.fragment.TableInfoFragment;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private LinearLayout ll_back;
	private TextView tv_title;
	
	private TextView tv_tableinfo,tv_service,tv_order,tv_mine;

	private FrameLayout fragment, fl_tableinfo, fl_service, fl_saoma, fl_order,
			fl_mine;
	private ImageView iv_tableinfo, iv_service, iv_saoma, iv_order, iv_mine;
	private LinearLayout ll_tableinfo_number, ll_service_number,
			ll_saoma_number, ll_order_number, ll_mine;
	private TextView tv_tableinfo_number, tv_service_number, tv_saoma_number,
			tv_order_number, tv_mine_number;
	
	private LinearLayout ll_right;

	private TableInfoFragment tableInfoFragment;
	private ServiceFragment serviceFragment;
	private OrderFragment orderFragment;
	private MineFragment mineFragment;
	
	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		preferences = getSharedPreferences("userinfo",
				getApplicationContext().MODE_PRIVATE);
		registerMessageReceiver();
		
		initView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.fl_tableinfo:
			setChioceItem(1);
			PullToRefreshView.ishidfootview = 1;
			break;
		case R.id.fl_service:
			setChioceItem(2);
			break;
		case R.id.fl_saoma:
			Intent intent = new Intent(getApplicationContext(),MipcaActivityCapture.class);
			startActivity(intent);
			break;
		case R.id.fl_order:
			PullToRefreshView.ishidfootview = 0;
			setChioceItem(3);
			break;
		case R.id.fl_mine:
			setChioceItem(4);
			break;
		case R.id.ll_right:
			Intent intent2 = new Intent(getApplicationContext(),PayImagesActivity.class);
			startActivity(intent2);
			break;
		default:
			break;
		}
	}
	

	public void initView() {
		// TODO Auto-generated method stub
		ll_back = (LinearLayout) findViewById(R.id.ll_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		ll_back.setVisibility(View.INVISIBLE);
		tv_title.setText(R.string.text_tv_title);

		fragment = (FrameLayout) findViewById(R.id.fragment);

		fl_tableinfo = (FrameLayout) findViewById(R.id.fl_tableinfo);
		fl_service = (FrameLayout) findViewById(R.id.fl_service);
		fl_saoma = (FrameLayout) findViewById(R.id.fl_saoma);
		fl_order = (FrameLayout) findViewById(R.id.fl_order);
		fl_mine = (FrameLayout) findViewById(R.id.fl_mine);
		
		tv_tableinfo = (TextView) findViewById(R.id.tv_tableinfo);
		tv_service = (TextView) findViewById(R.id.tv_service);
		tv_order = (TextView) findViewById(R.id.tv_order);
		tv_mine = (TextView) findViewById(R.id.tv_mine);

		fl_tableinfo.setOnClickListener(this);
		fl_service.setOnClickListener(this);
		fl_saoma.setOnClickListener(this);
		fl_order.setOnClickListener(this);
		fl_mine.setOnClickListener(this);

		iv_tableinfo = (ImageView) findViewById(R.id.iv_tableinfo);
		iv_service = (ImageView) findViewById(R.id.iv_service);
		iv_saoma = (ImageView) findViewById(R.id.iv_saoma);
		iv_order = (ImageView) findViewById(R.id.iv_order);
		iv_mine = (ImageView) findViewById(R.id.iv_mine);

		ll_tableinfo_number = (LinearLayout) findViewById(R.id.ll_tableinfo_number);
		ll_service_number = (LinearLayout) findViewById(R.id.ll_service_number);
		ll_saoma_number = (LinearLayout) findViewById(R.id.ll_saoma_number);
		ll_order_number = (LinearLayout) findViewById(R.id.ll_order_number);
		ll_mine = (LinearLayout) findViewById(R.id.ll_mine);

		tv_tableinfo_number = (TextView) findViewById(R.id.tv_tableinfo_number);
		tv_service_number = (TextView) findViewById(R.id.tv_service_number);
		tv_saoma_number = (TextView) findViewById(R.id.tv_saoma_number);
		tv_order_number = (TextView) findViewById(R.id.tv_order_number);
		tv_mine_number = (TextView) findViewById(R.id.tv_mine_number);
		
		ll_right = (LinearLayout) findViewById(R.id.ll_right);
		ll_right.setVisibility(View.VISIBLE);
		ll_right.setOnClickListener(this);
		
		fl_tableinfo.performClick();
	}

	// 定义一个点击处理事件
	private void setChioceItem(int index) {
		onResume();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		clearChioce();
		switch (index) {
		case 1:
			tv_tableinfo.setTextColor(getApplication().getResources().getColor(R.color.tab_bg_color));
			iv_tableinfo.setImageResource(R.drawable.tabbar1_cur);
			tableInfoFragment = new TableInfoFragment();
			ft.replace(R.id.fragment, tableInfoFragment);
			break;
		case 2:
			 tv_service.setTextColor(getApplication().getResources().getColor(R.color.tab_bg_color));
			iv_service.setImageResource(R.drawable.tabbar2_cur);
			serviceFragment = new ServiceFragment();
			ft.replace(R.id.fragment, serviceFragment);

			break;
		case 3:
			tv_order.setTextColor(getApplication().getResources().getColor(R.color.tab_bg_color));
			iv_order.setImageResource(R.drawable.tabbar4_cur);
			orderFragment = new OrderFragment();
			ft.replace(R.id.fragment, orderFragment);

			break;
		case 4:
		    tv_mine.setTextColor(getApplication().getResources().getColor(R.color.tab_bg_color));
			iv_mine.setImageResource(R.drawable.tabbar5_cur);
			mineFragment = new MineFragment();
			ft.replace(R.id.fragment, mineFragment);
			break;

		default:
			break;
		}
		ft.commit();
	}

	// 重置所有fragment
	private void clearChioce() {
		 tv_tableinfo.setTextColor(getApplication().getResources().getColor(R.color.text_color));
		 tv_service.setTextColor(getApplication().getResources().getColor(R.color.text_color));
		 tv_order.setTextColor(getApplication().getResources().getColor(R.color.text_color));
		 tv_mine.setTextColor(getApplication().getResources().getColor(R.color.text_color));
		 iv_tableinfo.setImageResource(R.drawable.tabbar1);
		 iv_service.setImageResource(R.drawable.tabbar2);
		 iv_order.setImageResource(R.drawable.tabbar4);
		 iv_mine.setImageResource(R.drawable.tabbar5);
	}
	
	//接受广播刷新页面
		// for receive customer msg from jpush server
		private MessageReceiver mMessageReceiver;
		public static final String MESSAGE_RECEIVED_ACTION = "com.mx.sy.MESSAGE_RECEIVED_ACTION";
		public static final String KEY_TITLE = "title";
		public static final String KEY_MESSAGE = "message";
		public static final String KEY_EXTRAS = "extras";


		public void registerMessageReceiver() {
			mMessageReceiver = new MessageReceiver();
			IntentFilter filter = new IntentFilter();
			filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
			filter.addAction(MESSAGE_RECEIVED_ACTION);
			registerReceiver(mMessageReceiver, filter);
		}

		public class MessageReceiver extends BroadcastReceiver {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
					onResume();
				}
			}
		}
		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			getnumber();
			super.onResume();
		}
		private long exitTime = 0;
		// 返回键按下时会被调用  
		public boolean onKeyDown(int keyCode, KeyEvent event) {  
			if (keyCode == KeyEvent.KEYCODE_BACK
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				// ToDo
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
					exitTime = System.currentTimeMillis();
				} else {
					finish();
				}
				return true;
			}
			return false;
		}
		
		private void getnumber() {
			// 用户登录
			AsyncHttpClient client = new AsyncHttpClient();
			client.addHeader("key", preferences.getString("loginkey", ""));
			client.addHeader("id", preferences.getString("userid", ""));
			String url = ApiConfig.API_URL + ApiConfig.GETNOREADNUMBER;
			RequestParams params = new RequestParams();
			params.put("shop_id", preferences.getString("shop_id", ""));
			client.post(url, params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					if (arg0 == 200) {
						try {
							String response = new String(arg2, "UTF-8");
							JSONObject jsonObject = new JSONObject(response);
							String CODE = jsonObject.getString("CODE");
							if (CODE.equals("1000")) {
								String ORDER_COUNT = jsonObject.getString("ORDER_COUNT");
								String SERVICE_COUNT = jsonObject.getString("SERVICE_COUNT");
								
								tv_order_number.setText(ORDER_COUNT);
								tv_service_number.setText(SERVICE_COUNT);
								
								if (ORDER_COUNT.equals("0")) {
									ll_order_number.setVisibility(View.GONE);
								}else {
									ll_order_number.setVisibility(View.VISIBLE);
								}
								
								if (SERVICE_COUNT.equals("0")) {
									ll_service_number.setVisibility(View.GONE);
								}else {
									ll_service_number.setVisibility(View.VISIBLE);
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
						}
					}

				}

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					// TODO Auto-generated method stub
					Log.i("出错了", arg3 + "");
					Toast.makeText(getApplicationContext(), "服务器异常",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
}
