package cn.chl.robClass.service.impl;

import cn.chl.robClass.controller.entity.ResponseData;
import cn.chl.robClass.entity.RobClassMessage;
import cn.chl.robClass.mapper.IRobClassMapper;
import cn.chl.robClass.service.IRobClassService;
import cn.chl.robClass.utils.IdWorker;
import cn.chl.robClass.utils.StreamMessageHandler;
import cn.chl.robClass.utils.UserHolder;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cn.chl.robClass.utils.RedisConstant.*;

@Service
@Slf4j
public class RobClassServiceImpl implements IRobClassService {

    @Autowired
    private IRobClassMapper mapper;

    @Autowired
    private StringRedisTemplate template;

    @Resource
    private IdWorker idWorker;

    @Autowired
    private RedissonClient redissonClient;

    @Resource
    private StreamMessageHandler messageHandler;

    private static final DefaultRedisScript<Long> ROB_CLASS_SCRIPT;

    static {
        ROB_CLASS_SCRIPT = new DefaultRedisScript<>();
        ROB_CLASS_SCRIPT.setResultType(Long.class);
        ROB_CLASS_SCRIPT.setLocation(new ClassPathResource("rob_class.lua"));
    }


    @Override
    public ResponseData<String> robClass(String cid, String typ) {

        String uid = UserHolder.getUser().getId();

        Long robId = idWorker.nextId("rob_class");

        Long canRobLong = template.execute(ROB_CLASS_SCRIPT,
                Collections.singletonList("{rob_class}"),
                cid, uid, typ, robId.toString()
        );

        int canRob = canRobLong.intValue();

        log.info("Lua script return value: " + canRob);

        if(canRob != 0){
            return ResponseData.fail(canRob == 1 ? "您已抢上该课程!" : "该课程满员了!");
        }

        log.info("rob class success; student: {}, class: {}", uid, cid);

        return ResponseData.ok("抢课成功");

    }

    private void handleRobClass(RobClassMessage message){

        RLock lock = redissonClient.getLock(ROB_CLASS_LOCK_PREFIX + message.getUid());

        boolean isLock = lock.tryLock();

        if(!isLock){
            log.warn("rob class repeat, student: " + message.getUid());
            return;
        }

        try{
            mapper.increMember(message.getCid());
            mapper.saveRobRecord(message);
        }finally{
            lock.unlock();
        }


    }

    @PostConstruct
    public void init(){
        ROB_CLASS_EXECUTOR.submit(new RobClassHandler());
    }

    /**
     * 创建单线程的线程池用于异步处理抢课请求
     */
    private final ExecutorService ROB_CLASS_EXECUTOR = Executors.newSingleThreadExecutor();

    private class RobClassHandler implements Runnable{

        @Override
        public void run(){
            messageHandler.listen(
                    ROB_CLASS_STREAM, ROB_CLASS_READ_GROUP, (value) -> {

                        RobClassMessage message = BeanUtil.fillBeanWithMap(value, new RobClassMessage(), true);

                        handleRobClass(message);
                    }
            );
        }
    }


//    private class RobClassHandler implements Runnable{
//        @Override
//        public void run(){
//
//            while(true){
//
//                try{
//                    List<MapRecord<String, Object, Object>> read = template.opsForStream().read(
//                            Consumer.from(ROB_CLASS_READ_GROUP, "c1"),
//                            StreamReadOptions.empty().count(1).block(Duration.ofSeconds(2)),
//                            StreamOffset.create(ROB_CLASS_STREAM, ReadOffset.lastConsumed())
//                    );
//
//                    if(read == null || read.isEmpty()){
//                        continue;
//                    }
//
//                    MapRecord<String, Object, Object> record = read.get(0);
//                    Map<Object, Object> value = record.getValue();
//                    RobClassMessage message = BeanUtil.fillBeanWithMap(value, new RobClassMessage(), true);
//
//                    System.out.println("收到消息：" + message);
//
//                    handleRobClass(message);
//
//                    template.opsForStream().acknowledge(ROB_CLASS_STREAM, ROB_CLASS_READ_GROUP, record.getId());
//                    System.out.println("消息确认完成");
//
//                }catch(Exception e){
//
//                    System.out.println("消息接收错误：");
//                    e.printStackTrace();
//
//                    handlePendingList();
//                }
//            }
//
//        }
//    }
//
//    public void handlePendingList(){
//
//        int countErr = 0;
//
//        while(countErr < 10){
//
//            try{
//                List<MapRecord<String, Object, Object>> read = template.opsForStream().read(
//                        Consumer.from(ROB_CLASS_READ_GROUP, "c2"),
//                        StreamReadOptions.empty().count(1),
//                        StreamOffset.create(ROB_CLASS_STREAM, ReadOffset.from("0"))
//                );
//
//                if(read == null || read.isEmpty()){
//                    break;
//                }
//
//                MapRecord<String, Object, Object> record = read.get(0);
//
//                RobClassMessage robClassMessage = BeanUtil.fillBeanWithMap(record.getValue(), new RobClassMessage(), true);
//
//                handleRobClass(robClassMessage);
//
//                template.opsForStream().acknowledge(ROB_CLASS_STREAM, ROB_CLASS_READ_GROUP, record.getId());
//
//                countErr = 0;
//
//            }catch(Exception e){
//                System.out.println("pendingList 异常");
//                countErr++;
//                try{
//                    Thread.sleep(200);
//                }catch(InterruptedException ie){
//
//                }
//            }
//
//
//        }
//
//    }
}
