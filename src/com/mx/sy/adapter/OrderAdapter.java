package com.mx.sy.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.mx.sy.R;
import com.mx.sy.activity.FoodCustomActivity;
import com.mx.sy.activity.OrderConductActivity;
import com.mx.sy.base.CommonBaseAdapter;
import com.mx.sy.base.CommonViewHolder;
import com.mx.sy.dialog.SweetAlertDialog;

/**
* <p>Title: OrderAdapter<／p>
* <p>Description: <／p>
* <p>Company: LTGames<／p> 
* @author lishp
* @date 2017年4月2日
 */
public class OrderAdapter  extends CommonBaseAdapter<HashMap<String, String>>{
	private Context context;
	private List<HashMap<String, String>> dateList;
	private int itemID;
	public OrderAdapter(Context context, List<HashMap<String, String>> datas,
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
		if (itemID==R.layout.item_order_untreated) {
			holder.getView(R.id.lv_push).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(context, "点击了推送", Toast.LENGTH_SHORT).show();
				}
			});
			holder.getView(R.id.lv_placeorder).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
	                .setTitleText("确定要打印订单？")
	                //.setContentText("Won't be able to recover this file!")
	                .setCancelText("取消")
	                .setConfirmText("确定")
	                .showCancelButton(true)
	                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
	                    @Override
	                    public void onClick(SweetAlertDialog sDialog) {
	                    	sDialog.cancel();
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
		}else if (itemID==R.layout.item_order_havingdinner) {
				holder.getView(R.id.lv_addfood).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context,FoodCustomActivity.class);
					context.startActivity(intent);
				}
				});
				// 结账
				holder.getView(R.id.lv_placeorder).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(context, "点击了结账", Toast.LENGTH_SHORT).show();
					}
				});
				
		}else if (itemID==R.layout.item_order_com) {
			holder.getView(R.id.lv_printorder).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
	                .setTitleText("确定要打印订单？")
	                //.setContentText("Won't be able to recover this file!")
	                .setCancelText("取消")
	                .setConfirmText("确定")
	                .showCancelButton(true)
	                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
	                    @Override
	                    public void onClick(SweetAlertDialog sDialog) {
	                    	sDialog.cancel();
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
		}
	}
}
