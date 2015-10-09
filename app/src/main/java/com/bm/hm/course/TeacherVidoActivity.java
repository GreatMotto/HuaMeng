package com.bm.hm.course;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.base.HMApplication;
import com.bm.hm.bean.Course;
import com.bm.hm.bean.User;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.util.DialogUtils;
import com.bm.hm.view.RefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liuz on 2015/4/28.
 */
public class TeacherVidoActivity extends BaseActivity {

    private Button btn_teah_vido, btn_teah_intro;
    private ListView lv_teah_list;
    private TeacherVidoAdapter mTeacherVidoAdapter;
    private LinearLayout ll_jianjie;
//    private ScrollView sv_jianjie;
    private int currPage = 1;
    private List<Course> list = new ArrayList<Course>();
    private RefreshLayout sr_ref;
    private User teacher;
    private TextView tv_teah_title, tv_teah_content;
    private ImageView iv_teah_pic;
    private String teacherName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_teacher_vido);

        teacher = (User) getIntent().getSerializableExtra("teacher");
        initView();
        getCollectionVideoData();
        DialogUtils.showProgressDialog("正在加载", this);

    }

    private void initView() {
        //标题
        teacherName=teacher.name;
        initTitleBarWithBack(teacherName);

        //老师发布视频
        btn_teah_vido = (Button) findViewById(R.id.btn_teah_vido);
        btn_teah_vido.setOnClickListener(this);

        //老师简介
        btn_teah_intro = (Button) findViewById(R.id.btn_teah_intro);
        btn_teah_intro.setOnClickListener(this);

        //视频列表
        lv_teah_list = (ListView) findViewById(R.id.lv_teah_list);
        mTeacherVidoAdapter = new TeacherVidoAdapter(TeacherVidoActivity.this, list);
        lv_teah_list.setAdapter(mTeacherVidoAdapter);
//        lv_teah_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                gotoOtherActivity(CourseInfoActivity.class);
//                finish();
//            }
//        });
        tv_teah_title = (TextView) findViewById(R.id.tv_teah_title);
        tv_teah_content = (TextView) findViewById(R.id.tv_teah_content);
        iv_teah_pic = (ImageView) findViewById(R.id.iv_teah_pic);

//        sv_jianjie=(ScrollView)findViewById(R.id.sv_jianjie);
          ll_jianjie=(LinearLayout)findViewById(R.id.ll_jianjie);

        sr_ref = (RefreshLayout) findViewById(R.id.sr_ref);
        sr_ref.setColorScheme(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        sr_ref.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sr_ref.setLoading(true);
                sr_ref.setLoad_More(true);
                currPage = 1;
                getCollectionVideoData();

            }
        });
        sr_ref.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                currPage++;
                getCollectionVideoData();

            }
        });
        tv_teah_content.setText(teacher.teacherDesc);
        tv_teah_title.setText(teacher.name);
        ImageLoader.getInstance().displayImage(teacher.teacherPic.path, iv_teah_pic, HMApplication.getInstance().getOptionsCircle());
    }

    /**
     * 重置按钮的字体颜色和背景色
     */
    private void reSetColor() {
        btn_teah_vido.setTextColor(getResources().getColor(R.color.orange));
        btn_teah_vido.setBackgroundResource(R.mipmap.down_load_unselect);
        btn_teah_intro.setTextColor(getResources().getColor(R.color.orange));
        btn_teah_intro.setBackgroundResource(R.mipmap.down_loading_unselect);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_teah_vido:
                reSetColor();
                btn_teah_vido.setTextColor(getResources().getColor(R.color.white));
                btn_teah_vido.setBackgroundResource(R.mipmap.down_load_select);
                sr_ref.setVisibility(View.VISIBLE);
                lv_teah_list.setVisibility(View.VISIBLE);
                ll_jianjie.setVisibility(View.GONE);

                break;
            case R.id.btn_teah_intro:
                reSetColor();
                btn_teah_intro.setTextColor(getResources().getColor(R.color.white));
                btn_teah_intro.setBackgroundResource(R.mipmap.down_loading_select);
                lv_teah_list.setVisibility(View.GONE);
                sr_ref.setVisibility(View.GONE);
                ll_jianjie.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }

    public void getCollectionVideoData() {
//        DialogUtils.showProgressDialog("正在加载", this);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("teacherId", teacher.id + "");
        param.put("pageNum", currPage + "");
        param.put("pageSize", Constant.PAGE_SIZE + "");
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.QUERY_MY_TEA_VIDEO, param, BaseData.class, Course.class,
                getCollectionVideoSuccessListener(), null);
    }

    public Response.Listener<BaseData> getCollectionVideoSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                DialogUtils.cancleProgressDialog();
                sr_ref.setRefreshing(false);
                sr_ref.setLoading(false);


                if (currPage == 1) {
                    list.clear();
                }

                if (response.data.page != null
                        && response.data.page.currentPage.equals(response.data.page.totalPage)) {
                    sr_ref.setLoad_More(false);
                }
                list.addAll(response.data.list);
                mTeacherVidoAdapter.notifyDataSetChanged();

            }
        };
    }

}
