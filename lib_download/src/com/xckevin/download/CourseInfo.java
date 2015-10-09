package com.xckevin.download;

import java.io.Serializable;

public class CourseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    public int id;

    public String name;

    public String imagePath;

    public int hasCacheCount;

    public String courseInfo;

    public String getCourseInfo() {
        return courseInfo;
    }

    public void setCourseInfo(String courseInfo) {
        this.courseInfo = courseInfo;
    }

    public boolean delete;

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getHasCacheCount() {
        return hasCacheCount;
    }

    public void setHasCacheCount(int hasCacheCount) {
        this.hasCacheCount = hasCacheCount;
    }

}
