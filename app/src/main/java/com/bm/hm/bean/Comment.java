package com.bm.hm.bean;

import java.io.Serializable;

public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    public int id;

    public User user;

    public String content;

    public String level;

    public String createDate;
}
