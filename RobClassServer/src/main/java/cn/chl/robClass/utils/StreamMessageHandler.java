package cn.chl.robClass.utils;

import jodd.template.StringTemplateParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Stream 消息处理器，封装一个，太麻烦了这东西
 */
@Component
@Slf4j
public class StreamMessageHandler {

    private StringRedisTemplate template;

    public StreamMessageHandler(StringRedisTemplate template){
        this.template = template;
    }

    public void listen(String stream, String group, Consumer<Map<Object, Object>> success){
        this.listen(stream, group, success, success, null);
    }

    /**
     * 持续监听Stream消息，如果消息处理异常，则开始消费pending-list
     * 如果pending-list卡住了，一次性处理错误10次，则执行错误回调
     *
     * @param stream 队列名
     * @param consumerGroup 消费者组名
     * @param success 获取消息成功回调
     * @param pending 处理pending-list回调
     * @param errCallback pending-list异常回调
     */
    public void listen(
            String stream,
            String consumerGroup,
            Consumer<Map<Object, Object>> success,
            Consumer<Map<Object, Object>> pending,
            BiConsumer<MapRecord<String, Object, Object>, Exception> errCallback
    ){
        log.info("listen stream :{}, consumer group:{}", stream, consumerGroup);

        while(true){

            try{
                List<MapRecord<String, Object, Object>> read = template.opsForStream().read(
                        org.springframework.data.redis.connection.stream.Consumer.from(consumerGroup, "consumer_0"),
                        StreamReadOptions.empty().count(1).block(Duration.ofSeconds(2)),
                        StreamOffset.create(stream, ReadOffset.lastConsumed())
                );

                if (read == null || read.isEmpty()) {
                    continue;
                }

                MapRecord<String, Object, Object> record = read.get(0);
                Map<Object, Object> value = record.getValue();

                success.accept(value);

                template.opsForStream().acknowledge(stream, consumerGroup, record.getId());

                Thread.sleep(5);

            }catch(Exception e){
                log.info("stream listen error: {}", e.getMessage());
                consumePendingList(stream, consumerGroup, pending, errCallback);
            }

        }
    }

    /**
     * 处理未确认消息队列，一直处理直到消费完
     * @param stream 队列名称
     * @param consumerGroup 消费者组
     * @param pending 处理pending-list回调
     * @param errCallback 错误回调
     */
    private void consumePendingList(String stream,
                                    String consumerGroup,
                                    Consumer<Map<Object, Object>> pending,
                                    BiConsumer<MapRecord<String, Object, Object>, Exception> errCallback){

        log.info("handle pending list");

        int errCount = 0;

        MapRecord<String, Object, Object> lastRecord = null;
        Exception lastException = null;

        while(errCount < 10){

            try{
                List<MapRecord<String, Object, Object>> read = template.opsForStream().read(
                        org.springframework.data.redis.connection.stream.Consumer.from(consumerGroup, "pending"),
                        StreamReadOptions.empty().count(1),
                        StreamOffset.create(stream, ReadOffset.from("0"))
                );

                if(read == null || read.isEmpty()){
                    log.info("pending-list empty, break;");
                    break;
                }

                MapRecord<String, Object, Object> record = read.get(0);

                lastRecord = record;

                Map<Object, Object> value = record.getValue();

                pending.accept(value);

                template.opsForStream().acknowledge(stream, consumerGroup, record.getId());

                errCount = 0;

            }catch(Exception e){

                errCount ++;

                lastException = e;

                try{
                    Thread.sleep(500);
                }catch(InterruptedException ie){

                }

            }
        }

        if(errCount == 10){
            log.error("pending-list handle error");
            if(errCallback != null){
                errCallback.accept(lastRecord, lastException);
            }
        }

    }
}
