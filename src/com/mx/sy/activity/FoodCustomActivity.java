package com.mx.sy.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mx.sy.R;
import com.mx.sy.adapter.DishesClassAdapter;
import com.mx.sy.adapter.DishesNameAdapter;
import com.mx.sy.base.BaseActivity;

/**
 * @author lishouping
 * 点餐页面
 */
public class FoodCustomActivity extends BaseActivity{
	private LinearLayout ll_back;
	private TextView tv_title;
	
	private ListView lv_dishesclass;
	private ListView lv_dishesname;
	
	private List<HashMap<String, String>> disclassList;
	private DishesClassAdapter dishesClassAdapter;
	
	private List<HashMap<String, String>> disNameList;
	private DishesNameAdapter dishesNameAdapter;
	
	private Button btn_price_dis,btn_place_order;
	
	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		case R.id.btn_price_dis:
			
			break;
		case R.id.btn_place_order:
			Intent intent = new Intent(getApplicationContext(),OrderSubmitActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void initParms(Bundle parms) {
		// TODO Auto-generated method stub
	}

	@Override
	public View bindView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_foodcustom;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		ll_back = $(R.id.ll_back);
		tv_title = $(R.id.tv_title);
		lv_dishesclass = $(R.id.lv_dishesclass);
		
		lv_dishesname = $(R.id.lv_dishesname);
		
		btn_price_dis = $(R.id.btn_price_dis);
		btn_place_order = $(R.id.btn_place_order);
		
	}

	@Override
	protected void initdata() {
		// TODO Auto-generated method stub
		tv_title.setText("点餐页面");
		disclassList = new ArrayList<HashMap<String,String>>();
		for (int i = 0; i < 4; i++) {
			disclassList.add(new HashMap<String, String>());
		}
		dishesClassAdapter = new DishesClassAdapter(getApplicationContext(), disclassList, R.layout.item_classselect);
		lv_dishesclass.setAdapter(dishesClassAdapter);
		lv_dishesclass.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "点击时间", Toast.LENGTH_SHORT).show();
				dishesClassAdapter.setSelectedPosition(position);
				dishesClassAdapter.notifyDataSetChanged();
			}
		});
		
		disNameList = new ArrayList<HashMap<String,String>>();
		for (int i = 0; i < 10; i++) {
			disNameList.add(new HashMap<String, String>());
		}
		dishesNameAdapter = new DishesNameAdapter(getApplicationContext(), disNameList, R.layout.item_discname);
		lv_dishesname.setAdapter(dishesNameAdapter);
		
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		ll_back.setOnClickListener(this);
		btn_price_dis.setOnClickListener(this);
		btn_place_order.setOnClickListener(this);
	}

	@Override
	public void doBusiness(Context mContext) {
		// TODO Auto-generated method stub
		
	}

}
