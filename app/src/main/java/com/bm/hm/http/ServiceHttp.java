package com.bm.hm.http;

import java.util.HashMap;

import android.content.Context;

import com.android.volley.Response.Listener;

public class ServiceHttp {

    private Context mContext;
    private String Url;
    private HashMap<String, String> param;
    private Listener<BaseData> successListener;
    private Class<?> child;

    public ServiceHttp(Context mContext, String url, HashMap<String, String> param,
            Listener<BaseData> successListener) {
        super();
        this.mContext = mContext;
        Url = url;
        this.param = param;
        this.successListener = successListener;
    }

    public void getData() {
        // HashMap<String, String> param = new HashMap<String, String>();
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(mContext);

        request.HttpVolleyRequestPost(Url, param, BaseData.class, child, successListener, null);
    }

    public void setBean(Class<?> child) {
        this.child = child;
    }

    // /**
    // *
    // * @Description 与服务器交互成功监听
    // * @param 说明参数含义
    // * @return 说明返回值含义
    // */
    // @SuppressWarnings("rawtypes")
    // private Listener<BaseData> successListener() {
    //
    // return new Listener<BaseData>()
    // {
    //
    // @SuppressWarnings("unused")
    // @Override
    // public void onResponse(BaseData response) {
    // Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT).show();
    // }
    // };
    // }

}
