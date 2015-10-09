package com.bm.hm.pay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.Response;
import com.bm.hm.R;
import com.bm.hm.base.BaseActivity;
import com.bm.hm.bean.Course;
import com.bm.hm.constant.Constant;
import com.bm.hm.constant.Urls;
import com.bm.hm.http.BaseData;
import com.bm.hm.http.HttpVolleyRequest;
import com.bm.hm.util.DialogUtils;
import com.bm.hm.util.SharedPreferencesUtils;
import com.bm.hm.util.SignUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

/**
 * Created by liuz on 2015/4/28.
 */
public class RechargeActivity extends BaseActivity {

    private EditText et_pay_integral;
    private Button btn_pay_sure;
    private ImageView iv_alipay,iv_card;

    private int payType =1;
    private int from ;

    //alipay

    //商户PID
    public static final String PARTNER = "2088911054289210";
    //商户收款账号
    public static final String SELLER = "huamengedu@foxmail.com";
    //商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOmOW5z5jUUBXh+MVzlO5LcyiQz/Lmmo0XA/9QILLmAm5stwKBPeRpyovc+e+KtGoH4BmnR2gTfj9rc1ItQtChAF+bgolJPl+Tne+CeXyhOdHeF8NOvaJmGTBfUzSKOJoD2sF8EVJRcmnUc22BERRrhHkv6VLAuNIeHR2nEeIvoDAgMBAAECgYB47KgqYlrue4LMOtF+boalA5lKDWVud2XZBIRKhpeAWSPt+SgHAXL18bg1I04FABBgrVvXKPRGh9VZjj12Ncua+s4Qn0QmGKFYZozEtIrHkdgciiqh3/lGXW6Xe3ucpHxJU/AS3w7/FOedvYj0D+jM38T8iWMFX0R5Da4ByPMMgQJBAP8X76lKlrfcOQ43NNMJR857onMYrCPz9I9o8Wy6YwWd4o18lMt+kqRsRyHd4r7MK/Pxkw+Qc9l6clKEbcdT8PMCQQDqYtQiyaG0aNznh365aPovoNcOIErbn8X1XLLSkscb25+2ZBOy/N7z6XVgCjGDuQK9qXmoUNndaA8u43YdKZaxAkBH/zh8PnoVgIl18qztF0R2Cb0K4R7MsvEGfOcO+fgywKINrujMGnhHAex9qYNyuGt7WWZZUTrxWbbkhU36oT1zAkEAnXnJDZZrDstloClo+ymM7nMiClun7+dpGXEutEvpes4UgHa8xYUgRsCUG/K9UcL8FHHJS00HE7rWeKpasT1AAQJBANyAzFYZJhNLnOJa1BRMrmNx9lIWSw7UJUmbp+4L9pmX6KRjhXzpeExVEUvwyrfVdWVmazYuHiBVatLxpAe4s/c=";
    //支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDpjluc+Y1FAV4fjFc5TuS3MokM/y5pqNFwP/UCCy5gJubLcCgT3kacqL3PnvirRqB+AZp0doE34/a3NSLULQoQBfm4KJST5fk53vgnl8oTnR3hfDTr2iZhkwX1M0ijiaA9rBfBFSUXJp1HNtgREUa4R5L+lSwLjSHh0dpxHiL6AwIDAQAB";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    private String orderId;
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_recharge);

        initView();
    }

    private void initView() {
        from = getIntent().getIntExtra("from",0);
        course = (Course) getIntent().getSerializableExtra("course");

        initTitleBarWithBack("充值");
        //充值
        et_pay_integral = (EditText) findViewById(R.id.et_pay_integral);

        //下一步
        btn_pay_sure = (Button) findViewById(R.id.btn_pay_sure);
        btn_pay_sure.setOnClickListener(this);

        iv_alipay = (ImageView) findViewById(R.id.iv_alipay);
        iv_alipay.setOnClickListener(this);

        iv_card = (ImageView) findViewById(R.id.iv_card);
        iv_card.setOnClickListener(this);
    }

    private Boolean canPay() {
        if (TextUtils.isEmpty(et_pay_integral.getText().toString())) {
            DialogUtils.dialogToast("充值积分不能为空", RechargeActivity.this);
            return false;
        }
        if ((Integer.parseInt(et_pay_integral.getText().toString())) == 0) {
            DialogUtils.dialogToast("充值积分必须大于0", RechargeActivity.this);
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_pay_sure:
                if (payType == 2) {
                    Intent intent = new Intent(getApplicationContext(), RechargeCardActivity.class);
                    intent.putExtra("course",course);
                    intent.putExtra("from", from);
                    startActivity(intent);
                } else {
                    //支付宝
                    if(canPay()){
                        createOrder();
                    }
                }
                break;
            case R.id.iv_alipay:
                payType = 1;

                iv_alipay.setImageResource(R.mipmap.select);
                iv_card.setImageResource(R.mipmap.unselect);
                break;
            case R.id.iv_card:
                payType = 2;

                iv_alipay.setImageResource(R.mipmap.unselect);
                iv_card.setImageResource(R.mipmap.select);
                break;
            default:
                break;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Intent intent = new Intent(getApplicationContext(), RechargeResultActivity.class);
                        intent.putExtra("result", "success");
                        startActivity(intent);
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(RechargeActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(RechargeActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(RechargeActivity.this, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        };
    };

    /**
     * call alipay sdk pay. 调用SDK支付
     *
     */
    public void pay(String price) {
        // 订单
        String orderInfo = getOrderInfo("华盟积分充值", "华盟积分充值", "0.01");

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(RechargeActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     *
     */
    public void check(View v) {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(RechargeActivity.this);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

    }

    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * create the order info. 创建订单信息
     *
     */
    public String getOrderInfo(String subject, String body, String price) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + orderId + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + Urls.ALIPAY_CALLBACK
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    public String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }


    private void createOrder() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", String.valueOf(SharedPreferencesUtils.getInstance().getInt(Constant.USER_ID)));
        HttpVolleyRequest<BaseData> request = new HttpVolleyRequest<BaseData>(this);
        request.HttpVolleyRequestPost(Urls.ALIPAY_CREATEORDER, param, BaseData.class, null,
                createOrderSuccessListener(), null);

    }

    public Response.Listener<BaseData> createOrderSuccessListener() {
        return new Response.Listener<BaseData>() {

            @Override
            public void onResponse(BaseData response) {
                orderId = response.data.orderId;

                pay(et_pay_integral.getText().toString());
            }
        };
    }
}
