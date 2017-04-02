package com.mx.sy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import com.mx.sy.R;
import com.mx.sy.adapter.TablesAdapter;
import com.mx.sy.base.BaseFragment;
import com.mx.sy.common.PullToRefreshView;
import com.mx.sy.common.PullToRefreshView.OnFooterRefreshListener;
import com.mx.sy.common.PullToRefreshView.OnHeaderRefreshListener;

/**
 * @author Administrator
 * 桌台信息
 */
public class TableInfoFragment extends BaseFragment implements OnFooterRefreshListener,OnHeaderRefreshListener{
	private GridView gri_tables;
	private List<HashMap<String, String>> dateList;
	private TablesAdapter tablesAdapter;
	
	PullToRefreshView mPullToRefreshView;
	@Override
	protected int setLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_tableinfo;
	}
	@Override
	protected void loadDate() {
		// TODO Auto-generated method stub
		super.loadDate();
		mPullToRefreshView = (PullToRefreshView) getActivity().findViewById(R.id.pullrefresh_table);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
	}

	@Override
	protected void initData(Bundle arguments) {
		// TODO Auto-generated method stub
		super.initData(arguments);
		
	}
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		gri_tables =findViewById(R.id.gri_tables);
		super.initView();
	}
	@Override
	protected void onLazyLoad() {
		// TODO Auto-generated method stub
		super.onLazyLoad();
		dateList = new ArrayList<HashMap<String,String>>();
		for (int i = 0; i < 10; i++) {
			dateList.add(new HashMap<String, String>());
		}
		tablesAdapter = new TablesAdapter(getActivity(), dateList, R.layout.item_tables);
		gri_tables.setAdapter(tablesAdapter);
		
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
