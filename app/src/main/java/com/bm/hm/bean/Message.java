package com.bm.hm.bean;

import java.io.Serializable;

/**
 * Created by chenb on 2015/5/21.
 * 通知消息
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    public int id;    //通知ID
    public String content;  //内容
    public String createDate; //更新时间
    public String type;   //类型
    public String isRead;  //是否已读  yes no

}
