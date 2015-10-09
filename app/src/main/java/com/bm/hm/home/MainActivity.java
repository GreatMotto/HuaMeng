package com.bm.hm.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.cache.CacheFragment;
import com.bm.hm.constant.Constant;
import com.bm.hm.me.MyCenterFragment;
import com.bm.hm.type.TypeFragment;
import com.bm.hm.user.LoginActivity;
import com.bm.hm.util.SharedPreferencesUtils;

public class MainActivity extends BaseActivity {

    FragmentTabHost mTabHost = null;

    private View indicator = null;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);

        initView();
        int tag = this.getIntent().getIntExtra("tag", 0);
        showCurrentTag(tag);
    }

    @Override
    public void onResume() {
        super.onResume();

        userId = SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID);
    }

    private void showCurrentTag(int tag) {
        switch (tag) {
            case 0:
                mTabHost.setCurrentTabByTag("home");
                setCurrentTabBg(tag);
                break;
            case 1:
                mTabHost.setCurrentTabByTag("type");
                setCurrentTabBg(tag);
                break;
            case 2:
                mTabHost.setCurrentTabByTag("cache");
                setCurrentTabBg(tag);
                break;
            case 3:
                mTabHost.setCurrentTabByTag("me");
                setCurrentTabBg(tag);
                break;
        }
    }

    private void initView() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        indicator = getIndicatorView(getResources().getString(R.string.home),
                R.layout.home_indicator);
        mTabHost.addTab(mTabHost.newTabSpec("home").setIndicator(indicator), HomeFragment.class,
                null);

        indicator = getIndicatorView(getResources().getString(R.string.type),
                R.layout.type_indicator);
        mTabHost.addTab(mTabHost.newTabSpec("type").setIndicator(indicator),
                TypeFragment.class, null);

        indicator = getIndicatorView(getResources().getString(R.string.cache),
                R.layout.cache_indicator);
        mTabHost.addTab(mTabHost.newTabSpec("cache").setIndicator(indicator),
                CacheFragment.class, null);

        indicator = getIndicatorView(getResources().getString(R.string.me), R.layout.me_indicator);
        mTabHost.addTab(mTabHost.newTabSpec("me").setIndicator(indicator), MyCenterFragment.class, null);

        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            tabOnclick(i);
        }
        mTabHost.getTabWidget().setDividerDrawable(null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showCurrentTag(0);
    }

    private View getIndicatorView(String name, int layoutId) {
        View v = getLayoutInflater().inflate(layoutId, null);
        TextView tv = (TextView) v.findViewById(R.id.tabText);
        tv.setText(name);
        return v;
    }

    private void tabOnclick(final int tabIndex) {
        mTabHost.getTabWidget().getChildAt(tabIndex).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((tabIndex == 3 || tabIndex == 2) && userId == 0) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.putExtra("tag", tabIndex);
                    startActivity(intent);
                } else {
                    showCurrentTag(tabIndex);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTabHost = null;
    }

    private void setCurrentTabBg(int index){
        for(int i=0;i<4;i++){
            View view = mTabHost.getTabWidget().getChildTabViewAt(i);
            View sjx = view.findViewById(R.id.sjx);
            if(i == index){
                sjx.setVisibility(View.VISIBLE);
                mTabHost.getTabWidget().getChildTabViewAt(i).setBackgroundColor(getResources().getColor(R.color.light_orange));
            }else{
                sjx.setVisibility(View.INVISIBLE);
                mTabHost.getTabWidget().getChildTabViewAt(i).setBackgroundColor(getResources().getColor(R.color.orange));
            }
        }
    }


}
