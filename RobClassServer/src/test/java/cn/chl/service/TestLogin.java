package cn.chl.service;

import cn.chl.robClass.RobClassApplication;
import cn.chl.robClass.entity.vo.UserVO;
import cn.chl.robClass.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = RobClassApplication.class)
@RunWith(SpringRunner.class)
public class TestLogin {

    @Autowired
    private IUserService service;

    @Test
    public void testLogin(){

//        201822360101,utlmvopw
//        201822360102,813f19qf
//        201822360103,yqn9y2a6
//        201822360104,gebuza37
//        201822360105,ia148gp6
        UserVO user1 = service.login("201822360101", "ptlmvopw");
        UserVO user2 = service.login("201822360102", "a13f19qf");
        UserVO user3 = service.login("201822360103", "yqn9y2a6");
        UserVO user4 = service.login("201822360104", "gebuza37");

        System.out.println(user1);
        System.out.println(user2);
        System.out.println(user3);
        System.out.println(user4);
    }
}
