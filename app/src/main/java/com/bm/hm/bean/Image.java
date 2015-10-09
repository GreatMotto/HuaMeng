package com.bm.hm.bean;

import java.io.Serializable;

public class Image implements Serializable {

    private static final long serialVersionUID = 1L;

    public int id;
    public String path;
    public String thumbs;
    public String attribute;
    public String createDate;

    public class Attribute implements Serializable {

        private static final long serialVersionUID = 1L;
        public int height;
        public int width;

    }
}
