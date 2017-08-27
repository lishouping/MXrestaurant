package com.mx.sy.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.mx.sy.R;
import com.mx.sy.base.CommonBaseAdapter;
import com.mx.sy.base.CommonViewHolder;
import com.mx.sy.utils.SendMessage;

/**
 * <p>
 * Title: MineUserAdapter<／p>
 * <p>
 * Description:菜品名称及价格 <／p>
 * <p>
 * Company: LTGames<／p>
 * 
 * @author lishp
 * @date 2017年4月2日
 */
public class DishesNameAdapter extends
		CommonBaseAdapter<HashMap<String, String>> {
	private Context context;
	private List<HashMap<String, String>> dateList;
	private int itemID;
	private SendMessage sendMessage;

	public DishesNameAdapter(Context context,
			List<HashMap<String, String>> datas, int itemID) {
		super(context, datas, itemID);
		// TODO Auto-generated constructor stub
		this.dateList = datas;
		this.context = context;
		this.itemID = itemID;
		
		sendMessage = (SendMessage) context;
	}

	@Override
	public void convert(final CommonViewHolder holder, final HashMap<String, String> bean) {
		// TODO Auto-generated method stub

		holder.setText(R.id.tv_goods_name, bean.get("goods_name"));
		holder.setText(R.id.tv_goods_price, "￥" + bean.get("pre_price"));

		holder.getView(R.id.lin_addfood).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						sendMessage.SendMsg(100,holder.getPosition());
					}
				});
		// holder.getConvertView().setBackgroundColor(Color.rgb(223, 90, 55));
	}
}
