package com.bm.hm.me;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.bean.MyPlan;
import com.bm.hm.util.ViewHolder;

import java.util.List;

/**
 * Created by chenb on 2015/5/6.
 */
public class MyPlanAdapter extends BaseExpandableListAdapter {

    private Context mcontext;
    private LayoutInflater mInflater;
    //   private List<PayBean> list = new ArrayList<PayBean>();
    private List<MyPlan> list;

    public MyPlanAdapter(Context mcontext, List<MyPlan> list) {
        super();
        this.mcontext = mcontext;
        mInflater = LayoutInflater.from(mcontext);
        this.list = list;
    }

    //得到子item需要关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    //得到子item的ID
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //获取当前父item下的子item的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).learnPlanContentList.size();
    }

    //设置子item的组件
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mcontext, R.layout.item_plan_childs, null);
        }
        TextView tv_plan_time = ViewHolder.get(convertView, R.id.tv_plan_time);
        TextView tv_plan_content = ViewHolder.get(convertView, R.id.tv_plan_content);
        tv_plan_content.setText(list.get(groupPosition).learnPlanContentList.get(childPosition).content);
        if (list.get(groupPosition).learnPlanContentList.get(childPosition).dateStr.length() > 11) {
            String yuan = list.get(groupPosition).learnPlanContentList.get(childPosition).dateStr;
            String[] ss = yuan.split("-");
            tv_plan_time.setText(ss[0] + "-" + "\n " + ss[1]);

        } else {
            tv_plan_time.setText(list.get(groupPosition).learnPlanContentList.get(childPosition).dateStr);
        }


        return convertView;
    }


    //获取当前父item的数据
    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    //设置父item组件
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_plan_groups, null);

        }
        TextView tv_plan_name = ViewHolder.get(convertView, R.id.tv_plan_name);
        ImageView iv_open = ViewHolder.get(convertView, R.id.iv_open);
        View view_white = ViewHolder.get(convertView, R.id.view_white);
        tv_plan_name.setText(list.get(groupPosition).name);

        //父item展开和关闭
        if (isExpanded) {
            iv_open.setBackgroundResource(R.mipmap.plan_close);
            view_white.setVisibility(View.GONE);
        } else {
            iv_open.setBackgroundResource(R.mipmap.plan_open);
            view_white.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}
