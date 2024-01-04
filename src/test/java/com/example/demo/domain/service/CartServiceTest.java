package com.example.demo.domain.service;

import com.example.demo.domain.entity.Cart;
import com.example.demo.domain.entity.ImageBoard;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.CartRepository;
import com.example.demo.domain.repository.ImageBoardRepository;
import com.example.demo.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class CartServiceTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageBoardRepository imageBoardRepository;

    @Test
    public void t1(){
        User user = userRepository.findById("ghkdqh853@gmail.com").get();
        ImageBoard imageBoard = imageBoardRepository.findById(4L).get();

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setImageBoard(imageBoard);
        cart.setRegdate(LocalDateTime.now());
        cartRepository.save(cart);
    }

}