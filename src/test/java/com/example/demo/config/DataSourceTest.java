package com.example.demo.config;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class DataSourceTest {


    @Autowired
    HikariDataSource dataSource;

    @Test
    public void t1(){
        System.out.println(dataSource);
    }

}