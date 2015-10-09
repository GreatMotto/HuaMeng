package com.bm.hm.course;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.base.HMApplication;
import com.bm.hm.bean.Course;
import com.bm.hm.bean.Video;
import com.bm.hm.util.ToastUtil;
import com.google.gson.Gson;
import com.xckevin.download.CourseInfo;
import com.xckevin.download.DownloadListener;
import com.xckevin.download.DownloadTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CacheListActivity extends BaseActivity implements DownloadListener {

    private ListView lv_videos;
    private List<Video> list;
    private ImageView iv_selectAll;
    private View btn_download;
    private CacheListAdapter adapter;
    private Course course;
    private TextView tv_course_name;
    private List<Video> selectList = new ArrayList<Video>();
    private ListView lv_courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_cache_list);

        initView();
    }

    private void initView() {
        View view = View.inflate(getApplicationContext(), R.layout.fg_cache, null);
        lv_courses = (ListView) view.findViewById(R.id.lv_courses);

        course = (Course) getIntent().getSerializableExtra("course");

        tv_course_name = (TextView) findViewById(R.id.tv_course_name);
        tv_course_name.setText(course.name);

        list = new ArrayList<Video>();
        list = (List<Video>) getIntent().getSerializableExtra("videoList");
        for (Video video : list) {
            DownloadTask task = HMApplication.downloadMgr.findDownloadTaskById(String.valueOf(video.id));
            if(task != null && task.getId()!=null){
                video.setEnable(true);
            }else {
                video.setEnable(false);
            }
            video.setSelect(false);
        }

        lv_videos = (ListView) findViewById(R.id.lv_videos);
        adapter = new CacheListAdapter(this, list);
        lv_videos.setAdapter(adapter);
        lv_videos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list.get(position).setSelect(!list.get(position).select);
                adapter.notifyDataSetChanged();
                if (list.get(position).isSelect()) {
                    selectList.add(list.get(position));
                } else {
                    selectList.remove(list.get(position));
                }

                if (isAllSelect()) {
                    iv_selectAll.setBackgroundResource(R.mipmap.select);
                } else {
                    iv_selectAll.setBackgroundResource(R.mipmap.unselect);
                }
            }
        });

        initTitleBarWithBack("缓存列表");

        iv_selectAll = (ImageView) findViewById(R.id.iv_selectAll);
        iv_selectAll.setOnClickListener(this);
        iv_selectAll.setTag(false);

        btn_download = findViewById(R.id.btn_download);
        btn_download.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_selectAll:
                select();
                break;
            case R.id.btn_download:
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    CourseInfo courseInfo = new CourseInfo();
                    courseInfo.setId(course.id);
                    courseInfo.setName(course.name);
                    courseInfo.setImagePath(course.image.path);
                    Gson gson = new Gson();
                    courseInfo.setCourseInfo(gson.toJson(course));
                    if (selectList != null && selectList.size() > 0) {
                        for (Video video : selectList) {
                            DownloadTask task = new DownloadTask();
                            task.setUrl(video.video.path);
                            task.setName(video.name);
                            task.setId(String.valueOf(video.id));
                            task.setCourseId(String.valueOf(course.id));
                            task.setStreamingPath(video.video.streamingPath);
                            HMApplication.downloadMgr.addDownloadTask(task, this, courseInfo);
                        }
                        ToastUtil.showToast(getApplicationContext(), "已经加入缓存列表", Toast.LENGTH_SHORT);

                        Intent intent = new Intent(getApplicationContext(), CourseInfoActivity.class);
                        intent.putExtra("course", (Serializable) course);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtil.showToast(getApplicationContext(), "请选择视频", Toast.LENGTH_SHORT);
                    }
                }else{
                    ToastUtil.showToast(getApplicationContext(), "该手机无SD卡，无法下载", Toast.LENGTH_SHORT);
                }

                break;
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(getApplicationContext(), CourseInfoActivity.class);
            intent.putExtra("course", (Serializable) course);
            startActivity(intent);
            finish();
        }
    };

    private void select() {
        if ((boolean) iv_selectAll.getTag()) {
            iv_selectAll.setBackgroundResource(R.mipmap.select);
            iv_selectAll.setTag(!(boolean) iv_selectAll.getTag());
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setSelect(!(boolean) iv_selectAll.getTag());
            }

            selectList.addAll(list);
        } else {
            iv_selectAll.setBackgroundResource(R.mipmap.unselect);
            iv_selectAll.setTag(!(boolean) iv_selectAll.getTag());
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setSelect(!(boolean) iv_selectAll.getTag());
            }

            selectList.removeAll(list);
        }

        adapter.notifyDataSetChanged();
    }

    // 是否全选
    private boolean isAllSelect() {
        boolean result = true;
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).select) {
                result = false;
                break;
            }
        }
        return result;
    }

    @Override
    public void onDownloadStart(DownloadTask downloadTask) {

    }

    @Override
    public void onDownloadUpdated(DownloadTask downloadTask, long l, long l1) {

    }

    @Override
    public void onDownloadPaused(DownloadTask downloadTask) {

    }

    @Override
    public void onDownloadResumed(DownloadTask downloadTask) {

    }

    @Override
    public void onDownloadSuccessed(DownloadTask downloadTask) {

    }

    @Override
    public void onDownloadCanceled(DownloadTask downloadTask) {

    }

    @Override
    public void onDownloadFailed(DownloadTask downloadTask) {
    }

    @Override
    public void onDownloadRetry(DownloadTask downloadTask) {
    }

}
