package com.bm.hm.cache;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.base.HMApplication;
import com.bm.hm.bean.Course;
import com.bm.hm.bean.Video;
import com.bm.hm.course.VideoViewPlayingActivity;
import com.bm.hm.util.ViewHolder;
import com.xckevin.download.CourseInfo;
import com.xckevin.download.DownloadTask;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

public class CachedVideoAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<DownloadTask> list;
    private boolean delete = false;
    private IOnCustomClickListener mListener;
    private int parentPosition;
    private Course course;

    public interface IOnCustomClickListener {

        void onCommentClick(int position);

    }

    public CachedVideoAdapter(Context context, List<DownloadTask> list, boolean delete,
                              IOnCustomClickListener mListener, int parentPosition,Course course) {
        this.mContext = context;
        this.list = list;
        this.delete = delete;
        this.mListener = mListener;
        this.parentPosition = parentPosition;
        this.course = course;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_has_cache_video, parent,
                    false);
        }

        TextView tv_video_name = ViewHolder.get(convertView, R.id.tv_video_name);
        tv_video_name.setText(list.get(position).getName());

        ImageView iv_delete = ViewHolder.get(convertView, R.id.iv_delete);
        if (delete) {
            iv_delete.setVisibility(View.VISIBLE);
        } else {
            iv_delete.setVisibility(View.GONE);
        }

        DecimalFormat df = new DecimalFormat("0.00");
        TextView tv_sumSize = ViewHolder.get(convertView, R.id.tv_sumSize);
        float sumSize = (float) list.get(position).getDownloadTotalSize() / 1024 / 1024;
        tv_sumSize.setText(df.format(sumSize) + "M");

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HMApplication.downloadMgr.deleteDownloadTask(list.get(position));

                CourseInfo info = HMApplication.downloadMgr.getCourseById(Integer.parseInt(list.get(position).getCourseId()));
                info.setHasCacheCount(info.getHasCacheCount() - 1);
                HMApplication.downloadMgr.updateCourse(info);

                File file = new File(list.get(position).getDownloadSavePath());
                if (file.isFile()) {
                    file.delete();
                }

                list.remove(position);
                notifyDataSetChanged();

                if (list == null || list.size() == 0) {
                    mListener.onCommentClick(parentPosition);
                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VideoViewPlayingActivity.class);
                String path = course.videoList.get(position).video.streamingPath;
                intent.setData(Uri.parse(path));
                intent.putExtra("position", getPosition(course.videoList, path));
                intent.putExtra("videoList",(Serializable)course.videoList);
                intent.putExtra("course",(Serializable)course);

                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    private int getPosition(List<Video> list,String path){
        int position = 0;
        if(list!=null && list.size()>0){
            for(int i= 0 ;i<list.size();i++){
                if(path.equals((list.get(i).video.streamingPath))){
                    position = i;
                    break;
                }
            }
        }
        return position;
    }


}
