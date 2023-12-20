package com.example.demo.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement

public class TxConfig {
    @Autowired
    private HikariDataSource dataSource;

    //JPA TransactionManager Settings
    //JPA용
    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    // DataSource용 Tx
    //MyBatis용
//    @Bean
//    public DataSourceTransactionManager TransactionManager() {
//        return new DataSourceTransactionManager(dataSource);
//    }

}