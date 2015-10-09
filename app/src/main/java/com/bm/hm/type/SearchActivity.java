package com.bm.hm.type;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.bean.Course;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.course.CourseAdapter;
import com.bm.hm.course.CourseInfoActivity;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.util.ToastUtil;
import com.bm.hm.view.RefreshLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liuz on 2015/4/29.
 */
public class SearchActivity extends BaseActivity {

    private ImageView iv_search_back, iv_search_button, iv_search_clear;
    private EditText et_search_content;
    private ListView lv_search_list;
    private int currPage = 1;
    private CourseAdapter adapter;
    private List<Course> list;
    private RefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_search);

        initView();
    }

    private void initView() {
        //返回
        iv_search_back = (ImageView) findViewById(R.id.iv_search_back);
        iv_search_back.setOnClickListener(this);

        //搜索按钮
        iv_search_button = (ImageView) findViewById(R.id.iv_search_button);
        iv_search_button.setOnClickListener(this);
        //输入框
        et_search_content = (EditText) findViewById(R.id.et_search_content);
        //清除输入框中的内容
        iv_search_clear = (ImageView) findViewById(R.id.iv_search_clear);
        iv_search_clear.setOnClickListener(this);
        //查询结果列表
        lv_search_list = (ListView) findViewById(R.id.lv_search_list);
        list = new ArrayList<Course>();
        adapter = new CourseAdapter(this, list);
        lv_search_list.setAdapter(adapter);

        lv_search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), CourseInfoActivity.class);
                intent.putExtra("course", (Serializable) list.get(position));
                startActivity(intent);
            }
        });

        //刷新与加载
        mRefreshLayout = (RefreshLayout) findViewById(R.id.rfl_shousuo);
        mRefreshLayout.setColorScheme(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        lv_search_list.addFooterView(mRefreshLayout.getFootView(), null, false);
        lv_search_list.setOnScrollListener(mRefreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setLoading(true);
                mRefreshLayout.setLoad_More(true);
                currPage = 1;
                searchRequest();
            }
        });

        mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {

            @Override
            public void onLoad() {
                currPage++;
                searchRequest();
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_search_back:
                finish();
                break;
            case R.id.iv_search_button:
                if (!TextUtils.isEmpty(et_search_content.getText().toString())) {
                    searchRequest();
                }
                break;
            case R.id.iv_search_clear:
                et_search_content.setText("");
                list.clear();
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    /**
     * 搜索请求接口
     */
    private void searchRequest() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("name", et_search_content.getText().toString());
        param.put("pageNum", Integer.toString(currPage));
        param.put("pageSize", Integer.toString(Constant.PAGE_SIZE));

        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.COURSE_LIST, param, BaseData.class, Course.class,
                searchSuccessListener(), null);
    }

    private Response.Listener<BaseData> searchSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                mRefreshLayout.setRefreshing(false);
                mRefreshLayout.setLoading(false);
                if (response.data.list != null) {
                    if (response.data.list.size() == 0) {
                        ToastUtil.showToast(getApplicationContext(), "暂无信息", Toast.LENGTH_SHORT);
                        list.clear();
                    } else {
                        if (currPage == 1) {
                            list.clear();
                        }
                        if (response.data.page != null
                                && response.data.page.currentPage.equals(response.data.page.totalPage)) {
                            mRefreshLayout.setLoad_More(false);
                        }
                        list.addAll(response.data.list);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        };
    }
}
