package com.bm.hm.course;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.bean.Comment;
import com.bm.hm.bean.Course;
import com.bm.hm.bean.Video;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.pay.OrderActivity;
import com.bm.hm.user.LoginActivity;
import com.bm.hm.util.CommentUtils;
import com.bm.hm.util.SharedPreferencesUtils;
import com.bm.hm.util.TextUtil;
import com.bm.hm.util.ToastUtil;
import com.bm.hm.view.RefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CourseInfoActivity extends BaseActivity{

    private TextView tv_videos,tv_introduce,tv_comments,tv_tech_title,tv_tech_content,tv_all,tv_good,tv_neutral,tv_bad,tv_buy
            ,tv_collect,tv_score,tv_sellNumber,tv_teach_name,tv_course_name,tv_level,tv_create_comment;
    private ListView lv_videos,lv_comments;
    private VideoAdapter videoAdapter;
    private CommentAdapter commentAdapter;
    private LinearLayout ll_comments,ll_level;
    private View view_leftLine,view_centerLine1,view_centerLine2,view_rightLine;
    private RelativeLayout rl_buy;
    private ImageView iv_download,iv_course_img;
    private Course course;
    private List<Video> videoList;
    private List<Comment> commentList;
    private int commentCurrPage = 1;
    private RefreshLayout mRefreshLayout;
    private String level="";
    private RadioGroup rg_level;
    private EditText et_comment_content;
    private PopupWindow popupWindow;
    private View ll_content;
    private int userId;
    private int  dengji;
    private String isBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_course_info);

        course = (Course) getIntent().getSerializableExtra("course");
        initView();

        getData();
    }

    @Override
    public void onResume() {
        super.onResume();

        userId = SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID);
    }

    private void initView(){
        ToastUtil.cancleToast();

        initTitleBarWithBack("课程详情");

        tv_teach_name = (TextView) findViewById(R.id.tv_teach_name);
        tv_teach_name.setOnClickListener(this);
        TextUtil.setPintLine(getApplicationContext(), tv_teach_name);

        lv_videos = (ListView) findViewById(R.id.lv_videos);
        videoList = new ArrayList<Video>();
        videoAdapter = new VideoAdapter(getApplicationContext(),videoList);
        lv_videos.setAdapter(videoAdapter);
        lv_videos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), VideoViewPlayingActivity.class);
                    String path = videoList.get(position).video.streamingPath;
                    intent.setData(Uri.parse(path));
                    intent.putExtra("position", position);
                    intent.putExtra("videoList",(Serializable)videoList);
                    intent.putExtra("course",(Serializable)course);
                    intent.putExtra("isBuy",tv_buy.getTag().toString());
                    startActivity(intent);
            }
        });

        lv_comments = (ListView) findViewById(R.id.lv_comments);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.swipe_container);
        mRefreshLayout.setColorScheme(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        lv_comments.addFooterView(mRefreshLayout.getFootView(), null, false);
        lv_comments.setOnScrollListener(mRefreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setLoading(true);
                mRefreshLayout.setLoad_More(true);
                commentCurrPage = 1;
                getCourseComment();
            }
        });

        mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {

            @Override
            public void onLoad() {
                commentCurrPage++;
                getCourseComment();
            }
        });
        commentList = new ArrayList<Comment>();
        commentAdapter = new CommentAdapter(getApplicationContext(),commentList);
        lv_comments.setAdapter(commentAdapter);

        tv_videos = (TextView) findViewById(R.id.tv_videos);
        tv_videos.setOnClickListener(this);

        tv_introduce = (TextView) findViewById(R.id.tv_introduce);
        tv_introduce.setOnClickListener(this);

        tv_comments = (TextView) findViewById(R.id.tv_comments);
        tv_comments.setOnClickListener(this);

        tv_tech_title = (TextView) findViewById(R.id.tv_tech_title);
        tv_tech_content = (TextView) findViewById(R.id.tv_tech_content);

        ll_comments = (LinearLayout) findViewById(R.id.ll_comments);
        ll_level=(LinearLayout)findViewById(R.id.ll_level);

        tv_all = (TextView) findViewById(R.id.tv_all);
        tv_all.setOnClickListener(this);

        tv_good = (TextView) findViewById(R.id.tv_good);
        tv_good.setOnClickListener(this);

        tv_neutral = (TextView) findViewById(R.id.tv_neutral);
        tv_neutral.setOnClickListener(this);

        tv_bad = (TextView) findViewById(R.id.tv_bad);
        tv_bad.setOnClickListener(this);

        tv_bad = (TextView) findViewById(R.id.tv_bad);

        view_leftLine = findViewById(R.id.view_leftLine);
        view_centerLine1 = findViewById(R.id.view_centerLine1);
        view_centerLine2 = findViewById(R.id.view_centerLine2);
        view_rightLine = findViewById(R.id.view_rightLine);

        rl_buy = (RelativeLayout) findViewById(R.id.rl_buy);

        tv_buy = (TextView) findViewById(R.id.tv_buy);
        tv_buy.setOnClickListener(this);
        tv_buy.setTag("2");

        iv_download = (ImageView) findViewById(R.id.iv_download);
        iv_download.setOnClickListener(this);

        tv_collect = (TextView) findViewById(R.id.tv_collect);
        tv_collect.setOnClickListener(this);

        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_sellNumber = (TextView) findViewById(R.id.tv_sellNumber);
        iv_course_img = (ImageView) findViewById(R.id.iv_course_img);
        tv_course_name = (TextView) findViewById(R.id.tv_course_name);

//        tv_level = (TextView) findViewById(R.id.tv_level);

        ll_content = findViewById(R.id.ll_content);

        View pop_view = View.inflate(getApplicationContext(), R.layout.comment_window, null);
        popupWindow = new PopupWindow(pop_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        rg_level = (RadioGroup) pop_view.findViewById(R.id.rg_level);

        et_comment_content = (EditText) pop_view.findViewById(R.id.et_comment_content);

        tv_create_comment = (TextView) pop_view.findViewById(R.id.tv_create_comment);
        tv_create_comment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tv_teach_name:
                Intent intent1 = new Intent(getApplicationContext(),TeacherVidoActivity.class);
                intent1.putExtra("teacher",course.teacher);
                startActivity(intent1);
                break;
            case R.id.tv_videos:
                tv_videos.setBackgroundResource(R.mipmap.tab1_select);
                tv_videos.setTextColor(getResources().getColor(R.color.white));

                tv_introduce.setBackgroundResource(R.mipmap.tab2);
                tv_introduce.setTextColor(getResources().getColor(R.color.orange));

                tv_comments.setBackgroundResource(R.mipmap.tab3);
                tv_comments.setTextColor(getResources().getColor(R.color.orange));

                rl_buy.setVisibility(View.VISIBLE);

                tv_tech_title.setVisibility(View.GONE);
                tv_tech_content.setVisibility(View.GONE);

                ll_comments.setVisibility(View.GONE);
                break;
            case R.id.tv_introduce:
                tv_videos.setBackgroundResource(R.mipmap.tab1);
                tv_videos.setTextColor(getResources().getColor(R.color.orange));

                tv_introduce.setBackgroundResource(R.mipmap.tab2_select);
                tv_introduce.setTextColor(getResources().getColor(R.color.white));

                tv_comments.setBackgroundResource(R.mipmap.tab3);
                tv_comments.setTextColor(getResources().getColor(R.color.orange));

                rl_buy.setVisibility(View.GONE);

                //tv_tech_title.setVisibility(View.VISIBLE);
                tv_tech_content.setVisibility(View.VISIBLE);

                ll_comments.setVisibility(View.GONE);

                //tv_tech_title.setText(course.teacher.teacherProjectStr);
                tv_tech_content.setText(course.content);

                break;
            case R.id.tv_comments:
                if(ll_comments.getVisibility()!=View.VISIBLE) {
                    commentCurrPage = 1;
                    tv_videos.setBackgroundResource(R.mipmap.tab1);
                    tv_videos.setTextColor(getResources().getColor(R.color.orange));

                    tv_introduce.setBackgroundResource(R.mipmap.tab2);
                    tv_introduce.setTextColor(getResources().getColor(R.color.orange));

                    tv_comments.setBackgroundResource(R.mipmap.tab3_select);
                    tv_comments.setTextColor(getResources().getColor(R.color.white));

                    rl_buy.setVisibility(View.GONE);

                    tv_tech_title.setVisibility(View.GONE);
                    tv_tech_content.setVisibility(View.GONE);

                    ll_comments.setVisibility(View.VISIBLE);

                    level = "";
                    getCourseComment();
                }
                break;
            case R.id.tv_all:
                if(!level.equals("")) {
                    commentCurrPage = 1;
                    tv_all.setTextColor(getResources().getColor(R.color.orange));
                    tv_good.setTextColor(getResources().getColor(R.color.item_title_color));
                    tv_neutral.setTextColor(getResources().getColor(R.color.item_title_color));
                    tv_bad.setTextColor(getResources().getColor(R.color.item_title_color));

                    view_leftLine.setVisibility(View.VISIBLE);
                    view_centerLine1.setVisibility(View.INVISIBLE);
                    view_centerLine2.setVisibility(View.INVISIBLE);
                    view_rightLine.setVisibility(View.INVISIBLE);

                    level = "";
                    getCourseComment();
                }
                break;
            case R.id.tv_good:
                if(!level.equals(Constant.COMMENT_LEVEL_GOOD)) {
                    commentCurrPage = 1;
                    tv_good.setTextColor(getResources().getColor(R.color.orange));
                    tv_all.setTextColor(getResources().getColor(R.color.item_title_color));
                    tv_neutral.setTextColor(getResources().getColor(R.color.item_title_color));
                    tv_bad.setTextColor(getResources().getColor(R.color.item_title_color));

                    view_centerLine1.setVisibility(View.VISIBLE);
                    view_leftLine.setVisibility(View.INVISIBLE);
                    view_centerLine2.setVisibility(View.INVISIBLE);
                    view_rightLine.setVisibility(View.INVISIBLE);

                    level = Constant.COMMENT_LEVEL_GOOD;
                    getCourseComment();
                }
                break;
            case R.id.tv_neutral:
                if(!level.equals(Constant.COMMENT_LEVEL_MID)) {
                    commentCurrPage = 1;
                    tv_neutral.setTextColor(getResources().getColor(R.color.orange));
                    tv_all.setTextColor(getResources().getColor(R.color.item_title_color));
                    tv_good.setTextColor(getResources().getColor(R.color.item_title_color));
                    tv_bad.setTextColor(getResources().getColor(R.color.item_title_color));

                    view_centerLine2.setVisibility(View.VISIBLE);
                    view_leftLine.setVisibility(View.INVISIBLE);
                    view_centerLine1.setVisibility(View.INVISIBLE);
                    view_rightLine.setVisibility(View.INVISIBLE);

                    level = Constant.COMMENT_LEVEL_MID;
                    getCourseComment();
                }
                break;
            case R.id.tv_bad:
                if(!level.equals(Constant.COMMENT_LEVEL_BAD)) {
                    commentCurrPage = 1;
                    tv_bad.setTextColor(getResources().getColor(R.color.orange));
                    tv_good.setTextColor(getResources().getColor(R.color.item_title_color));
                    tv_neutral.setTextColor(getResources().getColor(R.color.item_title_color));
                    tv_all.setTextColor(getResources().getColor(R.color.item_title_color));

                    view_rightLine.setVisibility(View.VISIBLE);
                    view_centerLine1.setVisibility(View.INVISIBLE);
                    view_centerLine2.setVisibility(View.INVISIBLE);
                    view_leftLine.setVisibility(View.INVISIBLE);

                    level = Constant.COMMENT_LEVEL_BAD;
                    getCourseComment();
                }
                break;
            case R.id.tv_buy:
                if(userId==0){
                    toLogin();
                }else {
                    if (tv_buy.getTag().toString().equals("1")) {
                        Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                        intent.putExtra("course", (Serializable) course);
                        startActivity(intent);
                    } else if (tv_buy.getTag().toString().equals("0")) {
                        popupWindow.showAtLocation(ll_content, Gravity.CENTER, 0, 0);
                    }
                }
                break;
            case R.id.iv_download:
                if(userId==0){
                    toLogin();
                }else {
                    if (tv_buy.getTag()!=null&&tv_buy.getTag().toString().equals("1") && course.score > 0) {
                        ToastUtil.showToast(getApplicationContext(), "该课程为付费课程，购买后才能缓存", Toast.LENGTH_SHORT);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), CacheListActivity.class);
                        intent.putExtra("videoList", (Serializable) videoList);
                        intent.putExtra("course", (Serializable) course);
                        startActivity(intent);
                        finish();
                    }
                }
                break;
            case R.id.tv_collect:
                if(userId==0){
                    toLogin();
                }else {
                    if (course.isCollection.equals("yes")) {
                        cancelCollection();
                    } else {
                        collectCourse();
                    }
                }
                break;
            case R.id.tv_create_comment:
                if(TextUtils.isEmpty(et_comment_content.getText())) {
                    ToastUtil.showToast(getApplicationContext(), "请输入评论内容", Toast.LENGTH_SHORT);
                }else{
                    popupWindow.dismiss();
                    createComment();
                }
                break;
        }
    }

    private void toLogin(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.putExtra("from", 3);
        intent.putExtra("course", (Serializable) course);
        startActivity(intent);
    }

    private void getData() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("courseId", Integer.toString(course.id));
        param.put("userId", Integer.toString(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.COURSE_DETAIL, param, BaseData.class, Course.class,
                getDataSuccessListener(), null);

    }

    public Response.Listener<BaseData> getDataSuccessListener() {
        return new Response.Listener<BaseData>()
        {

            @Override
            public void onResponse(BaseData response) {

                isBuy = response.data.isBuy;

                if(isBuy.equals("yes")) {
                    tv_buy.setText("评价");
                    tv_buy.setTag("0");
                }else{
                    tv_buy.setText("立即购买");
                    tv_buy.setTag("1");
                }
                 dengji =response.data.course.teacher.teacherLevel;

                collect(response.data.isCollection);
                course.isCollection = response.data.isCollection;

                course.videoList = response.data.courseVideoList;
                videoList.addAll(response.data.courseVideoList);
                videoAdapter.notifyDataSetChanged();
                setData();
            }
        };
    }

    private void getCourseComment() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("courseId", Integer.toString(course.id));
        param.put("pageNum", Integer.toString(commentCurrPage));
        param.put("pageSize", Integer.toString(Constant.PAGE_SIZE));
        param.put("level",level);
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.COURSE_COMMENT, param, BaseData.class, Comment.class,
                getCourseCommentSuccessListener(), null);

    }

    public Response.Listener<BaseData> getCourseCommentSuccessListener() {
        return new Response.Listener<BaseData>()
        {

            @Override
            public void onResponse(BaseData response) {
                mRefreshLayout.setRefreshing(false);
                mRefreshLayout.setLoading(false);

                if (commentCurrPage == 1) {
                    commentList.clear();
                }

                if (response.data.page != null
                        && response.data.page.currentPage.equals(response.data.page.totalPage)) {
                    mRefreshLayout.setLoad_More(false);
                }

                commentList.addAll(response.data.list);
                commentAdapter.notifyDataSetChanged();
            }
        };
    }

    private void collectCourse() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("courseId", Integer.toString(course.id));
        param.put("userId", Integer.toString(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.COURSE_COLLECTION, param, BaseData.class, null,
                collectCourseSuccessListener(), null);

    }

    public Response.Listener<BaseData> collectCourseSuccessListener() {
        return new Response.Listener<BaseData>()
        {

            @Override
            public void onResponse(BaseData response) {
                collect("yes");
                course.setIsCollection("yes");
                ToastUtil.showToast(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT);
            }
        };
    }

    private void collect(String isCollection){
        if(isCollection.equals("yes")) {
            tv_collect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.yellow_wjx), null, null);
        }else{
            tv_collect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.white_wjx), null, null);
        }
    }

    private void setData(){
        collect(course.isCollection);
        tv_course_name.setText(course.name);
        if(course.score == 0){
            tv_score.setText("免费");
        }else{
            tv_score.setText(course.score+" 积分");
        }

        tv_sellNumber.setText("已售:"+course.sellNumber);
        tv_teach_name.setText(course.teacher.name+">");
        if(!course.image.path.equals("")) {
            ImageLoader.getInstance().displayImage(course.image.path, iv_course_img);
        }

        if(dengji>0&& dengji<5){
            for(int i=0;i<dengji;i++){
                ImageView image=new ImageView(this);
                image.setImageResource(R.mipmap.dj_aixing);
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ll_level.addView(image);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) image.getLayoutParams();
                int  left= CommentUtils.dip2px(this,2);
                int  right= CommentUtils.dip2px(this,2);
                lp.leftMargin=left;
                lp.rightMargin=right;
                image.setLayoutParams(lp);
            }
        }
       else if(dengji>4&&dengji<9){
            int zx=dengji-4;
            for(int i=0;i<zx;i++){
                ImageView image=new ImageView(this);
                image.setImageResource(R.mipmap.dj_zs);
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ll_level.addView(image);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) image.getLayoutParams();
                int  left= CommentUtils.dip2px(this,2);
                int  right= CommentUtils.dip2px(this,2);
                lp.leftMargin=left;
                lp.rightMargin=right;
                image.setLayoutParams(lp);
            }
        }
        else if(dengji>8&&dengji<13){
            int hg=dengji-8;
            for(int i=0;i<hg;i++){
                ImageView image=new ImageView(this);
                image.setImageResource(R.mipmap.dj_hg);
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ll_level.addView(image);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) image.getLayoutParams();
                int  left= CommentUtils.dip2px(this,2);
                int  right= CommentUtils.dip2px(this,2);
                lp.leftMargin=left;
                lp.rightMargin=right;
                image.setLayoutParams(lp);
            }
        }

        tv_comments.setText("评价(" + course.commentNumber + ")");
    }

    private void cancelCollection() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("courseId", Integer.toString(course.id));
        param.put("userId", Integer.toString(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.CANCEL_COLLECTION, param, BaseData.class, null,
                cancelCollectionSuccessListener(), null);

    }

    public Response.Listener<BaseData> cancelCollectionSuccessListener() {
        return new Response.Listener<BaseData>()
        {

            @Override
            public void onResponse(BaseData response) {
                collect("no");
                course.setIsCollection("no");
                ToastUtil.showToast(getApplicationContext(),"取消收藏成功", Toast.LENGTH_SHORT);
            }
        };
    }

    private void createComment() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("courseId", Integer.toString(course.id));
        param.put("userId", Integer.toString(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        param.put("content", et_comment_content.getText().toString().trim());
        param.put("level", getLevel());
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.CREATE_COMMENT, param, BaseData.class, null,
                createCommentSuccessListener(), null);

    }

    public Response.Listener<BaseData> createCommentSuccessListener() {
        return new Response.Listener<BaseData>()
        {

            @Override
            public void onResponse(BaseData response) {
                commentCurrPage = 1;

                onClick(tv_comments);

                tv_comments.setText("评价(" + response.data.commentNumber + ")");
            }
        };
    }

    private String getLevel() {
        int checkId = rg_level.getCheckedRadioButtonId();
        String level = Constant.COMMENT_LEVEL_GOOD;
        switch (checkId) {
            case R.id.rb_good:
                level = Constant.COMMENT_LEVEL_GOOD;
                break;
            case R.id.rb_mid:
                level = Constant.COMMENT_LEVEL_MID;
                break;
            case R.id.rb_bad:
                level = Constant.COMMENT_LEVEL_BAD;
                break;

            default:
                break;
        }
        return level;
    }

}
