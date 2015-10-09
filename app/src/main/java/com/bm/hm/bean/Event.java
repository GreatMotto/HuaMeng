package com.bm.hm.bean;

import java.io.Serializable;

public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    public int id;

    public String name;

    public Image image;

    public String zb;

    public String cb;

    public String startDate;

    public String endDate;

    public String address;

    public String link;

    public int homeIndex;

    public int bannerIndex;

    public Image bannerImage;

    public String bannerStartDate;

    public String bannerEndDate;

    public int status;

    public int createStatus;

    public String statusStr;

}
