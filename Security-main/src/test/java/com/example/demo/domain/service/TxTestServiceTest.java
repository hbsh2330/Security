package com.example.demo.domain.service;

import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TxTestServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void TxTest1() {
    }

}