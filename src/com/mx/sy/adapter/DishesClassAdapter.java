package com.mx.sy.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.mx.sy.R;
import com.mx.sy.base.CommonBaseAdapter;
import com.mx.sy.base.CommonViewHolder;

/**
* <p>Title: MineUserAdapter<／p>
* <p>Description:菜品分类 <／p>
* <p>Company: LTGames<／p> 
* @author lishp
* @date 2017年4月2日
 */
public class DishesClassAdapter extends CommonBaseAdapter<HashMap<String, String>>{
	private Context context;
	private List<HashMap<String, String>> dateList;
	private int itemID;
	private int selectedPosition = 0;// 选中的位置
	
	public DishesClassAdapter(Context context, List<HashMap<String, String>> datas,
			int itemID) {
		super(context, datas, itemID);
		// TODO Auto-generated constructor stub
		this.dateList = datas;
		this.context = context;
		this.itemID = itemID;
	}
	public void setSelectedPosition(int position) {
	    selectedPosition = position;
	}
	@Override
	public void convert(CommonViewHolder holder, HashMap<String, String> bean) {
		// TODO Auto-generated method stub
		
		int position = holder.getPosition();
		if (selectedPosition == position) {
			holder.getView(R.id.lin_class).setBackgroundColor(Color.rgb(208, 208, 208));
		} else {
			holder.getView(R.id.lin_class).setBackgroundColor(Color.rgb(223, 90, 55));
		}
	}
}
