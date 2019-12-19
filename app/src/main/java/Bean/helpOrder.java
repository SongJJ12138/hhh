package Bean;

import java.io.Serializable;

public class helpOrder implements Serializable {


    /**
     * id : 49c39f28226d11eab878525400bdc3cb
     * sn : 20191219001
     * elevator_sn : 10001
     * elevator_name : 许昌芙蓉湖智慧大厦21层
     * reporter : 王磊
     * reporter_female : true
     * reporter_phone : 43546643
     * num_of_trapped : 2
     * injured : true
     * situation : 现场情况
     * cuser_name : 96333职员 昌易芹
     * ctime : 2019-12-19T14:38:57.257Z
     * response_progress : 1
     * rescue_progress : rescue_progress_responded
     * complete : false
     */

    private String id;
    private String sn;
    private String elevator_sn;
    private String elevator_name;
    private String reporter;
    private boolean reporter_female;
    private String reporter_phone;
    private int num_of_trapped;
    private boolean injured;
    private String situation;
    private String cuser_name;
    private String ctime;
    private String response_progress;
    private String rescue_progress;
    private boolean complete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getElevator_sn() {
        return elevator_sn;
    }

    public void setElevator_sn(String elevator_sn) {
        this.elevator_sn = elevator_sn;
    }

    public String getElevator_name() {
        return elevator_name;
    }

    public void setElevator_name(String elevator_name) {
        this.elevator_name = elevator_name;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public boolean isReporter_female() {
        return reporter_female;
    }

    public void setReporter_female(boolean reporter_female) {
        this.reporter_female = reporter_female;
    }

    public String getReporter_phone() {
        return reporter_phone;
    }

    public void setReporter_phone(String reporter_phone) {
        this.reporter_phone = reporter_phone;
    }

    public int getNum_of_trapped() {
        return num_of_trapped;
    }

    public void setNum_of_trapped(int num_of_trapped) {
        this.num_of_trapped = num_of_trapped;
    }

    public boolean isInjured() {
        return injured;
    }

    public void setInjured(boolean injured) {
        this.injured = injured;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getCuser_name() {
        return cuser_name;
    }

    public void setCuser_name(String cuser_name) {
        this.cuser_name = cuser_name;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getResponse_progress() {
        return response_progress;
    }

    public void setResponse_progress(String response_progress) {
        this.response_progress = response_progress;
    }

    public String getRescue_progress() {
        return rescue_progress;
    }

    public void setRescue_progress(String rescue_progress) {
        this.rescue_progress = rescue_progress;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
