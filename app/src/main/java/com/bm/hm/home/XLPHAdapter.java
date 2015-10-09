package com.bm.hm.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.bean.Course;
import com.bm.hm.util.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class XLPHAdapter extends BaseAdapter {
    private Context mContext = null;
    private List<Course> list;

    public XLPHAdapter(Context context,List<Course> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_xlph, parent,
                    false);
        }

        ImageView iv_course_img = ViewHolder.get(convertView, R.id.iv_course_img);
        ImageLoader.getInstance().displayImage(list.get(position).image.path,iv_course_img);

        TextView tv_course_name = ViewHolder.get(convertView, R.id.tv_course_name);
        tv_course_name.setText(list.get(position).name);

        TextView tv_order = ViewHolder.get(convertView,R.id.tv_order);

        if(position == 0){
            tv_order.setBackgroundResource(R.mipmap.top1);
            tv_order.setText("NO.1");
        }else if(position == 1){
            tv_order.setBackgroundResource(R.mipmap.top2);
            tv_order.setText("NO.2");
        }else if(position == 2){
            tv_order.setBackgroundResource(R.mipmap.top3);
            tv_order.setText("NO.3");
        }else if(position == 3){
            tv_order.setBackgroundResource(R.mipmap.top4);
            tv_order.setText("NO.4");
        }

        return convertView;
    }
}
