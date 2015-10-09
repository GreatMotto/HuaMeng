package com.bm.hm.course;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.adpter.ViewPagerAdapter;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.bean.Course;
import com.bm.hm.bean.Image;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.home.MainActivity;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.type.SearchActivity;
import com.bm.hm.util.SharedPreferencesUtils;
import com.bm.hm.view.PageIndicatorView;
import com.bm.hm.view.RefreshLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CourseActivity extends BaseActivity {

    private ViewPager viewPager;
    private List<View> bannerList;
    private PageIndicatorView indicator_view;
    private ViewPagerAdapter bannerAdapter;
    private int index = 1;

    private ImageView iv_right, iv_search;

    private TextView tv_zxsj, tv_xlzg, tv_leftLine, tv_rightLine;

    private int currPage = 1;
    private int sort = 0;
    private RefreshLayout mRefreshLayout;
    private ListView lv_videos;
    private List<Course> list;
    private CourseAdapter adapter;

    private String level1TypeId;
    private String level2TypeId;
    private String level3TypeIds;

    private boolean hasSort = false;
    private int tag = 0;
    private int currentTag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_course);

        initView();
        getBannerList();
        getData();
    }

    private void initView() {
        lv_videos = (ListView) findViewById(R.id.lv_videos);
        list = new ArrayList<Course>();
        adapter = new CourseAdapter(this, list);
        lv_videos.setAdapter(adapter);
        lv_videos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), CourseInfoActivity.class);
                intent.putExtra("course", (Serializable) list.get(position));
                startActivity(intent);
            }
        });

        mRefreshLayout = (RefreshLayout) findViewById(R.id.swipe_container);
        mRefreshLayout.setColorScheme(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        lv_videos.addFooterView(mRefreshLayout.getFootView(), null, false);
        lv_videos.setOnScrollListener(mRefreshLayout);
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

        iv_right = (ImageView) findViewById(R.id.iv_right);
        iv_right.setOnClickListener(this);
        iv_right.setVisibility(View.VISIBLE);

        iv_search = (ImageView) findViewById(R.id.iv_search);
        iv_search.setOnClickListener(this);
        iv_search.setVisibility(View.VISIBLE);

        tv_zxsj = (TextView) findViewById(R.id.tv_zxsj);
        tv_zxsj.setOnClickListener(this);

        tv_xlzg = (TextView) findViewById(R.id.tv_xlzg);
        tv_xlzg.setOnClickListener(this);

        tv_leftLine = (TextView) findViewById(R.id.tv_leftLine);
        tv_rightLine = (TextView) findViewById(R.id.tv_rightLine);

        level1TypeId = getIntent().getStringExtra("level1TypeId");
        level2TypeId = getIntent().getStringExtra("level2TypeId");
        level3TypeIds = getIntent().getStringExtra("level3TypeIds");

        String type = getIntent().getStringExtra("type");
        initTitleBarWithBack(type);
        if (type.equals("最新上架")) {
            sort = 0;
        } else if (type.equals("销量排行")) {
            sort = 1;
            selectLeftOrRight();
        } else {
            sort = 3;
        }

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
        indicator_view.setTotalPage(bannerList.size());

        indicator_view.setCurrentPage(0);

        handler.sendEmptyMessageDelayed(1, 5000);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (bannerList.size() > 0) {
                index++;
                viewPager.setCurrentItem(index % bannerList.size());
                handler.sendEmptyMessageDelayed(1, 5000);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_zxsj:
                sort = 0;
                selectLeftOrRight();
                currentTag = 1;
                break;
            case R.id.tv_xlzg:
                if (hasSort) {
                    sort = tag;
                }

                if (currentTag == 2) {
                    if (sort == 1) {
                        sort = 2;
                        tv_xlzg.setText("销量最低");
                    } else if (sort == 2) {
                        sort = 1;
                        tv_xlzg.setText("销量最高");
                    }
                }

                if (tag == 0) {
                    sort = 1;
                    tv_xlzg.setText("销量最高");
                }

                tag = sort;

                selectLeftOrRight();
                hasSort = true;
                currentTag = 2;
                break;
            case R.id.iv_search:
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                break;
            case R.id.iv_right:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("tag", 1);
                startActivity(intent);
                break;
        }
    }

    private void selectLeftOrRight() {
        if (sort == 0) {
            tv_zxsj.setTextColor(getResources().getColor(R.color.orange));
            tv_xlzg.setTextColor(getResources().getColor(R.color.item_title_color));

            tv_leftLine.setVisibility(View.VISIBLE);
            tv_rightLine.setVisibility(View.INVISIBLE);
        } else {
            tv_zxsj.setTextColor(getResources().getColor(R.color.item_title_color));
            tv_xlzg.setTextColor(getResources().getColor(R.color.orange));

            tv_rightLine.setVisibility(View.VISIBLE);
            tv_leftLine.setVisibility(View.INVISIBLE);
        }
        getData();
    }

    private void getBannerList() {
        HashMap<String, String> param = new HashMap<String, String>();
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.COURSE_BANNER_LIST, param, BaseData.class, Course.class,
                getBannerListSuccessListener(), null);

    }

    public Response.Listener<BaseData> getBannerListSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                List<Course> list = response.data.list;
                for (final Course course : list) {
                    if (course.bannerImage != null && course.bannerImage.attribute != null) {
                        ImageView imageView = new ImageView(getApplicationContext());
                        ImageLoader.getInstance().displayImage(course.bannerImage.path, imageView);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                        Gson gson = new Gson();
                        Image.Attribute attr = gson.fromJson(course.bannerImage.attribute, Image.Attribute.class);
                        setLayoutParams(imageView, attr.width, attr.height);

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(CourseActivity.this, CourseInfoActivity.class);
                                intent.putExtra("course", (Serializable) course);
                                startActivity(intent);
                            }
                        });

                        bannerList.add(imageView);
                    }
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
        if (sort != 3) {
            param.put("sort", Integer.toString(sort));
        }

        if (level3TypeIds != null) {
            param.put("level3TypeIds", level3TypeIds);
        } else if (level2TypeId != null) {
            param.put("level2TypeId", level2TypeId);
        } else if (level1TypeId != null) {
            param.put("level1TypeId", level1TypeId);
        }
        param.put("pageNum", Integer.toString(currPage));
        param.put("pageSize", Integer.toString(Constant.PAGE_SIZE));
        param.put("userId", Integer.toString(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.COURSE_LIST, param, BaseData.class, Course.class,
                getDataSuccessListener(), null);

    }

    public Response.Listener<BaseData> getDataSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
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
                adapter.notifyDataSetChanged();
            }
        };
    }
}
