package com.mx.sy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.mx.sy.R;
import com.mx.sy.activity.FoodCustomActivity;
import com.mx.sy.adapter.TablesAdapter;
import com.mx.sy.base.BaseFragment;
import com.mx.sy.common.PullToRefreshView;
import com.mx.sy.common.PullToRefreshView.OnFooterRefreshListener;
import com.mx.sy.common.PullToRefreshView.OnHeaderRefreshListener;
import com.mx.sy.dialog.ClassSelectDialog;

/**
 * @author Administrator
 * 桌台信息
 */
public class TableInfoFragment extends BaseFragment implements OnFooterRefreshListener,OnHeaderRefreshListener,OnClickListener{
	public static int TABLE_STATE = 100;
	public static int TABLE_CLASS = 101;
	
	
	private GridView gri_tables;
	private List<HashMap<String, String>> dateList;
	private TablesAdapter tablesAdapter;
	
	private Button btn_table_state,btn_table_class;
	
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
		btn_table_state = findViewById(R.id.btn_table_state);
		btn_table_state.setOnClickListener(this);
		btn_table_class = findViewById(R.id.btn_table_class);
		btn_table_class.setOnClickListener(this);
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
		gri_tables.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent();
				if (position==0) {
					// 点餐页面
					intent.setClass(getActivity(), FoodCustomActivity.class);
					
				}
				
				startActivity(intent);
			}
		});
		
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
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_table_state:
			Intent intent = new Intent(getActivity(),ClassSelectDialog.class);
			intent.putExtra("selectType", "100");
			startActivityForResult(intent, TABLE_STATE);
			break;
		case R.id.btn_table_class:
			Intent intent1 = new Intent(getActivity(),ClassSelectDialog.class);
			intent1.putExtra("selectType", "101");
			startActivityForResult(intent1, TABLE_CLASS);
			break;
		
		default:
			break;
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==TABLE_STATE) {
			btn_table_state.setText(data.getStringExtra("className"));
		}else if (resultCode==TABLE_CLASS) {
			btn_table_class.setText(data.getStringExtra("className"));
		}
		
		
	}
}
