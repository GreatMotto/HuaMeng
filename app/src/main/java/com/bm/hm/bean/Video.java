package com.bm.hm.bean;

import java.io.Serializable;

public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    public int id;
    public String name;
    public Boolean select;
    public String path;

    public String downloadPath;
    public boolean enable;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public Video(String name, boolean select) {
        this.name = name;
        this.select = select;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public CourseVideo video;
}
