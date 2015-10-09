package com.bm.hm.me;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.bean.AccountDetails;
import com.bm.hm.util.ViewHolder;

import java.util.List;

/**
 * Created by chenb on 2015/4/22.
 */
public class AccountDetailsAdapter extends BaseAdapter {

    private Context context;
    private List<AccountDetails> list;
    private String type;
    private String score;

    public AccountDetailsAdapter(Context context, List<AccountDetails> list) {
        super();
        this.context = context;
        this.list = list;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_account_details, null);
        }

        ImageView iv_details_image = ViewHolder.get(convertView, R.id.iv_details_image);
        TextView tv_integral_type = ViewHolder.get(convertView, R.id.tv_integral_type);
        TextView tv_integral_time = ViewHolder.get(convertView, R.id.tv_integral_time);
        TextView tv_integral = ViewHolder.get(convertView, R.id.tv_integral);

        iv_details_image.setImageBitmap(null);
        switch (list.get(position).type) {
            case "CARD":   //充值卡充值
                iv_details_image.setImageResource(R.mipmap.details_image_1);
                break;
            case "ALIPAY": //支付宝充值
                iv_details_image.setImageResource(R.mipmap.details_image_1);
                break;
            case "RECOMMEND":  //推荐人加积分
                iv_details_image.setImageResource(R.mipmap.details_image_2);
                break;
            case "PAY": //支付
                iv_details_image.setImageResource(R.mipmap.details_image_3);
                break;
            default:
                break;
        }

        tv_integral_type.setText(list.get(position).content);
        tv_integral_time.setText(list.get(position).createDate);

        score=list.get(position).changeScore;
        if(score.equals("-0")){
            tv_integral.setText("0");
        }else {
            tv_integral.setText(score);
        }

        return convertView;
    }


}
