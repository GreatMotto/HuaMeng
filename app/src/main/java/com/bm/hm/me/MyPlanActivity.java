package com.bm.hm.me;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.bean.MyPlan;
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
 * Created by chenb on 2015/5/6.
 */
public class MyPlanActivity extends BaseActivity {
    private ExpandableListView listView = null;
    private MyPlanAdapter adapter;
    private RefreshLayout mRefreshLayout;
    private List<MyPlan> list = new ArrayList<MyPlan>();
    private LinearLayout ll_no_plan;
    private int currPage = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_my_plan);
        initView();
        getData();
        DialogUtils.showProgressDialog("正在加载", this);
    }

    private void initView() {
        initTitleBarWithBack("我的计划");
        ll_no_plan = (LinearLayout) findViewById(R.id.ll_no_plan);

        listView = (ExpandableListView) this.findViewById(R.id.lv_my_plan);


        adapter = new MyPlanAdapter(this, list);
        listView.setAdapter(adapter);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.rfl_help);
        mRefreshLayout.setColorScheme(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setLoading(true);
                mRefreshLayout.setLoad_More(true);
                currPage = 1;
                getData();
            }
        });

        mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {

            @Override
            public void onLoad() {
                currPage++;
                getData();
            }
        });
    }

    public void getData() {
//        DialogUtils.showProgressDialog("正在加载", this);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID) + "");
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.MYLEARNPLAN, param, BaseData.class, MyPlan.class,
                GetSuccessListener(), null);
    }

    public Response.Listener<BaseData> GetSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                DialogUtils.cancleProgressDialog();
                mRefreshLayout.setRefreshing(false);
                mRefreshLayout.setLoading(false);
                    if (currPage == 1) {
                        list.clear();
                    if (response.data.page != null
                            && response.data.page.currentPage.equals(response.data.page.totalPage)) {
                        mRefreshLayout.setLoad_More(false);
                    }
                        if (null == response.data.list) {
                            ll_no_plan.setVisibility(View.VISIBLE);
                        } else {
                            ll_no_plan.setVisibility(View.GONE);
                            list.addAll(response.data.list);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
        };
    }
}
