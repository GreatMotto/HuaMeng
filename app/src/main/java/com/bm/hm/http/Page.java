package com.bm.hm.http;

import java.io.Serializable;

public class Page implements Serializable {

    private static final long serialVersionUID = -7991002736828223928L;
    public Integer totalPage;// 每页显示的记录数
    public Integer currentPage;// 当前页码
    public Integer totalNum;// 数据总条数
}
