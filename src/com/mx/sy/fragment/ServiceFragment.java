package com.mx.sy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mx.sy.R;
import com.mx.sy.adapter.ServiceAdapter;
import com.mx.sy.adapter.TablesAdapter;
import com.mx.sy.base.BaseFragment;
import com.mx.sy.common.PullToRefreshView;
import com.mx.sy.common.PullToRefreshView.OnFooterRefreshListener;
import com.mx.sy.common.PullToRefreshView.OnHeaderRefreshListener;

public class ServiceFragment extends BaseFragment implements OnClickListener ,OnFooterRefreshListener,OnHeaderRefreshListener{
	private LinearLayout lin_nomanage,lin_processed;
	private ListView lv_service;
	private List<HashMap<String, String>> dateList;
	private ServiceAdapter serviceAdapter;
	
	PullToRefreshView mPullToRefreshView;
	@Override
	protected int setLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_service;
	}
	
	@Override
	protected void loadDate() {
		// TODO Auto-generated method stub
		super.loadDate();
		mPullToRefreshView = (PullToRefreshView) getActivity().findViewById(R.id.pullrefresh_service);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
	}
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		lin_nomanage = findViewById(R.id.lin_nomanage);
		lin_nomanage.setOnClickListener(this);
		lin_processed = findViewById(R.id.lin_processed);
		lin_processed.setOnClickListener(this);
		lv_service = findViewById(R.id.lv_service);
	}
	@Override
	protected void initData(Bundle arguments) {
		// TODO Auto-generated method stub
		super.initData(arguments);
	}
	@Override
	protected void onLazyLoad() {
		// TODO Auto-generated method stub
		super.onLazyLoad();
		dateList = new ArrayList<HashMap<String,String>>();
		for (int i = 0; i < 10; i++) {
			dateList.add(new HashMap<String, String>());
		}
		serviceAdapter = new ServiceAdapter(getActivity(), dateList, R.layout.item_servicemanage);
		lv_service.setAdapter(serviceAdapter);
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lin_nomanage:
			
			break;
		case R.id.lin_processed:
			
			break;
		default:
			break;
		}
	}
	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		mPullToRefreshView.postDelayed(new Runnable() {
			@Override
			public void run() {
				// 下拉刷新
				mPullToRefreshView.onHeaderRefreshComplete();
//				page = 1;
//				dateList.clear();
//				getData();
			}
		}, 1000);
	}
	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		mPullToRefreshView.postDelayed(new Runnable() {
			@Override
			public void run() {
				// 上滑加载
				mPullToRefreshView.onFooterRefreshComplete();
//				if (page == pageIndex) {
//					Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_LONG).show();
//				}else {
//					page++;
//					getData();
//				}

			}
		}, 1000);
	}

}
