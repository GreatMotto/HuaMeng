package com.bm.hm.bean;

import java.io.Serializable;

/**
 * Created by guoky on 2015/5/19.
 */
public class timelineTeacher implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public String courseName;
    public String id;
    public User teacher;
    public  TimeLine timeline;
}
