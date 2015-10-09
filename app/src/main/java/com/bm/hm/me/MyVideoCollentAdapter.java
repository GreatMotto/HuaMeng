package com.bm.hm.me;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.bean.Course;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.course.CourseInfoActivity;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.util.SharedPreferencesUtils;
import com.bm.hm.util.ToastUtil;
import com.bm.hm.util.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chenb on 2015/4/27.
 */
public class MyVideoCollentAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<Course> list;
    private int p;

    public MyVideoCollentAdapter(Context context, List<Course> list, Course course) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_my_video_collect, viewGroup,
                    false);

        }

        p = position;

        ImageView iv_video = ViewHolder.get(convertView, R.id.iv_video);
        final ImageView img_collect = ViewHolder.get(convertView, R.id.img_collect);
        TextView tv_jifen_num = ViewHolder.get(convertView, R.id.tv_jifen_num);
        TextView tv_vedio_name = ViewHolder.get(convertView, R.id.tv_vedio_name);
        tv_jifen_num.setText(list.get(position).score + "");
        tv_vedio_name.setText(list.get(position).name);
        ImageLoader.getInstance().displayImage(list.get(position).image.path, iv_video);
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent in = new Intent(mContext, CourseInfoActivity.class);
                in.putExtra("course", (Serializable) list.get(position));
                mContext.startActivity(in);
            }
        });

        img_collect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                cancelCollection(list.get(position).id);
            }
        });
        return convertView;
    }

    private void cancelCollection(int id) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("courseId", Integer.toString(id));
        param.put("userId", Integer.toString(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(mContext);
        request.HttpVolleyRequestPost(Urls.CANCEL_COLLECTION, param, BaseData.class, null,
                cancelCollectionSuccessListener(), null);

    }

    public Response.Listener<BaseData> cancelCollectionSuccessListener() {
        return new Response.Listener<BaseData>() {
            @Override
            public void onResponse(BaseData response) {
                list.remove(p);
                notifyDataSetChanged();
                ToastUtil.showToast(mContext, "取消收藏成功", Toast.LENGTH_SHORT);
            }
        };
    }
}
