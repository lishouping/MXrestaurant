package com.mx.sy.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mx.sy.R;
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

	private TableInfoFragment tableInfoFragment;
	private ServiceFragment serviceFragment;
	private OrderFragment orderFragment;
	private MineFragment mineFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.fl_tableinfo:
			setChioceItem(1);
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
		
		fl_tableinfo.performClick();
	}

	// 定义一个点击处理事件
	private void setChioceItem(int index) {
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
		public static final String MESSAGE_RECEIVED_ACTION = "com.cpjw.bj.MESSAGE_RECEIVED_ACTION";
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
					//刷新通知列表
					//				setChioceItem(1);
					onResume();
					// 返回的时候需要刷新列表数据
					/*
					String messge = intent.getStringExtra(KEY_MESSAGE);
					String extras = intent.getStringExtra(KEY_EXTRAS);
					StringBuilder showMsg = new StringBuilder();
					showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
					if (!ExampleUtil.isEmpty(extras)) {
						showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
					}
					setCostomMsg(showMsg.toString());
					 */
				}
			}
		}

}
