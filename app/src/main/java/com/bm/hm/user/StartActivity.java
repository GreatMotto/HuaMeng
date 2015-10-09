package com.bm.hm.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bm.hm.R;
import com.bm.hm.adpter.ViewPagerAdapter;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.home.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuz on 2015/4/21.
 * 启动页面和启动引导页
 */
public class StartActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    // 引导图片资源
    private static final int[] pics = { R.mipmap.startpic_one, R.mipmap.startpic_two, R.mipmap.startpic_three };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_start);


        new Handler().postDelayed(new Runnable()
        {

            public void run() {
                /*
                 * Create an Intent that will start the Main WordPress Activity.
                 */
                if (isFirstEnter(StartActivity.this, StartActivity.this.getClass()
                        .getName())) {
                    initGuide();
                }
                else {
                    gotoOtherActivity(MainActivity.class);
//                    gotoOtherActivity(RegGetCodeActivity.class);
                    StartActivity.this.finish();
                }
            }
        }, 2000);

    }

    /**
     * 引导界面初始化
     */
    private void initGuide() {
        views = new ArrayList<View>();
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // 初始化引导图片列表
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setBackgroundResource(pics[i]);
            iv.setOnClickListener(this);
            iv.setId(i);
            views.add(iv);
        }
        vp = (ViewPager) findViewById(R.id.viewpager);
        // 初始化Adapter
        vpAdapter = new ViewPagerAdapter(views);
        vp.setAdapter(vpAdapter);
        // 绑定回调
        vp.setOnPageChangeListener(this);
        vp.setOnClickListener(this);
    }

    /**
     * 判断应用是否初次加载，读取SharedPreferences中的guide_activity字段
     */
    private static final String SHAREDPREFERENCES_NAME = "my_pref";
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";

    private boolean isFirstEnter(Context context, String className) {
        if (context == null || className == null || "".equalsIgnoreCase(className))
            return false;
        String mResultStr = context.getSharedPreferences(SHAREDPREFERENCES_NAME,
                Context.MODE_WORLD_READABLE).getString(KEY_GUIDE_ACTIVITY, "");
        if (mResultStr.equalsIgnoreCase("false"))
            return false;
        else
            return true;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case 2:
                setGuided();
                gotoOtherActivity(MainActivity.class);
//                gotoOtherActivity(RegGetCodeActivity.class);
                this.finish();
                break;

        }
    }
    /**
     * 设置已经引导
     */
    private void setGuided() {
        SharedPreferences settings = getSharedPreferences(SHAREDPREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(KEY_GUIDE_ACTIVITY, "false");
        editor.commit();
    }
    // 当前页面被滑动时调用
    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int i) {
        System.out.println("onPageSelected----------->" + i);
        if (i == 2) {
            Button btn_start = (Button) findViewById(R.id.btn_start);
            btn_start.setVisibility(View.GONE);
            // btn_start.setOnClickListener(this);

        }
    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
