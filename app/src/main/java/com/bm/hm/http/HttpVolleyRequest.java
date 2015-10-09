package com.bm.hm.http;

import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.bm.hm.base.HMApplication;
import com.bm.hm.util.DataCache;
import com.bm.hm.util.DialogUtils;
import com.bm.hm.util.ErrorUtils;

public class HttpVolleyRequest<T> {

    private ErrorListener errorListener;
    private Listener<T> successListener;
    private Class<T> baseClass;
    private Context mAct;
    private boolean isCache = false; // 是否开启缓存
    private boolean isExit = true; // 判断缓存文件是否存在
    private String urlKey;
    private HashMap<String, String> params;
    private int method;
    private int tag;

    // 监听fail 和 Empty
    private OnResposeListener mOnResposeListener;

    public interface OnResposeListener {

        public void OnFailData(String error);

        public void OnEmptyData();
    }

    public void setOnResposeListener(OnResposeListener onResposeListener) {
        mOnResposeListener = onResposeListener;
    }

    public HttpVolleyRequest() {
    }

    public HttpVolleyRequest(Context mAct) {
        this.mAct = mAct;
    }

    public HttpVolleyRequest(Context mAct, boolean isCache) {
        this.isCache = isCache;
        this.mAct = mAct;
    }

    private Handler handler = new Handler()
    {

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            successListener.onResponse((T) msg.obj);
        }
    };

    /**
     * Volley Get方法
     * 
     * @param url
     *            请求地址
     * @param parentClass
     *            父类数据结构
     * @param class1
     *            子类数据结构
     * @param listener
     *            成功监听
     * @param errorListener
     *            错误监听
     */
    @SuppressWarnings("unchecked")
    public void HttpVolleyRequestGet(String url, Class<T> parentClass, Class<?> class1,
            Listener<T> listener, ErrorListener errorListener) {

        GsonRequest<T> request = new GsonRequest<T>(Method.GET, url, parentClass, class1,
                SuccessListener(), ErrorListener());
        RequestManager.getRequestQueue().add(request);
        this.errorListener = errorListener;
        this.successListener = listener;
        this.baseClass = parentClass;
        this.urlKey = url;
        this.method = Method.GET;
        // 把缓存带出去
        if (isCache) {
            // successListener.onResponse((T)
            // UpairsApplication.getInstance().getCache()
            // .getAsObject(getCacheKey(url)));
            T t = (T) HMApplication.getInstance().getCache().getAsObject(getCacheKey(url));
            if (t != null) {
                handler.sendMessageDelayed(handler.obtainMessage(0, t), 1000);
            }
            else {
                isExit = false;
            }
        }
        else {
            isExit = false;
        }
    }

    /**
     * Volley Post方法
     * 
     * @param url
     *            请求地址
     * @param params
     *            上传的Map数据结构
     * @param parentClass
     *            父类数据结构
     * @param class1
     *            子类数据结构
     * @param listener
     *            成功监听
     * @param errorListener
     *            错误监听
     */
    @SuppressWarnings("unchecked")
    public void HttpVolleyRequestPost(String url, HashMap<String, String> params,
            Class<T> parentClass, Class<?> class1, Listener<T> listener, ErrorListener errorListener) {

        GsonRequest<T> request = new GsonRequest<T>(Method.POST, url, params, parentClass, class1,
                SuccessListener(), ErrorListener());
        RequestManager.getRequestQueue().add(request);
        this.errorListener = errorListener;
        this.successListener = listener;
        this.baseClass = parentClass;
        this.urlKey = url;
        this.method = Method.POST;
        this.params = params;
        // 把缓存带出去
        if (isCache) {
            // successListener.onResponse((T)
            // UpairsApplication.getInstance().getCache()
            // .getAsObject(getCacheKey(url)));
            T t = (T) HMApplication.getInstance().getCache()
                    .getAsObject(getPostCacheKey(url, params));
            if (t != null) {
                handler.sendMessageDelayed(handler.obtainMessage(0, t), 1000);
            }
            else {
                isExit = false;
            }
        }
        else {
            isExit = false;
        }
    }

    @SuppressWarnings("unchecked")
    public void HttpVolleyRequestPost(String url, HashMap<String, String> params,
            Class<T> parentClass, Class<?> class1, Listener<T> listener,
            ErrorListener errorListener, int tag) {

        GsonRequest<T> request = new GsonRequest<T>(Method.POST, url, params, parentClass, class1,
                SuccessListener(), ErrorListener());
        RequestManager.getRequestQueue().add(request);
        this.errorListener = errorListener;
        this.successListener = listener;
        this.baseClass = parentClass;
        this.urlKey = url;
        this.method = Method.POST;
        this.params = params;
        this.tag = tag;
        // 把缓存带出去
        if (isCache) {
            // successListener.onResponse((T)
            // UpairsApplication.getInstance().getCache()
            // .getAsObject(getCacheKey(url)));
            T t = (T) HMApplication.getInstance().getCache()
                    .getAsObject(getPostCacheKey(url, params));
            if (t != null) {
                handler.sendMessageDelayed(handler.obtainMessage(0, t), 1000);
            }
            else {
                isExit = false;
            }
        }
        else {
            isExit = false;
        }
    }

    /**
     * 缓存成功处理
     * 
     * @return
     */
    private Listener<T> SuccessListener() {
        return new Listener<T>()
        {

            @SuppressWarnings({ "unchecked", "deprecation" })
            @Override
            public void onResponse(T response) {

                if ("0".equals(((BaseData<T>) response).status)) {
                    if (isCache) {
                        if (!isExit) {
                            if (successListener != null) {
                                successListener.onResponse(response);
                            }
                        }
                        if (method == Method.GET) {
                            HMApplication
                                    .getInstance()
                                    .getCache()
                                    .put(getCacheKey(urlKey), (BaseData<T>) response,
                                            DataCache.TIME_DAY);
                        }
                        else {
                            HMApplication
                                    .getInstance()
                                    .getCache()
                                    .put(getPostCacheKey(urlKey, params), (BaseData<T>) response,
                                            DataCache.TIME_DAY);
                        }

                    }
                    else {
                        if (successListener != null) {
                            successListener.onResponse(response);
                        }
                    }
                }
                else if ("1".equals(((BaseData<T>) response).status)) {
                    if (tag == 1) {
                        successListener.onResponse(response);
                    }
                    DialogUtils.cancleProgressDialog();
                    ErrorUtils.showErrorMsg(mAct, ((BaseData<T>) response).msg);
                }

            }
        };
    }

    /**
     * 返回错误Response 集中处理错误提示消息APPMsg
     * 
     * @return
     */
    private ErrorListener ErrorListener() {
        return new ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (mAct != null) {
                    DialogUtils.cancleProgressDialog();
                    String message = VolleyErrorHelper.getMessage(error, mAct);
                    Toast.makeText(mAct, message, Toast.LENGTH_LONG).show();

                }

                if (errorListener != null)
                    errorListener.onErrorResponse(error);
            }
        };
    }

    private String getCacheKey(String url) {
        return url.substring(0, url.indexOf("&"));
    }

    private String getPostCacheKey(String url, HashMap<String, String> map) {
        return url + getParams(map);
    }

    public String getParams(HashMap<String, String> map) {
        String str1 = "";
        // 参数为空
        if (map.isEmpty()) {
            return str1;
        }
        Iterator<String> localIterator = map.keySet().iterator();
        while (true) {
            if (!localIterator.hasNext()) {
                return str1.substring(0, -1 + str1.length());
            }
            String str2 = (String) localIterator.next();
            str1 = str1 + str2 + "_" + (String) map.get(str2) + ",";
        }
    }
}
