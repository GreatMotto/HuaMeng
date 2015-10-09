package com.bm.hm.me;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.bean.Course;
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
 * Created by chenb on 2015/4/27.
 */
public class MyVideoActivity extends BaseActivity {

    private ListView lv_video1;
    private TextView tv_left, tv_center, tv_right, hx_buy, hx_publish, hx_collect, tv_sc;
    private View view_publish;
    private ImageView iv_sc;
    private Course course;
    private int type = 0;
    private int currPage = 1;

    private MyVideoBuyAdapter buyAdapter;
    private MyVideoCollentAdapter collentAdapter;
    private MyVideoPublishAdapter publishAdapter;
    private RefreshLayout sr_ref;

    private List<Course> byList = new ArrayList<Course>();
    private String role;
    private List<Course> pbList = new ArrayList<Course>();
    private List<Course> colecList = new ArrayList<Course>();
    private LinearLayout ll_no_vedio;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_my_video);
        role = SharedPreferencesUtils.getInstance().getString(Constant.ROLE);
        course = (Course) getIntent().getSerializableExtra("course");
        initView();
    }

    private void initView() {
        initTitleBarWithBack("我的视频");
        tv_left = (TextView) findViewById(R.id.tv_buy);
        tv_center = (TextView) findViewById(R.id.tv_publish);
        tv_right = (TextView) findViewById(R.id.tv_collect);

        hx_buy = (TextView) findViewById(R.id.hx_buy);
        hx_collect = (TextView) findViewById(R.id.hx_collect);
        hx_publish = (TextView) findViewById(R.id.hx_publish);

        view_publish = findViewById(R.id.view_publish);
        lv_video1 = (ListView) findViewById(R.id.lv_vedio);

        //没视频
        ll_no_vedio = (LinearLayout) findViewById(R.id.ll_no_vedio);
        iv_sc = (ImageView) findViewById(R.id.iv_sc);
        tv_sc = (TextView) findViewById(R.id.tv_sc);


        buyAdapter = new MyVideoBuyAdapter(this, byList);
        lv_video1.setAdapter(buyAdapter);
        collentAdapter = new MyVideoCollentAdapter(this, colecList, course);
        publishAdapter = new MyVideoPublishAdapter(this, pbList);

        tv_left.setOnClickListener(this);
        tv_center.setOnClickListener(this);
        tv_right.setOnClickListener(this);

        sr_ref = (RefreshLayout) findViewById(R.id.sr_ref);
        sr_ref.setColorScheme(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        lv_video1.addFooterView(sr_ref.getFootView(), null, false);
        lv_video1.setOnScrollListener(sr_ref);
        sr_ref.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sr_ref.setLoading(true);
                sr_ref.setLoad_More(true);
                currPage = 1;
                switch (type) {
                    case 0:
                        getByVideoData();
                        break;
                    case 1:
                        getCollectionVideoData();
                        break;
                    case 2:
                        getPublshVideoData();
                        break;
                }


            }
        });
        sr_ref.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                currPage++;
                switch (type) {
                    case 0:
                        getByVideoData();
                        break;
                    case 1:
                        getCollectionVideoData();
                        break;
                    case 2:
                        getPublshVideoData();
                        break;
                }
            }
        });
        if (!TextUtils.isEmpty(role)) {
            if ("teacher".equals(role)) {
                tv_center.setVisibility(View.VISIBLE);
                hx_publish.setVisibility(View.INVISIBLE);
                view_publish.setVisibility(View.VISIBLE);
            } else {
                hx_publish.setVisibility(View.GONE);
                tv_center.setVisibility(View.GONE);
                view_publish.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 发布
     */
    public void getPublshVideoData() {
//        DialogUtils.showProgressDialog("正在加载", this);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID) + "");
        param.put("pageNum", currPage + "");
        param.put("pageSize", Constant.PAGE_SIZE + "");
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.QUERY_MY_TEA_VIDEO, param, BaseData.class, Course.class,
                getPublshVideoSuccessListener(), null);
    }

    public Response.Listener<BaseData> getPublshVideoSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                DialogUtils.cancleProgressDialog();
                sr_ref.setRefreshing(false);
                sr_ref.setLoading(false);

                if (currPage == 1) {
                    pbList.clear();
                }

                if (response.data.page != null
                        && response.data.page.currentPage.equals(response.data.page.totalPage)) {
                    sr_ref.setLoad_More(false);
                }

                pbList.addAll(response.data.list);
                if (pbList.size() == 0) {
                    ll_no_vedio.setVisibility(View.VISIBLE);
                    tv_sc.setText("暂无视频教程");
                    iv_sc.setImageResource(R.mipmap.vedio_no);
                } else {
                    ll_no_vedio.setVisibility(View.GONE);
                }
                publishAdapter.notifyDataSetChanged();
            }
        };
    }

    /**
     * 收藏
     */
    public void getCollectionVideoData() {
//        DialogUtils.showProgressDialog("正在加载", this);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID) + "");
        param.put("pageNum", currPage + "");
        param.put("pageSize", Constant.PAGE_SIZE + "");
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.QUERY_MY_COLLEC, param, BaseData.class, Course.class,
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
                    colecList.clear();
                }

                if (response.data.page != null
                        && response.data.page.currentPage.equals(response.data.page.totalPage)) {
                    sr_ref.setLoad_More(false);
                }

                colecList.addAll(response.data.list);
                if (colecList.size() == 0) {
                    ll_no_vedio.setVisibility(View.VISIBLE);
                    tv_sc.setText("暂无收藏");
                    iv_sc.setImageResource(R.mipmap.collect_no);
                } else {
                    ll_no_vedio.setVisibility(View.GONE);
                }
                collentAdapter.notifyDataSetChanged();
            }
        };
    }

    /**
     * 购买
     */
    public void getByVideoData() {
//        DialogUtils.showProgressDialog("正在加载", this);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID) + "");
        param.put("pageNum", currPage + "");
        param.put("pageSize", 10 + "");
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.QUERY_BUY_VIDEO, param, BaseData.class, Course.class,
                GetByVideoSuccessListener(), null);
    }

    public Response.Listener<BaseData> GetByVideoSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                DialogUtils.cancleProgressDialog();
                sr_ref.setRefreshing(false);
                sr_ref.setLoading(false);

                if (currPage == 1) {
                    byList.clear();
                }

                if (response.data.page != null
                        && response.data.page.currentPage.equals(response.data.page.totalPage)) {
                    sr_ref.setLoad_More(false);
                }

                byList.addAll(response.data.list);
                if (byList.size() == 0) {
                    ll_no_vedio.setVisibility(View.VISIBLE);
                    tv_sc.setText("暂无视频教程");
                    iv_sc.setImageResource(R.mipmap.vedio_no);
                } else {
                    ll_no_vedio.setVisibility(View.GONE);
                }
                buyAdapter.notifyDataSetChanged();
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        switch (type) {
            case 0:
                getByVideoData();
                break;
            case 1:
                getCollectionVideoData();
                break;
            case 2:
                getPublshVideoData();
                break;
        }
    }

    public void onClick(View v) {
        super.onClick(v);
        resetColor();
        ll_no_vedio.setVisibility(View.GONE);
        switch (v.getId()) {
            case R.id.tv_buy: //购买
                tv_left.setTextColor(getResources().getColor(R.color.orange));
                getByVideoData();
                hx_buy.setVisibility(View.VISIBLE);
                hx_collect.setVisibility(View.INVISIBLE);
                hx_publish.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(role)) {
                    if ("teacher".equals(role)) {
                        tv_center.setVisibility(View.VISIBLE);
                        hx_publish.setVisibility(View.INVISIBLE);
                    } else {
                        hx_publish.setVisibility(View.GONE);
                        tv_center.setVisibility(View.GONE);
                    }
                }
                hx_buy.setBackgroundColor(getResources().getColor(R.color.orange));
                type = 0;
                lv_video1.setAdapter(buyAdapter);
                buyAdapter.notifyDataSetChanged();

                break;
            case R.id.tv_publish:  //发布
                tv_center.setTextColor(getResources().getColor(R.color.orange));
                getPublshVideoData();
                hx_buy.setVisibility(View.INVISIBLE);
                hx_publish.setVisibility(View.VISIBLE);
                hx_collect.setVisibility(View.INVISIBLE);

                hx_publish.setBackgroundColor(getResources().getColor(R.color.orange));
                type = 2;

                lv_video1.setAdapter(publishAdapter);
                publishAdapter.notifyDataSetChanged();

                break;
            case R.id.tv_collect: //收藏
                tv_right.setTextColor(getResources().getColor(R.color.orange));
                getCollectionVideoData();
                hx_buy.setVisibility(View.INVISIBLE);
                hx_publish.setVisibility(View.INVISIBLE);
                hx_collect.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(role)) {
                    if ("teacher".equals(role)) {
                        tv_center.setVisibility(View.VISIBLE);
                        hx_publish.setVisibility(View.INVISIBLE);
                    } else {
                        hx_publish.setVisibility(View.GONE);
                        tv_center.setVisibility(View.GONE);
                    }
                }
                hx_collect.setBackgroundColor(getResources().getColor(R.color.orange));
                type = 1;
                lv_video1.setAdapter(collentAdapter);
                collentAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private void resetColor() {
        tv_left.setTextColor(getResources().getColor(R.color.item_title_color));
        tv_center.setTextColor(getResources().getColor(R.color.item_title_color));
        tv_right.setTextColor(getResources().getColor(R.color.item_title_color));

    }
    
}
