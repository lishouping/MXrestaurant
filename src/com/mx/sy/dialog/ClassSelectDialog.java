package com.mx.sy.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mx.sy.R;
import com.mx.sy.adapter.TableTypeAdapter;
import com.mx.sy.base.BaseActivity;


/**
 * @author lishouping
 *
 */
public class ClassSelectDialog extends BaseActivity{
	public static int TABLE_STATE = 100;
	public static int TABLE_CLASS = 101;
	
	private ListView lv_chooseclass;
	private List<HashMap<String, String>> dateList;
	private TableTypeAdapter tableTypeAdapter;
	String classType;
	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initParms(Bundle parms) {
		// TODO Auto-generated method stub
		classType = parms.getString("selectType");
	}

	@Override
	public View bindView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.dialog_clsaaselect;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		lv_chooseclass = $(R.id.lv_chooseclass);
	}

	@Override
	protected void initdata() {
		// TODO Auto-generated method stub
		dateList = new  ArrayList<HashMap<String,String>>();
		for (int i = 0; i < 10; i++) {
			dateList.add(new HashMap<String, String>());
		}
		tableTypeAdapter = new TableTypeAdapter(getApplicationContext(), dateList, R.layout.item_classselect);
		lv_chooseclass.setAdapter(tableTypeAdapter);
		lv_chooseclass.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (classType.equals("100")) {
					Intent intent = new Intent();
					intent.putExtra("className", "正在用餐");
					setResult(TABLE_STATE, intent);
					finish();
				}else if (classType.equals("101")) {
					Intent intent = new Intent();
					intent.putExtra("className", "餐桌分裂1");
					setResult(TABLE_CLASS, intent);
					finish();
				}
			}
		});
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doBusiness(Context mContext) {
		// TODO Auto-generated method stub
		
	}

}
