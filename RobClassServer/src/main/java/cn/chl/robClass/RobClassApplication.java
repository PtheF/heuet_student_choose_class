package cn.chl.robClass;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.chl.robClass.mapper")
public class RobClassApplication {

    public static void main(String[] args) {
        SpringApplication.run(RobClassApplication.class, args);
    }
}
