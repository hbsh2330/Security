package com.example.demo.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MybatisConfigTest {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Test
    public void t1(){
        System.out.println(sqlSessionFactory);
        System.out.println(sqlSessionFactory.openSession());
        System.out.println(sqlSessionFactory.openSession().getConnection());
    }

}