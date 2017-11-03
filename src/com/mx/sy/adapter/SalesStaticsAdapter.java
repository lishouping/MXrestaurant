package com.mx.sy.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.mx.sy.R;
import com.mx.sy.base.CommonBaseAdapter;
import com.mx.sy.base.CommonViewHolder;

// 服务列表adapter
public class SalesStaticsAdapter extends CommonBaseAdapter<HashMap<String, String>>{
	private Context context;
	private List<HashMap<String, String>> dateList;
	private int itemID;
	public SalesStaticsAdapter(Context context, List<HashMap<String, String>> datas,
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
		holder.setText(R.id.tv_item_num, holder.getPosition()+1+"");
		holder.setText(R.id.tv_food_name, bean.get("good_name"));
		holder.setText(R.id.tv_foodnum, bean.get("good_zs"));
		holder.setText(R.id.tv_total_price, "￥"+bean.get("good_ze"));
	}

}
