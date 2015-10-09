package com.bm.hm.me;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.bean.timelineContent;
import com.bm.hm.util.ViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by liuz on 2015/5/4.
 */
public class TimeLineAdapter extends BaseAdapter {
    private Context context;
    private List<timelineContent> list;

    public TimeLineAdapter(Context context, List<timelineContent> list) {
        super();
        this.context = context;
        this.list = list;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.time_line_list, parent,
                    false);

        }
        TextView tv_Linetime = ViewHolder.get(convertView, R.id.tv_Linetime);
        TextView tv_time_title = ViewHolder.get(convertView, R.id.tv_time_title);
        TextView tv_lj_time = ViewHolder.get(convertView, R.id.tv_lj_time);
        TextView tv_time_detail = ViewHolder.get(convertView, R.id.tv_time_detail);
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime = null;
        try {
            currentTime = formatter1.parse(list.get(position).date);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            String dateString = formatter.format(currentTime);
            tv_Linetime.setText(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tv_time_title.setText("第" + list.get(position).times + "次课");
        tv_lj_time.setText("累计" + list.get(position).hours + "课时");
        tv_time_detail.setText(list.get(position).guide + "\n" + list.get(position).tutor + "\n" + list.get(position).homework);


        return convertView;
    }
}
