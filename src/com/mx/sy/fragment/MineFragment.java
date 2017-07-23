package com.mx.sy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

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

public class MineFragment extends BaseFragment {
	private ListView lv_mine_user;
	private MineUserAdapter mineUserAdapter;
	private List<HashMap<String, String>> dateList;

	LayoutInflater inflater;
	View view;

	@Override
	protected int setLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_mine;
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
		inflater = (LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.headview_mine, null);
	}

	@Override
	protected void onLazyLoad() {
		// TODO Auto-generated method stub
		super.onLazyLoad();

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
				Intent intent = new Intent();
				switch (position) {
				case 1:
					//销售统计
					intent.setClass(getActivity(), SalesStatisticsActivity.class);
					break;
				case 2:
					//服务统计
					intent.setClass(getActivity(), ServiceStatisticsActivity.class);
					break; 
				case 3:
					//推送消息
					intent.setClass(getActivity(), PushSeetingActivity.class);
					break;
				case 4:
					//个人信息
					intent.setClass(getActivity(), UserInfoActivity.class);
					break;
				case 5:
					//打印机设置
					intent.setClass(getActivity(), PrinterSeetingActivity.class);
					break;
				case 6:
					// 意见反馈
					intent.setClass(getActivity(), FeedBackActivity.class);
					break;
				case 7:
					// 关于我们
					intent.setClass(getActivity(), AboutUsActivity.class);
					break;
				default:
					break;
				}
				startActivity(intent);
			}
		});
	}

	private List<HashMap<String, Object>> makeDate() {
		List<HashMap<String, Object>> dateList = new ArrayList<HashMap<String, Object>>();

//		HashMap<String, Object> userInfoMap = new HashMap<String, Object>();
//		userInfoMap.put("content", "服务员管理");
//		userInfoMap.put("contentImg", R.drawable.ic_launcher);
//		dateList.add(userInfoMap);

		HashMap<String, Object> userInfoMap1 = new HashMap<String, Object>();
		userInfoMap1.put("content", "销售统计");
		userInfoMap1.put("contentImg", R.drawable.ic_launcher);
		dateList.add(userInfoMap1);

		HashMap<String, Object> userInfoMap2 = new HashMap<String, Object>();
		userInfoMap2.put("content", "服务统计");
		userInfoMap2.put("contentImg", R.drawable.ic_login_logo);
		dateList.add(userInfoMap2);

		HashMap<String, Object> userInfoMap3 = new HashMap<String, Object>();
		userInfoMap3.put("content", "推送消息");
		userInfoMap3.put("contentImg", R.drawable.ic_login_logo);
		dateList.add(userInfoMap3);

		HashMap<String, Object> userInfoMap4 = new HashMap<String, Object>();
		userInfoMap4.put("content", "个人信息");
		userInfoMap4.put("contentImg", R.drawable.ic_login_logo);
		dateList.add(userInfoMap4);

		HashMap<String, Object> userInfoMap5 = new HashMap<String, Object>();
		userInfoMap5.put("content", "打印机设置");
		userInfoMap5.put("contentImg", R.drawable.ic_login_logo);
		dateList.add(userInfoMap5);

		HashMap<String, Object> userInfoMap6 = new HashMap<String, Object>();
		userInfoMap6.put("content", "打印模板");
		userInfoMap6.put("contentImg", R.drawable.ic_login_logo);

		HashMap<String, Object> userInfoMap7 = new HashMap<String, Object>();
		userInfoMap7.put("content", "意见反馈");
		userInfoMap7.put("contentImg", R.drawable.ic_login_logo);
		dateList.add(userInfoMap7);

		HashMap<String, Object> userInfoMap8 = new HashMap<String, Object>();
		userInfoMap8.put("content", "关于我们");
		userInfoMap8.put("contentImg", R.drawable.ic_login_logo);
		dateList.add(userInfoMap8);

		return dateList;
	}

}
