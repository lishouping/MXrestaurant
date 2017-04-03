package com.mx.sy.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.mx.sy.R;
import com.mx.sy.base.CommonBaseAdapter;
import com.mx.sy.base.CommonViewHolder;
import com.mx.sy.dialog.SweetAlertDialog;

// 服务列表adapter
public class ServiceAdapter extends CommonBaseAdapter<HashMap<String, String>>{
	private Context context;
	private List<HashMap<String, String>> dateList;
	private int itemID;
	public ServiceAdapter(Context context, List<HashMap<String, String>> datas,
			int itemID) {
		super(context, datas, itemID);
		// TODO Auto-generated constructor stub
		this.dateList = datas;
		this.context = context;
		this.itemID = itemID;
	}

	@Override
	public void convert(CommonViewHolder holder, HashMap<String, String> bean) {
		// TODO Auto-generated method stub
		if (itemID==R.layout.item_servicemanage) {
			holder.getView(R.id.lin_manage).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
	                .setTitleText("A102加水？")
	                //.setContentText("Won't be able to recover this file!")
	                .setCancelText("取消")
	                .setConfirmText("确定")
	                .showCancelButton(true)
	                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
	                    @Override
	                    public void onClick(SweetAlertDialog sweetAlertDialog) {
	                    	
	                    }
	                })
	                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
	                    @Override
	                    public void onClick(SweetAlertDialog sDialog) {
	                        sDialog.cancel();
	                    }
	                }).show();
				}
			});
		}else if (itemID==R.layout.item_serviceprocess) {
			
		}
	}

}
