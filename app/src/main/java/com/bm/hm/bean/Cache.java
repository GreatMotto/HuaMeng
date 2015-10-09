package com.bm.hm.bean;

import java.io.Serializable;

public class Cache implements Serializable {



    private static final long serialVersionUID = 1L;

    public Cache(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public boolean isDelete;

}
