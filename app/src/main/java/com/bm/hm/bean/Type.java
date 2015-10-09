package com.bm.hm.bean;

import java.io.Serializable;

public class Type implements Serializable {

    private static final long serialVersionUID = 1L;

    public int id;

    public String name;

    public boolean select;

    public int pid;

    public int level;

    public Type(String name, boolean select) {
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
}
