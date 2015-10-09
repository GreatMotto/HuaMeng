package com.bm.hm.me;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.base.HMApplication;
import com.bm.hm.bean.Image;
import com.bm.hm.bean.User;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.home.MainActivity;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpPostUtil;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.util.SharedPreferencesUtils;
import com.bm.hm.util.ToastUtil;
import com.bm.hm.util.TwoBtnDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by liuz on 2015/4/23.
 */
public class MyCenterMessage extends BaseActivity {

    private Button btn_my_log_exit;
    private ImageView iv_my_center_pic;
    private RelativeLayout rl_message_username, rl_message_number, rl_message_sex,
            rl_message_data, rl_message_password, rl_my_center_email;
    private TextView tv_my_center_username, tv_my_center_number, tv_my_center_sex, tv_my_center_data,
            tv_my_center_phone;
    private LinearLayout ll_gerenxx;
    private Button btn_dialog_photo, btn_dialog_gallery, btn_my_log_cancel;

    private Dialog alertDialog;
    private Dialog alertDialog2;
    private Dialog dlg;
    private String picUrl;
    private String number;
    private String name;
    private String email;
    private String picurl;
    File file;
    private Calendar calendar;// 用来装日期的
    private DatePickerDialog dialog;
    private String path;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ImageLoader.getInstance().displayImage(path, iv_my_center_pic, HMApplication.getInstance().getOptionsCircle());
            ToastUtil.showToast(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_my_message);

        initView();
        alertDialog = new Dialog(MyCenterMessage.this, R.style.MyDialog);
        userinfoRequest();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (HMApplication.getInstance().isOK) {
            userinfoRequest();
        }
    }

    private void initView() {
        initTitleBarWithBack("个人信息");
        //退出登录
        btn_my_log_exit = (Button) findViewById(R.id.btn_my_log_exit);
        btn_my_log_exit.setOnClickListener(this);
        ll_gerenxx = (LinearLayout) findViewById(R.id.ll_gerenxx);
        //头像
        iv_my_center_pic = (ImageView) findViewById(R.id.iv_my_center_pic);
        iv_my_center_pic.setOnClickListener(this);
        //昵称
        rl_message_username = (RelativeLayout) findViewById(R.id.rl_message_username);
        rl_message_username.setOnClickListener(this);
        tv_my_center_username = (TextView) findViewById(R.id.tv_my_center_username);
        //学号
        rl_message_number = (RelativeLayout) findViewById(R.id.rl_message_number);
        rl_message_number.setOnClickListener(this);
        tv_my_center_number = (TextView) findViewById(R.id.tv_my_center_number);
        //性别
        rl_message_sex = (RelativeLayout) findViewById(R.id.rl_message_sex);
        rl_message_sex.setOnClickListener(this);
        tv_my_center_sex = (TextView) findViewById(R.id.tv_my_center_sex);
        //生日
        rl_message_data = (RelativeLayout) findViewById(R.id.rl_message_data);
        rl_message_data.setOnClickListener(this);
        tv_my_center_data = (TextView) findViewById(R.id.tv_my_center_data);
        //手机
        tv_my_center_phone = (TextView) findViewById(R.id.tv_my_center_phone);
        //修改密码
        rl_message_password = (RelativeLayout) findViewById(R.id.rl_message_password);
        rl_message_password.setOnClickListener(this);
        //邮箱
        rl_my_center_email = (RelativeLayout) findViewById(R.id.rl_my_center_email);
        rl_my_center_email.setOnClickListener(this);
    }

    private void init() {
        if (!TextUtils.isEmpty(picurl)) {
            ImageLoader.getInstance().displayImage(picurl, iv_my_center_pic,
                    HMApplication.getInstance().getOptionsCircle());
        } else {
            iv_my_center_pic.setImageResource(R.mipmap.touxiang11);
        }
        iv_my_center_pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1001:   //拍照
                if (resultCode == -1) {
                    startPhotoZoom(Uri.fromFile(file));
                }
                break;
            case 2:     //相册
                if (data != null) {
                    if (data.getData() != null)
                        picUrl = doPhoto(data.getData());
                    startPhotoZoom(data.getData());
                }
                break;
            case 3:     //裁剪图片
                if (data != null) {
//                    setPicToView(data);
                    initData_updateHead();
                }
                break;
            case 4:    //性别
                if (data != null) {
                    String type = data.getStringExtra("sex");
                    if (type.equals("0")) {
                        tv_my_center_sex.setText("男");
                    } else {
                        tv_my_center_sex.setText("女");
                    }
                }
                break;
            case 5:   //昵称
                if (data != null) {
                    String nickname = data.getStringExtra("nickname");
                    tv_my_center_username.setText(nickname);
                }
                break;
            case 6:   //邮箱
                if (data != null) {
                    email = data.getStringExtra("email");
                }
                break;
            case 7:     //学号
                if (data != null) {
                    String number = data.getStringExtra("number");
                    tv_my_center_number.setText(number);
                }
                break;
        }
        return;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent mintent = null;
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.btn_my_log_exit:  //退出
                dlg = new TwoBtnDialog(MyCenterMessage.this, "是否退出?", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.btn_left:
                                dlg.dismiss();
                                break;
                            case R.id.btn_right:
                                dlg.dismiss();
                                clearData();
                                ToastUtil.showToast(getApplicationContext(), "退出登录成功!", Toast.LENGTH_SHORT);
                                gotoOtherActivity(MainActivity.class);
                                finish();
                                break;
                            default:
                                break;
                        }
                    }
                });
                dlg.show();
                break;
            case R.id.iv_my_center_pic:  //头像
                HMApplication.getInstance().isOK = false;
                showDialog();
                break;
            case R.id.rl_message_username:  // 昵称'
                mintent = new Intent(this, ModifyUsername.class);
                mintent.putExtra("nickname", tv_my_center_username.getText().toString().trim());
                startActivityForResult(mintent, 5);
                break;
            case R.id.rl_message_number:  //  学号
                mintent = new Intent(this, BindingNumActivity.class);
                mintent.putExtra("number", tv_my_center_number.getText().toString().trim());
                mintent.putExtra("name", name);
                startActivityForResult(mintent, 7);
                break;
            case R.id.rl_message_sex:     //性别
                mintent = new Intent(this, ModifySex.class);
                mintent.putExtra("sex", tv_my_center_sex.getText().toString());
                startActivityForResult(mintent, 4);
                break;
            case R.id.rl_message_data:  //生日
                ll_gerenxx.setEnabled(false);
                showbritherDialog();
                break;
            case R.id.rl_message_password:  //登陆密码
                gotoOtherActivity(ModifyPasswordActivity.class);
                break;
            case R.id.rl_my_center_email:  //邮箱
                mintent = new Intent(this, ModifyEmailActivity.class);
                mintent.putExtra("email", email);
                startActivityForResult(mintent, 6);
                break;
            case R.id.btn_my_log_cancel:   //取消
                alertDialog.dismiss();
                break;
            case R.id.btn_dialog_photo:  //拍照
                alertDialog.dismiss();
                getImageFromCamera();
                break;
            case R.id.btn_dialog_gallery:  //相册中选择
                alertDialog.dismiss();
                doPickPhotoFromGallery();
                break;

            default:
                break;
        }
    }

    /**
     * @Description 查询个人中心用户信息 请求后台数据
     */
    private void userinfoRequest() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", String.valueOf(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.USERINFO, param, BaseData.class, User.class,
                userinfoSuccessListener(), null);
    }

    //回调成功
    private Response.Listener<BaseData> userinfoSuccessListener() {
        return new Response.Listener<BaseData>() {
            @Override
            public void onResponse(BaseData result) {

                User user = result.data.user;
                name = user.studentNumber.name;
                email = user.email;
                number = user.studentNumber.number;
                picurl = user.head.path;
                SharedPreferencesUtils.getInstance().putString(Constant.PHOTO, picurl);
                init();
//                ImageLoader.getInstance().displayImage(picUrl,iv_my_center_pic,ImageUtils.getSpecialOptions());
                tv_my_center_username.setText(user.nickname);

                tv_my_center_number.setText(user.studentNumber.number);
                if (user.sex == 0) {
                    tv_my_center_sex.setText("男");
                } else {
                    tv_my_center_sex.setText("女");
                }
                tv_my_center_data.setText(user.birthday);
                tv_my_center_phone.setText(user.mobile);

            }
        };
    }

    /**
     * @Description 上传头像
     */
    private void initData_updateHead() {
        final String id = String.valueOf(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID));
        if (TextUtils.isEmpty(id)) {
            return;
        }
        new Thread() {
            public void run() {
                try {
                    HttpPostUtil u = new HttpPostUtil(Urls.UPLOADHEAD);
                    u.addTextParameter("userId", id);
                    u.addFileParameter("file", new File(picUrl));
                    picurl = picUrl;
                    byte[] b = u.send();
                    String result = new String(b);

                    JSONObject jsonR = new JSONObject(result);


                    if (jsonR.getString("status").equals("0")) {

//                        path=jsonR.getString("data");
                        String data = jsonR.getString("data");
                        JSONObject json = new JSONObject(data);
                        Gson gson = new Gson();
                        Image image = gson.fromJson(json.getString("head"), new TypeToken<Image>() {
                        }.getType());
                        path = image.path;
                        SharedPreferencesUtils.getInstance().putString(Constant.PHOTO, path);
                        mHandler.sendEmptyMessage(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ;
        }.start();
    }

    /**
     * 显示弹出框
     */
    private void showDialog() {
        alertDialog.show();
        WindowManager manager = getWindowManager();

        Display display = manager.getDefaultDisplay();

        int width = display.getWidth();

        int height = display.getHeight();

        alertDialog.getWindow().setLayout(android.view.WindowManager.LayoutParams.FILL_PARENT,
                android.view.WindowManager.LayoutParams.FILL_PARENT);
        alertDialog.getWindow().setContentView(R.layout.dialog_view);
        //拍照
        btn_dialog_photo = (Button) alertDialog.getWindow().findViewById(R.id.btn_dialog_photo);
        btn_dialog_photo.setOnClickListener(this);
        //从相册中选择
        btn_dialog_gallery = (Button) alertDialog.getWindow().findViewById(R.id.btn_dialog_gallery);
        btn_dialog_gallery.setOnClickListener(this);
        //取消
        btn_my_log_cancel = (Button) alertDialog.getWindow().findViewById(R.id.btn_my_log_cancel);
        btn_my_log_cancel.setOnClickListener(this);

    }

    /**
     * @return
     * @params 调用相册
     */
    public void doPickPhotoFromGallery() {
//        Intent intent =new  Intent();
        sdScan();
        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        this.startActivityForResult(intent, 2);
    }

    public void sdScan() {
        try {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
                    + Environment.getExternalStorageDirectory())));
        } catch (Exception e) {
            MediaScannerConnection.scanFile(this, new String[]{"file://"
                    + Environment.getExternalStorageDirectory()}, null, null);
        }
    }

    /**
     * @return
     * @params 调用相机拍照
     */
    public void getImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File dir = Environment.getExternalStorageDirectory();
            file = new File(dir, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
                    + ".jpg");
            picUrl = file.getAbsolutePath();
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra("output", Uri.fromFile(file));
//            startActivityForResult(intent, 1001);
            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                    MediaStore.EXTRA_OUTPUT, Uri.fromFile(file)), 1001);
        } else {
            Toast.makeText(this, "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        /*
         * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
         * yourself_sdk_path/docs/reference/android/content/Intent.html
         * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的，小马不懂C C++
         * 这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么 制做的了...吼吼
         */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        if (file != null) {
            intent.putExtra("output", Uri.fromFile(file));
        }
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);

        intent.putExtra("scale", true);  //保留比例
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        intent.putExtra("noFaceDetection", true); // no face detection

        startActivityForResult(intent, 3);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
//        initData_updateHead();
//        if (extras != null) {
//            Bitmap bitmap = (Bitmap) extras.get("data");
//            HMApplication.getInstance().currBitmap=bitmap;
//            // Drawable drawable = new BitmapDrawable(photo);
////            upImage(bitmap);
//            Bitmap roundBitmap = ImageUtils.toRoundBitmap(bitmap);
//            iv_my_center_pic.setImageBitmap(roundBitmap);
//     Bitmap bitmap = ImageUtils.getimage(picUrl);
//     bitmap = ImageUtils.compressImage(bitmap);
//     Bitmap roundBitmap = ImageUtils.toRoundBitmap(bitmap);
//     iv_my_center_pic.setImageBitmap(roundBitmap);

    }

    // 转换成UrL
    public String doPhoto(Uri photoUri) {
        String[] pojo = {MediaStore.MediaColumns.DATA};
        String picPath = null;
        if (photoUri != null) {
            @SuppressWarnings("deprecation")
            Cursor cursor = getContentResolver().query(photoUri, pojo, null, null, null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                picPath = cursor.getString(columnIndex);
                cursor.close();
            }
        }
        return picPath;
    }


    /**
     * 生日提示框
     */
    private void showbritherDialog() {
        briShowDialog();
        ll_gerenxx.setEnabled(true);
        alertDialog2.setCanceledOnTouchOutside(true);
        alertDialog2.getWindow().setContentView(R.layout.userinfo_data_pivk);
        final DatePicker dp_user_bri = (DatePicker) alertDialog2.getWindow().findViewById(
                R.id.dp_user_bri);
        dp_user_bri.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        TextView tv_quxiao = (TextView) alertDialog2.getWindow().findViewById(R.id.tv_quxiao);
        TextView tv_queding = (TextView) alertDialog2.getWindow().findViewById(R.id.tv_queding);


        tv_queding.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int month2 = dp_user_bri.getMonth();
                int month3 = month2 + 1;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                tv_my_center_data.setText(dp_user_bri.getYear() + "-" + month3 + "-"
                        + dp_user_bri.getDayOfMonth());
                String strDate = tv_my_center_data.getText().toString().trim();
                try {
                    Date date = sdf.parse(strDate);
                    Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                    int compareTo = date.compareTo(curDate);
                    if (compareTo > 0) {
                        ToastUtil.showToast(MyCenterMessage.this, "不能大于当前时间", Toast.LENGTH_SHORT);
                        return;
                    } else {

                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                brithdayRequest();
                alertDialog2.dismiss();
            }
        });
        tv_quxiao.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog2.dismiss();

            }
        });
    }

    /**
     * 显示生日的对话框
     */
    private void briShowDialog() {
        alertDialog2 = new Dialog(this, R.style.MyDialogStyle);
        alertDialog2.show();
        WindowManager manager = getWindowManager();
        Display display = manager.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        alertDialog2.getWindow().setLayout((width * 3) / 4, android.view.WindowManager.LayoutParams.WRAP_CONTENT);

    }


    /**
     * 修改生日接口
     */
    private void brithdayRequest() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", String.valueOf(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        param.put("birthday", tv_my_center_data.getText().toString());
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.UPDATE_USERINFO, param, BaseData.class, User.class,
                modifyBirthdatSuccessListener(), null);
    }

    private Response.Listener<BaseData> modifyBirthdatSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData baseData) {
                ToastUtil.showToast(getApplicationContext(), "生日修改成功", Toast.LENGTH_SHORT);
                getIntent().putExtra("birthday", tv_my_center_data.getText().toString().trim());
            }
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (path != null) {
            getIntent().putExtra("picurl", path);
            setResult(1001, getIntent());
        }

    }

    private void clearData() {
        SharedPreferencesUtils.getInstance().putInt(Constant.USER_ID, 0);
        SharedPreferencesUtils.getInstance().putString(Constant.USER_PASSWORD, "");
        SharedPreferencesUtils.getInstance().putString(Constant.PHOTO, "");  //图片路径
        SharedPreferencesUtils.getInstance().putString(Constant.NICKNAME, ""); //昵称
        SharedPreferencesUtils.getInstance().putInt(Constant.SCORE, 0);// 积分
        SharedPreferencesUtils.getInstance().putInt(Constant.SCALE, 0);  //等级
        SharedPreferencesUtils.getInstance().putString(Constant.ROLE, "");   //身份
        SharedPreferencesUtils.getInstance().putString(Constant.STUDENTNUMBER, "");//学号

        SharedPreferencesUtils.getInstance().putString(Constant.NAME, "");   //姓名
        SharedPreferencesUtils.getInstance().putString(Constant.EMAIL, "");   //邮箱
        SharedPreferencesUtils.getInstance().putString(Constant.MOBILE, "");   //手机

        SharedPreferencesUtils.getInstance().putBoolean(Constant.IS_REMEMBER_PASS, false);
    }
}
