package com.bm.hm.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.bean.User;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.util.SharedPreferencesUtils;
import com.bm.hm.util.ToastUtil;

import java.util.HashMap;

/**
 * Created by liuz on 2015/4/24.
 */
public class ModifySex extends BaseActivity {

    private RelativeLayout rl_sex_man, rl_sex_woman;
    private ImageView iv_sex_man, iv_sex_woman;
    private String sex_modify;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_modify_sex);
        initView();
    }

    private void initView() {
        initTitleBarWithBack("性别");
        rl_sex_man = (RelativeLayout) findViewById(R.id.rl_sex_man);
        rl_sex_man.setOnClickListener(this);
        iv_sex_man = (ImageView) findViewById(R.id.iv_sex_man);

        rl_sex_woman = (RelativeLayout) findViewById(R.id.rl_sex_woman);
        rl_sex_woman.setOnClickListener(this);
        iv_sex_woman = (ImageView) findViewById(R.id.iv_sex_woman);

        sex_modify = getIntent().getStringExtra("sex");
        if (TextUtils.equals(sex_modify, "男")) {
            iv_sex_man.setImageResource(R.mipmap.sex_sure);
            iv_sex_woman.setImageResource(R.mipmap.sex_false);
        } else {
            iv_sex_woman.setImageResource(R.mipmap.sex_sure);
            iv_sex_man.setImageResource(R.mipmap.sex_false);
        }
    }

    private void clear() {
        iv_sex_man.setImageResource(R.mipmap.sex_false);
        iv_sex_woman.setImageResource(R.mipmap.sex_false);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.rl_sex_man:
                clear();
                iv_sex_man.setImageResource(R.mipmap.sex_sure);
                type = "0";
                sexRequest(type);
                break;
            case R.id.rl_sex_woman:
                clear();
                iv_sex_woman.setImageResource(R.mipmap.sex_sure);
                type = "1";
                sexRequest(type);
                break;

            default:
                break;

        }
    }

    /**
     * @Description 修改性别  请求后台数据
     */
    private void sexRequest(String type) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", String.valueOf(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        param.put("sex", type);
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.UPDATE_USERINFO, param, BaseData.class, User.class,
                modifySexSuccessListener(), null);
    }

    //回调成功
    private Response.Listener<BaseData> modifySexSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData baseData) {
                ToastUtil.showToast(getApplicationContext(), "性别修改成功", Toast.LENGTH_SHORT);
                getIntent().putExtra("sex", type);
                setResult(4, getIntent());
                finish();
            }
        };
    }
}
