package com.mx.sy.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mx.sy.R;
import com.mx.sy.api.ApiConfig;
import com.mx.sy.app.MyApplication;
import com.mx.sy.base.BaseActivity;

/**
 * @author lishouping 订单未处理详情页面
 */
public class PayImagesActivity extends BaseActivity {
	private LinearLayout ll_back;
	private TextView tv_title;
	
	private ImageView imag_wchat,imag_zhifubao;
	
	private SharedPreferences preferences;

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
		return R.layout.activity_payimages;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		ll_back = $(R.id.ll_back);
		tv_title = $(R.id.tv_title);
		imag_wchat = $(R.id.imag_wchat);
		imag_zhifubao = $(R.id.imag_zhifubao);
	}

	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		default:
			break;
		}
	}


	@Override
	protected void initdata() {
		// TODO Auto-generated method stub
		tv_title.setText("付款二维码");
		preferences = getSharedPreferences("userinfo",
				LoginActivity.MODE_PRIVATE);
		
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		ll_back.setOnClickListener(this);
	}

	@Override
	public void doBusiness(Context mContext) {
		// TODO Auto-generated method stub
		getShopTableInfo();
	}

	// 查询店铺信息
	public void getShopTableInfo() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.GETSHOPINFO + "/"
				+ preferences.getString("shop_id", "");
		client.get(url , new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				if (arg0 == 200) {
					try {
						String response = new String(arg2, "UTF-8");
						JSONObject jsonObject = new JSONObject(response);
						String CODE = jsonObject.getString("CODE");
						if (CODE.equals("1000")) {
							JSONObject jsonObject2 = new JSONObject(jsonObject.getString("DATA"));
							String wechat_img = jsonObject2.getString("wechat_img");
							String alipay_img = jsonObject2.getString("alipay_img");
							MyApplication.mLoader.loadImage(ApiConfig.RESOURCE_URL+wechat_img, imag_wchat, true);
							MyApplication.mLoader.loadImage(ApiConfig.RESOURCE_URL+alipay_img, imag_zhifubao, true);
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
