package com.bm.hm.bean;

import java.io.Serializable;

/**
 * Created by chenb on 2015/5/25.
 * 我的评论
 */
public class MyComment implements Serializable {
    private static final long serialVersionUID = 1L;

    public int id;
    public User user;
    public Course course;
    public String content;
    public String level;
    public String createDate;
}
