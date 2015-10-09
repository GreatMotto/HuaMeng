package com.bm.hm.course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.bean.Video;
import com.bm.hm.util.ViewHolder;

import java.util.List;

public class CacheListAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<Video> list;

    public CacheListAdapter(Context context,List<Video> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_cache_video, parent,
                    false);
        }

        TextView tv_title = ViewHolder.get(convertView,R.id.tv_title);
        tv_title.setText(list.get(position).name);

        ImageView iv_select = ViewHolder.get(convertView,R.id.iv_select);
        if(!list.get(position).enable) {
            iv_select.setVisibility(View.VISIBLE);
//            iv_select.setEnabled(false);
            if (list.get(position).select) {
                iv_select.setBackgroundResource(R.mipmap.select);
            } else {
                iv_select.setBackgroundResource(R.mipmap.unselect);
            }
        }else{
            iv_select.setVisibility(View.INVISIBLE);
//            iv_select.setEnabled(true);
        }

        return convertView;
    }

}
