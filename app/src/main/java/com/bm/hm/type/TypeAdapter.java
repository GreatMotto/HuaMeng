package com.bm.hm.type;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.bean.Type;
import com.bm.hm.util.ViewHolder;

import java.util.List;

public class TypeAdapter extends BaseAdapter {
    private Context mContext = null;
   private List<Type> list;

    public TypeAdapter(Context context, List<Type> list) {
        this.mContext = context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adpter_english_item, parent,
                    false);
        }
        TextView tv_type_content = ViewHolder.get(convertView, R.id.tv_type_content);

        if(list.get(position).isSelect()){
            tv_type_content.setBackgroundResource(R.drawable.type_bg_item_select);
            tv_type_content.setTextColor(mContext.getResources().getColor(R.color.orange));
        }else{
            tv_type_content.setBackgroundResource(R.drawable.type_bg_item_unselect);
            tv_type_content.setTextColor(mContext.getResources().getColor(R.color.item_content_color));
        }

        tv_type_content.setText(list.get(position).getName());

        return convertView;
    }

}
