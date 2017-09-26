package com.mx.sy.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mx.sy.R;
import com.mx.sy.api.ApiConfig;
import com.mx.sy.base.BaseActivity;
import com.mx.sy.fragment.MineFragment;

/**
* <p>Title: AboutUsActivity<／p>
* <p>Description: 意见反馈<／p>
* <p>Company: LTGames<／p> 
* @author lishp
* @date 2017年7月23日
 */
public class FeedBackActivity extends BaseActivity{
	private LinearLayout ll_back;
	private TextView tv_title;
	private EditText edit_feedback;
	private Button btn_submit_feedback;
	private SharedPreferences preferences;
	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		case R.id.btn_submit_feedback:
			if (edit_feedback.getText().toString().equals("")) {
				Toast.makeText(getApplicationContext(), "请填写您的意见或建议",
						Toast.LENGTH_SHORT).show();
			}else {
				submitFeedback();
			}
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
		return R.layout.activity_feedback;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		ll_back = $(R.id.ll_back);
		tv_title = $(R.id.tv_title);
		edit_feedback = $(R.id.edit_feedback);
		btn_submit_feedback = $(R.id.btn_submit_feedback);
	}

	@Override
	protected void initdata() {
		// TODO Auto-generated method stub
		tv_title.setText("意见反馈");
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		ll_back.setOnClickListener(this);
		btn_submit_feedback.setOnClickListener(this);
	}

	@Override
	public void doBusiness(Context mContext) {
		// TODO Auto-generated method stub
		
	}

	private void submitFeedback() {
		showDilog("提交中");
		// 用户登录
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.SAVESUGGEST;
		RequestParams params = new RequestParams();
		params.put("waiter_id", preferences.getString("business_id", ""));
		params.put("content", edit_feedback.getText().toString());
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
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
							finish();
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
