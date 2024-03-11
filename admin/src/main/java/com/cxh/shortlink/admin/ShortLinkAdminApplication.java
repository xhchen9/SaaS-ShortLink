package com.cxh.shortlink.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cxh.shortlink.admin.dao.mapper")
public class ShortLinkAdminApplication {
    public static void main(String[] args){
        SpringApplication.run(ShortLinkAdminApplication.class, args);
    }
}
