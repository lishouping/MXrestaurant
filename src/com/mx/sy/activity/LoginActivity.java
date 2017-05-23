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
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mx.sy.R;
import com.mx.sy.api.ApiConfig;
import com.mx.sy.base.BaseActivity;

/**
 * @author Administrator
 * 登录页面
 */
public class LoginActivity extends BaseActivity {
	private EditText edit_user;
	private EditText edit_pass;
	private Button btn_login;
	private SharedPreferences preferences;
	@Override	
	public void widgetClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_login:
			if (isNull()) {
				// 调用登录方法
				userLogin();
			}
//			Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//			startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	public void initParms(Bundle parms) {
		// TODO Auto-generated method stub
		preferences = getSharedPreferences("userinfo", LoginActivity.MODE_PRIVATE); 
	}

	@Override
	public View bindView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_login;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		edit_user = $(R.id.edit_user);
		edit_pass = $(R.id.edit_pass);
		btn_login = $(R.id.btn_login);
	}

	@Override
	protected void initdata() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		btn_login.setOnClickListener(this);
	}

	@Override
	public void doBusiness(Context mContext) {
		// TODO Auto-generated method stub

	}
	private Boolean isNull(){
		if (edit_user.getText().toString().trim().equals("")) {
			Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
			return false;
		}else if (edit_pass.getText().toString().trim().equals("")) {
			Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
			return false;
		}else {
			return true;
		}
	}
	private void userLogin(){
		showDilog("登录中");
		//用户登录
		AsyncHttpClient client = new AsyncHttpClient();
		String url = ApiConfig.API_URL + ApiConfig.userLoginUrl;
		//&uname=test&pwd=000000
		RequestParams params = new RequestParams();
		params.put("uname", edit_user.getText().toString());
		params.put("pwd", edit_pass.getText().toString());
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 == 200) {
					try {
						String response = new String(arg2,"UTF-8");
						JSONObject jsonObject = new JSONObject(response);
						String error = jsonObject.getString("error");
						if (error.equals("false")) {
							JSONObject object = new JSONObject(jsonObject.getString("msg"));
							String userid = object.getString("uid");//用户id
							String name = object.getString("shortname");//用户昵称
							final String telephone = object.getString("phone");//手机号码
//							String sex = object.getString("sex");//性别
//							String birthday = object.getString("birthday");//出生日期
//							preferences.edit().putString("userid", userid).commit();
//							preferences.edit().putString("name", name).commit();
//							preferences.edit().putString("telephone", telephone).commit();
//							preferences.edit().putString("sex", sex).commit();
//							preferences.edit().putString("birthday", birthday).commit();
							Intent intent = new Intent(getApplicationContext(), MainActivity.class);
							startActivity(intent);
							finish();

							dissmissDilog();
						}else {
							btn_login.setClickable(true);
							Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
							dissmissDilog();
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						btn_login.setClickable(true);
						Log.i("异常了", e + "");
						Toast.makeText(getApplicationContext(), "服务器异常", Toast.LENGTH_SHORT).show();
						dissmissDilog();
					} 
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				Log.i("出错了", arg3 + "");
				Toast.makeText(getApplicationContext(), "服务器异常", Toast.LENGTH_SHORT).show();
				dissmissDilog();
			}
		});
	}

}
