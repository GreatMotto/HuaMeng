package com.bm.hm.bean;

import java.io.Serializable;

public class Banner implements Serializable {

    private static final long serialVersionUID = 1L;

    public int id;

    public String  name;

    public String link;

    public String position;

    public int index;

    public String type;

    public Course course;

    public String activityLink;

    public Image image;

    public String startDate;

    public String endDate;

    public int status;
}
