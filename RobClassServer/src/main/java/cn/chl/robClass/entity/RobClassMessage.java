package cn.chl.robClass.entity;

public class RobClassMessage {

    private Long rid;

    private String uid;

    private Integer cid;

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    @Override
    public String toString() {
        return "RobClassMessage{" +
                "rid=" + rid +
                ", uid='" + uid + '\'' +
                ", cid=" + cid +
                '}';
    }
}
