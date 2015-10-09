package com.bm.hm.me;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.bean.timelineContent;
import com.bm.hm.bean.timelineTeacher;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.util.DialogUtils;
import com.bm.hm.util.SharedPreferencesUtils;
import com.bm.hm.view.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liuz on 2015/5/4.
 */
public class TimeLineActivity extends BaseActivity {

    private TextView tv_time_line_no, tv_student, tv_stu_number, tv_class, tv_totalHours, tv_examination, tv_examtimeStr, tv_assistant,tv_title2;
    private TimeLineAdapter mTimeLineAdapter;
    private ListView lv_time_line_list;
    private ImageView iv_time_line_back, riv_time_line_pic,iv_back1;
    private TextView tv_te1;
    private TextView tv_te2;
    private TextView tv_kc1;
    private TextView tv_kc2;
    private LinearLayout ll_te;
    private RelativeLayout rl_no_time_line, rl_yes_time_line;
    private TextView tv_Assistant_phone;
    private RefreshLayout sr_ref_timeline;
    private List<timelineContent> list = new ArrayList<timelineContent>();
    private int currPage = 1;
    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_time_line);
        initView();
//        initDate();
        getData();
        DialogUtils.showProgressDialog("正在加载", this);
    }

    private void initView() {

        headerView= LayoutInflater.from(this).inflate(R.layout.ac_time_line2,null,false);
        rl_no_time_line = (RelativeLayout) headerView.findViewById(R.id.rl_no_time_line);
        rl_yes_time_line = (RelativeLayout) headerView.findViewById(R.id.rl_yes_time_line);
        iv_back1=(ImageView)headerView.findViewById(R.id.iv_back1);
        iv_back1.setOnClickListener(this);
        tv_title2=(TextView)headerView.findViewById(R.id.tv_title2);
        tv_time_line_no = (TextView)headerView.findViewById(R.id.tv_time_line_no);
        iv_time_line_back = (ImageView) headerView.findViewById(R.id.iv_time_line_back);
        iv_time_line_back.setOnClickListener(this);
        riv_time_line_pic = (ImageView)headerView. findViewById(R.id.riv_time_line_pic);


        tv_student = (TextView) headerView.findViewById(R.id.tv_student);
        tv_stu_number = (TextView)headerView.findViewById(R.id.tv_stu_number);
        tv_class = (TextView) headerView.findViewById(R.id.tv_class);
        tv_totalHours = (TextView) headerView.findViewById(R.id.tv_totalHours);
        tv_examination = (TextView)headerView.findViewById(R.id.tv_examination);
        tv_examtimeStr = (TextView) headerView.findViewById(R.id.tv_examtimeStr);
        tv_te1 = (TextView) headerView.findViewById(R.id.tv_te1);
        tv_kc1 = (TextView) headerView.findViewById(R.id.tv_kc1);
        tv_te2 = (TextView) headerView.findViewById(R.id.tv_te2);
        tv_kc2 = (TextView)headerView.findViewById(R.id.tv_kc2);
        ll_te = (LinearLayout) headerView.findViewById(R.id.ll_te);
        tv_assistant = (TextView)headerView.findViewById(R.id.tv_assistant);
        tv_Assistant_phone = (TextView) headerView.findViewById(R.id.tv_Assistant_phone);

        lv_time_line_list = (ListView) findViewById(R.id.lv_time_line_list);

        lv_time_line_list.addHeaderView(headerView);
        mTimeLineAdapter = new TimeLineAdapter(TimeLineActivity.this, list);
        lv_time_line_list.setAdapter(mTimeLineAdapter);


        sr_ref_timeline = (RefreshLayout) findViewById(R.id.sr_ref_timeline);
        sr_ref_timeline.setColorScheme(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        lv_time_line_list.addFooterView(sr_ref_timeline.getFootView(), null, false);
        lv_time_line_list.setOnScrollListener(sr_ref_timeline);
        sr_ref_timeline.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sr_ref_timeline.setLoading(true);
                sr_ref_timeline.setLoad_More(true);
                currPage = 1;
                getData();
            }
        });
        sr_ref_timeline.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                currPage++;
                getData();
            }
        });
    }

    //加载圆形的头像
//    private void initDate() {
//        String path = SharedPreferencesUtils.getInstance().getString(Constant.PHOTO);
//        if (!TextUtils.isEmpty(path)) {
//            ImageLoader.getInstance().displayImage(path, riv_time_line_pic,
//                    HMApplication.getInstance().getOptionsCircle());
//        } else {
//            riv_time_line_pic.setImageResource(R.mipmap.touxiang11);
//            riv_time_line_pic.setScaleType(ImageView.ScaleType.FIT_XY);
//        }
//    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_time_line_back:
                finish();
                break;
            case  R.id.iv_back1:
                finish();
                break;
            default:
                break;

        }

    }

    public void getData() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID) + "");
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.TIMELINE, param, BaseData.class, null,
                GetSuccessListener(), null);
    }

    public Response.Listener<BaseData> GetSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                DialogUtils.cancleProgressDialog();
                sr_ref_timeline.setRefreshing(false);
                sr_ref_timeline.setLoading(false);
                if (currPage == 1) {
                    list.clear();
                }
                if (response.data.page != null
                        && response.data.page.currentPage.equals(response.data.page.totalPage)) {
                    sr_ref_timeline.setLoad_More(false);
                }
                if (null != response.data) {
                    if (null != response.data.timeline) {
                        rl_no_time_line.setVisibility(View.GONE);
                        rl_yes_time_line.setVisibility(View.VISIBLE);

                        tv_class.setText(response.data.timeline.courseName);
                        if (null != response.data.timeline.student) {
                            if (!TextUtils.isEmpty(response.data.timeline.student.nickname)) {
                                tv_student.setText(response.data.timeline.student.nickname);
                            }
                            if (null != response.data.timeline.student.studentNumber && !TextUtils.isEmpty(response.data.timeline.student.studentNumber.number)) {
                                tv_stu_number.setText(response.data.timeline.student.studentNumber.number + "");
                            }
                        }
                        tv_totalHours.setText("总" + response.data.timeline.totalHours + "课时");
                        tv_examination.setText("入学成绩:" + response.data.timeline.entranceScore + "分");
                        tv_examtimeStr.setText("考试时间:" + response.data.timeline.examtimeStr);
                        tv_assistant.setText("助教姓名:" + response.data.timeline.aid.name);
                        tv_Assistant_phone.setText("助教电话:" + response.data.timeline.aid.mobile);

                    } else {
                        rl_no_time_line.setVisibility(View.VISIBLE);
                        rl_yes_time_line.setVisibility(View.GONE);
                    }

                    if (response.data.timelineTeacherList != null && 0 != response.data.timelineTeacherList.size()) {
                        List<timelineTeacher> tlist = new ArrayList<timelineTeacher>();
                        tlist.addAll(response.data.timelineTeacherList);
                        if (tlist.size() > 1) {
                            ll_te.setVisibility(View.VISIBLE);
                            tv_kc1.setText("课程:" + tlist.get(0).courseName);
                            tv_te1.setText("老师:" + tlist.get(0).teacher.name);
                            tv_kc2.setText("课程:" + tlist.get(1).courseName);
                            tv_te2.setText("老师:" + tlist.get(1).teacher.name);
                        } else {
                            ll_te.setVisibility(View.INVISIBLE);
                            tv_kc1.setText("课程:" + tlist.get(0).courseName);
                            tv_te1.setText("老师:" + tlist.get(0).teacher.name);
                        }
                        list.clear();
                        list.addAll(response.data.timelineContentList);

                    }

                    mTimeLineAdapter.notifyDataSetChanged();

                }
            }
        };
    }
}
