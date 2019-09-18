package Bean;

public class Inspect {

    /**
     * order : 1
     * content : 机房、滑轮间环境
     * demand : 清洁，门窗完好、照明正常
     * photo : false
     * photo_num : null
     */

    private int order;
    private String content;
    private String demand;
    private boolean photo;
    private Object photo_num;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDemand() {
        return demand;
    }

    public void setDemand(String demand) {
        this.demand = demand;
    }

    public boolean isPhoto() {
        return photo;
    }

    public void setPhoto(boolean photo) {
        this.photo = photo;
    }

    public Object getPhoto_num() {
        return photo_num;
    }

    public void setPhoto_num(Object photo_num) {
        this.photo_num = photo_num;
    }
}
