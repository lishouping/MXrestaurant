package com.mx.sy.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.mx.sy.R;
import com.mx.sy.activity.FoodCustomActivity;
import com.mx.sy.base.CommonBaseAdapter;
import com.mx.sy.base.CommonViewHolder;
import com.mx.sy.utils.SendMessage;

// 服务列表adapter
public class ReserveAdapter extends CommonBaseAdapter<HashMap<String, String>> {
	private Context context;
	private List<HashMap<String, String>> dateList;
	private int itemID;
	private SendMessage sendMessage;

	public ReserveAdapter(Context context, List<HashMap<String, String>> datas,
			int itemID) {
		super(context, datas, itemID);
		// TODO Auto-generated constructor stub
		this.dateList = datas;
		this.context = context;
		this.itemID = itemID;
		sendMessage = (SendMessage) context;
	}

	@Override
	public void convert(CommonViewHolder holder,
			final HashMap<String, String> bean) {
		// TODO Auto-generated method stub
		holder.setText(R.id.tv_use_time, "需求时间:" + bean.get("use_time"));
		holder.setText(R.id.tv_personname, "预订人:" + bean.get("name"));
		holder.setText(R.id.tv_phone_number, "联系电话:" + bean.get("phone"));
		holder.setText(R.id.tv_table_name, "餐桌名称:" + bean.get("table_name"));
		holder.setText(R.id.tv_people_number, "用餐人数:" + bean.get("people_num"));
		holder.getView(R.id.lv_todiancan).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setClass(context, FoodCustomActivity.class);
						intent.putExtra("table_id", bean.get("table_id"));
						intent.putExtra("table_name", bean.get("table_name"));
						context.startActivity(intent);
						sendMessage.SendMsg(0, "1");
					}
				});
	}

}
