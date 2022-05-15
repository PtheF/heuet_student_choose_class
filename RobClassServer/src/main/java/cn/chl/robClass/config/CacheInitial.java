package cn.chl.robClass.config;

import cn.chl.robClass.entity.po.ClassPO;
import cn.chl.robClass.entity.po.ClassStockPO;
import cn.chl.robClass.entity.po.UserPO;
import cn.chl.robClass.mapper.IClassMapper;
import cn.chl.robClass.mapper.IUserMapper;
import cn.chl.robClass.mapper.InitialMapper;
import cn.chl.robClass.service.IClassService;
import cn.chl.robClass.service.IUserService;
import cn.chl.robClass.utils.CacheClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static cn.chl.robClass.utils.RedisConstant.*;

@Component
public class CacheInitial implements InitializingBean {

    @Autowired
    private StringRedisTemplate template;

    @Autowired
    private IClassService classService;

    @Autowired
    private IUserMapper userMapper;

    @Autowired
    private IClassMapper classMapper;

    @Autowired
    private CacheClient cacheClient;

    @Autowired
    private InitialMapper initialMapper;


    @Override
    public void afterPropertiesSet() throws Exception {


        // 初始化学生数据缓存，用于登录，第一次启动这个得写上，以后启动可以注释掉了。
        initStudent();


        refreshDB();
        initClassCache();
        clearStudentClass();

//        clearUserCache();
    }

    /**
     * 刷新数据库，删除抢课记录，恢复课程容量
     */
    private void refreshDB(){

        initialMapper.initStock();

        initialMapper.clearRecord();

    }

    /**
     * 构建抢课缓存，同时清理历史抢课信息
     */
    private void initClassCache(){
        List<ClassStockPO> classStock = classService.getClassStock();

        classStock.forEach(stock -> {
            // 初始化课程已选人数
            template.opsForValue().set(
                    CLASS_SELECT_PREFIX + stock.getId(), String.valueOf(stock.getSelected())
            );

            // 初始化课程余量
            template.opsForValue().set(
                    CLASS_REMAIN_PREFIX + stock.getId(), String.valueOf(stock.getRemain())
            );

            // 删除所有课程成员信息
            template.delete(CLASS_MEMBER_PREFIX + stock.getId());

        });


        String[] clsNoLib = {
                "10011", "10012", "10013", "10014", "10015", "10016", "10017", "10018", "10019"
        };

        // 各班级课程集合加入缓存
        for (String s : clsNoLib) {
            List<ClassPO> classDes = classMapper.getClassDes(s);

            cacheClient.setCache(CLASS_PREFIX + s, classDes, 24, TimeUnit.DAYS);
        }


    }

    /**
     * 删除Redis中的学生选课信息
     */
    private void clearStudentClass(){
        List<UserPO> pos = userMapper.queryAll();

        pos.forEach(po -> {
            template.delete(STU_SELECT_PREFIX + po.getId());
        });
    }

    /**
     * 初始化学生数据用于登录
     */
    private void initStudent(){
        List<UserPO> userPO = userMapper.queryAll();

        userPO.forEach(po -> {
            cacheClient.setCache(USER_PREFIX + po.getId(), po, 24, TimeUnit.DAYS);
        });
    }

    /**
     * 清理学生数据用于测试，没啥用了这个方法
     */
    private void clearUserCache(){
        List<UserPO> pos = userMapper.queryAll();

        pos.forEach(po -> {
            template.delete(USER_PREFIX + po.getId());
        });
    }
}
