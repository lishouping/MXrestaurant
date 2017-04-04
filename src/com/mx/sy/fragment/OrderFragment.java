package com.mx.sy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mx.sy.R;
import com.mx.sy.activity.OrderConductActivity;
import com.mx.sy.activity.OrderEndActivity;
import com.mx.sy.activity.OrderUntreatedActivity;
import com.mx.sy.adapter.OrderAdapter;
import com.mx.sy.base.BaseFragment;
import com.mx.sy.common.PullToRefreshView;
import com.mx.sy.common.PullToRefreshView.OnFooterRefreshListener;
import com.mx.sy.common.PullToRefreshView.OnHeaderRefreshListener;

/**
* <p>Title: OrderFragment<／p>
* <p>Description: <／p>
* <p>Company: LTGames<／p> 
* @author lishp
* @date 2017年4月2日
 */
public class OrderFragment extends BaseFragment implements OnClickListener ,OnFooterRefreshListener,OnHeaderRefreshListener{
	private LinearLayout lin_order_nomanage,lin_order_manageing,lin_order_managend;
	private ListView lv_order;
	private List<HashMap<String, String>> dateList;
	private OrderAdapter orderAdapter;
	
	PullToRefreshView mPullToRefreshView;	
	
	private int selectBtnFlag = 0;
	
	@Override
	protected int setLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_order;
	}
	@Override
	protected void loadDate() {
		// TODO Auto-generated method stub
		super.loadDate();
		mPullToRefreshView = (PullToRefreshView) getActivity().findViewById(R.id.pullrefresh_order);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
	}
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		lin_order_nomanage = findViewById(R.id.lin_order_nomanage);
		lin_order_nomanage.setOnClickListener(this);
		lin_order_manageing = findViewById(R.id.lin_order_manageing);
		lin_order_manageing.setOnClickListener(this);
		lin_order_managend = findViewById(R.id.lin_order_managend);
		lin_order_managend.setOnClickListener(this);
		lv_order = findViewById(R.id.lv_order);
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
		orderAdapter = new OrderAdapter(getActivity(), dateList, R.layout.item_order_untreated);
		lv_order.setAdapter(orderAdapter);
		lv_order.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				if (selectBtnFlag==0) {
					intent.setClass(mActivity, OrderUntreatedActivity.class);
				}else if (selectBtnFlag==1) {
					intent.setClass(mActivity, OrderConductActivity.class);
				}else if (selectBtnFlag==2) {
					intent.setClass(mActivity, OrderEndActivity.class);
				}
				startActivity(intent);
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lin_order_nomanage:
			// 未处理
			selectBtnFlag = 0;
			changeBtnBg(selectBtnFlag);
			dateList.clear();
			for (int i = 0; i < 10; i++) {
				dateList.add(new HashMap<String, String>());
			}
			orderAdapter = new OrderAdapter(getActivity(), dateList, R.layout.item_order_untreated);
			lv_order.setAdapter(orderAdapter);
			orderAdapter.notifyDataSetChanged();
			break;
		case R.id.lin_order_manageing:
			// 正在用餐
			selectBtnFlag = 1;
			changeBtnBg(selectBtnFlag);
			dateList.clear();
			for (int i = 0; i < 10; i++) {
				dateList.add(new HashMap<String, String>());
			}
			orderAdapter = new OrderAdapter(getActivity(), dateList, R.layout.item_order_havingdinner);
			lv_order.setAdapter(orderAdapter);
			orderAdapter.notifyDataSetChanged();
			break;
		case R.id.lin_order_managend:
			// 已完成
			selectBtnFlag = 2;
			changeBtnBg(selectBtnFlag);
			dateList.clear();
			for (int i = 0; i < 10; i++) {
				dateList.add(new HashMap<String, String>());
			}
			orderAdapter = new OrderAdapter(getActivity(), dateList, R.layout.item_order_com);
			lv_order.setAdapter(orderAdapter);
			orderAdapter.notifyDataSetChanged();
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
	
	private void changeBtnBg(int selectTag){
		switch (selectTag) {
		case 0:
			lin_order_nomanage.setBackgroundColor(Color.rgb(208, 208, 208));
			lin_order_manageing.setBackgroundColor(Color.rgb(223, 90, 55));
			lin_order_managend.setBackgroundColor(Color.rgb(223, 90, 55));
			break;
		case 1:
			lin_order_nomanage.setBackgroundColor(Color.rgb(223, 90, 55));
			lin_order_manageing.setBackgroundColor(Color.rgb(208, 208, 208));
			lin_order_managend.setBackgroundColor(Color.rgb(223, 90, 55));
			break;
		case 2:
			lin_order_nomanage.setBackgroundColor(Color.rgb(223, 90, 55));
			lin_order_manageing.setBackgroundColor(Color.rgb(223, 90, 55));
			lin_order_managend.setBackgroundColor(Color.rgb(208, 208, 208));
			break;
		default:
			break;
		}
	}

}
