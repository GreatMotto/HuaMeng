package com.bm.hm.bean;

import java.io.Serializable;

public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    public int id;

    public String content;

    public String answerA;

    public String answerB;

    public String answerC;

    public String answerD;

    public String answerE;

    public String answerF;

    public String answerG;

    public String rightAnswer;

    public int questionIndex;

    public  Questionnaire questionnaire;
}
