package com.mx.sy.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.widget.ImageView;

import com.mx.sy.R;
import com.mx.sy.base.CommonBaseAdapter;
import com.mx.sy.base.CommonViewHolder;

/**
* <p>Title: MineUserAdapter<／p>
* <p>Description: <／p>
* <p>Company: LTGames<／p> 
* @author lishp
* @date 2017年4月2日
 */
public class MineUserAdapter extends CommonBaseAdapter<HashMap<String, Object>>{
	private Context context;
	private List<HashMap<String, Object>> dateList;
	private int itemID;
	public MineUserAdapter(Context context, List<HashMap<String, Object>> datas,
			int itemID) {
		super(context, datas, itemID);
		// TODO Auto-generated constructor stub
		this.dateList = datas;
		this.context = context;
		this.itemID = itemID;
	}

	@Override
	public void convert(CommonViewHolder holder, HashMap<String, Object> bean) {
		// TODO Auto-generated method stub
		holder.setText(R.id.tv_mine_content, bean.get("content")+"");
		ImageView imageView = holder.getView(R.id.img_type);
		imageView.setBackgroundResource((Integer) bean.get("contentImg"));
	}
}
