package com.bm.hm.course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.bean.Video;
import com.bm.hm.util.ViewHolder;

import java.util.List;

public class VideoListAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<Video> list;

    public VideoListAdapter(Context context, List<Video> list) {
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
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.pop_video_item, parent,
                    false);
        }

        TextView tv_video_name = ViewHolder.get(convertView,R.id.tv_video_name);
        tv_video_name.setText(list.get(position).name);

        if(list.get(position).select){
            tv_video_name.setTextColor(mContext.getResources().getColor(R.color.orange));
        }else{
            tv_video_name.setTextColor(mContext.getResources().getColor(R.color.white));
        }

        return convertView;
    }
}
