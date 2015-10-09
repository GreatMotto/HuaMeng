package com.bm.hm.me;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.hm.R;
import com.bm.hm.base.HMApplication;
import com.bm.hm.bean.Message;
import com.bm.hm.bean.MyComment;
import com.bm.hm.course.CourseInfoActivity;
import com.bm.hm.util.TwoBtnDialog;
import com.bm.hm.util.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenb on 2015/5/22.
 */
public class MessageAdapter extends BaseAdapter {
    private Context mcontext;
    private LayoutInflater mInflater;
    private int flag;
    private Dialog dlg;
    private List<Message> list;
    private List<MyComment> plist;


    public MessageAdapter(Context mcontext, int flag, List<Message> list) {
        super();
        this.mcontext = mcontext;
        this.flag = 0;
        this.list = list;
        mInflater = LayoutInflater.from(mcontext);
    }

    public MessageAdapter(Context mcontext, List<MyComment> plist) {
        super();
        this.mcontext = mcontext;
        this.plist = plist;
        this.flag = 1;
        mInflater = LayoutInflater.from(mcontext);
    }

    @Override
    public int getCount() {
        if (flag == 0) {
            return list.size();
        } else {
            return plist.size();
        }

    }

    @Override
    public Object getItem(int position) {
        if (flag == 0) {
            return list.get(position);
        } else {
            return plist.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (flag == 0) {
            convertView = mInflater.inflate(R.layout.item_tongzhi, null);
            ImageView img_hm = ViewHolder.get(convertView, R.id.img_hm);
            ImageView iv_red_dot = ViewHolder.get(convertView, R.id.iv_red_dot);
            TextView tv_inform_content = ViewHolder.get(convertView, R.id.tv_inform_content);
            TextView tv_inform_time = ViewHolder.get(convertView, R.id.tv_inform_time);
            ImageView iv_detail = ViewHolder.get(convertView, R.id.iv_detail);
            final Message lists = list.get(position);

            tv_inform_time.setText(lists.createDate);
            tv_inform_content.setText(lists.content);


            //是否已读??
            if (lists.isRead.equals("no")) {
                iv_red_dot.setVisibility(View.VISIBLE);
            } else {
                iv_red_dot.setVisibility(View.GONE);
            }


            //item点击跳转
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    MessageActivity ac = (MessageActivity) mcontext;
                    ac.markReadRequest(position);
                    switch (lists.type) {
                       case "timetable":
                           intent=new Intent(mcontext,MyTimetableActivity.class);
                           intent.putExtra("userid",lists.id);
                           mcontext.startActivity(intent);
                           break;
                        case "timeline":
                            intent = new Intent(mcontext, TimeLineActivity.class);
                            intent.putExtra("userid", lists.id);
                            mcontext.startActivity(intent);
//                           ((MessageActivity) mcontext).deleteMessageRequest(index);
//                           ((MessageActivity)mcontext).markReadRequest();
                            break;
                        case "learnPlan":
                            intent = new Intent(mcontext, MyPlanActivity.class);
                            intent.putExtra("userid", lists.id);
                            mcontext.startActivity(intent);
                            break;
                       case "all":
                             intent=new Intent(mcontext,MessageChildActivity.class);
                             intent.putExtra("content",lists.content);
                             mcontext.startActivity(intent);
                           break;
                        case "recommend_register":
                            intent = new Intent(mcontext, AccountDetailsActivity.class);
                            intent.putExtra("userid", lists.id);
                            mcontext.startActivity(intent);
                            break;
                       case "class_remind":
                           intent =new Intent(mcontext,MyTimetableActivity.class);
                           intent.putExtra("userid",lists.id);
                           mcontext.startActivity(intent);
                           break;
                        default:
                            break;

                    }
                }
            });
            //删除通知消息
            iv_detail.setTag(position);
            iv_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int index = (Integer) view.getTag();
                    dlg = new TwoBtnDialog(mcontext, "是否删除?", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()) {
                                case R.id.btn_left:
                                    dlg.dismiss();
                                    break;
                                case R.id.btn_right:
                                    ((MessageActivity) mcontext).deleteMessageRequest(index);
                                    dlg.dismiss();
                                    break;

                                default:
                                    break;
                            }
                        }
                    });
                    dlg.show();
                }
            });

        } else {
            convertView = mInflater.inflate(R.layout.item_xiaoxi, null);
            ImageView iv_touxaing = ViewHolder.get(convertView, R.id.iv_touxaing);
            ImageView iv_xx_delete = ViewHolder.get(convertView, R.id.iv_xx_delete);
            ImageView iv_tp = ViewHolder.get(convertView, R.id.iv_tp);

            TextView tv_nicheng = ViewHolder.get(convertView, R.id.tv_nicheng);
            TextView tv_kc_pinlun = ViewHolder.get(convertView, R.id.tv_kc_pinlun);
            TextView iv_xx_time = ViewHolder.get(convertView, R.id.iv_xx_time);
            TextView tv_kc = ViewHolder.get(convertView, R.id.tv_kc);

            final MyComment plistdata = plist.get(position);

            tv_nicheng.setText(plistdata.user.nickname);
            iv_xx_time.setText(plistdata.createDate);
            tv_kc.setText(plistdata.course.name);
            tv_kc_pinlun.setText(plistdata.content);

            //设置头像
            if (!TextUtils.isEmpty(plistdata.user.head.path)) {
                ImageLoader.getInstance().displayImage(plistdata.user.head.path, iv_touxaing,
                        HMApplication.getInstance().getOptionsCircle());
            } else {
                iv_touxaing.setImageResource(R.mipmap.touxiang11);
            }
            iv_touxaing.setScaleType(ImageView.ScaleType.CENTER_CROP);

            //设置课程图片
            if (!TextUtils.isEmpty(plistdata.course.image.path)) {
                ImageLoader.getInstance().displayImage(plistdata.course.image.path, iv_tp);
            }

            //进入到相应的课程评论
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mcontext, CourseInfoActivity.class);
                    intent.putExtra("course", (Serializable) plist.get(position).course);
                    mcontext.startActivity(intent);
                }
            });


            //删除通知
            iv_xx_delete.setTag(position);
            iv_xx_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int index1 = (Integer) view.getTag();
                    dlg = new TwoBtnDialog(mcontext, "是否删除?", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()) {
                                case R.id.btn_left:
                                    dlg.dismiss();
                                    break;
                                case R.id.btn_right:
                                    ((MessageActivity) mcontext).deletePinglunRequest(index1);
                                    dlg.dismiss();
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    dlg.show();
                }
            });
        }
        return convertView;
    }
}
