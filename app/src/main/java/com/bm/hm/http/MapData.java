package com.bm.hm.http;

import com.bm.hm.bean.Course;
import com.bm.hm.bean.Question;
import com.bm.hm.bean.Questionnaire;
import com.bm.hm.bean.Message;
import com.bm.hm.bean.SightTime;
import com.bm.hm.bean.TimeLine;
import com.bm.hm.bean.User;
import com.bm.hm.bean.Video;
import com.bm.hm.bean.timelineContent;
import com.bm.hm.bean.timelineTeacher;

import java.io.Serializable;
import java.util.List;

public class MapData<T> implements Serializable {

    /**
     * MapData封装所有数据类型
     */
    private static final long serialVersionUID = 2L;
    /* 数据List */
    public List<T> list;
    public Page page;

    public User user;
    public List<Course> basic, sat, sellRank, ielts, newest, toefljunior, toefl;
    public TimeLine timeline;
    public List<timelineTeacher> timelineTeacherList;
    public List<timelineContent> timelineContentList;
    public String isCollection;
    public int commentNumber;
    public List<Video> courseVideoList;
    public String isBuy;
    public int newMessageNumber;
    public int plusScore;
    public String isSubmit;
    public Questionnaire questionnaire;
    public List<Question> questionList;
    public Message message;
    public int sellNumber;

    public String orderId;

    public Course course;

    public SightTime sightTime;
}
