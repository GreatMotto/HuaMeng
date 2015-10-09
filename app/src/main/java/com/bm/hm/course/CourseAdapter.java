package com.bm.hm.course;

import android.content.Context;
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

import java.util.List;

public class CourseAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<Course> list;

    public CourseAdapter(Context context,List<Course> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_course, parent,
                    false);
        }

        ImageView iv_course_img = ViewHolder.get(convertView,R.id.iv_course_img);
        if(list.get(position).image!=null) {
            ImageLoader.getInstance().displayImage(list.get(position).image.path,iv_course_img);
        }

        TextView tv_title = ViewHolder.get(convertView, R.id.tv_title);
        tv_title.setText(list.get(position).name);

        TextView tv_score = ViewHolder.get(convertView, R.id.tv_score);
        if(list.get(position).score==0) {
            tv_score.setText("免费");
        }else{
            tv_score.setText(list.get(position).score + "积分");
        }        TextView tv_sellNumber = ViewHolder.get(convertView, R.id.tv_sellNumber);
        tv_sellNumber.setText("已售:"+list.get(position).sellNumber);

        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        tv_name.setText(list.get(position).teacher.name);

        return convertView;
    }
}
