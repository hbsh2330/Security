package com.example.demo.domain.service;

import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean memberJoin(UserDto dto) {
        //비지니스 유효성 체크

        //Dto -> Entity
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        user.setRole("ROLE_USER");

        //Db saved.
        userRepository.save(user);

        return userRepository.existsById(user.getUsername());
    }
}
