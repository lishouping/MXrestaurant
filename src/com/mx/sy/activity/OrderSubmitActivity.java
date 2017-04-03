package com.mx.sy.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mx.sy.R;
import com.mx.sy.adapter.OrderSubmitAdapter;
import com.mx.sy.base.BaseActivity;
import com.mx.sy.dialog.SweetAlertDialog;
import com.tnktech.weight.TNKListView;



/**
 * @author lishouping
 * 订单确认页面 可以进行删除菜品的操作
 */
public class OrderSubmitActivity extends BaseActivity{
	private LinearLayout ll_back;
	private TextView tv_title;
	private TNKListView lv_order_dinner;
	private List<HashMap<String, String>> dateList;
	private OrderSubmitAdapter orderSubmitAdapter;
	@Override
	public void widgetClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
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
		return R.layout.activity_ordersubmit;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		ll_back = $(R.id.ll_back);
		tv_title = $(R.id.tv_title);
		lv_order_dinner = $(R.id.lv_order_dinner);
	}

	@Override
	protected void initdata() {
		// TODO Auto-generated method stub
		tv_title.setText("订单详情");
		dateList = new ArrayList<HashMap<String,String>>();
		for (int i = 0; i < 10; i++) {
			dateList.add(new HashMap<String, String>());
		}
		orderSubmitAdapter = new OrderSubmitAdapter(getApplicationContext(), dateList, R.layout.item_order_foodlv);
		lv_order_dinner.setAdapter(orderSubmitAdapter);
		lv_order_dinner.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				
			}
		});
		lv_order_dinner.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                    final int position, long id) {
            	new SweetAlertDialog(OrderSubmitActivity.this, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("确定要删除菜品吗？")
                //.setContentText("Won't be able to recover this file!")
                .setCancelText("取消")
                .setConfirmText("确定")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                    	dateList.remove(position);
                    	orderSubmitAdapter.notifyDataSetChanged();
                    	sDialog.cancel();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                }).show();
            	
                return true;
            }
 
        });
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		ll_back.setOnClickListener(this);
	}

	@Override
	public void doBusiness(Context mContext) {
		// TODO Auto-generated method stub
		
	}

}
