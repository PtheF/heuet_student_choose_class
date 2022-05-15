package cn.chl.robClass.mapper;

import cn.chl.robClass.entity.po.StudentClassPO;
import cn.chl.robClass.entity.po.UserPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IUserMapper {

    void insertUser(UserPO user);

    UserPO queryUserById(String id);

    List<UserPO> queryAll();

    List<StudentClassPO> getStudentClass();
}
