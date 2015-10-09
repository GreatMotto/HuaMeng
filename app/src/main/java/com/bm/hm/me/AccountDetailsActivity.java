package com.bm.hm.me;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.bean.AccountDetails;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.util.SharedPreferencesUtils;
import com.bm.hm.view.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chenb on 2015/4/22.
 */
public class AccountDetailsActivity extends BaseActivity {

    private RelativeLayout rl_tisi;
    private LinearLayout ll_no_details;
    private ListView details_list;
    private AccountDetailsAdapter accountadapter;
    private int currPage = 1;
    private RefreshLayout mRefreshLayout;
    private List<AccountDetails> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_account_details);
        inieView();
        accountDetailsRequest();
    }

    private void inieView() {
        initTitleBarWithBack("账户明细");


        rl_tisi = (RelativeLayout) findViewById(R.id.rl_tisi);
        ll_no_details = (LinearLayout) findViewById(R.id.ll_no_details);
        details_list = (ListView) findViewById(R.id.lv_account_details);


        mRefreshLayout = (RefreshLayout) findViewById(R.id.swipe_details);
        mRefreshLayout.setColorScheme(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        details_list.addFooterView(mRefreshLayout.getFootView(), null, false);
        details_list.setOnScrollListener(mRefreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setLoading(true);
                mRefreshLayout.setLoad_More(true);
                currPage = 1;
                accountDetailsRequest();
            }
        });

        mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {

            @Override
            public void onLoad() {
                currPage++;
                accountDetailsRequest();
            }
        });

        list = new ArrayList<AccountDetails>();
        accountadapter = new AccountDetailsAdapter(this, list);
        details_list.setAdapter(accountadapter);


    }

    /**
     * @Description 查询账户明细  请求后台数据
     */
    public void accountDetailsRequest() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", String.valueOf(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        param.put("pageNum", Integer.toString(currPage));
        param.put("pageSize", Integer.toString(Constant.PAGE_SIZE));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.SCOREDETAIL, param, BaseData.class, AccountDetails.class,
                accountDetailsSuccessListener(), null);
    }

    private Response.Listener<BaseData> accountDetailsSuccessListener() {

        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                mRefreshLayout.setRefreshing(false);
                mRefreshLayout.setLoading(false);
                if (response.data.list != null) {
                    if (response.data.list.size() == 0) {   //没有消息时
                        rl_tisi.setVisibility(View.GONE);
                        ll_no_details.setVisibility(View.VISIBLE);
                    } else {
                        rl_tisi.setVisibility(View.VISIBLE);
                        ll_no_details.setVisibility(View.GONE);
                        mRefreshLayout.setRefreshing(false);
                        mRefreshLayout.setLoading(false);

                        if (currPage == 1) {
                            list.clear();
                        }
                        if (response.data.page != null
                                && response.data.page.currentPage.equals(response.data.page.totalPage)) {
                            mRefreshLayout.setLoad_More(false);
                        }
                        list.addAll(response.data.list);

                    }
                    accountadapter.notifyDataSetChanged();
                }
            }
        };
    }
}
