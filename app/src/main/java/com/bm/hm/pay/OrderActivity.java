package com.bm.hm.pay;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.bean.Course;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.util.SharedPreferencesUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;

/**
 * Created by liuz on 2015/4/29.
 */
public class OrderActivity extends BaseActivity {
    private TextView tv_course_name, tv_score, tv_pay_score,tv_my_score,tv_int_text_underline,tv_tip1,tv_tip2;
    private Button btn_pay;
    private ImageView iv_course_img;
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_order);

        initView();
    }

    private void initView() {
        initTitleBarWithBack("积分支付");

        course = (Course) getIntent().getSerializableExtra("course");

        tv_course_name = (TextView) findViewById(R.id.tv_course_name);
        tv_course_name.setText(course.name);

        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_score.setText(String.valueOf(course.score));

        tv_pay_score = (TextView) findViewById(R.id.tv_pay_score);
        tv_pay_score.setText(String.valueOf(course.score));

        tv_my_score = (TextView) findViewById(R.id.tv_my_score);
        tv_my_score.setText(String.valueOf(SharedPreferencesUtils.getInstance().getInt(Constant.SCORE)));

        iv_course_img = (ImageView) findViewById(R.id.iv_course_img);
        ImageLoader.getInstance().displayImage(course.image.path,iv_course_img);

        //下划线
        tv_int_text_underline = (TextView) findViewById(R.id.tv_int_text_underline);
        tv_int_text_underline.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_int_text_underline.setTextColor(getResources().getColor(R.color.orange));

        //充值和支付
        btn_pay = (Button) findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(this);

        tv_tip1 = (TextView) findViewById(R.id.tv_tip1);
        tv_tip2 = (TextView) findViewById(R.id.tv_tip2);

        int score = SharedPreferencesUtils.getInstance().getInt(Constant.SCORE);
        if(score<course.score){
            btn_pay.setText("充值");
            btn_pay.setTag("1");
        }else{
            btn_pay.setText("积分支付");
            btn_pay.setTag("2");

            tv_tip1.setVisibility(View.INVISIBLE);
            tv_tip2.setVisibility(View.INVISIBLE);
            tv_int_text_underline.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_pay:
                if(btn_pay.getTag().equals("1")){
                    Intent intent = new Intent(getApplicationContext(),RechargeActivity.class);
                    intent.putExtra("from",3);
                    intent.putExtra("course",course);
                    startActivity(intent);
                }else{
                    payCourse();
                }
                break;

            default:
                break;
        }
    }

    private void payCourse() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", String.valueOf(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        param.put("courseId", Integer.toString(course.id));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.PAY_COURSE, param, BaseData.class, null,
                payCourseSuccessListener(), null,1);

    }

    public Response.Listener<BaseData> payCourseSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                Intent intent = new Intent(getApplicationContext(), PayResultActivity.class);
                if(response.status.equals("0")){
                    course.sellNumber = response.data.sellNumber;
                    int score = SharedPreferencesUtils.getInstance().getInt(Constant.SCORE)-course.score;
                    SharedPreferencesUtils.getInstance().putInt(Constant.SCORE,score);
                    intent.putExtra("course",course);
                    intent.putExtra("result","success");
                }else{
                    intent.putExtra("course",course);
                    intent.putExtra("result","faile");
                }
                startActivity(intent);
            }
        };
    }
}
