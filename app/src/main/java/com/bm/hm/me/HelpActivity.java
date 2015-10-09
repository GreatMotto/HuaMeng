package com.bm.hm.me;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.bean.HelpBean;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.view.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chenb on 2015/5/7.
 */
public class HelpActivity  extends BaseActivity {

    private  HelpAdapter  helpAdapter;
    private ListView lv_help;
    private RefreshLayout mRefreshLayout;
    private List<HelpBean>list;
    private int currPage = 1;
    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_help);
        initView();
        helpRequest();
    }

    private void initView() {
        initTitleBarWithBack("帮助");

        lv_help=(ListView)findViewById(R.id.lv_help);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.rfl_help);

        mRefreshLayout.setColorScheme(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        lv_help.addFooterView(mRefreshLayout.getFootView(), null, false);
        lv_help.setOnScrollListener(mRefreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setLoading(true);
                mRefreshLayout.setLoad_More(true);
                currPage = 1;
                helpRequest();
            }
        });

        mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {

            @Override
            public void onLoad() {
                currPage++;
                helpRequest();
            }
        });

        list=new ArrayList<HelpBean>();
        helpAdapter=new HelpAdapter(this,list);
        lv_help .setAdapter(helpAdapter);
    }

    /**
     * @Descriptionn 帮助信息   请求后台数据
     */
    public void helpRequest() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("pageNum", Integer.toString(currPage));
        param.put("pageSize", Integer.toString(Constant.PAGE_SIZE));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.HELP, param, BaseData.class, HelpBean.class,
                helpSuccessListener(), null);
    }

    private Response.Listener<BaseData> helpSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                mRefreshLayout.setRefreshing(false);
                mRefreshLayout.setLoading(false);
                if (response.data.list != null) {
                    if (currPage == 1) {
                        list.clear();
                    }
                    if (response.data.page != null
                            && response.data.page.currentPage.equals(response.data.page.totalPage)) {
                        mRefreshLayout.setLoad_More(false);
                    }
                    list.addAll(response.data.list);
                }
                helpAdapter .notifyDataSetChanged();
            }
        };
    }
}
