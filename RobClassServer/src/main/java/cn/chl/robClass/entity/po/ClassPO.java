package cn.chl.robClass.entity.po;

public class ClassPO {
    private Integer id;
    private String name;
    private String teacher;
    private String clsNo;
    private String cTime;
    private int typ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClsNo() {
        return clsNo;
    }

    public void setClsNo(String clsNo) {
        this.clsNo = clsNo;
    }

    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }

    public int getTyp() {
        return typ;
    }

    public void setTyp(int typ) {
        this.typ = typ;
    }

    @Override
    public String toString() {
        return "ClassPO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teacher='" + teacher + '\'' +
                ", clsNo='" + clsNo + '\'' +
                ", cTime='" + cTime + '\'' +
                ", typ=" + typ +
                '}';
    }
}
