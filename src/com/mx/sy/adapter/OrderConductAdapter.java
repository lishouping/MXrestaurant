package com.mx.sy.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import com.mx.sy.R;
import com.mx.sy.base.CommonBaseAdapter;
import com.mx.sy.base.CommonViewHolder;

/**
* <p>Title: MineUserAdapter<／p>
* <p>Description:订单详情确认下单 <／p>
* <p>Company: LTGames<／p> 
* @author lishp
* @date 2017年4月2日
 */
public class OrderConductAdapter extends CommonBaseAdapter<HashMap<String, String>>{
	private Context context;
	private List<HashMap<String, String>> dateList;
	private int itemID;
	public OrderConductAdapter(Context context, List<HashMap<String, String>> datas,
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
		if (bean.get("if_up").equals("1")) {
			holder.setText(R.id.tv_orderfoodname, bean.get("good_name")+"(已上菜)");
		}else {
			holder.setText(R.id.tv_orderfoodname, bean.get("good_name"));
		}
		//holder.setText(R.id.tv_orderfoodname, bean.get("good_name"));
		holder.setText(R.id.tv_pricenum, bean.get("good_price"));
		holder.setText(R.id.number,"X"+bean.get("good_num"));
		holder.setText(R.id.tv_totalprice, "￥"+bean.get("good_total_price"));
//		holder.getConvertView().setBackgroundColor(Color.rgb(223, 90, 55));
	}
}
