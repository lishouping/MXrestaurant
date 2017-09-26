package com.mx.sy.activity;

import java.util.Set;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mx.sy.R;
import com.mx.sy.api.ApiConfig;
import com.mx.sy.base.BaseActivity;
import com.mx.sy.dialog.SweetAlertDialog;
import com.mx.sy.fragment.MineFragment;
import com.mx.sy.service.PendingRemindedService;

/**
 * <p>
 * Title: AboutUsActivity<／p>
 * <p>
 * Description: 个人信息<／p>
 * <p>
 * Company: LTGames<／p>
 * 
 * @author lishp
 * @date 2017年7月23日
 */
public class UserInfoActivity extends BaseActivity {
	private LinearLayout ll_back;
	private TextView tv_title;
	private EditText edit_old_pass, edit_new_pass, edit_subnew_pass;
	private Button btn_update;
	private SharedPreferences preferences;

	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		case R.id.btn_update:
			new SweetAlertDialog(UserInfoActivity.this,
					SweetAlertDialog.NORMAL_TYPE)
					.setTitleText("确定要修改密码吗？")
					.setContentText("修改成功后退出登录生效")
					.setCancelText("取消")
					.setConfirmText("确定")
					.showCancelButton(true)
					.setConfirmClickListener(
							new SweetAlertDialog.OnSweetClickListener() {
								@Override
								public void onClick(SweetAlertDialog sDialog) {
									if (edit_old_pass.getText().toString().equals("")) {
										Toast.makeText(getApplicationContext(),
												"请输入旧密码",
												Toast.LENGTH_SHORT).show();
									}else if (edit_new_pass.getText().toString().equals("")) {
										Toast.makeText(getApplicationContext(),
												"请输入新密码",
												Toast.LENGTH_SHORT).show();
									}else if (edit_subnew_pass.getText().toString().equals("")) {
										Toast.makeText(getApplicationContext(),
												"请确认新密码",
												Toast.LENGTH_SHORT).show();
									}else {
										updatePassword();
									}
								}
							})
					.setCancelClickListener(
							new SweetAlertDialog.OnSweetClickListener() {
								@Override
								public void onClick(SweetAlertDialog sDialog) {
									sDialog.cancel();
								}
							}).show();
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
		return R.layout.activity_userinfo;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		ll_back = $(R.id.ll_back);
		tv_title = $(R.id.tv_title);
		edit_old_pass = $(R.id.edit_old_pass);
		edit_new_pass = $(R.id.edit_new_pass);
		edit_subnew_pass = $(R.id.edit_subnew_pass);
		btn_update = $(R.id.btn_update);
	}

	@Override
	protected void initdata() {
		// TODO Auto-generated method stub
		tv_title.setText("密码修改");
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		ll_back.setOnClickListener(this);
		btn_update.setOnClickListener(this);
	}

	@Override
	public void doBusiness(Context mContext) {
		// TODO Auto-generated method stub

	}

	private void updatePassword() {
		showDilog("修改中");
		// 用户登录
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.UPDATEPASSWORD;
		RequestParams params = new RequestParams();
		// params.put("password",
		// CommonUtils.md5(edit_pass.getText().toString()));
		params.put("oldpassword", edit_old_pass.getText().toString());
		params.put("password", edit_new_pass.getText().toString());
		params.put("passwordnew", edit_subnew_pass.getText().toString());
		params.put("waiter_id", preferences.getString("business_id", ""));
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 == 200) {
					try {
						String response = new String(arg2, "UTF-8");
						JSONObject jsonObject = new JSONObject(response);
						String CODE = jsonObject.getString("CODE");
						if (CODE.equals("1000")) {
							Toast.makeText(getApplicationContext(),
									"请重新登录",
									Toast.LENGTH_SHORT).show();
							finish();
							MineFragment.islogout = 1;
							dissmissDilog();
						} else {
							Toast.makeText(getApplicationContext(),
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
							dissmissDilog();
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
				Log.i("出错了", arg3 + "");
				Toast.makeText(getApplicationContext(), "服务器异常",
						Toast.LENGTH_SHORT).show();
				dissmissDilog();
			}
		});
	}
}
