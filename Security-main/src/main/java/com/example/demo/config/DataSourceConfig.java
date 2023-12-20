package com.example.demo.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

    @Bean // 빈 이름을 지정안하면 함수명으로 빈이 설정이됨
    public HikariDataSource dataSource(){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername("root");
        dataSource.setPassword("1234");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/testdb"); // appilcation.properties

        return dataSource;
    }
}
