package cn.chl.demo;

import cn.chl.robClass.RobClassApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = RobClassApplication.class)
@RunWith(SpringRunner.class)
public class TestRedisCluster {

    @Autowired
    private StringRedisTemplate template;

    @Test
    public void testTemplate(){
        template.opsForValue().set("name", "jack");

        String name = template.opsForValue().get("name");

        System.out.println(name);
    }
}
