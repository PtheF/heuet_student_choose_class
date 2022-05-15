package cn.chl.robClass.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 全局ID生成器
 */
@Component
public class IdWorker {

    private StringRedisTemplate template;

    public IdWorker(StringRedisTemplate template){
        this.template = template;
    }

    // 起始时间戳
    private final long START_STAMP = 1652390390;

    // 序号位数 32
    private final int BIT_COUNT = 32;

    public Long nextId(String prefix){


        LocalDateTime now = LocalDateTime.now();

        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);

        long sec = nowSecond - START_STAMP;

        String date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        long count = template.opsForValue().increment("icr:" + prefix + ":" + date);

        return sec << BIT_COUNT | count;

    }

}
