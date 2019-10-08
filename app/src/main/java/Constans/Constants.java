package Constans;

public class Constants {
    //模块（用于区分地区属性）的默认值：1
    public static final int DEFAULT_MODULE = 1;
    /**
     * 正式版本后台
     */
    public static final String HOST = "http://moiau.com:8000/";

    //登录
    public static final String LOGININ="api/login/";
    //获取所有电梯
    public static final String GETALL_ELEVATOR = "api/get_elevators_by_mcompanystaffpk/";
    //获取电梯种类
    public static final String GETELEVATOR_TYPE ="api/get_mtctypes/";
    //添加电梯维保工单
    public static final String ADD_MTC="api/add_mtc/";
    //添加电梯子工单
    public static final String ADD_LOG="api/check_mtclog/";
    //提交维保工单
    public static final String SUBMIT_MTC="api/submit_mtc/";
    //获取维保工单
    public static final String GET_MTC_BY_MCOMPANY ="api/get_mtcs_by_mcompanystaffpk/" ;
    //获取已添加的未完成工单
    public static final String GET_MTC ="api/get_mtc/";
    public static final String GET_MTC_BY_PCOMPANY = "api/get_mtcs_by_pcompanystaffpk/";
    public static final String CONFIRM = "api/confirm_mtc/";
}
