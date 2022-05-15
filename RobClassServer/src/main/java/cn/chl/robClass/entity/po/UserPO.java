package cn.chl.robClass.entity.po;

public class UserPO {
    private String id;
    private String name;
    private String gender;
    private String clsNo;
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return "UserPO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", clsNo='" + clsNo + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getClsNo() {
        return clsNo;
    }

    public void setClsNo(String clsNo) {
        this.clsNo = clsNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
