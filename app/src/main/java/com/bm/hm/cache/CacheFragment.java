package com.bm.hm.cache;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.base.HMApplication;
import com.xckevin.download.CourseInfo;
import com.xckevin.download.DownloadListener;
import com.xckevin.download.DownloadTask;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CacheFragment extends Fragment implements View.OnClickListener,DownloadListener {

    private View view;
    private TextView tv_title, tv_has_doweload, tv_doweloading, tv_right,tv_tip;
    private ListView lv_courses;
    private ImageView iv_no_video;

    private List<CourseInfo> hasCourseList = new ArrayList<CourseInfo>();
    private HasCacheAdapter hasAdapter;
    private int tabIndex = 1;
    private boolean isEdit = false;

    private List<CourseInfo> cachingList = new ArrayList<CourseInfo>();
    private CacheAdapter adapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        initView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }

        if (view == null) {
            view = inflater.inflate(R.layout.fg_cache, container, false);
        }

        return view;
    }

    private void initView() {
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText("缓存");

        tv_right = (TextView) view.findViewById(R.id.tv_right);
        tv_right.setText("编辑");
        tv_right.setOnClickListener(this);

        iv_no_video = (ImageView) view.findViewById(R.id.iv_no_video);
        tv_tip = (TextView) view.findViewById(R.id.tv_tip);

        hasCourseList = HMApplication.downloadMgr.getCourseByStatus(DownloadTask.STATUS_FINISHED);

        lv_courses = (ListView) view.findViewById(R.id.lv_courses);
        hasAdapter = new HasCacheAdapter(getActivity().getApplicationContext(), hasCourseList,isEdit);
        lv_courses.setAdapter(hasAdapter);
        if(hasCourseList== null || hasCourseList.size()==0){
            iv_no_video.setVisibility(View.VISIBLE);
            tv_tip.setText("暂无已下载视频");
            tv_tip.setVisibility(View.VISIBLE);
        }else{
            iv_no_video.setVisibility(View.INVISIBLE);
            tv_tip.setVisibility(View.INVISIBLE);
        }

        tv_has_doweload = (TextView) view.findViewById(R.id.tv_has_doweload);
        tv_has_doweload.setOnClickListener(this);

        tv_doweloading = (TextView) view.findViewById(R.id.tv_doweloading);
        tv_doweloading.setOnClickListener(this);

        onClick(tv_has_doweload);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_has_doweload:
                tv_has_doweload.setTextColor(getResources().getColor(R.color.white));
                tv_has_doweload.setBackground(getResources().getDrawable(R.mipmap.cache_tab1_select));

                tv_doweloading.setTextColor(getResources().getColor(R.color.orange));
                tv_doweloading.setBackground(getResources().getDrawable(R.mipmap.cache_tab2));

                hasCourseList = HMApplication.downloadMgr.getCourseByStatus(DownloadTask.STATUS_FINISHED);
                hasAdapter = new HasCacheAdapter(getActivity().getApplicationContext(), hasCourseList,isEdit);
                lv_courses.setAdapter(hasAdapter);
                if(hasCourseList== null || hasCourseList.size()==0){
                    iv_no_video.setVisibility(View.VISIBLE);
                    tv_tip.setText("暂无已下载视频");
                    tv_tip.setVisibility(View.VISIBLE);
                }else{
                    iv_no_video.setVisibility(View.INVISIBLE);
                    tv_tip.setVisibility(View.INVISIBLE);
                }

                tabIndex = 1;
                tv_right.setText("编辑");
                isEdit = false;
                edit();
                break;
            case R.id.tv_doweloading:
                tv_has_doweload.setTextColor(getResources().getColor(R.color.orange));
                tv_has_doweload.setBackground(getResources().getDrawable(R.mipmap.cache_tab1));

                tv_doweloading.setTextColor(getResources().getColor(R.color.white));
                tv_doweloading.setBackground(getResources().getDrawable(R.mipmap.cache_tab2_select));

                cachingList = HMApplication.downloadMgr.getCourseByDownloading();
                adapter = new CacheAdapter(getActivity(), this,cachingList,isEdit);
                lv_courses.setAdapter(adapter);
                if(cachingList== null || cachingList.size()==0){
                    iv_no_video.setVisibility(View.VISIBLE);
                    tv_tip.setText("暂无下载中视频");
                    tv_tip.setVisibility(View.VISIBLE);
                }else{
                    iv_no_video.setVisibility(View.INVISIBLE);
                    tv_tip.setVisibility(View.INVISIBLE);
                }

                tabIndex = 2;
                tv_right.setText("编辑");
                isEdit = false;
                edit();
                break;
            case R.id.tv_right:
                if (tv_right.getText().equals("编辑")) {
                    isEdit = true;
                    tv_right.setText("完成");
                } else {
                    isEdit = false;
                    tv_right.setText("编辑");
                }

                edit();
                break;
        }
    }

    private void edit() {
        if (tabIndex == 1) {
            hasAdapter = new HasCacheAdapter(getActivity(),hasCourseList,isEdit);
            lv_courses.setAdapter(hasAdapter);
        } else {
            adapter = new CacheAdapter(getActivity(), this,cachingList,isEdit);
            lv_courses.setAdapter(adapter);
        }
    }

    @Override
    public void onDownloadStart(DownloadTask downloadTask) {
        ImageView iv_status = (ImageView)lv_courses.findViewWithTag((downloadTask.getUrl()+"start"));
        if(iv_status!=null) {
            iv_status.setImageResource(R.mipmap.download);
        }

        TextView tv_status = (TextView)lv_courses.findViewWithTag((downloadTask.getUrl() + "status"));
        if(tv_status!=null) {
            tv_status.setText("下载中");
        }

        Log.e("download:", "start");
    }

    @Override
    public void onDownloadUpdated(DownloadTask downloadTask, long l, long l1) {
        ImageView iv_status = (ImageView)lv_courses.findViewWithTag((downloadTask.getUrl()+"start"));
        if(iv_status!=null) {
            iv_status.setImageResource(R.mipmap.download);
        }

        TextView tv_status = (TextView) lv_courses.findViewWithTag((downloadTask.getUrl() + "status"));
        if(tv_status!=null) {
             tv_status.setText("下载中");
        }

        setValue(downloadTask);
    }

    @Override
    public void onDownloadPaused(DownloadTask downloadTask) {
        ImageView iv_status = (ImageView)lv_courses.findViewWithTag((downloadTask.getUrl()+"start"));
        iv_status.setImageResource(R.mipmap.pause);

        TextView tv_status = (TextView)lv_courses.findViewWithTag((downloadTask.getUrl() + "status"));
        tv_status.setText("暂停");

        Log.e("download:", "pause" + downloadTask.getStatus());
    }

    @Override
    public void onDownloadResumed(DownloadTask downloadTask) {
        ImageView iv_status = (ImageView)lv_courses.findViewWithTag((downloadTask.getUrl()+"start"));
        iv_status.setImageResource(R.mipmap.download);

        TextView tv_status = (TextView)lv_courses.findViewWithTag((downloadTask.getUrl() + "status"));
        tv_status.setText("下载中");

        Log.e("download:", "resume");
    }

    @Override
    public void onDownloadSuccessed(DownloadTask downloadTask) {
        TextView tv_status = (TextView)lv_courses.findViewWithTag((downloadTask.getUrl() + "status"));
        if(tv_status!=null){
            tv_status.setText("下载完成");
        }

        cachingList = HMApplication.downloadMgr.getCourseByDownloading();
        adapter = new CacheAdapter(getActivity(), this,cachingList,isEdit);
        lv_courses.setAdapter(adapter);

        Log.e("download:", "success==="+downloadTask.getStatus());
    }

    @Override
    public void onDownloadCanceled(DownloadTask downloadTask) {
        TextView tv_status = (TextView)lv_courses.findViewWithTag((downloadTask.getUrl() + "status"));
        if(tv_status!=null) {
            tv_status.setText("取消下载");
        }

    }

    @Override
    public void onDownloadFailed(DownloadTask downloadTask) {
        TextView tv_status = (TextView)lv_courses.findViewWithTag((downloadTask.getUrl() + "status"));
        if(tv_status!=null) {
            tv_status.setText("下载失败");
        }
    }

    @Override
    public void onDownloadRetry(DownloadTask downloadTask) {
    }

    private void setValue(DownloadTask downloadTask){
        TextView tv_sumSize = (TextView)lv_courses.findViewWithTag((downloadTask.getUrl() + "sumSize"));

        DecimalFormat df = new DecimalFormat("0.00");
        if(tv_sumSize!=null) {
            float sumSize = (float) downloadTask.getDownloadTotalSize() / 1024 / 1024;
            tv_sumSize.setText(df.format(sumSize) + "M");
        }

        float downloadSize = (float)downloadTask.getDownloadFinishedSize()/1024/1024;
        TextView tv_downloadSize = (TextView)lv_courses.findViewWithTag((downloadTask.getUrl() + "downloadSize"));
        if(tv_downloadSize!=null) {
            tv_downloadSize.setText(df.format(downloadSize) + "M/");
        }

        int progress = (int)((downloadTask.getDownloadFinishedSize()*100)/downloadTask.getDownloadTotalSize());
        ProgressBar pb = (ProgressBar)lv_courses.findViewWithTag(downloadTask.getUrl()+"progress");
        if(tv_downloadSize!=null) {
            tv_downloadSize.setText(df.format(downloadSize) + "M/");
            pb.setProgress(progress);
        }
    }
}
