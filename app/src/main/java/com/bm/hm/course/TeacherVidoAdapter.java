package com.bm.hm.course;

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
import com.bm.hm.util.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.List;

public class TeacherVidoAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<Course> list;

    public TeacherVidoAdapter(Context context, List<Course> list) {
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_teah_vido, parent,
                    false);
        }
        TextView tv_teah_title = ViewHolder.get(convertView, R.id.tv_teah_title);
        TextView tv_teah_price = ViewHolder.get(convertView, R.id.tv_teah_price);//已售
        TextView tv_teah_count = ViewHolder.get(convertView, R.id.tv_teah_count);//赞
        TextView tv_teah_content = ViewHolder.get(convertView, R.id.tv_teah_content);//免费
        ImageView iv_teah_video = ViewHolder.get(convertView, R.id.iv_teah_video);//图片
        ImageLoader.getInstance().displayImage(list.get(position).image.path, iv_teah_video);
        tv_teah_title.setText(list.get(position).name);
        tv_teah_content.setText(list.get(position).score + "");
        tv_teah_price.setText("已售" + list.get(position).sellNumber);
        tv_teah_count.setText(list.get(position).commentNumber + "");
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent in = new Intent(mContext, CourseInfoActivity.class);
                in.putExtra("course", (Serializable) list.get(position));
                mContext.startActivity(in);
            }
        });
        return convertView;
    }
}
