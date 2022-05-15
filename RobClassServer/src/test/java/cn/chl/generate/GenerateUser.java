package cn.chl.generate;

import cn.chl.robClass.RobClassApplication;
import cn.chl.robClass.entity.po.UserPO;
import cn.chl.robClass.mapper.IUserMapper;
import cn.chl.robClass.utils.MD5Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Random;

@SpringBootTest(classes = RobClassApplication.class)
@RunWith(SpringRunner.class)
public class GenerateUser {

    private final Random rand = new Random();

    @Autowired
    private IUserMapper mapper;

    private  PrintWriter writer;

    @Test
    public void testMapper(){
        UserPO user = new UserPO();

        mapper.insertUser(user);
    }

    @Test
    public void testUser(){
        UserPO user = new UserPO();
        user.setId("201922450108");
        user.setName("chl");
        user.setClsNo("10012");
        user.setGender("m");
        user.setPassword(MD5Utils.generate("123456"));

        mapper.insertUser(user);
    }

    @Test
    public void generateUser() throws FileNotFoundException, UnsupportedEncodingException{

        writer = new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream(new File("login.csv")), "utf-8"));

        getId("201822");
        getId("201922");
        getId("202022");
        getId("202122");

        writer.close();

    }

    private void getId(String prefix){

        for (int i = 36; i <= 48; i++) {

            for (int j = 1; j < 6; j++) {

                for(int z = 1; z <= 40; z++){
                    String id = prefix;
                    id += i;
                    id += "0" + j;

                    id = z < 10 ? (id + "0" + z) : (id + z);

                    try{
                        handle(id);
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    private void handle(String id) throws FileNotFoundException, UnsupportedEncodingException {
        String[] nameLib = {
                "Jack", "Bob", "Kara", "Tod", "Linus", "Stone", "Mike", "Frank", "David", "Tom", "Jenny", "Wood"
        };

        String[] genderLib = {"m", "f"};

        int[] clsLib = {
                10011, 10012, 10013,10014, 10015, 10016, 10017, 10018, 10019
        };

        UserPO user = new UserPO();

        String name = nameLib[rand.nextInt(12)];
        String gender = genderLib[rand.nextInt(2)];
        String clsNo = String.valueOf(clsLib[rand.nextInt(9)]);
        String password = randomPassword();

        user.setId(id);
        user.setName(name);
        user.setGender(gender);
        user.setClsNo(clsNo);
        user.setPassword(MD5Utils.generate(password));

        writer.println("" + id + "," + password);

//        System.out.println(user);



        mapper.insertUser(user);

    }

    private String randomPassword(){
        String slib = "0123456789abcdefghijklmnopqrstuvwxyz";


        String pw = "";
        for (int i = 0; i < 8; i++) {
            pw += slib.charAt(rand.nextInt(36));
        }

        return pw;
    }

//    @Test
//    public void getLoginTxt() throws FileNotFoundException, UnsupportedEncodingException{
//
//        List<UserPO> userList = mapper.getUser();
//
//        PrintWriter writer = new PrintWriter(
//                new OutputStreamWriter(
//                        new FileOutputStream(new File("login.csv")), "utf-8"));
//
//        userList.forEach(user -> {
//            writer.println("" + user.getId() + "," + user.getPassword());
//        });
//
//        writer.close();
//    }

    @Test
    public void updateUserPw()throws Exception{

        Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.73.100:3306/rob_class?serverTimezone=UTC", "chl", "123456");

        PreparedStatement sql = conn.prepareStatement("update user set password = ? where id = '1001' or 1 = 1;");

        sql.setString(1, MD5Utils.generate("123456"));

        boolean execute = sql.execute();
        System.out.println(execute);
    }
}
