package com.mx.sy.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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
import com.mx.sy.utils.BluetoothPrintFormatUtil;
import com.mx.sy.utils.CommonUtils;
import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;

/**
 * <p>
 * Title: PrintActivity<／p>
 * <p>
 * 打印机打印: <／p>
 * <p>
 * Company: LTGames<／p>
 * 
 * @author lishp
 * @date 2017年9月7日
 */
public class PrintActivity extends Activity {

	Button btnSearch;
	Button btnSendDraw;
	Button btnSend;
	Button btnClose;
	EditText edtContext;
	EditText edtPrint;
	private static final int REQUEST_ENABLE_BT = 2;
	BluetoothService mService = null;
	BluetoothDevice con_dev = null;
	private static final int REQUEST_CONNECT_DEVICE = 1; // 获取设备消息
	private LinearLayout ll_back;
	private TextView tv_title;
	String order_num;
	
	private SharedPreferences preferences;
	private List<HashMap<String, String>> dateList;
	
	private String printcontent = "";
	
	LinkedList<String> list;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		mService = new BluetoothService(this, mHandler);
		// 蓝牙不可用退出程序
		if (mService.isAvailable() == false) {
			Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
			finish();
		}

		ll_back = (LinearLayout) findViewById(R.id.ll_back);
		ll_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("打印小票");
		
		preferences = getSharedPreferences("userinfo",
				LoginActivity.MODE_PRIVATE);
		
		list = new LinkedList<String>();
		
		
		Intent intent = getIntent();
		order_num = intent.getStringExtra("order_num");
		
		getOrderDeatiledByOrderNum();

	}

	@Override
	public void onStart() {
		super.onStart();
		// 蓝牙未打开，打开蓝牙
		if (mService.isBTopen() == false) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}
		try {
			btnSendDraw = (Button) this.findViewById(R.id.btn_test);
			btnSendDraw.setOnClickListener(new ClickEvent());
			btnSearch = (Button) this.findViewById(R.id.btnSearch);
			btnSearch.setOnClickListener(new ClickEvent());
			btnSend = (Button) this.findViewById(R.id.btnSend);
			btnSend.setOnClickListener(new ClickEvent());
			btnClose = (Button) this.findViewById(R.id.btnClose);
			btnClose.setOnClickListener(new ClickEvent());
			edtContext = (EditText) findViewById(R.id.txt_content);
			//btnClose.setEnabled(false);
			//btnSend.setEnabled(false);
			//btnSendDraw.setEnabled(false);

			if (order_num.equals("100000")) {
				edtContext.setVisibility(View.VISIBLE);
			}
		} catch (Exception ex) {
			Log.e("出错信息", ex.getMessage());
		}
	}

//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		if (mService != null)
//			mService.stop();
//		mService = null;
//	}

	class ClickEvent implements View.OnClickListener {
		public void onClick(View v) {
			if (v == btnSearch) {
				Intent serverIntent = new Intent(PrintActivity.this,
						DeviceListActivity.class); // 运行另外一个类的活动
				startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			} else if (v == btnSend) {
				String msg = edtContext.getText().toString();
				if (msg.length() > 0) {
					String title = BluetoothPrintFormatUtil.printTitle("测试预结单"+"\n");
					
					String line1 = "-------------------------------\n";

					LinkedHashMap<String, String> leftMsgMap = new LinkedHashMap<String, String>();
					LinkedHashMap<String, String> rightMsgMap = new LinkedHashMap<String, String>();

					leftMsgMap.put("用餐桌号", "102");
					rightMsgMap.put("用餐人数", "2"+"\n");
					leftMsgMap.put("账单编号", "2017091012431098537"+"\n");
					leftMsgMap.put("订单时间", "20170922 21:49"+"\n");

					String tableinfo = BluetoothPrintFormatUtil
							.printSymmetryMSG(leftMsgMap, rightMsgMap);
					
					String line2 = "-------------------------------\n";

					LinkedList<String> list = new LinkedList<String>();
					list.add("羊肉串$12$2");
					LinkedHashMap<String, LinkedList<String>> menuMsgMap = new LinkedHashMap<String, LinkedList<String>>();
					menuMsgMap.put("", list);
					String goodsinfo = BluetoothPrintFormatUtil
							.printMenuMSG(menuMsgMap);
					
					String line3 = "-------------------------------\n";

					LinkedHashMap<String, String> leftMsgMap1 = new LinkedHashMap<String, String>();
					LinkedHashMap<String, String> rightMsgMap1 = new LinkedHashMap<String, String>();
					leftMsgMap1.put("总计金额", "102"+"\n");
					leftMsgMap1.put("应收金额", "2"+"\n");
					leftMsgMap1.put("折扣金额", "12"+"\n");
					leftMsgMap1.put("实收金额", "22"+"\n");
					String total = BluetoothPrintFormatUtil
							.printSymmetryMSG(leftMsgMap1, rightMsgMap1);
					
					String line4 = "-------------------------------\n\n";

					mService.sendMessage(title+line1 + tableinfo+line2 + goodsinfo+line3+total+line4
							, "GBK");
				} else {
					mService.sendMessage(printcontent + "\n", "GBK");
				}
			} else if (v == btnClose) {
				mService.stop();
			} else if (v == btnSendDraw) {
				String msg = "";
				String lang = getString(R.string.strLang);
				// printImage();

				byte[] cmd = new byte[3];
				cmd[0] = 0x1b;
				cmd[1] = 0x21;
				if ((lang.compareTo("en")) == 0) {
					cmd[2] |= 0x10;
					mService.write(cmd); // 倍宽、倍高模式
					mService.sendMessage("Congratulations!\n", "GBK");
					cmd[2] &= 0xEF;
					mService.write(cmd); // 取消倍高、倍宽模式
					msg = "  You have sucessfully created communications between your device and our bluetooth printer.\n\n"
							+ "  the company is a high-tech enterprise which specializes"
							+ " in R&D,manufacturing,marketing of thermal printers and barcode scanners.\n\n";

					mService.sendMessage(msg, "GBK");
				} else if ((lang.compareTo("ch")) == 0) {
					cmd[2] |= 0x10;
					mService.write(cmd); // 倍宽、倍高模式
					mService.sendMessage("恭喜您！\n", "GBK");
					cmd[2] &= 0xEF;
					mService.write(cmd); // 取消倍高、倍宽模式
					msg = "  您已经成功的连接上了我们的蓝牙打印机！\n\n"
							+ "  本公司是一家专业从事研发，生产，销售商用票据打印机和条码扫描设备于一体的高科技企业.\n\n";

					mService.sendMessage(msg, "GBK");
				}
			}
		}
	}

	/**
	 * 创建一个Handler实例，用于接收BluetoothService类返回回来的消息
	 */
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BluetoothService.MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED: // 已连接
					Toast.makeText(getApplicationContext(),
							"Connect successful", Toast.LENGTH_SHORT).show();
//					btnClose.setEnabled(true);
//					btnSend.setEnabled(true);
//					btnSendDraw.setEnabled(true);
					break;
				case BluetoothService.STATE_CONNECTING: // 正在连接
					Log.d("蓝牙调试", "正在连接.....");
					break;
				case BluetoothService.STATE_LISTEN: // 监听连接的到来
				case BluetoothService.STATE_NONE:
					Log.d("蓝牙调试", "等待连接.....");
					break;
				}
				break;
			case BluetoothService.MESSAGE_CONNECTION_LOST: // 蓝牙已断开连接
				Toast.makeText(getApplicationContext(),
						"Device connection was lost", Toast.LENGTH_SHORT)
						.show();
//				btnClose.setEnabled(false);
//				btnSend.setEnabled(false);
//				btnSendDraw.setEnabled(false);
				break;
			case BluetoothService.MESSAGE_UNABLE_CONNECT: // 无法连接设备
				Toast.makeText(getApplicationContext(),
						"Unable to connect device", Toast.LENGTH_SHORT).show();
				break;
			}
		}

	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_ENABLE_BT: // 请求打开蓝牙
			if (resultCode == Activity.RESULT_OK) { // 蓝牙已经打开
				Toast.makeText(this, "Bluetooth open successful",
						Toast.LENGTH_LONG).show();
			} else { // 用户不允许打开蓝牙
				finish();
			}
			break;
		case REQUEST_CONNECT_DEVICE: // 请求连接某一蓝牙设备
			if (resultCode == Activity.RESULT_OK) { // 已点击搜索列表中的某个设备项
				String address = data.getExtras().getString(
						DeviceListActivity.EXTRA_DEVICE_ADDRESS); // 获取列表项中设备的mac地址
				con_dev = mService.getDevByMac(address);

				mService.connect(con_dev);
			}
			break;
		}
	}

	// 打印图形
	@SuppressLint("SdCardPath")
	private void printImage() {
		byte[] sendData = null;
		PrintPic pg = new PrintPic();
		pg.initCanvas(384);
		pg.initPaint();
		pg.drawImage(0, 0, "/mnt/sdcard/icon.jpg");
		sendData = pg.printDraw();
		mService.write(sendData); // 打印byte流数据
	}
	
	public void getOrderDeatiledByOrderNum() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("key", preferences.getString("loginkey", ""));
		client.addHeader("id", preferences.getString("userid", ""));
		String url = ApiConfig.API_URL + ApiConfig.GETORDERBYNO;
		RequestParams params = new RequestParams();
		params.put("order_num", order_num);
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
							JSONObject object = new JSONObject(jsonObject
									.getString("DATA"));
							String order_num = object.getString("order_num");
							JSONObject tabobj = new JSONObject(object
									.getString("table"));
							String order_time = CommonUtils.getStrTime(object.getString("order_time"));
							
							JSONObject cartobj = new JSONObject(object
									.getString("cart"));

							String table_name = tabobj.getString("table_name");
							String people_count = tabobj
									.getString("people_count");

							String total_price = cartobj
									.getString("total_price");
							String payment = object.getString("payment");//应收金额
							String discount_payment = object.getString("discount_payment");//折扣后金额
							String real_payment = object.getString("real_payment");//实收金额
							
							
							JSONArray jsonArray = cartobj
									.getJSONArray("goods_set");
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject object2 = jsonArray.getJSONObject(i);
								String newobj = object2.getString("good_name")+"$"+object2.getString("good_num")+"*"+object2.getString("good_price")+"$"+object2.getString("good_total_price");
								list.add(newobj);
							}
							
							// 打印内容
							String title = BluetoothPrintFormatUtil.printTitle("预结单"+"\n");
							
							String line1 = "-------------------------------\n";

							LinkedHashMap<String, String> leftMsgMap = new LinkedHashMap<String, String>();
							LinkedHashMap<String, String> rightMsgMap = new LinkedHashMap<String, String>();

							leftMsgMap.put("用餐桌号", table_name);
							rightMsgMap.put("用餐人数", people_count+"\n");
							leftMsgMap.put("账单编号", order_num+"\n");
							leftMsgMap.put("订单时间", order_time+"\n");

							String tableinfo = BluetoothPrintFormatUtil
									.printSymmetryMSG(leftMsgMap, rightMsgMap);
							
							String line2 = "-------------------------------\n";
							LinkedHashMap<String, LinkedList<String>> menuMsgMap = new LinkedHashMap<String, LinkedList<String>>();
							menuMsgMap.put("", list);
							String goodsinfo = BluetoothPrintFormatUtil
									.printMenuMSG(menuMsgMap);
							
							String line3 = "-------------------------------\n";

							LinkedHashMap<String, String> leftMsgMap1 = new LinkedHashMap<String, String>();
							LinkedHashMap<String, String> rightMsgMap1 = new LinkedHashMap<String, String>();
							leftMsgMap1.put("总计金额", total_price+"\n");
							leftMsgMap1.put("应收金额", payment+"\n");
							leftMsgMap1.put("折扣金额", discount_payment+"\n");
							leftMsgMap1.put("实收金额", real_payment+"\n");
							String total = BluetoothPrintFormatUtil
									.printSymmetryMSG(leftMsgMap1, rightMsgMap1);
							
							String line4 = "-------------------------------\n\n";
							
							printcontent = title+line1 + tableinfo+line2 + goodsinfo+line3+total+line4;
							
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
				Toast.makeText(getApplicationContext(), "服务器异常",
						Toast.LENGTH_LONG).show();
			}
		});

	}


}
