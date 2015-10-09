package com.bm.hm.http;

import java.io.Serializable;

public class BaseData<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /* 标志位 0成功 1失败 */
    public String status;
    /* 错误信息 */
    public String msg;
    /* 总数据结构 */
    public MapData<T> data;

}