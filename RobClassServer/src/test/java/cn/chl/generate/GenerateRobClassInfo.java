package cn.chl.generate;

import cn.chl.robClass.RobClassApplication;
import cn.chl.robClass.controller.UserController;
import cn.chl.robClass.controller.entity.ResponseData;
import cn.chl.robClass.controller.entity.UserLoginRequest;
import cn.chl.robClass.entity.RobClassMessage;
import cn.chl.robClass.entity.po.StudentClassPO;
import cn.chl.robClass.entity.vo.UserVO;
import cn.chl.robClass.mapper.IUserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;

@SpringBootTest(classes = RobClassApplication.class)
@RunWith(SpringRunner.class)
public class GenerateRobClassInfo {

    @Autowired
    private IUserMapper mapper;

    @Autowired
    private UserController controller;

    @Test
    public void g() throws Exception{
        List<StudentClassPO> pos = mapper.getStudentClass();

        PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream(new File("rob.csv")), "UTF-8"
                )
        );

        pos.forEach(po -> {
            String uid = po.getUid();

            UserLoginRequest userLoginRequest = new UserLoginRequest();
            userLoginRequest.setId(uid);
            userLoginRequest.setPw("123456");

            ResponseData<UserVO> login = controller.login(userLoginRequest);

            String jwt = login.getAuthorization();

            writer.println(
                    "" + uid + "," + po.getCid() + "," + po.getTyp() + "," + jwt
            );
        });

        writer.close();
    }

}
