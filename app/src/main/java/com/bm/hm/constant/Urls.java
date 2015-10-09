package com.bm.hm.constant;

/**
 * @Description 后台请求地址
 * @time 2015-5-13
 */
public class Urls {

    /**
     * 内网
     */
//    public static final String BASE_URL = "http://10.58.169.86:8081/huameng/";// 高磊

    /**
     * 外网
     */
    public static final String BASE_URL = "http://122.0.72.9:8080/huameng/";

    /**
     * 登陆
     */
    public static final String LOGIN = BASE_URL + "api/user/login";

    /**
     * 注册
     */
    public static final String REGISTER = BASE_URL + "api/user/register";

    /**
     * 发送验证码
     */
    public static final String SEND_CODE = BASE_URL + "api/sms/sendCode";

    /**
     * 校验验证码
     */
    public static final String CHECK_CODE = BASE_URL + "api/user/check/code";


    /**
     * 查询个人信息
     */
    public static final String USERINFO = BASE_URL + "api/user/info";

    /**
     * 首页广告banner
     */
    public static final String HOME_BANNER_LIST = BASE_URL + "api/ad/list";

    /**
     * 首页课程列表
     */
    public static final String HOME_COURSE_LIST = BASE_URL + "api/course/homepageList";

    /**
     * 课程列表广告banner
     */
    public static final String COURSE_BANNER_LIST = BASE_URL + "api/course/banner";

    /**
     * 个人信息修改
     */
    public static final String UPDATE_USERINFO = BASE_URL + "api/user/update";

    /**
     * 课程列表
     */
    public static final String COURSE_LIST = BASE_URL + "api/course/list";
    /**
     * 时间轴
     */
    public static final String TIMELINE = BASE_URL + "api/timeline/myTimeline";
    /**
     * 我的计划
     */
    public static final String MYLEARNPLAN = BASE_URL + "api/learnPlan/myLearnPlan";

    /**
     * 课程详情
     */
    public static final String COURSE_DETAIL = BASE_URL + "api/course/detail";

    /**
     * 修改密码
     */
    public static final String UPDATEPWD = BASE_URL + "api/user/modifyPassword";

    /**
     * 收藏课程
     */
    public static final String COURSE_COLLECTION = BASE_URL + "api/user/doCollection";

    /**
     * 课程评论
     */
    public static final String COURSE_COMMENT = BASE_URL + "api/comment/courseComment";
    /**
     * 查询我购买的视频
     */
    public static final String QUERY_BUY_VIDEO = BASE_URL + "api/course/myBuyCourse";
    /**
     * 查询我的老师
     */
    public static final String QUERY_MY_TEA = BASE_URL + "api/user/myTeacher";
    /**
     * 查询我的收藏
     */
    public static final String QUERY_MY_COLLEC = BASE_URL + "api/user/myCollection";

    /**
     * 绑定学号
     */
    public static final String BIND_NUM = BASE_URL + "api/user/bindStudentNumber";

    /**
     * 上传头像
     */
    public static final String UPLOADHEAD = BASE_URL + "api/user/uploadHead";

    /**
     * 查询账户明细
     */
    public static final String SCOREDETAIL = BASE_URL + "api/user/scoreDetail";

    /**
     * 我的消息
     */
    public static final String MYMESSAGE = BASE_URL + "api/message/myMessage";

    /**
     * 删除消息
     */
    public static  final String DELETEMSG=BASE_URL+"api/message/delete";

    /**
     * 把消息标记已读
     */
    public static  final  String MARKREAD=BASE_URL+"api/message/markRead";

    /**
     * 查询我的评论
     */
    public static  final  String MYCOMMENT=BASE_URL+"api/comment/myComment";

    /**
     * 删除评论
     */
    public static  final String   DELETE_PL=BASE_URL+"api/comment/delete";

    /**
     * 卡密充值
     */
    public static final String CARD_RECHARGE = BASE_URL + "api/recharge/cardRecharge";

    /**
     * 找回密码验证码
     */
    public static final String PASSWORD_CHECK_CODE = BASE_URL + "api/user/find/password/check";

    /**
     * 找回密码设置新密码
     */
    public static final String CHANGE_PASSWORD = BASE_URL + "api/user/find/password/change";

    /**
     * 查询课程类别信息
     */
    public static final String GET_COURSE_TYPE = BASE_URL + "api/courseType/list";
    /**
     * 查询老师发布的视频
     */
    public static final String QUERY_MY_TEA_VIDEO = BASE_URL + "api/course/listByTeacherId";

    /**
     * 帮助信息
     */
     public static  final  String HELP=BASE_URL+"api/helpfulQuestion/list";

    /**
     * 增加意见
     */

    public static  final  String FEEDBACK=BASE_URL+"api/feedback/create";

    /**
     * 活动列表banner
     */
    public static final String GET_ACTIVITY_BANNER = BASE_URL + "api/activity/banner";

    /**
     * 活动列表
     */
    public static final String GET_ACTIVITY_LIST = BASE_URL + "api/activity/list";

    /**
     * 购买课程
     */
    public static final String PAY_COURSE = BASE_URL + "api/user/payCourse";

    /**
     * 取消收藏
     */
    public static final String CANCEL_COLLECTION = BASE_URL + "api/user/cancelCollection";

    /**
     * 我的华盟-未读消息
     */
    public static final String NOT_READ_MSG = BASE_URL + "api/user/myHuameng";

    /**
     * 添加评论
     */
    public static final String CREATE_COMMENT = BASE_URL + "api/comment/create";

    /**
     * 是否参加过问卷调查
     */
    public static final String JOIN_ASK = BASE_URL + "api/questionnaire/isSubmit";

    /**
     * 查询最新问卷
     */
    public static final String GET_ASK = BASE_URL + "api/questionnaire/newest";

    /**
     * 提交问卷
     */
    public static final String SUBMIT_ANSWER = BASE_URL + "api/questionnaire/submitAnswer";
    /**
     * 我的课表
     */
    public static  final String MYTIMETABLE=BASE_URL+"api/timetable/myTimetable";

    /**
     * 支付回调
     */
    public static  final String ALIPAY_CALLBACK=BASE_URL+"api/alipay/alipayCallBack";

    /**
     * 创建订单
     */
    public static  final String ALIPAY_CREATEORDER=BASE_URL+"api/alipay/createOrder";

    /**
     * 播放时长
     */
    public static  final String GET_SIGHT_TIME=BASE_URL+"api/course/sightTime";
}
