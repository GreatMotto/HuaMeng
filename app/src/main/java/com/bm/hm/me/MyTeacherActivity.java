package com.bm.hm.me;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.bean.User;
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
 * Created by chenb on 2015/4/30.
 */
public class MyTeacherActivity extends BaseActivity {
    private ListView lv_my_teacher;
    private LinearLayout ll_my_teacher_null;
    private RefreshLayout sr_ref;
    private int currPage = 1;
    private List<User> list = new ArrayList<User>();
    private MyTeacherAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_my_teacher);
        initView();
        getCollectionVideoData();
        DialogUtils.showProgressDialog("正在加载", this);
    }

    private void initView() {
        initTitleBarWithBack("我的老师");
        lv_my_teacher = (ListView) findViewById(R.id.lv_my_teacher);
        adapter = new MyTeacherAdapter(this, list);
        lv_my_teacher.setAdapter(adapter);
        sr_ref = (RefreshLayout) findViewById(R.id.sr_ref);
        sr_ref.setColorScheme(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
//        lv_my_teacher.addFooterView(sr_ref.getFootView(), null, false);
//        lv_my_teacher.setOnScrollListener(sr_ref);
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

        //item 点击事件
//        lv_my_teacher.setOnItemClickListener(new  AdapterView.OnItemClickListener(){
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                gotoOtherActivity(TeacherVidoActivity.class);
//            }
//        });
        ll_my_teacher_null = (LinearLayout) findViewById(R.id.ll_my_teacher_null);

    }

    public void getCollectionVideoData() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID) + "");
        param.put("pageNum", currPage + "");
        param.put("pageSize", Constant.PAGE_SIZE + "");
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.QUERY_MY_TEA, param, BaseData.class, User.class,
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
                if (list.size() < 1) {
                    ll_my_teacher_null.setVisibility(View.VISIBLE);
                } else {
                    ll_my_teacher_null.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();

            }
        };
    }
}
