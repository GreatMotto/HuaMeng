package com.bm.hm.me;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.base.HMApplication;
import com.bm.hm.bean.User;
import com.bm.hm.course.TeacherVidoActivity;
import com.bm.hm.util.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.List;


/**
 * Created by chenb on 2015/4/30.
 */
public class MyTeacherAdapter extends BaseAdapter {
    private Context context;
    private List<User> list;

    public MyTeacherAdapter(Context context, List<User> list) {
        super();
        this.context = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_teacher, parent,
                    false);
            //convertView=View.inflate(context, R.layout.item_my_teacher,null);
        }
        ImageView iv_touxaing = ViewHolder.get(convertView, R.id.iv_touxaing);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_tf = ViewHolder.get(convertView, R.id.tv_tf);
        TextView tv_kec = ViewHolder.get(convertView, R.id.tv_kec);
        tv_name.setText(list.get(position).name);
        tv_tf.setText(list.get(position).teacherProjectStr);
        tv_kec.setText(list.get(position).teacherSubjectStr);

        if(!TextUtils.isEmpty(list.get(position).head.path)){
        ImageLoader.getInstance().displayImage(list.get(position).head.path, iv_touxaing,
                HMApplication.getInstance().getOptionsCircle());}
        else{
            iv_touxaing.setImageResource(R.mipmap.touxiang11);
        }
        iv_touxaing.setScaleType(ImageView.ScaleType.CENTER_CROP);

        //item 点击事件
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity base = (BaseActivity) context;
                Intent intent = new Intent(context, TeacherVidoActivity.class);
                intent.putExtra("teacher", (Serializable) list.get(position));
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
