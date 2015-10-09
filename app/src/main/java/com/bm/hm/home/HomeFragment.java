package com.bm.hm.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.abroad.AbroadMessageActivity;
import com.bm.hm.abroad.AbroadMessageJoin;
import com.bm.hm.adpter.ViewPagerAdapter;
import com.bm.hm.bean.Banner;
import com.bm.hm.bean.Course;
import com.bm.hm.bean.Image;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.course.CourseActivity;
import com.bm.hm.course.CourseInfoActivity;
import com.bm.hm.event.EventActivity;
import com.bm.hm.event.EventInfoActivity;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.me.MessageActivity;
import com.bm.hm.pay.RechargeActivity;
import com.bm.hm.type.SearchActivity;
import com.bm.hm.user.LoginActivity;
import com.bm.hm.util.SharedPreferencesUtils;
import com.bm.hm.view.PageIndicatorView;
import com.bm.hm.zxing.activity.CaptureActivity;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener {

    private View view;
    private TextView tv_lx, tv_ys, tv_tf, tv_sd, tv_xtf, tv_yy, tv_jf, tv_hd,tv_search;

    private ViewPager viewPager;
    private ViewPagerAdapter bannerAdapter;
    private List<View> bannerList;
    private PageIndicatorView indicator_view;
    private int index = 1;

    private RelativeLayout rl_zxsj, rl_xlph, rl_ys, rl_tf, rl_sd, rl_xtf, rl_jcyy;
    private ImageView iv_right,iv_code;
    private GridView gv_zxsj,gv_xlph,gv_ys,gv_tf,gv_sd,gv_xtf,gv_jcyy;
    public List<Course> basic,sat,sellRank,ielts,newest,toefljunior,toefl;
    public GridAdapter basicAdapter,satAdapter,ieltsAdapter,newestAdapter,toefljuniorAdapter,toeflAdapter;
    public XLPHAdapter sellRankAdapter;
    private int userId;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }

        if (view == null) {
            view = inflater.inflate(R.layout.fg_home, container, false);
        }

        initView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        userId = SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID);
        getBannerList();
        getData();
    }

    private void initView() {
        //搜索输入框
        tv_search = (TextView) view.findViewById(R.id.tv_search);
        tv_search.setOnClickListener(this);

        iv_right = (ImageView) view.findViewById(R.id.iv_right);
        iv_right.setOnClickListener(this);

        tv_lx = (TextView) view.findViewById(R.id.tv_lx);
        tv_lx.setOnClickListener(this);

        tv_ys = (TextView) view.findViewById(R.id.tv_ys);
        tv_ys.setOnClickListener(this);

        tv_tf = (TextView) view.findViewById(R.id.tv_tf);
        tv_tf.setOnClickListener(this);

        tv_sd = (TextView) view.findViewById(R.id.tv_sd);
        tv_sd.setOnClickListener(this);

        tv_xtf = (TextView) view.findViewById(R.id.tv_xtf);
        tv_xtf.setOnClickListener(this);

        tv_yy = (TextView) view.findViewById(R.id.tv_yy);
        tv_yy.setOnClickListener(this);

        tv_jf = (TextView) view.findViewById(R.id.tv_jf);
        tv_jf.setOnClickListener(this);

        tv_hd = (TextView) view.findViewById(R.id.tv_hd);
        tv_hd.setOnClickListener(this);

        rl_zxsj = (RelativeLayout) view.findViewById(R.id.rl_zxsj);
        rl_zxsj.setOnClickListener(this);

        rl_xlph = (RelativeLayout) view.findViewById(R.id.rl_xlph);
        rl_xlph.setOnClickListener(this);

        rl_ys = (RelativeLayout) view.findViewById(R.id.rl_ys);
        rl_ys.setOnClickListener(this);

        rl_tf = (RelativeLayout) view.findViewById(R.id.rl_tf);
        rl_tf.setOnClickListener(this);

        rl_sd = (RelativeLayout) view.findViewById(R.id.rl_sd);
        rl_sd.setOnClickListener(this);

        rl_xtf = (RelativeLayout) view.findViewById(R.id.rl_xtf);
        rl_xtf.setOnClickListener(this);

        rl_jcyy = (RelativeLayout) view.findViewById(R.id.rl_jcyy);
        rl_jcyy.setOnClickListener(this);

        gv_zxsj = (GridView) view.findViewById(R.id.gv_zxsj);
        newest = new ArrayList<Course>();
        newestAdapter = new GridAdapter(getActivity(),newest);
        gv_zxsj.setAdapter(newestAdapter);
        gv_zxsj.setOnItemClickListener(this);

        gv_xlph = (GridView) view.findViewById(R.id.gv_xlph);
        sellRank = new ArrayList<Course>();
        sellRankAdapter = new XLPHAdapter(getActivity(),sellRank);
        gv_xlph.setAdapter(sellRankAdapter);
        gv_xlph.setOnItemClickListener(this);

        gv_ys = (GridView) view.findViewById(R.id.gv_ys);
        ielts = new ArrayList<Course>();
        ieltsAdapter = new GridAdapter(getActivity(),ielts);
        gv_ys.setAdapter(ieltsAdapter);
        gv_ys.setOnItemClickListener(this);

        gv_tf = (GridView) view.findViewById(R.id.gv_tf);
        toefl = new ArrayList<Course>();
        toeflAdapter = new GridAdapter(getActivity(),toefl);
        gv_tf.setAdapter(toeflAdapter);
        gv_tf.setOnItemClickListener(this);

        gv_sd = (GridView) view.findViewById(R.id.gv_sd);
        sat = new ArrayList<Course>();
        satAdapter = new GridAdapter(getActivity(),sat);
        gv_sd.setAdapter(satAdapter);
        gv_sd.setOnItemClickListener(this);

        gv_xtf = (GridView) view.findViewById(R.id.gv_xtf);
        toefljunior = new ArrayList<Course>();
        toefljuniorAdapter = new GridAdapter(getActivity(),toefljunior);
        gv_xtf.setAdapter(toefljuniorAdapter);
        gv_xtf.setOnItemClickListener(this);

        gv_jcyy = (GridView) view.findViewById(R.id.gv_jcyy);
        basic = new ArrayList<Course>();
        basicAdapter = new GridAdapter(getActivity(),basic);
        gv_jcyy.setAdapter(basicAdapter);
        gv_jcyy.setOnItemClickListener(this);

        iv_code = (ImageView) view.findViewById(R.id.iv_code);
        iv_code.setOnClickListener(this);

        initViewPager();
    }

    private void initViewPager() {
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

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

        indicator_view = (PageIndicatorView) view.findViewById(R.id.indicator_view);
        indicator_view.setImageId(R.mipmap.banner_focus);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //扫描二维码
            case R.id.iv_code:
                Intent openCameraIntent = new Intent(getActivity(),CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);

                break;
            //消息
            case R.id.iv_right:
                if(userId!=0){
                    Intent intent= new Intent(getActivity(),MessageActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("from",4);
                    startActivity(intent);
                }

                break;
            //搜索输入框
            case R.id.tv_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            //我要留学
            case R.id.tv_lx:
                if(userId!=0){
                    joinAsk();
                }else{
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("from",1);
                    startActivity(intent);
                }
                break;
            //雅思
            case R.id.tv_ys:
                gotoCourseActivity("雅思","1");
                break;
            //托福
            case R.id.tv_tf:
                gotoCourseActivity("托福","2");
                break;
            //赛达
            case R.id.tv_sd:
                gotoCourseActivity("赛达","3");
                break;
            //小托福
            case R.id.tv_xtf:
                gotoCourseActivity("小托福","4");
                break;
            //英语
            case R.id.tv_yy:
                gotoCourseActivity("基础英语","5");
                break;
            //积分充值
            case R.id.tv_jf:
                if(userId!=0){
                    Intent intent = new Intent(getActivity(), RechargeActivity.class);
                    intent.putExtra("from",1);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("from",2);
                    startActivity(intent);
                }

                break;
            //华盟活动
            case R.id.tv_hd:
                startActivity(new Intent(getActivity(), EventActivity.class));
                break;
            //最新上架
            case R.id.rl_zxsj:
                gotoCourseActivity("最新上架",null);
                break;
            //销量排行
            case R.id.rl_xlph:
                gotoCourseActivity("销量排行",null);
                break;
            //雅思
            case R.id.rl_ys:
                gotoCourseActivity("雅思","1");
                break;
            //托福
            case R.id.rl_tf:
                gotoCourseActivity("托福","2");
                break;
            //赛达
            case R.id.rl_sd:
                gotoCourseActivity("赛达","3");
                break;
            //小托福
            case R.id.rl_xtf:
                gotoCourseActivity("小托福","4");
                break;
            //英语
            case R.id.rl_jcyy:
                gotoCourseActivity("基础英语","5");
                break;
        }
    }

    private void gotoCourseActivity(String type,String level1TypeId){
        Intent intent = new Intent(getActivity(), CourseActivity.class);
        intent.putExtra("level1TypeId", level1TypeId);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (resultCode == -1) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            Intent intent = new Intent(getActivity(),EventInfoActivity.class);
            startActivity(intent);
        }
    }

    public void setLayoutParams(View v, int width, int height) {
        int scWidth = this.getResources().getDisplayMetrics().widthPixels;
        float ratio = (float)width / height;
        android.view.ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(scWidth,(int) (scWidth / ratio));
        v.setLayoutParams(lp);
    }

    private void getBannerList() {
        HashMap<String, String> param = new HashMap<String, String>();
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(getActivity());
        request.HttpVolleyRequestPost(Urls.HOME_BANNER_LIST, param, BaseData.class, Banner.class,
                getBannerListSuccessListener(), null);

    }

    public Response.Listener<BaseData> getBannerListSuccessListener() {
        return new Response.Listener<BaseData>()
        {

            @Override
            public void onResponse(BaseData response) {
                bannerList.clear();
                List<Banner> list = response.data.list;
                for(final Banner banner : list){
                    ImageView imageView = new ImageView(getActivity());
                    ImageLoader.getInstance().displayImage(banner.image.path, imageView);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    if(banner.image!=null && banner.image.attribute!=null){
                        Gson gson = new Gson();
                        Image.Attribute attr = gson.fromJson(banner.image.attribute, Image.Attribute.class);
                        setLayoutParams(imageView,attr.width,attr.height);
                    }
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (banner.type.equals(Constant.BANNER_TYPE_COURSE)) {
                                Intent intent = new Intent(getActivity(), CourseInfoActivity.class);
                                intent.putExtra("course", (Serializable) banner.course);
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(getActivity(), EventInfoActivity.class);
                                intent.putExtra("link", (Serializable)banner.activityLink);
                                startActivity(intent);
                            }
                        }
                    });
                    bannerList.add(imageView);
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
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(getActivity());
        request.HttpVolleyRequestPost(Urls.HOME_COURSE_LIST, param, BaseData.class, Course.class,
                getDataSuccessListener(), null);

    }

    public Response.Listener<BaseData> getDataSuccessListener() {
        return new Response.Listener<BaseData>()
        {

            @Override
            public void onResponse(BaseData response) {
                if(newest!=null){
                    newest.clear();
                }
                newest.addAll(response.data.newest);
                newestAdapter.notifyDataSetChanged();

                if(sellRank!=null){
                    sellRank.clear();
                }
                sellRank.addAll(response.data.sellRank);
                sellRankAdapter.notifyDataSetChanged();

                if(ielts!=null){
                    ielts.clear();
                }
                ielts.addAll(response.data.ielts);
                ieltsAdapter.notifyDataSetChanged();

                if(toefl!=null){
                    toefl.clear();
                }
                toefl.addAll(response.data.toefl);
                toeflAdapter.notifyDataSetChanged();

                if(toefljunior!=null){
                    toefljunior.clear();
                }
                toefljunior.addAll(response.data.toefljunior);
                toefljuniorAdapter.notifyDataSetChanged();

                if(sat!=null){
                    sat.clear();
                }
                sat.addAll(response.data.sat);
                satAdapter.notifyDataSetChanged();

                if(basic!=null){
                    basic.clear();
                }
                basic.addAll(response.data.basic);
                basicAdapter.notifyDataSetChanged();
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(),CourseInfoActivity.class);
        Course course = new Course();
        switch (parent.getId()){
            case R.id.gv_zxsj:
                course = newest.get(position);
                break;
            case R.id.gv_xlph:
                course = sellRank.get(position);
                break;
            case R.id.gv_ys:
                course = ielts.get(position);
                break;
            case R.id.gv_tf:
                course = toefl.get(position);
                break;
            case R.id.gv_sd:
                course = sat.get(position);
                break;
            case R.id.gv_xtf:
                course = toefljunior.get(position);
                break;
            case R.id.gv_jcyy:
                course = basic.get(position);
                break;
        }

        intent.putExtra("course",  (Serializable) course);
        startActivity(intent);
    }

    private void joinAsk() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", String.valueOf(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(getActivity());
        request.HttpVolleyRequestPost(Urls.JOIN_ASK, param, BaseData.class, null,
                joinAskListener(), null);

    }

    public Response.Listener<BaseData> joinAskListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                if(response.data.isSubmit.equals("YES")){
                    Intent intent = new Intent(getActivity(), AbroadMessageJoin.class);
                    intent.putExtra("result",1);
                    startActivity(intent);
                }else{
                    startActivity(new Intent(getActivity(), AbroadMessageActivity.class));
                }
            }
        };
    }
}
