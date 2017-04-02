package com.mx.sy.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonBaseAdapter<T> extends BaseAdapter {
    private Context mContext;
    private List<T> mDatas;
    private int  itemID;
    public CommonBaseAdapter(Context context,List<T> datas,int itemID) {
        this.mContext = context;
        this.mDatas = datas;
        this.itemID=itemID;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewHolder holder = CommonViewHolder.getInstance(mContext, itemID, position, convertView, parent);
        convert(holder,mDatas.get(position));
        return holder.getConvertView();
    }
    /**
     * 填充holder里面控件的数据
     * @param holder
     * @param bean
     */
    public abstract void convert(CommonViewHolder holder,T bean);
}