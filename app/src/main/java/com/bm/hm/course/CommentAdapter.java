package com.bm.hm.course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.base.HMApplication;
import com.bm.hm.bean.Comment;
import com.bm.hm.util.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class CommentAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<Comment> list;

    public CommentAdapter(Context context,List<Comment> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent,
                    false);
        }

        ImageView iv_head = ViewHolder.get(convertView,R.id.iv_head);
        if(list.get(position).user.head!=null&&list.get(position).user.head.path!=null&&!list.get(position).user.head.equals("")) {
            ImageLoader.getInstance().displayImage(list.get(position).user.head.path, iv_head, HMApplication.getInstance().getOptionsCircle());
        }else{
            ImageLoader.getInstance().displayImage("drawable://"+R.mipmap.touxiang11, iv_head, HMApplication.getInstance().getOptionsCircle());
        }

        TextView tv_nick_name = ViewHolder.get(convertView,R.id.tv_nick_name);
        tv_nick_name.setText(list.get(position).user.nickname);

        TextView tv_content = ViewHolder.get(convertView,R.id.tv_content);
        tv_content.setText(list.get(position).content);

        TextView tv_create_date = ViewHolder.get(convertView,R.id.tv_create_date);
        tv_create_date.setText(list.get(position).createDate);

        return convertView;
    }
}
