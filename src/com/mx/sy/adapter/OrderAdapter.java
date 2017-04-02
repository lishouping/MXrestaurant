package com.mx.sy.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.mx.sy.base.CommonBaseAdapter;
import com.mx.sy.base.CommonViewHolder;

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
		
	}
}
