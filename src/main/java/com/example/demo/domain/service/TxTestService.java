package com.example.demo.domain.service;

import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class TxTestService {
    @Autowired
    private UserRepository userRepository;

//    @Transactional(rollbackFor = Exception.class) // (rollbackFor = Exception.class)을 안넣어주면 기본으로 RuntimeException이 들어간다.
    public void TxTest1() {
        log.info("[TxTestService] TXTest()");

    }
}
