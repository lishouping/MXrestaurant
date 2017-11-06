package com.mx.sy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mx.sy.R;
import com.mx.sy.activity.AboutUsActivity;
import com.mx.sy.activity.FeedBackActivity;
import com.mx.sy.activity.PrinterSeetingActivity;
import com.mx.sy.activity.PushSeetingActivity;
import com.mx.sy.activity.SalesStatisticsActivity;
import com.mx.sy.activity.ServiceStatisticsActivity;
import com.mx.sy.activity.UserInfoActivity;
import com.mx.sy.adapter.MineUserAdapter;
import com.mx.sy.base.BaseFragment;
import com.mx.sy.common.RoundedImageView;
import com.mx.sy.dialog.SweetAlertDialog;
import com.mx.sy.push.SettingActivity;

public class MineFragment extends BaseFragment {
	private ListView lv_mine_user;
	private MineUserAdapter mineUserAdapter;
	private List<HashMap<String, String>> dateList;
	private RoundedImageView user_round;
	private Button btn_signout;

	LayoutInflater inflater;
	View view;
	
	private TextView tv_user_name;
	
	private SharedPreferences preferences;
	
	public static int islogout = 0;
	
	private String role_id;
	
	@Override
	protected int setLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_mine;
	}
	@Override
	public void onResume() {
		if (islogout==1) {
			preferences.edit().putString("username", "").commit();
			preferences.edit().putString("password", "").commit();
			getActivity().finish();
			islogout = 0;
		}
		super.onResume();
	}
	@Override
	protected void initData(Bundle arguments) {
		// TODO Auto-generated method stub
		super.initData(arguments);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		lv_mine_user = findViewById(R.id.lv_mine_user);
		btn_signout = findViewById(R.id.btn_signout);
		btn_signout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 退出登录
				new SweetAlertDialog(getActivity(),
						SweetAlertDialog.NORMAL_TYPE)
						.setTitleText("确定要退出登录吗？")
						// .setContentText("Won't be able to recover this file!")
						.setCancelText("取消")
						.setConfirmText("确定")
						.showCancelButton(true)
						.setConfirmClickListener(
								new SweetAlertDialog.OnSweetClickListener() {
									@Override
									public void onClick(
											SweetAlertDialog sDialog) {
										
										preferences.edit().putString("username", "").commit();
										preferences.edit().putString("password", "").commit();
										
										sDialog.cancel();
										getActivity().finish();
									}
								})
						.setCancelClickListener(
								new SweetAlertDialog.OnSweetClickListener() {
									@Override
									public void onClick(
											SweetAlertDialog sDialog) {
										sDialog.cancel();
									}
								}).show();
			}
		});
		inflater = (LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.headview_mine, null);
		
		tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
		user_round = (RoundedImageView) view.findViewById(R.id.user_round);
		user_round.setOval(true);
	}

	@Override
	protected void onLazyLoad() {
		// TODO Auto-generated method stub
		super.onLazyLoad();

		preferences = getActivity().getSharedPreferences("userinfo",
				getActivity().MODE_PRIVATE);
		role_id = preferences.getString("role_id", "");
		
		String name = preferences.getString("name", "");
		tv_user_name.setText(name);
		
		// dateList = new ArrayList<HashMap<String,String>>();
		mineUserAdapter = new MineUserAdapter(mActivity, makeDate(),
				R.layout.item_mineuser);
		lv_mine_user.addHeaderView(view);
		lv_mine_user.setAdapter(mineUserAdapter);
		lv_mine_user.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				if (role_id.equals("2")) {
					Intent intent = new Intent();
					switch (position) {
					case 1:
						//推送消息
						intent.setClass(getActivity(),SettingActivity.class);
						startActivity(intent);
						break;
					case 2:
						//个人信息
						intent.setClass(getActivity(), UserInfoActivity.class);
						startActivity(intent);
						break;
					case 3:
						//打印机设置
						intent.setClass(getActivity(), PrinterSeetingActivity.class);
						startActivity(intent);
						break;
					case 4:
						// 意见反馈
						intent.setClass(getActivity(), FeedBackActivity.class);
						startActivity(intent);
						break;
					case 5:
						// 关于我们
						intent.setClass(getActivity(), AboutUsActivity.class);
						startActivity(intent);
						break;
					default:
						break;
					}
				}else {
					Intent intent = new Intent();
					switch (position) {
					case 1:
						//销售统计
						intent.setClass(getActivity(), SalesStatisticsActivity.class);
						startActivity(intent);
						break;
					case 2:
						//服务统计
						intent.setClass(getActivity(), ServiceStatisticsActivity.class);
						startActivity(intent);
						break; 
					case 3:
						//推送消息
						intent.setClass(getActivity(),SettingActivity.class);
						startActivity(intent);
						break;
					case 4:
						//个人信息
						intent.setClass(getActivity(), UserInfoActivity.class);
						startActivity(intent);
						break;
					case 5:
						//打印机设置
						intent.setClass(getActivity(), PrinterSeetingActivity.class);
						startActivity(intent);
						break;
					case 6:
						// 意见反馈
						intent.setClass(getActivity(), FeedBackActivity.class);
						startActivity(intent);
						break;
					case 7:
						// 关于我们
						intent.setClass(getActivity(), AboutUsActivity.class);
						startActivity(intent);
						break;
					default:
						break;
					}
				}
				
			}
		});
	}

	private List<HashMap<String, Object>> makeDate() {
		if (role_id.equals("2")) {//服务员
			List<HashMap<String, Object>> dateList = new ArrayList<HashMap<String, Object>>();

			HashMap<String, Object> userInfoMap3 = new HashMap<String, Object>();
			userInfoMap3.put("content", "推送消息");
			userInfoMap3.put("contentImg", R.drawable.icon_tip);
			userInfoMap3.put("mytypeImg", R.drawable.icon_my3);
			dateList.add(userInfoMap3);

			HashMap<String, Object> userInfoMap4 = new HashMap<String, Object>();
			userInfoMap4.put("content", "密码修改");
			userInfoMap4.put("contentImg", R.drawable.icon_tip);
			userInfoMap4.put("mytypeImg", R.drawable.icon_my4);
			dateList.add(userInfoMap4);

			HashMap<String, Object> userInfoMap5 = new HashMap<String, Object>();
			userInfoMap5.put("content", "打印机设置");
			userInfoMap5.put("contentImg", R.drawable.icon_tip);
			userInfoMap5.put("mytypeImg", R.drawable.icon_my5);
			dateList.add(userInfoMap5);

			HashMap<String, Object> userInfoMap7 = new HashMap<String, Object>();
			userInfoMap7.put("content", "意见反馈");
			userInfoMap7.put("contentImg", R.drawable.icon_tip);
			userInfoMap7.put("mytypeImg", R.drawable.icon_my6);
			dateList.add(userInfoMap7);

			HashMap<String, Object> userInfoMap8 = new HashMap<String, Object>();
			userInfoMap8.put("content", "关于我们");
			userInfoMap8.put("contentImg", R.drawable.icon_tip);
			userInfoMap8.put("mytypeImg", R.drawable.icon_my7);
			dateList.add(userInfoMap8);

			return dateList;
		}else {
			List<HashMap<String, Object>> dateList = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> userInfoMap1 = new HashMap<String, Object>();
			userInfoMap1.put("content", "销售统计");
			userInfoMap1.put("contentImg", R.drawable.icon_tip);
			userInfoMap1.put("mytypeImg", R.drawable.icon_my1);
			dateList.add(userInfoMap1);

			HashMap<String, Object> userInfoMap2 = new HashMap<String, Object>();
			userInfoMap2.put("content", "服务统计");
			userInfoMap2.put("contentImg", R.drawable.icon_tip);
			userInfoMap2.put("mytypeImg", R.drawable.icon_my2);
			dateList.add(userInfoMap2);

			HashMap<String, Object> userInfoMap3 = new HashMap<String, Object>();
			userInfoMap3.put("content", "推送消息");
			userInfoMap3.put("contentImg", R.drawable.icon_tip);
			userInfoMap3.put("mytypeImg", R.drawable.icon_my3);
			dateList.add(userInfoMap3);

			HashMap<String, Object> userInfoMap4 = new HashMap<String, Object>();
			userInfoMap4.put("content", "密码修改");
			userInfoMap4.put("contentImg", R.drawable.icon_tip);
			userInfoMap4.put("mytypeImg", R.drawable.icon_my4);
			dateList.add(userInfoMap4);

			HashMap<String, Object> userInfoMap5 = new HashMap<String, Object>();
			userInfoMap5.put("content", "打印机设置");
			userInfoMap5.put("contentImg", R.drawable.icon_tip);
			userInfoMap5.put("mytypeImg", R.drawable.icon_my5);
			dateList.add(userInfoMap5);

			HashMap<String, Object> userInfoMap7 = new HashMap<String, Object>();
			userInfoMap7.put("content", "意见反馈");
			userInfoMap7.put("contentImg", R.drawable.icon_tip);
			userInfoMap7.put("mytypeImg", R.drawable.icon_my6);
			dateList.add(userInfoMap7);

			HashMap<String, Object> userInfoMap8 = new HashMap<String, Object>();
			userInfoMap8.put("content", "关于我们");
			userInfoMap8.put("contentImg", R.drawable.icon_tip);
			userInfoMap8.put("mytypeImg", R.drawable.icon_my7);
			dateList.add(userInfoMap8);

			return dateList;
		}
		
	}

}
