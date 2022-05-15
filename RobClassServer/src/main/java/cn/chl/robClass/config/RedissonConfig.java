package cn.chl.robClass.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson 配置类
 */
@Configuration
public class RedissonConfig {


    /**
     * 注入RedissonClient对象，创建方法就是通过config创建
     *
     * 我们这里配置Redis集群，固定写法如下，记住就行了
     */
    @Bean
    public RedissonClient getRedissonConfig(){
        Config config = new Config();

        // 单Redis服务就修改这行代码，把Redis换成你的Redis，记得把下面的注释了
//        config.useSingleServer().setAddress("redis://192.168.73.11:6379");

        // 如果是Redis集群，修改下面的所有Redis配置为你集群的配置
        config.useClusterServers().addNodeAddress(
                "redis://192.168.73.11:6379",
                "redis://192.168.73.11:6380",
                "redis://192.168.73.12:6379",
                "redis://192.168.73.12:6380",
                "redis://192.168.73.13:6379",
                "redis://192.168.73.13:6380"
        );

        return Redisson.create(config);
    }
}
