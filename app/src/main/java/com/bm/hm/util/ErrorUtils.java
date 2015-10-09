package com.bm.hm.util;

import android.content.Context;
import android.widget.Toast;

public class ErrorUtils {

    public static void showErrorMsg(Context context, String error) {
        String errorMsg = "";
        String[] errorMsgSplits = error.split("_");
        String errorMsgSplit = errorMsgSplits[1];
        int errorCodeInt = Integer.valueOf(errorMsgSplit);
        switch (errorCodeInt) {
            case 1:
                errorMsg = "参数不正确";
                break;
            case 2:
                errorMsg = "服务器异常";
                break;
            case 3:
                errorMsg = "类型不存在";
                break;
            case 4:
                errorMsg = "用户不存在";
                break;
            case 5:
                errorMsg = "用户名或密码错误";
                break;
            case 6:
                errorMsg = "用户被禁用";
                break;
            case 7:
                errorMsg = "手机号被占用";
                break;
            case 8:
                errorMsg = "昵称被占用";
                break;
            case 9:
                errorMsg = "验证码错误";
                break;
            case 10:
                errorMsg = "该用户已经绑定过业主信息";
                break;
            case 11:
                errorMsg = "绑定时输入的业主姓名错误";
                break;
            case 12:
                errorMsg = "原密码错误";
                break;
            case 13:
                errorMsg = "手机号未注册用户";
                break;
            case 14:
                errorMsg = "绑定学号或姓名错误";
                break;
            case 15:
                errorMsg = "充值卡卡号或密码不正确";
                break;
            case 16:
                errorMsg = "充值卡已经被使用过，已经失效";
                break;
            case 17:
                errorMsg = "注册时，输入的推荐人不存在";
                break;
            case 18:
                errorMsg = "购买课程，积分不足";
                break;
            case 19:
                errorMsg = "课程已经购买，不需要重复购买";
                break;
        }
        if (DialogUtils.mProgressDialog != null) {
            DialogUtils.mProgressDialog.cancel();
        }
        if(errorCodeInt!=2 && errorCodeInt!=14 && errorCodeInt!=18 && errorCodeInt!=19) {
            ToastUtil.showToast(context, errorMsg, Toast.LENGTH_SHORT);
        }

    }
}
