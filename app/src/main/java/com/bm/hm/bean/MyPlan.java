package com.bm.hm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guoky on 2015/5/19.
 */
public class MyPlan implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public String id;
    public String name;
    public List<ChildPlan> learnPlanContentList;

    public class ChildPlan implements Serializable {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
        public String content;
        public String dateStr;
        public String endDate;
        public String id;
        public String startDate;


    }

}
