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
    public static final String ADD_MTC="api/add_mtc/";
}
