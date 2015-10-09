package com.bm.hm.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.hm.R;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends FragmentActivity implements OnClickListener {

    public TextView tv_title;
    public ImageView iv_back;

    @SuppressWarnings("static-access")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        HMApplication.getInstance().getDeviceInfo(this);

    }

    /**
     * 
     * 标题栏初始化没箭头
     */
    public void initTitleBar(String titleText) {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(titleText);
    }

    /**
     * 
     * 标题栏初始化有箭头
     */
    public void initTitleBarWithBack(String titleText) {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(titleText);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        iv_back.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.iv_back:
            onBackPressed();

            break;
        default:
            break;
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    }

    // 跳转到其他Activity
    public void gotoOtherActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivity(intent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top
                    && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            }
            else {
                return true;
            }
        }
        return false;
    }

    public void hiddenSoftInput(EditText v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        MobclickAgent.onResume(this); // 统计时长
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        MobclickAgent.onPause(this);
//    }

    public void setLayoutParams(View v, int width, int height) {
        int scWidth = this.getResources().getDisplayMetrics().widthPixels;
        float ratio = (float)width / height;
        android.view.ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(scWidth,(int) (scWidth / ratio));
        v.setLayoutParams(lp);
    }

}
