package com.example.demo.config.auth;

import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrincipalDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // 넣어진 데이터를 가져오는 녀석
        Optional<User> userOptional = userRepository.findById(username);
        if (userOptional.isEmpty())
            return null;
        //Entity -> Dto
        UserDto dto = new UserDto();
        dto.setUsername(userOptional.get().getUsername());
        dto.setPassword(userOptional.get().getPassword());
        dto.setRole(userOptional.get().getRole());
        return new PrincipalDetails(dto);
    }
}
