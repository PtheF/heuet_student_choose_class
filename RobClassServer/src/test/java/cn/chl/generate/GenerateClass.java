package cn.chl.generate;

import cn.chl.robClass.RobClassApplication;
import cn.chl.robClass.entity.po.ClassPO;
import cn.chl.robClass.entity.po.ClassStockPO;
import cn.chl.robClass.mapper.IClassMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = RobClassApplication.class)
@RunWith(SpringRunner.class)
public class GenerateClass {

    @Autowired
    private IClassMapper mapper;

    private final String[] ELE_CLASS_NAME = {
            "Java程序设计",
            "微信小程序",
            "C++面向对象",
            "JavaEE企业框架",
            "网页设计",
            "数据库原理",
            "操作系统",
            "软件工程",
            "马克思主义原理",
            "数据结构与算法",
            "机器学习",
            "Spark程序设计",
            "大数据技术基础",
            "计算机导论",
            "Python程序设计",
            "爬虫",
            "金融学",
            "管理学"
    };

    private final String[] PE_CLASS = {
            "羽毛球",
            "轮滑",
            "空竹",
            "初级剑",
            "篮球",
            "乒乓球",
            "羽毛球",
            "轮滑",
            "空竹",
            "初级剑",
            "篮球",
            "乒乓球",
            "羽毛球",
            "轮滑",
            "空竹",
            "初级剑",
            "篮球",
            "足球"
    };

    private final String[] CLS_NO = {
            "10013", "10015", "10012", "10018", "10019", "10017", "10011", "10014", "10016"
    };

    private final String[] TEACHER = {
            "高老师", "薛老师", "侯老师", "赵老师", "魏老师", "霍老师", "张老师", "王老师", "陈老师"
    };

    private final String[] TIME = {
            "周一1-2",  "周一3-5", "周一6-7",
            "周二8-10", "周二1-2", "周二3-4",
            "周三3-5",  "周四1-2", "周四3-4",
            "周四6-7",  "周四8-9", "周五1-2",
            "周五3-4",  "周五6-7", "周五8-9"
    };

    @Test
    public void generateClass(){

        System.out.println(mapper);

        int startId = 10001;

        ClassPO c1 = new ClassPO();
        ClassStockPO c2 = new ClassStockPO();
        c2.setSelected(0);

        for(int i = 0; i < 18; i++){
            startId ++;
            String name = ELE_CLASS_NAME[i];
            String teacher = TEACHER[i % 9];
            String clsNo = CLS_NO[i % 9];
            String time = TIME[i % 15];
            int typ = 1;

            c1.setId(startId);
            c1.setName(name);
            c1.setTeacher(teacher);
            c1.setClsNo(clsNo);
            c1.setTyp(typ);
            c1.setcTime(time);

            c2.setId(startId);
            c2.setStock(100);
            c2.setRemain(100);

            mapper.insertClass(c1);
            mapper.insertClassStock(c2);

        }

        for(int i = 0; i < 18; i++){
            startId++;
            String name = PE_CLASS[i];
            String teacher = TEACHER[i % 9];
            String clsNo = CLS_NO[i % 9];
            String time = TIME[(i + 3) % 15];
            int typ = 2;

            c1.setId(startId);
            c1.setName(name);
            c1.setTeacher(teacher);
            c1.setClsNo(clsNo);
            c1.setTyp(typ);
            c1.setcTime(time);

            c2.setId(startId);
            c2.setStock(49);
            c2.setRemain(49);

            mapper.insertClass(c1);
            mapper.insertClassStock(c2);

        }
    }
}
