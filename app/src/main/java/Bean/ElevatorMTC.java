package Bean;

public class ElevatorMTC {

    /**
     * pk : 4d017cd4e5e411e98b751c36bb206538
     * elevator_name : 493栋721单元中梯
     * type : month
     * mcompany_staff : 维修工 许安静
     * pcompany_staff : 安全员 冯冰
     * submitted_date : null
     * confirmed_date : null
     * status : unsubmitted
     */

    private String pk;
    private String elevator_name;
    private String type;
    private String mcompany_staff;
    private String pcompany_staff;
    private Object submitted_date;
    private Object confirmed_date;
    private String status;

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getElevator_name() {
        return elevator_name;
    }

    public void setElevator_name(String elevator_name) {
        this.elevator_name = elevator_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMcompany_staff() {
        return mcompany_staff;
    }

    public void setMcompany_staff(String mcompany_staff) {
        this.mcompany_staff = mcompany_staff;
    }

    public String getPcompany_staff() {
        return pcompany_staff;
    }

    public void setPcompany_staff(String pcompany_staff) {
        this.pcompany_staff = pcompany_staff;
    }

    public Object getSubmitted_date() {
        return submitted_date;
    }

    public void setSubmitted_date(Object submitted_date) {
        this.submitted_date = submitted_date;
    }

    public Object getConfirmed_date() {
        return confirmed_date;
    }

    public void setConfirmed_date(Object confirmed_date) {
        this.confirmed_date = confirmed_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
