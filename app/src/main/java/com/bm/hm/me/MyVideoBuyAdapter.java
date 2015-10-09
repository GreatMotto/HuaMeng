package com.bm.hm.me;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.bean.Course;
import com.bm.hm.course.CourseInfoActivity;
import com.bm.hm.util.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenb on 2015/4/27.
 */
public class MyVideoBuyAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<Course> list;

    public MyVideoBuyAdapter(Context context, List<Course> list) {
        this.mContext = context;
        this.list = list;
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
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_my_video_buy, viewGroup,
                    false);
        }
        ImageView iv_video = ViewHolder.get(convertView, R.id.iv_video);
        TextView tv_vedio_name = ViewHolder.get(convertView, R.id.tv_vedio_name);
        TextView tv_zhuangtai = ViewHolder.get(convertView, R.id.tv_zhuangtai);
        tv_vedio_name.setText(list.get(position).name + "");
        tv_zhuangtai.setText(list.get(position).content + "");
        ImageLoader.getInstance().displayImage(list.get(position).image.path,iv_video);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(mContext, CourseInfoActivity.class);
                in.putExtra("course",(Serializable)list.get(position));
                mContext.startActivity(in);
            }
        });
        return convertView;
    }
}
