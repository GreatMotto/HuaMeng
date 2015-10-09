package com.bm.hm.me;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.bean.Message;
import com.bm.hm.bean.MyComment;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.util.DialogUtils;
import com.bm.hm.util.SharedPreferencesUtils;
import com.bm.hm.util.ToastUtil;
import com.bm.hm.view.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chenb on 2015/5/4.
 */
public class MessageActivity extends BaseActivity {
    private LinearLayout ll_tongzhi, ll_pinlun, ll_no_msg;
    private TextView iv_tongzhi, iv_xiaoxi, tv_tongzhi, tv_pinlun, tv_no_tongzhi;
    private ImageView iv_no_tongzhi;
    private ListView lv_message;
    private RefreshLayout mRefreshLayout;
    private int currPage = 1;
    private int pageSzie = 10;
    private int page = 1;
    private int genre = 0;//旗标

    private List<Message> list = new ArrayList<Message>();
    private List<MyComment> plist = new ArrayList<MyComment>();

    private MessageAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_message);
        initView();
        messageRequest();
    }

    private void initView() {
        initTitleBarWithBack("通知和消息");

        ll_tongzhi = (LinearLayout) findViewById(R.id.ll_tongzhi);
        ll_pinlun = (LinearLayout) findViewById(R.id.ll_pinlun);
        ll_no_msg = (LinearLayout) findViewById(R.id.ll_no_msg);

        tv_tongzhi = (TextView) findViewById(R.id.tv_tongzhi);
        tv_pinlun = (TextView) findViewById(R.id.tv_pinlun);
        iv_tongzhi = (TextView) findViewById(R.id.iv_tongzhi);
        iv_xiaoxi = (TextView) findViewById(R.id.iv_xiaoxi);

        //没有通知和消息
        tv_no_tongzhi = (TextView) findViewById(R.id.tv_no_tongzhi);
        iv_no_tongzhi = (ImageView) findViewById(R.id.iv_no_tongzhi);

        //list
        lv_message = (ListView) findViewById(R.id.lv_message);

        //刷新的初始化和监听
        mRefreshLayout = (RefreshLayout) findViewById(R.id.ref_message);
        adapter = new MessageAdapter(MessageActivity.this, genre, list);
        lv_message.setAdapter(adapter);
        mRefreshLayout.setColorScheme(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        lv_message.addFooterView(mRefreshLayout.getFootView(), null, false);
        lv_message.setOnScrollListener(mRefreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setLoading(true);
                mRefreshLayout.setLoad_More(true);
                currPage = 1;
                if (genre == 0) {
                    messageRequest();
                } else {
                    pinglunRequest();
                }
                ;
            }
        });
        mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {

            @Override
            public void onLoad() {
                currPage++;

                if (genre == 0) {
                    messageRequest();
                } else {
                    pinglunRequest();
                }
                ;
            }
        });
        ll_pinlun.setOnClickListener(this);
        ll_tongzhi.setOnClickListener(this);
    }

    public void onClick(View v) {
        super.onClick(v);
        ll_no_msg.setVisibility(View.GONE);
        switch (v.getId()) {
            case R.id.ll_tongzhi:  //通知消息
                genre = 0;
                initLeft();
                pageSzie = page * pageSzie;
                page = 1;
                adapter = new MessageAdapter(MessageActivity.this, genre, list);
                lv_message.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                messageRequest();
                break;
            case R.id.ll_pinlun:  //评论消息
                genre = 1;
                initRight();
                pageSzie = page * pageSzie;
                page = 1;
                adapter = new MessageAdapter(MessageActivity.this, plist);
                lv_message.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                pinglunRequest();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    private void initLeft() {
        tv_tongzhi.setTextColor(getResources().getColor(R.color.white));
        ll_tongzhi.setBackgroundResource(R.mipmap.left_orange);
        iv_tongzhi.setBackgroundResource(R.mipmap.yuan_white);

        tv_pinlun.setTextColor(getResources().getColor(R.color.pinglunxx));
        ll_pinlun.setBackgroundResource(R.mipmap.right_white);
        iv_xiaoxi.setBackgroundResource(R.mipmap.yuan_orange);
    }

    private void initRight() {
        tv_pinlun.setTextColor(getResources().getColor(R.color.white));
        ll_pinlun.setBackgroundResource(R.mipmap.right_orange);
        iv_xiaoxi.setBackgroundResource(R.mipmap.yuan_white);

        tv_tongzhi.setTextColor(getResources().getColor(R.color.pinglunxx));
        ll_tongzhi.setBackgroundResource(R.mipmap.left_white);
        iv_tongzhi.setBackgroundResource(R.mipmap.yuan_orange);
    }

    /**
     * @Description 通知消息  请求后台数据
     */
    public void messageRequest() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", String.valueOf(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        param.put("pageNum", Integer.toString(currPage));
        param.put("pageSize", Integer.toString(Constant.PAGE_SIZE));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.MYMESSAGE, param, BaseData.class, Message.class,
                messageSuccessListener(), null);
    }

    private Response.Listener<BaseData> messageSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
//                SharedPreferencesUtils.getInstance().putInt(Constant.MESSAGEID,response.data.message.id);
                mRefreshLayout.setRefreshing(false);
                mRefreshLayout.setLoading(false);
                if (response.data.list != null) {
                    if (response.data.list.size() == 0) {   //没有通知消息时
                        list.clear();
                        ll_no_msg.setVisibility(View.VISIBLE);
                        iv_no_tongzhi.setImageResource(R.mipmap.no_tongzhi);
                        tv_no_tongzhi.setText(R.string.no_tongzhi);
                    } else {
                        ll_no_msg.setVisibility(View.GONE);
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
                }
                adapter.notifyDataSetChanged();
            }
        };
    }

    /**
     * @Description 删除通知消息  请求后台数据
     */
    public void deleteMessageRequest(int pos) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("messageId", String.valueOf(list.get(pos).id));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.DELETEMSG, param, BaseData.class, Message.class,
                deleMsgSuccessListener(), null);
    }

    private Response.Listener<BaseData> deleMsgSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData baseData) {
                DialogUtils.cancleProgressDialog();
                ToastUtil.showToast(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT);
                messageRequest();
            }
        };
    }

    /**
     * @Description 消息标记为已读  请求后台数据
     */
    public void markReadRequest(int pos1) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("messageId", String.valueOf(list.get(pos1).id));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.MARKREAD, param, BaseData.class, Message.class,
                markReadSuccessListener(), null);
    }

    private Response.Listener<BaseData> markReadSuccessListener() {
        return new Response.Listener<BaseData>() {


            @Override
            public void onResponse(BaseData baseData) {
                messageRequest();
            }
        };
    }


    /**
     * @Description 评论消息  请求后台数据
     */
    public void pinglunRequest() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", String.valueOf(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        param.put("pageNum", Integer.toString(currPage));
        param.put("pageSize", Integer.toString(Constant.PAGE_SIZE));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.MYCOMMENT, param, BaseData.class, MyComment.class,
                pinglunSuccessListener(), null);
    }

    private Response.Listener<BaseData> pinglunSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                mRefreshLayout.setRefreshing(false);
                mRefreshLayout.setLoading(false);
                if (response.data.list != null) {

                    if (response.data.list.size() == 0) {   //没有评论消息时
                        plist.clear();
                        ll_no_msg.setVisibility(View.VISIBLE);
                        iv_no_tongzhi.setImageResource(R.mipmap.no_xiaoxi);
                        tv_no_tongzhi.setText(R.string.no_xiaoxi);
                    } else {
                        ll_no_msg.setVisibility(View.GONE);
                        mRefreshLayout.setRefreshing(false);
                        mRefreshLayout.setLoading(false);
                        if (currPage == 1) {
                            plist.clear();
                        }
                        if (response.data.page != null
                                && response.data.page.currentPage.equals(response.data.page.totalPage)) {
                            mRefreshLayout.setLoad_More(false);
                        }
                        plist.addAll(response.data.list);

                    }
                }
                adapter.notifyDataSetChanged();
            }

        };
    }

    /**
     * @Description 删除评论消息  请求后台数据
     */
    public void deletePinglunRequest(int pos1) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("id", String.valueOf(plist.get(pos1).id));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.DELETE_PL, param, BaseData.class, MyComment.class,
                deletePinlunSuccessListener(), null);
    }

    private Response.Listener<BaseData> deletePinlunSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData baseData) {
                DialogUtils.cancleProgressDialog();
                ToastUtil.showToast(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT);
                pinglunRequest();
            }
        };
    }
}
