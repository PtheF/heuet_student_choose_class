package cn.chl.robClass.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
@Slf4j
public class CacheClient {

    private StringRedisTemplate template;

    public CacheClient(StringRedisTemplate template){
        this.template = template;
    }

    private final ObjectMapper JSON = new ObjectMapper();

    public <R, ID> List<R> getCacheList(String prefix, ID id, TypeReference<List<R>> tr,
                                        Function<ID, List<R>> callback, long expire, TimeUnit tu){

        String key = prefix + id;

        String value = template.opsForValue().get(key);

        // 未命中
        if(value == null){
            try{
                List<R> r = callback.apply(id);

                // 数据库没有，存空值解决缓存穿透
                if(r == null){
                    template.opsForValue().set(key, "", 30, TimeUnit.SECONDS);
                    return null;
                }

                this.setCache(key, JSON.writeValueAsString(r), expire, tu);

                return r;
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }

        // 命中但是为空
        if("".equals(value)){
            return null;
        }

        // 成功命中
        try{
            return JSON.readValue(value, tr);

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public <R, ID> R getCacheNotSave(String prefix, ID id, Class<R> rtp,
                                     Function<ID, R> callback){
        return getCache(prefix, id, rtp, callback, 0, null, false);
    }

    public <R, ID> R getCacheAndSave(String prefix, ID id, Class<R> rtp,
                                     Function<ID, R> callback,
                                     long expire, TimeUnit tu){
        return getCache(prefix, id, rtp, callback, expire, tu, true);
    }

    public <R, ID> R getCache(String prefix, ID id, Class<R> rtp,
                              Function<ID, R> callback,
                              long expire, TimeUnit tu, boolean save){

        String key = prefix + id;

        String value = template.opsForValue().get(key);

        // 未命中
        if(value == null){
            try{
                R r = callback.apply(id);

                // 数据库没有，存空值解决缓存穿透
                if(r == null){
                    template.opsForValue().set(key, "", 30, TimeUnit.SECONDS);
                    return null;
                }

                if(save){
                    // 数据库命中，存入缓存，设置过期时间
                    this.setCache(key, r, expire, tu);
                }

                log.info("db hit, key:{}", key);

                return r;
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }

        // 命中但是为空
        if("".equals(value)){
            return null;
        }

        // 成功命中
        try{

            log.info("cache hit, key:{}", key);

            return JSON.readValue(value, rtp);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void setCache(String key, Object value, long expire, TimeUnit tu){

        try{
            String s = JSON.writeValueAsString(value);
            template.opsForValue().set(key, s, expire, tu);

        }catch(JsonProcessingException e) {
            e.printStackTrace();

        }
    }
}
