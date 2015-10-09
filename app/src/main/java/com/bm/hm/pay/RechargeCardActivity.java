package com.bm.hm.pay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.bean.Course;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.util.SharedPreferencesUtils;

import java.util.HashMap;

/**
 * Created by liuz on 2015/4/28.
 */
public class RechargeCardActivity extends BaseActivity {

    private EditText et_card_number,et_card_password;
    private Button btn_card_pay_now;
    private int from;
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_pay_card);
        initView();
    }

    private void initView() {
        from = getIntent().getIntExtra("from",from);
        course = (Course) getIntent().getSerializableExtra("course");

        initTitleBarWithBack("充值");
        et_card_number = (EditText)findViewById(R.id.et_card_number);
        et_card_password = (EditText)findViewById(R.id.et_card_password);
        btn_card_pay_now = (Button)findViewById(R.id.btn_card_pay_now);
        btn_card_pay_now.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_card_pay_now:
                recharge();
                break;

            default:
                break;
        }
    }

    private void recharge() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", Integer.toString(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        param.put("number",  et_card_number.getText().toString().trim());
        param.put("password", et_card_password.getText().toString().trim());
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.CARD_RECHARGE, param, BaseData.class, null,
                rechargeSuccessListener(), null,1);

    }

    public Response.Listener<BaseData> rechargeSuccessListener() {
        return new Response.Listener<BaseData>()
        {

            @Override
            public void onResponse(BaseData response) {
                int score = SharedPreferencesUtils.getInstance().getInt(Constant.SCORE);
                SharedPreferencesUtils.getInstance().putInt(Constant.SCORE, response.data.plusScore + score);

                Intent intent = new Intent(getApplicationContext(), RechargeResultActivity.class);
                intent.putExtra("from", from);
                intent.putExtra("course", course);
                if(response.status.equals("0")){
                    intent.putExtra("result", "success");
                }else{
                    intent.putExtra("result", "fail");
                }
                startActivity(intent);
            }
        };
    }

}
