package com.fanggu.stone;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@MapperScan("com.fanggu.stone.dao")
@EnableRedisHttpSession
public class StoneApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoneApplication.class, args);
    }

}
