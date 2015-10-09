package com.bm.hm.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.bm.hm.R;
import com.bm.hm.bean.Banner;
import com.bm.hm.bean.Image;
import com.bm.hm.util.FixedSpeedScroller;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 广告模块图片滚动
 */
public class AutomaticViewPager extends ViewPager {

    Activity mActivity; // 上下文
    // List<NetworkImageView> mListViews; // 图片组
    int mScrollTime = 0;
    Timer timer;
    int oldIndex = 0;
    int curIndex = 0;
    private List<Banner> list;
    int position = 0;
    PointF downP = new PointF();
    PointF curP = new PointF();
    private MyPagerAdapter adapter = new MyPagerAdapter();

    public AutomaticViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 开始广告滚动
     *
     * @param mainActivity     显示广告的主界面
     * @param scrollTime       滚动间隔 ,0为不滚动
     * @param ovalLayout       圆点容器,可为空,LinearLayout类型
     * @param ovalLayoutId     ovalLayout为空时 写0, 圆点layout XMl
     * @param ovalLayoutItemId ovalLayout为空时 写0,圆点layout XMl 圆点XMl下View ID
     * @param focusedId        ovalLayout为空时 写0, 圆点layout XMl 选中时的动画
     * @param normalId         ovalLayout为空时 写0, 圆点layout XMl 正常时背景
     */
    public void start(Activity mainActivity, int scrollTime, LinearLayout ovalLayout,
                      int ovalLayoutId, int ovalLayoutItemId, int focusedId, int normalId,
                      List<Banner> list) {

        // if (list == null) {
        mActivity = mainActivity;
        // mListViews = imgList;
        mScrollTime = scrollTime;
        this.list = list;

        // 设置圆点
        setOvalLayout(ovalLayout, ovalLayoutId, ovalLayoutItemId, focusedId, normalId);
        this.setAdapter(adapter);// 设置适配器

        if (scrollTime != 0 && list.size() > 1) {
            // 设置滑动动画时间 ,如果用默认动画时间可不用 ,反射技术实现
            new FixedSpeedScroller(mActivity).setDuration(this, 1000);

            startTimer();
            // 触摸时停止滚动
            this.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        startTimer();
                    } else {
                        stopTimer();
                        ((ViewGroup) v).requestDisallowInterceptTouchEvent(true);
                        if (downP.x == curP.x && downP.y == curP.y) {
                            return false;
                        }
                    }
                    return false;
                }
            });
        }
        if (list.size() > 1) {
            this.setCurrentItem(0);// 设置选中为中间/图片为和第0张一样
        }
    }

    // else {
    // list.clear();
    // // mListViews = imgList;
    // this.list = list;
    // removeAllViews();
    // adapter.notifyDataSetChanged();
    // }
    // }

    // 设置圆点
    private void setOvalLayout(final LinearLayout ovalLayout, int ovalLayoutId,
                               final int ovalLayoutItemId, final int focusedId, final int normalId) {
        if (ovalLayout != null) {
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            ovalLayout.removeAllViews();
            for (int i = 0; i < list.size(); i++) {
                ovalLayout.addView(inflater.inflate(ovalLayoutId, null));

            }
            // 选中第一个
            ovalLayout.getChildAt(0).findViewById(ovalLayoutItemId)
                    .setBackgroundResource(focusedId);
            this.setOnPageChangeListener(new OnPageChangeListener() {

                @Override
                public void onPageSelected(int i) {
                    curIndex = i % list.size();
                    // 取消圆点选中
                    ovalLayout.getChildAt(oldIndex).findViewById(ovalLayoutItemId)
                            .setBackgroundResource(normalId);
                    // 圆点选中
                    ovalLayout.getChildAt(curIndex).findViewById(ovalLayoutItemId)
                            .setBackgroundResource(focusedId);
                    oldIndex = curIndex;
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                }
            });
        }
    }

    /**
     * 取得当明选中下标
     *
     * @return
     */
    public int getCurIndex() {
        return curIndex;
    }

    /**
     * 停止滚动
     */
    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 开始滚动
     */
    public void startTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (timer == null) {
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        AutomaticViewPager.this.setCurrentItem(AutomaticViewPager.this
                                .getCurrentItem() + 1);
                    }
                });
            }
        }, mScrollTime, mScrollTime);
    }

    // 适配器 //循环设置
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (list.size() == 1) {// 一张图片时不用流动
                return list.size();
            }
            return Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            // TODO Auto-generated method stub

            ImageView imageView = new ImageView(mActivity);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            imageView.setImageResource(R.mipmap.img_moren);
            ImageLoader.getInstance().displayImage(list.get(position % list.size()).image.path, imageView);

            container.addView(imageView);
            return imageView;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);

        }
    }

}
