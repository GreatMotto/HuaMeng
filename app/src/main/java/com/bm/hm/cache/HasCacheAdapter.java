package com.bm.hm.cache;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.base.HMApplication;
import com.bm.hm.bean.Course;
import com.bm.hm.course.CourseInfoActivity;
import com.bm.hm.util.ViewHolder;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xckevin.download.CourseInfo;
import com.xckevin.download.DownloadTask;

import java.util.List;

public class HasCacheAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<CourseInfo> cacheList;
    private boolean delete = false;

    public HasCacheAdapter(Context context,List<CourseInfo> cacheList,boolean delete) {
        this.mContext = context;
        this.cacheList = cacheList;
        this.delete = delete;
    }

    @Override
    public int getCount() {
        return cacheList.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_cache, parent,
                    false);
        }

        Gson gson = new Gson();
        final Course course = gson.fromJson(cacheList.get(position).courseInfo, Course.class);

        ImageView iv_video = ViewHolder.get(convertView,R.id.iv_video);
        ImageLoader.getInstance().displayImage(cacheList.get(position).getImagePath(), iv_video);

        TextView tv_course_name = ViewHolder.get(convertView,R.id.tv_course_name);
        tv_course_name.setText(cacheList.get(position).getName());

        TextView tv_cache_count = ViewHolder.get(convertView,R.id.tv_cache_count);
        tv_cache_count.setText("共缓存" + cacheList.get(position).getHasCacheCount() + "节课");

        ListView lv_video_list = ViewHolder.get(convertView,R.id.lv_video_list);
        List<DownloadTask> taskList= HMApplication.downloadMgr.getFinishedDownloadTask(String.valueOf(cacheList.get(position).getId()));

        CachedVideoAdapter adapter = new CachedVideoAdapter(mContext,taskList,delete,mListener,position,course);
        lv_video_list.setAdapter(adapter);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CourseInfoActivity.class);
                intent.putExtra("course", course);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    CachedVideoAdapter.IOnCustomClickListener mListener = new CachedVideoAdapter.IOnCustomClickListener() {
        @Override
        public void onCommentClick(int position) {
            cacheList.remove(position);
            notifyDataSetChanged();
        }
    };

}
