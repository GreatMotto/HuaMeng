package com.bm.hm.event;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.adpter.ViewPagerAdapter;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.bean.Event;
import com.bm.hm.bean.Image;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.view.PageIndicatorView;
import com.bm.hm.view.RefreshLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liuz on 2015/4/30.
 */
public class EventActivity extends BaseActivity {

    private ViewPager viewPager;
    private List<View> bannerList;
    private PageIndicatorView indicator_view;
    private int index = 1;
    private ViewPagerAdapter bannerAdapter;
    private TextView tv_event_title;
    private ListView lv_event_list;

    private RefreshLayout mRefreshLayout;
    private EventAdapter mEventAdapter;
    private int currPage = 1;
    private List<Event> eventList;
    private List<Event> bannerData = new ArrayList<Event>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_event);

        initView();

        getBannerList();

        getData();
    }

    private void initView() {
        initTitleBarWithBack("华盟活动");

        tv_event_title = (TextView) findViewById(R.id.tv_event_title);
        lv_event_list = (ListView) findViewById(R.id.lv_event_list);
        eventList = new ArrayList<Event>();
        mEventAdapter = new EventAdapter(EventActivity.this, eventList);
        lv_event_list.setAdapter(mEventAdapter);
//        lv_event_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getApplicationContext(), EventInfoActivity.class);
//                intent.putExtra("url", "http://www.baidu.com");
//                startActivity(intent);
//            }
//        });ew<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getApplicationContext(), EventInfoActivity.class);
//                intent.putExtra("url", "http://www.baidu.com");
//                startActivity(intent);
//            }
//        });

        mRefreshLayout = (RefreshLayout) findViewById(R.id.swipe_container);
        mRefreshLayout.setColorScheme(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        lv_event_list.addFooterView(mRefreshLayout.getFootView(), null, false);
        lv_event_list.setOnScrollListener(mRefreshLayout);
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

        initViewPager();
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        bannerList = new ArrayList<View>();
        bannerAdapter = new ViewPagerAdapter(bannerList);
        viewPager.setAdapter(bannerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                indicator_view.setCurrentPage(arg0);
                tv_event_title.setText(bannerData.get(arg0).name);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        indicator_view = (PageIndicatorView) findViewById(R.id.indicator_view);
        indicator_view.setImageId(R.mipmap.banner_focus);
        indicator_view.setAlign(1);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            index++;
            viewPager.setCurrentItem(index % bannerList.size());
            handler.sendEmptyMessageDelayed(1, 5000);

            super.handleMessage(msg);
        }
    };

    private void getBannerList() {
        HashMap<String, String> param = new HashMap<String, String>();
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.GET_ACTIVITY_BANNER, param, BaseData.class, Event.class,
                getBannerListSuccessListener(), null);

    }

    public Response.Listener<BaseData> getBannerListSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {

                bannerData = response.data.list;
                for (final Event banner : bannerData) {
                    ImageView imageView = new ImageView(getApplicationContext());
                    ImageLoader.getInstance().displayImage(banner.bannerImage.path, imageView);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    if (banner.bannerImage != null && banner.bannerImage.attribute != null) {
                        Gson gson = new Gson();
                        Image.Attribute attr = gson.fromJson(banner.bannerImage.attribute, Image.Attribute.class);
                        setLayoutParams(imageView, attr.width, attr.height);
                    }
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (banner.link != null) {
                                Intent intent = new Intent(getApplicationContext(), EventInfoActivity.class);
                                intent.putExtra("link", banner.link);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getApplicationContext(), EventInfoActivity.class);
                                intent.putExtra("link", "http://www.huamengedu.cn/");
                                startActivity(intent);
                            }
                        }
                    });
                    bannerList.add(imageView);
                }

                if (bannerData != null && bannerData.size() > 0) {
                    tv_event_title.setText(bannerData.get(0).name);
                }

                bannerAdapter.notifyDataSetChanged();

                indicator_view.setTotalPage(bannerList.size());

                indicator_view.setCurrentPage(0);

                handler.sendEmptyMessageDelayed(1, 5000);
            }
        };
    }

    private void getData() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("pageNum", Integer.toString(currPage));
        param.put("pageSize", Integer.toString(Constant.PAGE_SIZE));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.GET_ACTIVITY_LIST, param, BaseData.class, Event.class,
                getDataSuccessListener(), null);

    }

    public Response.Listener<BaseData> getDataSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                mRefreshLayout.setRefreshing(false);
                mRefreshLayout.setLoading(false);

                if (currPage == 1) {
                    eventList.clear();
                }

                if (response.data.page != null
                        && response.data.page.currentPage.equals(response.data.page.totalPage)) {
                    mRefreshLayout.setLoad_More(false);
                }

                eventList.addAll(response.data.list);
                mEventAdapter.notifyDataSetChanged();
            }
        };
    }

}
