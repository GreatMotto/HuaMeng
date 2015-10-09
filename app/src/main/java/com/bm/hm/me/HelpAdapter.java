package com.bm.hm.me;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.bean.HelpBean;
import com.bm.hm.util.ViewHolder;

import java.util.List;

/**
 * Created by chenb on 2015/5/7.
 */
public class HelpAdapter extends BaseAdapter {

    private Context context;
    private boolean kaiguan=true;//判断打开还是关闭
    private List<HelpBean> list;

    public HelpAdapter(Context context, List<HelpBean>list){
        super();
        this.context=context;
        this.list=list;

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup viewGroup) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_help, viewGroup,
                    false);
        }

         final  ImageView iv_help= ViewHolder.get(convertView,R.id.iv_help);
         final   TextView tv_answer=ViewHolder.get(convertView,R.id.tv_answer);
          TextView tv_help=ViewHolder.get(convertView,R.id.tv_help);
          ImageView  iv_A=ViewHolder.get(convertView,R.id.iv_A);


        tv_answer.setText("     "+list.get(position).content);
        tv_help.setText(list.get(position).title);

        iv_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.iv_help:
                             if(kaiguan) {
                                 iv_help.setImageResource(R.mipmap.help_close);
                                 tv_answer.setMaxLines(tv_answer.getText().toString().length());
                             }
                              else {
                                 iv_help.setImageResource(R.mipmap.help_open);
                                 tv_answer.setMaxLines(3);
                             }
                        kaiguan=!kaiguan;

                        break;
                    default:
                        break;
                }

            }
        });
        return convertView;
    }
}
