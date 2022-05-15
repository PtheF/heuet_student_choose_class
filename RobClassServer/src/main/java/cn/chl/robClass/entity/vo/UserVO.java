package cn.chl.robClass.entity.vo;

public class UserVO {
    private String id;
    private String name;
    private String gender;
    private String clsNo;

    public String getClsNo() {
        return clsNo;
    }

    public UserVO setClsNo(String clsNo) {
        this.clsNo = clsNo;
        return this;
    }

    public String getId() {
        return id;
    }

    public UserVO setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserVO setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", clsNo='" + clsNo + '\'' +
                '}';
    }

    public String getGender() {
        return gender;
    }

    public UserVO setGender(String gender) {
        this.gender = gender.equals("m") ? "男" : "女";
        return this;
    }
}
