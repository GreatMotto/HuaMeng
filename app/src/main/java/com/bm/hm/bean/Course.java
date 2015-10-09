package com.bm.hm.bean;

import java.io.Serializable;
import java.util.List;

public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    public int id;

    public String name;

    public Image image;

    public int score;

    public User teacher;

    public LevelType level1Type;

    public LevelType level2Type;

    public LevelType level3Type;

    public String content;

    public int bannerIndex;

    public Image bannerImage;

    public String bannerStartDate;

    public String bannerEndDate;

    public int status;

    public int isDel;

    public int sellNumber;

    public String isCollection;

    public String getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(String isCollection) {
        this.isCollection = isCollection;
    }

    public int commentNumber;

    public List<Video> videoList;

    public String isPublish;
    public String publishDate;
    public String videoNum;
    public String typeStr;

}
