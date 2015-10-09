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
import com.xckevin.download.DownloadListener;
import com.xckevin.download.DownloadTask;

import java.util.List;

public class CacheAdapter extends BaseAdapter {

    private Context mContext = null;
    private DownloadListener listener;
    private List<CourseInfo> list;
    private boolean delete = false;

    public CacheAdapter(Context context,DownloadListener listener,List<CourseInfo> list,boolean delete) {
        this.mContext = context;
        this.listener = listener;
        this.list = list;
        this.delete = delete;
    }

    @Override
    public int getCount() {
        return list !=null ? list.size() : 0;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_download, parent,
                    false);
        }

        Gson gson = new Gson();
        final Course course = gson.fromJson(list.get(position).courseInfo, Course.class);

        ImageView iv_course_img = ViewHolder.get(convertView,R.id.iv_course_img);
        ImageLoader.getInstance().displayImage(list.get(position).getImagePath(), iv_course_img);

        TextView tv_course_name = ViewHolder.get(convertView, R.id.tv_course_name);
        tv_course_name.setText(list.get(position).getName());

        TextView tv_count = ViewHolder.get(convertView, R.id.tv_count);
        tv_count.setText("共缓存"+list.get(position).getHasCacheCount()+"节课");

        ListView lv_video_list = ViewHolder.get(convertView, R.id.lv_video_list);
        List<DownloadTask> taskList= HMApplication.downloadMgr.getDownloadTaskByCourseId(String.valueOf(list.get(position).getId()));
        CachingVideoAdapter adapter = new CachingVideoAdapter(mContext, listener, taskList, delete, mListener, position, course);
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

    CachingVideoAdapter.IOnCustomClickListener mListener = new CachingVideoAdapter.IOnCustomClickListener() {
        @Override
        public void onCommentClick(int position) {
            list.remove(position);
            notifyDataSetChanged();
        }
    };

}
