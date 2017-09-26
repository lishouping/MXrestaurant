package com.mx.sy.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mx.sy.R;
import com.mx.sy.base.BaseActivity;

/**
* <p>Title: AboutUsActivity<／p>
* <p>Description: 关于我们<／p>
* <p>Company: LTGames<／p> 
* @author lishp
* @date 2017年7月23日
 */
public class AboutUsActivity extends BaseActivity{
	private LinearLayout ll_back;
	private TextView tv_title;
	private TextView tv_callphone;
	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		case R.id.tv_callphone:
			 Intent intent = new Intent(); 
			   //把打电话的动作ACTION_CALL封装至意图对象当中 
			   intent.setAction(Intent.ACTION_CALL); 
			   //设置打给谁 
			   intent.setData(Uri.parse("tel:" + "024-31128174"));//这个tel：必须要加上，表示我要打电话。否则不会有打电话功能，由于在打电话清单文件里设置了这个“协议” 
			   //把动作告诉系统,启动系统打电话功能。 
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
		return R.layout.activity_aboutus;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		ll_back = $(R.id.ll_back);
		tv_title = $(R.id.tv_title);
		tv_callphone = $(R.id.tv_callphone);
	}

	@Override
	protected void initdata() {
		// TODO Auto-generated method stub
		tv_title.setText("关于我们");
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		ll_back.setOnClickListener(this);
		tv_callphone.setOnClickListener(this);
	}

	@Override
	public void doBusiness(Context mContext) {
		// TODO Auto-generated method stub
		
	}

}
