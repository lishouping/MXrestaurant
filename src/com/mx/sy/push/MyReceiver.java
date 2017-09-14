package com.mx.sy.push;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.mx.sy.R;
import com.mx.sy.activity.LoginActivity;
import com.mx.sy.activity.MainActivity;
import com.mx.sy.activity.OrderDetailedActivity;
import com.mx.sy.activity.ServiceDetailedActivity;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	public static int isbackresu = 0;

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			processCustomMessage(context, bundle);
			// receivingNotification(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

			String order_id = null;
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			try {
				JSONObject jsonObject = new JSONObject(extras);
				String key = jsonObject
						.getString("androidNotification extras key");
				JSONObject jsonObject2 = new JSONObject(key);
				order_id = jsonObject2.getString("id");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 打开自定义的Activity
			Intent i = new Intent(context, OrderDetailedActivity.class);
			i.putExtra("detailedpage", "1");
			i.putExtra("order_num", order_id);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(i);

//			// 打开自定义的Activity
//			Intent ni = new Intent(context, ServiceDetailedActivity.class);
//			ni.putExtra("service_id", "1");
//			ni.putExtra("service_state", order_id);
//			intent.putExtra("content", "餐桌:"+""+"服务");
//			ni.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			ni.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			context.startActivity(ni);

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction()
					+ " connected state change to " + connected);
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle
						.getString(JPushInterface.EXTRA_EXTRA))) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(
							bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it = json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" + myKey + " - "
								+ json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	// send msg to MainActivity
	@SuppressWarnings("unused")
	private void processCustomMessage(Context context, Bundle bundle) {
		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
		msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
		if (!ExampleUtil.isEmpty(extras)) {
			try {
				JSONObject extraJson = new JSONObject(extras);
				if (extraJson.length() > 0) {
					msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
				}
			} catch (JSONException e) {

			}

		}
		context.sendBroadcast(msgIntent);
	}

	private void receivingNotification(Context context, Bundle bundle) {
		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		String title = bundle.getString(JPushInterface.EXTRA_TITLE);
		String type = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		try {
			JSONObject jsonObject = new JSONObject(extras);
			// String contentid = jsonObject.getString("contentid");
			// String oldid = jsonObject.getString("oldid");
			// String pendingType = jsonObject.getString("pendingType");//0-会议提醒
			// 1-非会议提醒 2-再次提醒
			// String state = jsonObject.getString("state");
			// String pendingTime = "";
			// String pendingTitle = "";
			// String pendingContent = "";
			// try {
			// pendingTime = jsonObject.getString("pendingTime").substring(0,
			// jsonObject.getString("pendingTime").length()-2);//待办事情时间
			// pendingTitle = jsonObject.getString("Title");//待办事情标题
			// pendingContent = jsonObject.getString("pendingContent");//待办内容
			// } catch (JSONException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			NotificationCompat.Builder mBuilder = new Builder(context);
			mBuilder.setContentIntent(getDefalutIntent(
					PendingIntent.FLAG_UPDATE_CURRENT, context, bundle));
			mBuilder.setContentText("");
			mBuilder.setSmallIcon(R.drawable.ic_login_logo);
			mBuilder.setContentTitle("" + "提醒");
			mBuilder.setDefaults(Notification.DEFAULT_SOUND);
			mBuilder.setOngoing(false);
			Notification notify = mBuilder.build();
			notify.flags |= Notification.FLAG_AUTO_CANCEL; // 点击通知后通知栏消失
			// 通知id需要唯一，要不然会覆盖前一条通知
			int notifyId = (int) System.currentTimeMillis();
			NotificationManager mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify(notifyId, notify);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 向mainactivity发送广播
		Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
		// msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
		context.sendBroadcast(msgIntent);

	}

	private PendingIntent getDefalutIntent(int flags, Context context,
			Bundle bundle) {
		// String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		// String type = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);
		// String contentid = "";
		// String template = "";
		// String pendingType = "";//0-会议提醒 1-非会议提醒 2-再次提醒
		// try {
		// JSONObject jsonObject = new JSONObject(extras);
		// contentid = jsonObject.getString("contentid");
		// template = jsonObject.getString("template");
		// pendingType = jsonObject.getString("pendingType");
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		Intent transferIntent = new Intent();
		transferIntent.setClass(context, LoginActivity.class);
		// transferIntent.putExtra("isbackresu", "1");
		// transferIntent.putExtra("contentid", contentid);
		transferIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// transferIntent.putExtra(PushReceiverConstant.KEY_FORM_TYPE,
		// pushType);

		// 第二个参数不能写死，可以写一个随机数或者是时间毫秒数 保证唯一
		PendingIntent pendingIntent = PendingIntent.getActivity(context,
				(int) (Math.random() * 100), transferIntent, flags);
		return pendingIntent;
	}

}
