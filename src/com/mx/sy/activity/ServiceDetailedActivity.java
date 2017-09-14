package com.mx.sy.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mx.sy.R;
import com.mx.sy.api.ApiConfig;
import com.mx.sy.base.BaseActivity;
import com.mx.sy.base.CommonViewHolder;

/**
 * <p>
 * Title: ServiceDetailedActivity<／p>
 * <p>
 * Description: <／p> service 详情
 * <p>
 * Company: LTGames<／p>
 * 
 * @author lishp
 * @date 2017年9月14日
 */
public class ServiceDetailedActivity extends BaseActivity {
	private TextView tv_service_content, tv_service_states;
	private Button btn_subservice;
	private SharedPreferences preferences;
	private String service_id;
	private String service_state;
	private String content;

	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_subservice:
			todoSertice();
			break;

		default:
			break;
		}
	}

	@Override
	public void initParms(Bundle parms) {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		service_id = intent.getStringExtra("service_id");
		service_state = intent.getStringExtra("service_state");
		content = intent.getStringExtra("content");
	}

	@Override
	public View bindView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_servicedetailed;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		tv_service_content = $(R.id.tv_service_content);
		tv_service_states = $(R.id.tv_service_states);
		btn_subservice = $(R.id.btn_subservice);

		preferences = getSharedPreferences("userinfo",
				LoginActivity.MODE_PRIVATE);
	}

	@Override
	protected void initdata() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		btn_subservice.setOnClickListener(this);
	}

	@Override
	public void doBusiness(Context mContext) {
		// TODO Auto-generated method stub

	}

	// 获取Service
	public void todoSertice() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.TODOSERVICE;
		RequestParams params = new RequestParams();
		params.put("service_id", service_id);
		params.put("waiter_id", preferences.getString("business_id", ""));
		params.put("status", "1");
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				if (arg0 == 200) {
					try {
						String response = new String(arg2, "UTF-8");
						JSONObject jsonObject = new JSONObject(response);
						String CODE = jsonObject.getString("CODE");
						if (CODE.equals("1000")) {
							Toast.makeText(getApplicationContext(), "处理成功",
									Toast.LENGTH_SHORT).show();
							finish();
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
				Toast.makeText(getApplicationContext(), "服务器异常",
						Toast.LENGTH_LONG).show();
			}
		});
	}
}
