package com.bm.hm.event;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.bean.Event;
import com.bm.hm.util.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by liuz on 2015/4/30.
 */
public class EventAdapter extends BaseAdapter {
    private Context mContext = null;
    private List<Event> list;

    public EventAdapter(Context context,List<Event> list) {
        this.mContext = context;
        this.list = list;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return null;
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link android.view.LayoutInflater#inflate(int, android.view.ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.ac_event_item, parent,
                    false);
        }

        ImageView iv_event_pic = ViewHolder.get(convertView,R.id.iv_event_pic);
        ImageLoader.getInstance().displayImage(list.get(position).image.path,iv_event_pic);

        TextView tv_event_status = ViewHolder.get(convertView,R.id.tv_event_status);
        tv_event_status.setText(list.get(position).statusStr);
        setBackground(tv_event_status,list.get(position).statusStr);

        TextView tv_date = ViewHolder.get(convertView,R.id.tv_date);
        String date = list.get(position).startDate.substring(0,10).replace("-","/")+"-"+
                list.get(position).endDate.substring(0,10).replace("-","/");
        tv_date.setText(date);

        TextView tv_event_title = ViewHolder.get(convertView,R.id.tv_event_title);
        tv_event_title.setText(list.get(position).name);



        convertView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,EventInfoActivity.class);
                intent.putExtra("link", list.get(position).link);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    private void setBackground(TextView tv,String status){
        switch (status){
            case "已结束":
                tv.setBackgroundResource(R.mipmap.top3);
                break;
            case "进行中":
                tv.setBackgroundResource(R.mipmap.top2);
                break;
            case "活动预告":
                tv.setBackgroundResource(R.mipmap.top1);
                break;
        }
    }
}
