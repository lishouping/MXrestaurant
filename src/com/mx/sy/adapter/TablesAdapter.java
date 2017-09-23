package com.mx.sy.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;

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
			holder.setText(R.id.tv_tablestate, "空闲");
			holder.getView(R.id.lin_table_bg).setBackgroundResource(R.drawable.shape_tablesitem_bg);
			holder.setTextColor(R.id.tv_tablestate, Color.rgb(255, 81, 93));
			holder.setTextColor(R.id.tv_tablename, Color.rgb(255, 81, 93));
		}else if (bean.get("table_status").equals("1")) {
			holder.setText(R.id.tv_tablestate, "正在用餐");
			holder.getView(R.id.lin_table_bg).setBackgroundResource(R.drawable.shape_tablesitem_useringbg);
		}else if (bean.get("table_status").equals("2")) {
			holder.setText(R.id.tv_tablestate, "预定");
			holder.getView(R.id.lin_table_bg).setBackgroundResource(R.drawable.shape_tablesitem_yudingbg);
		}else if (bean.get("table_status").equals("3")) {
			holder.setText(R.id.tv_tablestate, "占用");
			holder.getView(R.id.lin_table_bg).setBackgroundResource(R.drawable.shape_tablesitem_zhanyongbg);
		}else if (bean.get("table_status").equals("4")) {
			holder.setText(R.id.tv_tablestate, "其他");
			holder.getView(R.id.lin_table_bg).setBackgroundResource(R.drawable.shape_tablesitem_otherbg);
		}
		holder.setText(R.id.tv_tablename, bean.get("table_name"));
	}

}
