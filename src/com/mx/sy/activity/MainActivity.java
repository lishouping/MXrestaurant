package com.mx.sy.activity;

import android.content.Intent;
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
import com.mx.sy.fragment.MineFragment;
import com.mx.sy.fragment.OrderFragment;
import com.mx.sy.fragment.ServiceFragment;
import com.mx.sy.fragment.TableInfoFragment;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private LinearLayout ll_back;
	private TextView tv_title;

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
			Intent intent = new Intent(getApplicationContext(),QrCodeActivity.class);
			startActivity(intent);
			break;
		case R.id.fl_order:
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
			// tv_tableinfo.setTextColor(getApplication().getResources().getColor(R.color.common_title_bg));
			// iv_notice.setImageResource(R.drawable.ic_launcher);
			tableInfoFragment = new TableInfoFragment();
			ft.replace(R.id.fragment, tableInfoFragment);
			break;
		case 2:
			// tv_meeting.setTextColor(getApplication().getResources().getColor(R.color.common_title_bg));
			// iv_meeting.setImageResource(R.drawable.ic_launcher);
			serviceFragment = new ServiceFragment();
			ft.replace(R.id.fragment, serviceFragment);

			break;
		case 3:
			// tv_survey.setTextColor(getApplication().getResources().getColor(R.color.common_title_bg));
			// iv_survey.setImageResource(R.drawable.ic_launcher);
			orderFragment = new OrderFragment();
			ft.replace(R.id.fragment, orderFragment);

			break;
		case 4:
			// tv_information.setTextColor(getApplication().getResources().getColor(R.color.common_title_bg));
			// iv_information.setImageResource(R.drawable.ic_launcher);
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
		// tv_notice.setTextColor(getApplication().getResources().getColor(R.color.common_text_gray_bg));
		// tv_meeting.setTextColor(getApplication().getResources().getColor(R.color.common_text_gray_bg));
		// tv_survey.setTextColor(getApplication().getResources().getColor(R.color.common_text_gray_bg));
		// tv_information.setTextColor(getApplication().getResources().getColor(R.color.common_text_gray_bg));
		// iv_notice.setImageResource(R.drawable.cp_notice_not_press);
		// iv_meeting.setImageResource(R.drawable.cp_meeting_not_press);
		// iv_survey.setImageResource(R.drawable.cp_survey_not_press);
		// iv_information.setImageResource(R.drawable.cp_information_not_press);
	}

}
