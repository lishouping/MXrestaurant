package com.mx.sy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mx.sy.R;
import com.mx.sy.base.BaseActivity;

/**
* <p>Title: AboutUsActivity<／p>
* <p>Description: 打印机设置<／p>
* <p>Company: LTGames<／p> 
* @author lishp
* @date 2017年7月23日
 */
public class PrinterSeetingActivity extends BaseActivity{
	private LinearLayout ll_back;
	private TextView tv_title;
	private Button btn_print_test;
	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		case R.id.btn_print_test:
			Intent intent = new Intent(getApplicationContext(),PrintActivity.class);
			startActivity(intent);
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
		return R.layout.activity_printerseeting;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		ll_back = $(R.id.ll_back);
		tv_title = $(R.id.tv_title);
		btn_print_test = $(R.id.btn_print_test);
	}

	@Override
	protected void initdata() {
		// TODO Auto-generated method stub
		tv_title.setText("打印机设置");
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		ll_back.setOnClickListener(this);
	}

	@Override
	public void doBusiness(Context mContext) {
		// TODO Auto-generated method stub
		
	}
}
