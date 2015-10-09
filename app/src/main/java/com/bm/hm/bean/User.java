package com.bm.hm.bean;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    public int id;

    public String nickname;

    public String password;

    public String name;

    public Image head;

    public String role;

    public UserStudentNum studentNumber;

    public String mobile;

    public String recommendMobile;

    public String email;

    public int sex;

    public String birthday;

    public String registerTime;

    public int score;

    public int status;

    public String teacherDesc;

    public Image teacherPic;

    public int teacherLevel;

    public int isDel;

    public String classNames;

    public String teacherProjectStr;

    public String teacherSubjectStr;

}
