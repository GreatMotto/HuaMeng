package com.bm.hm.course;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.bean.Video;
import com.bm.hm.util.ToastUtil;
import com.bm.hm.util.ViewHolder;

import java.util.List;

public class VideoAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<Video> list;

    public VideoAdapter(Context context,List<Video> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_video, parent,
                    false);
        }

        TextView tv_index = ViewHolder.get(convertView, R.id.tv_index);
        tv_index.setText(Integer.toString(position+1));

        TextView tv_video_name = ViewHolder.get(convertView,R.id.tv_video_name);
        tv_video_name.setText(list.get(position).name);

        return convertView;
    }
}
