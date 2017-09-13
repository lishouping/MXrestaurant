package com.mx.sy.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;





import java.util.Set;

import org.apache.http.Header;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mx.sy.activity.LoginActivity;
import com.mx.sy.activity.MainActivity;
import com.mx.sy.api.ApiConfig;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

/**
 * @作者：zhoulei
 * @时间：2017年2月17日 上午9:49:34
 * @备注：待办提醒Service
 */
public class PendingRemindedService extends Service {
	SharedPreferences preferences ;
	private int isloading = 0;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		
		return null;
	}
	

	//启动service
	@SuppressLint("SimpleDateFormat") @Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		preferences = this.getSharedPreferences("userinfo",  
	             MODE_PRIVATE); 
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				userLogin();
			}
		}, 1000 * 60 * 20);
		
		
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour =  1000 * 60 *28;   // 这是一小时的毫秒数
		long triggerAtTime = SystemClock.elapsedRealtime() + anHour; 
		intent.setClass(getApplicationContext(), AlarmReceiver.class); 
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 在Service结束后关闭AlarmManager
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent i = new Intent(this, AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		manager.cancel(pi);

	}
	private void userLogin() {
		// 用户登录
		AsyncHttpClient client = new AsyncHttpClient();
		String url = ApiConfig.API_URL + ApiConfig.USERLOGIN_URL;
		RequestParams params = new RequestParams();
		// params.put("password",
		// CommonUtils.md5(edit_pass.getText().toString()));
		String username = preferences.getString("username", "");
		String password = preferences.getString("password", "");
		params.put("user_name", username);
		params.put("password", password);
		params.put("type", "2");// Number 1:商户，2:管理员，3:个人用户，4:超级管理员
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 == 200) {
					try {
						String response = new String(arg2, "UTF-8");
						JSONObject jsonObject = new JSONObject(response);
						String CODE = jsonObject.getString("CODE");
						if (CODE.equals("1000")) {
							
							JSONObject object = new JSONObject(jsonObject
									.getString("DATA"));

							JSONObject object2 = new JSONObject(object
									.getString("waiter"));

							// JSONObject object3 = new
							// JSONObject(object2.getString("create_time"));
							//String user_id = object.getString("user_id");
							String alias = object.getString("alias");
							String login_key = object.getString("login_key");
							String shop_id = object2.getString("shop_id");
							String name = object2.getString("name");
							String business_id = object.getString("business_id");

							preferences.edit().putString("userid", business_id)
									.commit();
							preferences.edit().putString("loginkey", login_key)
									.commit();
							preferences.edit().putString("shop_id", shop_id)
									.commit();
							preferences.edit().putString("name", name).commit();
							preferences.edit().putString("business_id", business_id).commit();
						} else {
							Toast.makeText(getApplicationContext(),
									jsonObject.getString("MESSAGE"),
									Toast.LENGTH_SHORT).show();
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
