package com.bm.hm.cache;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.base.HMApplication;
import com.bm.hm.bean.Course;
import com.bm.hm.bean.Video;
import com.bm.hm.course.VideoViewPlayingActivity;
import com.bm.hm.util.ViewHolder;
import com.xckevin.download.DownloadListener;
import com.xckevin.download.DownloadTask;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

public class CachingVideoAdapter extends BaseAdapter {

    private Context mContext = null;
    private DownloadListener listener;
    private List<DownloadTask> list;
    private boolean delete = false;
    private IOnCustomClickListener mListener;
    private int parentPosition;
    private Course course;

    public interface IOnCustomClickListener {

        void onCommentClick(int position);

    }

    public CachingVideoAdapter(Context context, DownloadListener listener, List<DownloadTask> list,boolean delete,
                               IOnCustomClickListener mListener,int parentPosition,Course course) {
        this.mContext = context;
        this.listener = listener;
        this.list = list;
        this.delete = delete;
        this.mListener = mListener;
        this.parentPosition = parentPosition;
        this.course = course;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_caching_video, parent,
                    false);
        }

        final DownloadTask task = list.get(position);

        HMApplication.downloadMgr.updateDownloadTaskListener(task, listener);

        TextView tv_video_name = ViewHolder.get(convertView, R.id.tv_video_name);
        tv_video_name.setText(task.getName());

        ProgressBar pb_download = (ProgressBar) ViewHolder.get(convertView, R.id.pb_download);
        pb_download.setMax(100);
        pb_download.setTag(task.getUrl() + "progress");

        pb_download.setProgress(0);
        if (task.getDownloadTotalSize() > 0) {
            int progress = (int) ((task.getDownloadFinishedSize() * 100) / task.getDownloadTotalSize());
            pb_download.setProgress(progress);
        }

        DecimalFormat df = new DecimalFormat("0.00");

        TextView tv_sumSize = (TextView) ViewHolder.get(convertView, R.id.tv_sumSize);
        tv_sumSize.setTag(task.getUrl() + "sumSize");
        float sumSize = (float) task.getDownloadTotalSize() / 1024 / 1024;
        tv_sumSize.setText(df.format(sumSize) + "M");

        TextView tv_downloadSize = (TextView) ViewHolder.get(convertView, R.id.tv_downloadSize);
        tv_downloadSize.setTag(task.getUrl() + "downloadSize");
        float downloadSize = (float) task.getDownloadFinishedSize() / 1024 / 1024;
        tv_downloadSize.setText(df.format(downloadSize) + "M/");

        TextView tv_status = ViewHolder.get(convertView, R.id.tv_status);
        tv_status.setTag(task.getUrl() + "status");

        ImageView iv_status = ViewHolder.get(convertView, R.id.iv_status);
        iv_status.setTag(task.getUrl() + "start");
        if (task.getStatus() == DownloadTask.STATUS_RUNNING) {
            iv_status.setImageResource(R.mipmap.download);
            tv_status.setText("下载中");
        } else {
            iv_status.setImageResource(R.mipmap.pause);
            if(task.getStatus()==DownloadTask.STATUS_PENDDING) {
                tv_status.setText("准备中");
            }else{
                tv_status.setText("暂停");
            }
        }

        iv_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (task.getStatus()) {
                    case DownloadTask.STATUS_CANCELED:
                        HMApplication.downloadMgr.addDownloadTask(task);
                        break;
                    case DownloadTask.STATUS_PAUSED:
                        HMApplication.downloadMgr.resumeDownload(task, listener);
                        task.setStatus(DownloadTask.STATUS_RUNNING);
                        break;
                    case DownloadTask.STATUS_FINISHED:
                        HMApplication.downloadMgr.getAllDownloadTask().remove(task);
                        break;
                    case DownloadTask.STATUS_RUNNING:
                        HMApplication.downloadMgr.pauseDownload(task, listener);
                        task.setStatus(DownloadTask.STATUS_PAUSED);
                        break;
                    default:
                        break;
                }
            }
        });

        ImageView iv_delete = ViewHolder.get(convertView, R.id.iv_delete);
        if(delete){
            iv_delete.setVisibility(View.VISIBLE);
        }else{
            iv_delete.setVisibility(View.GONE);
        }

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HMApplication.downloadMgr.cancelDownload(task,listener);

                list.remove(position);
                notifyDataSetChanged();

                if(list == null || list.size()==0) {
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
                intent.putExtra("videoList", (Serializable) course.videoList);
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
