package com.bm.hm.me;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.base.HMApplication;
import com.bm.hm.bean.User;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.pay.RechargeActivity;
import com.bm.hm.user.LoginActivity;
import com.bm.hm.util.CommentUtils;
import com.bm.hm.util.DialogUtils;
import com.bm.hm.util.SharedPreferencesUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

import java.io.File;
import java.util.HashMap;

/**
 * Created by liuz on 2015/4/23.
 */
public class MyCenterFragment extends Fragment implements View.OnClickListener {

    private View view;
    private TextView tv_cen_band_number, tv_cen_integral, tv_count;
    public LinearLayout ll_dengji;
    private ImageView iv_center_pic;
    private View view_xs;
    private Button btn_cen_pay;
    private TextView tv_cen_news, tv_cen_video, tv_cen_tercher, tv_cen_course, tv_cen_plan, tv_cen_timer_shaft, tv_cen_name;
    private RelativeLayout rl_cen_clear_memory, rl_cen_message_back, rl_cen_about_us, rl_cen_versions, rl_cen_contact, rl_help;
    private final int photoResultCode = 1001;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        if (view == null) {
            view = inflater.inflate(R.layout.fg_my_center, container, false);
        }

        initView();
        getMsgCount();
        initData();
        return view;
    }

    private void initView() {
        //昵称
        tv_cen_name = (TextView) view.findViewById(R.id.tv_cen_name);
        //等级
        view_xs = (View) view.findViewById(R.id.view_xs);
        ll_dengji = (LinearLayout) view.findViewById(R.id.ll_dengji);
        //绑定学号
        tv_cen_band_number = (TextView) view.findViewById(R.id.tv_cen_band_number);
        tv_cen_band_number.setOnClickListener(this);
        //用户图片  进入个人信息
        iv_center_pic = (ImageView) view.findViewById(R.id.iv_center_pic);
        iv_center_pic.setOnClickListener(this);
        //积分明细
        tv_cen_integral = (TextView) view.findViewById(R.id.tv_cen_integral);
        tv_cen_integral.setOnClickListener(this);
        //充值
        btn_cen_pay = (Button) view.findViewById(R.id.btn_cen_pay);
        btn_cen_pay.setOnClickListener(this);
        //消息
        tv_cen_news = (TextView) view.findViewById(R.id.tv_cen_news);
        tv_cen_news.setOnClickListener(this);
        //视频
        tv_cen_video = (TextView) view.findViewById(R.id.tv_cen_video);
        tv_cen_video.setOnClickListener(this);
        //我的老师
        tv_cen_tercher = (TextView) view.findViewById(R.id.tv_cen_tercher);
        tv_cen_tercher.setOnClickListener(this);
        //课表
        tv_cen_course = (TextView) view.findViewById(R.id.tv_cen_course);
        tv_cen_course.setOnClickListener(this);
        //计划
        tv_cen_plan = (TextView) view.findViewById(R.id.tv_cen_plan);
        tv_cen_plan.setOnClickListener(this);
        //时间轴
        tv_cen_timer_shaft = (TextView) view.findViewById(R.id.tv_cen_timer_shaft);
        tv_cen_timer_shaft.setOnClickListener(this);
        //清除缓存
        rl_cen_clear_memory = (RelativeLayout) view.findViewById(R.id.rl_cen_clear_memory);
        rl_cen_clear_memory.setOnClickListener(this);
        //意见反馈
        rl_cen_message_back = (RelativeLayout) view.findViewById(R.id.rl_cen_message_back);
        rl_cen_message_back.setOnClickListener(this);
        //关于我们
        rl_cen_about_us = (RelativeLayout) view.findViewById(R.id.rl_cen_about_us);
        rl_cen_about_us.setOnClickListener(this);
        //版本更新
        rl_cen_versions = (RelativeLayout) view.findViewById(R.id.rl_cen_versions);
        rl_cen_versions.setOnClickListener(this);
        //联系我们
        rl_cen_contact = (RelativeLayout) view.findViewById(R.id.rl_cen_contact);
        rl_cen_contact.setOnClickListener(this);
        //帮助
        rl_help = (RelativeLayout) view.findViewById(R.id.rl_help);
        rl_help.setOnClickListener(this);

        tv_count = (TextView) view.findViewById(R.id.tv_count);
    }

    private void initData() {
        String path = SharedPreferencesUtils.getInstance().getString(Constant.PHOTO);
        String nicheng = SharedPreferencesUtils.getInstance().getString(Constant.NICKNAME);
        int jifen = SharedPreferencesUtils.getInstance().getInt(Constant.SCORE);   //积分
        int dengji = SharedPreferencesUtils.getInstance().getInt(Constant.SCALE);   //等级
        String shenfen = SharedPreferencesUtils.getInstance().getString(Constant.ROLE);  //身份

        tv_cen_name.setText(nicheng);
        tv_cen_integral.setText("积分" + String.valueOf(jifen));

        if (shenfen.equals("teacher")) {
            ll_dengji.setVisibility(View.VISIBLE);
            view_xs.setVisibility(View.VISIBLE);

            if (dengji > 0 && dengji < 5) {
                for (int i = 0; i < dengji; i++) {
                    ImageView image = new ImageView(getActivity());
                    image.setImageResource(R.mipmap.dj_aixing);
                    image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ll_dengji.addView(image);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) image.getLayoutParams();
                    int left = CommentUtils.dip2px(getActivity(), 2);
                    int right = CommentUtils.dip2px(getActivity(), 2);
                    lp.leftMargin = left;
                    lp.rightMargin = right;
                    image.setLayoutParams(lp);
                }
            } else if (dengji > 4 && dengji < 9) {
                int zx = dengji - 4;
                for (int i = 0; i < zx; i++) {
                    ImageView image = new ImageView(getActivity());
                    image.setImageResource(R.mipmap.dj_zs);
                    image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ll_dengji.addView(image);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) image.getLayoutParams();
                    int left = CommentUtils.dip2px(getActivity(), 2);
                    int right = CommentUtils.dip2px(getActivity(), 2);
                    lp.leftMargin = left;
                    lp.rightMargin = right;
                    image.setLayoutParams(lp);
                }
            } else if (dengji > 8 && dengji < 13) {
                int hg = dengji - 8;
                for (int i = 0; i < hg; i++) {
                    ImageView image = new ImageView(getActivity());
                    image.setImageResource(R.mipmap.dj_hg);
                    image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ll_dengji.addView(image);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) image.getLayoutParams();
                    int left = CommentUtils.dip2px(getActivity(), 2);
                    int right = CommentUtils.dip2px(getActivity(), 2);
                    lp.leftMargin = left;
                    lp.rightMargin = right;
                    image.setLayoutParams(lp);
                }
            }

        } else {
            ll_dengji.setVisibility(View.GONE);
            view_xs.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(path)) {
            ImageLoader.getInstance().displayImage(path, iv_center_pic,
                    HMApplication.getInstance().getOptionsCircle());
        } else {
            iv_center_pic.setImageResource(R.mipmap.touxiang11);
        }


    }

    @Override
    public void onClick(View v) {
        Intent mIntent = new Intent();
        switch (v.getId()) {
            case R.id.tv_cen_band_number:   //学号
                mIntent.setClass(getActivity(), BindingNumActivity.class);
                mIntent.putExtra("number", SharedPreferencesUtils.getInstance().getString(Constant.STUDENTNUMBER));
                mIntent.putExtra("name", SharedPreferencesUtils.getInstance().getString(Constant.NAME));
                startActivity(mIntent);
                break;
            case R.id.iv_center_pic:  // 头像
                if (SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID) != 0) {
                    mIntent.setClass(getActivity(), MyCenterMessage.class);
                    startActivityForResult(mIntent, photoResultCode);
                } else {
                    mIntent.setClass(getActivity(), LoginActivity.class);
                    startActivity(mIntent);
                }
                break;
            case R.id.tv_cen_integral: //账户明细
                mIntent.setClass(getActivity(), AccountDetailsActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btn_cen_pay: ///充值
                mIntent.setClass(getActivity(), RechargeActivity.class);
                mIntent.putExtra("from", 2);
                startActivity(mIntent);
                break;
            case R.id.tv_cen_news://消息
                mIntent.setClass(getActivity(), MessageActivity.class);
                startActivity(mIntent);
                break;
            case R.id.tv_cen_video://我的视频
                mIntent.setClass(getActivity(), MyVideoActivity.class);
                startActivity(mIntent);
                break;
            case R.id.tv_cen_tercher: //我的老师
                mIntent.setClass(getActivity(), MyTeacherActivity.class);
                mIntent.putExtra("url", "http://10.58.174.236:8080/huameng.html");
                startActivity(mIntent);
                break;
            case R.id.tv_cen_course: //课程
                mIntent.setClass(getActivity(), MyTimetableActivity.class);
                startActivity(mIntent);
                break;
            case R.id.tv_cen_plan:  //计划
                mIntent.setClass(getActivity(), MyPlanActivity.class);
                startActivity(mIntent);
                break;
            case R.id.tv_cen_timer_shaft:   //时间轴
                mIntent.setClass(getActivity(), TimeLineActivity.class);
                startActivity(mIntent);

                break;
            case R.id.rl_cen_clear_memory:   //清除缓存
                File sdDir = Environment.getExternalStorageDirectory();
                File imageFile = new File(sdDir, Constant.CACHE_IMAGE_PATH);
                if (imageFile.exists()) {
                    imageFile.delete();
                }
                File jsonFile = new File(sdDir, Constant.CACHE_JSON_DATA_PATH);
                if (jsonFile.exists()) {
                    jsonFile.delete();
                }
                File errorFile = new File(sdDir, Constant.CRASH_ERROR_FILE_PATH);
                if (errorFile.exists()) {
                    errorFile.delete();
                }

                Toast.makeText(getActivity(), "清除成功", Toast.LENGTH_SHORT).show();

                break;
            case R.id.rl_cen_message_back:  //意见反馈
                mIntent.setClass(getActivity(), FeedBackActivity.class);
                mIntent.putExtra("email", SharedPreferencesUtils.getInstance().getString(Constant.EMAIL));
                startActivity(mIntent);
                break;

            case R.id.rl_cen_versions:   //版本跟新
                UmengUpdateAgent.setDownloadListener(null);
                UmengUpdateAgent.setUpdateOnlyWifi(false);
                UmengUpdateAgent.setUpdateAutoPopup(false);
                UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
                    @Override
                    public void onUpdateReturned(int updateStatus,
                                                 UpdateResponse updateInfo) {
                        switch (updateStatus) {
                            case 0: // has update
                                UmengUpdateAgent.showUpdateDialog(MyCenterFragment.this.getActivity(), updateInfo);
                                break;
                            case 1: // has no update
                                DialogUtils.dialogToast("当前版本已为最新", MyCenterFragment.this.getActivity());
                                break;
                            case 2: // none wifi
                                DialogUtils.dialogToast("没有wifi连接， 只在wifi下更新", MyCenterFragment.this.getActivity());
                                break;
                            case 3: // time out
                                DialogUtils.dialogToast("连接超时,请检查网络状态", MyCenterFragment.this.getActivity());
                                break;
                        }
                    }
                });
                UmengUpdateAgent.update(MyCenterFragment.this.getActivity());
                break;

            case R.id.rl_cen_about_us:  //关于我们
                mIntent.setClass(getActivity(), AboutUsActivity.class);
                startActivity(mIntent);
                break;
            case R.id.rl_cen_contact:             //联系我们
                mIntent.setClass(getActivity(), ContactUsActivity.class);
                startActivity(mIntent);
                break;

            case R.id.rl_help:     //帮助
                mIntent.setClass(getActivity(), HelpActivity.class);
                startActivity(mIntent);
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getMsgCount();
        userinfoRequest();
//        tv_cen_integral.setText("积分" + SharedPreferencesUtils.getInstance().getInt(Constant.SCORE));
    }

    private void getMsgCount() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", Integer.toString(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(getActivity());
        request.HttpVolleyRequestPost(Urls.NOT_READ_MSG, param, BaseData.class, null,
                getMsgCountSuccessListener(), null);

    }

    public Response.Listener<BaseData> getMsgCountSuccessListener() {
        return new Response.Listener<BaseData>() {
            String path = SharedPreferencesUtils.getInstance().getString(Constant.PHOTO);

            @Override
            public void onResponse(BaseData response) {
                if (response.data.newMessageNumber > 0) {
                    tv_count.setText(String.valueOf(response.data.newMessageNumber));
                    tv_count.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    /**
     * @Description 查询个人中心用户信息 请求后台数据
     */
    private void userinfoRequest() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", String.valueOf(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(getActivity());
        request.HttpVolleyRequestPost(Urls.USERINFO, param, BaseData.class, User.class,
                userinfoSuccessListener(), null);
    }

    private Response.Listener<BaseData> userinfoSuccessListener() {
        return new Response.Listener<BaseData>() {
            @Override
            public void onResponse(BaseData response) {
                tv_cen_integral.setText("积分" + response.data.user.score);

                SharedPreferencesUtils.getInstance().putInt(Constant.SCORE, response.data.user.score);// 积分
            }
        };
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case photoResultCode:
                String nickname = SharedPreferencesUtils.getInstance().getString(Constant.NICKNAME);
                tv_cen_name.setText(nickname);

                String path = SharedPreferencesUtils.getInstance().getString(Constant.PHOTO);
                if (!TextUtils.isEmpty(path))
                    ImageLoader.getInstance().displayImage(path, iv_center_pic,
                            HMApplication.getInstance().getOptionsCircle());
                break;
            default:
                break;
        }
    }
}
