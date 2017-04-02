package com.mx.sy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.mx.sy.R;
import com.mx.sy.adapter.MineUserAdapter;
import com.mx.sy.base.BaseFragment;

public class MineFragment extends BaseFragment{
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
		inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.headview_mine, null);
	}
	
	@Override
	protected void onLazyLoad() {
		// TODO Auto-generated method stub
		super.onLazyLoad();
		
		//dateList = new ArrayList<HashMap<String,String>>();
		mineUserAdapter = new MineUserAdapter(mActivity, makeDate(), R.layout.item_mineuser);
		lv_mine_user.addHeaderView(view);
		lv_mine_user.setAdapter(mineUserAdapter);
	}
	
	private List<HashMap<String, Object>> makeDate(){
		List<HashMap<String, Object>> dateList = new ArrayList<HashMap<String,Object>>();
		
		HashMap<String, Object> userInfoMap = new HashMap<String, Object>();
		userInfoMap.put("content", "个人信息");
		userInfoMap.put("contentImg", R.drawable.ic_launcher);
		dateList.add(userInfoMap);
		
		HashMap<String, Object> userInfoMap1 = new HashMap<String, Object>();
		userInfoMap1.put("content", "服务员管理");
		userInfoMap1.put("contentImg", R.drawable.ic_launcher);
		dateList.add(userInfoMap1);
		
		HashMap<String, Object> userInfoMap2 = new HashMap<String, Object>();
		userInfoMap2.put("content", "打印模板");
		userInfoMap2.put("contentImg", R.drawable.ic_login_logo);
		dateList.add(userInfoMap2);
		
		HashMap<String, Object> userInfoMap3 = new HashMap<String, Object>();
		userInfoMap3.put("content", "销售统计");
		userInfoMap3.put("contentImg", R.drawable.ic_login_logo);
		dateList.add(userInfoMap3);
		
		HashMap<String, Object> userInfoMap4 = new HashMap<String, Object>();
		userInfoMap4.put("content", "服务统计");
		userInfoMap4.put("contentImg", R.drawable.ic_login_logo);
		dateList.add(userInfoMap4);
	
		HashMap<String, Object> userInfoMap5 = new HashMap<String, Object>();
		userInfoMap5.put("content", "关于我们");
		userInfoMap5.put("contentImg", R.drawable.ic_login_logo);
		dateList.add(userInfoMap5);
	
		HashMap<String, Object> userInfoMap6 = new HashMap<String, Object>();
		userInfoMap6.put("content", "意见反馈");
		userInfoMap6.put("contentImg", R.drawable.ic_login_logo);
		
		HashMap<String, Object> userInfoMap7 = new HashMap<String, Object>();
		userInfoMap7.put("content", "推送消息");
		userInfoMap7.put("contentImg", R.drawable.ic_login_logo);
		dateList.add(userInfoMap7);
		
		HashMap<String, Object> userInfoMap8 = new HashMap<String, Object>();
		userInfoMap8.put("content", "打印机设置");
		userInfoMap8.put("contentImg", R.drawable.ic_login_logo);
		dateList.add(userInfoMap8);
		
		HashMap<String, Object> userInfoMap9 = new HashMap<String, Object>();
		userInfoMap9.put("content", "安全登录");
		userInfoMap9.put("contentImg", R.drawable.ic_login_logo);
		dateList.add(userInfoMap9);
		return dateList;
	}
	

}
