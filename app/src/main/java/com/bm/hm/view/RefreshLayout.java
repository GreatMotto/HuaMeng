package com.bm.hm.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bm.hm.R;

/**
 * Created by sunqq on 2015/5/4 0004.
 */
public class RefreshLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener {

    /**
     * listview实例
     */
    private ListView mListView;
    /**
     * 上拉监听器, 到了最底部的上拉加载操作
     */
    private OnLoadListener mOnLoadListener;
    private boolean mLastItemVisible;
    /**
     * 是否在加载中 ( 上拉加载更多 )
     */
    private boolean isLoading = false;
    boolean load_more = true;
    private View footView;

    ProgressBar bottom_progressBar;
    TextView tip_view;

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        footView = LayoutInflater.from(context).inflate(R.layout.ac_footer, null);

        bottom_progressBar = (ProgressBar) footView.findViewById(R.id.bottom_progressBar);
        tip_view = (TextView) footView.findViewById(R.id.tip_view);
        footView.setVisibility(View.GONE);
    }

    public void addFooterView() {
        mListView.addFooterView(footView);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // if (mListView == null) {
        // getListView();
        // }
        // 初始化ListView对象
    }

    /**
     * 获取ListView对象
     */
    private void getListView() {
        int childs = getChildCount();
        if (childs > 0) {
            View childView = getChildAt(0);
            if (childView instanceof ListView) {
                mListView = (ListView) childView;
                // 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
                mListView.setOnScrollListener(this);
            }
        }
    }

    public View getFootView() {
        return footView;
    }

    /**
     * @param loadListener
     */
    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }

    /**
     * 加载更多的监听器
     *
     * @author mrsimple
     */
    public static interface OnLoadListener {

        public void onLoad();
    }

    /**
     * @param loading
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    /**
     * @param loadmore
     */
    public void setLoad_More(boolean loadmore) {
        load_more = loadmore;

        if (!load_more) {
            footView.setVisibility(View.VISIBLE);
            bottom_progressBar.setVisibility(View.GONE);
            tip_view.setText(getResources().getString(R.string.more_tip));
        }
        else {
            bottom_progressBar.setVisibility(View.VISIBLE);
            tip_view.setText(getResources().getString(R.string.load_tip));
            footView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (!isLoading && load_more && mLastItemVisible) {
            footView.setVisibility(View.VISIBLE);
            setLoading(true);
            mOnLoadListener.onLoad();
        }

//        switch (scrollState) {
//            case OnScrollListener.SCROLL_STATE_IDLE: //
////                Constant.isRefresh = false;
//                break;
//            case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
////                Constant.isRefresh = true;
//                break;
//        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        mLastItemVisible = (totalItemCount > 0)
                && (firstVisibleItem + visibleItemCount >= totalItemCount - 1);
    }
}
