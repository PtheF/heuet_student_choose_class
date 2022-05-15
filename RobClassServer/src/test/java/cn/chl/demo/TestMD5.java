package cn.chl.demo;


import cn.chl.robClass.utils.MD5Utils;
import org.junit.Test;

public class TestMD5 {

    @Test
    public void testMD5(){
        String password = "123456";
        String generate = MD5Utils.generate(password);

        System.out.println(generate);

        boolean verify = MD5Utils.verify("12s3456", generate);
        System.out.println(verify);
    }
}
