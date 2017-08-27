package com.mx.sy.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.mx.sy.R;
import com.mx.sy.base.CommonBaseAdapter;
import com.mx.sy.base.CommonViewHolder;

// 餐桌分类列表adapter
public class TablesAdapter extends CommonBaseAdapter<HashMap<String, String>>{
	private Context context;
	private List<HashMap<String, String>> dateList;
	private int itemID;
	public TablesAdapter(Context context, List<HashMap<String, String>> datas,
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
		if (bean.get("table_status").equals("0")) {
			holder.setText(R.id.tv_tablestate, "未使用");
		}else if (bean.get("table_status").equals("1")) {
			holder.setText(R.id.tv_tablestate, "使用中");
		}else if (bean.get("table_status").equals("2")) {
			holder.setText(R.id.tv_tablestate, "预定中");
		}else if (bean.get("table_status").equals("3")) {
			holder.setText(R.id.tv_tablestate, "占用中");
		}else if (bean.get("table_status").equals("4")) {
			holder.setText(R.id.tv_tablestate, "其他");
		}
		holder.setText(R.id.tv_tablename, bean.get("table_name"));
	}

}
