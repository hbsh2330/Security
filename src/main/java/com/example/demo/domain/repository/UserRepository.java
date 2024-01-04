package com.example.demo.domain.repository;


import com.example.demo.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    User findByNicknameAndPhone(@Param("nickname") String nickname, @Param("phone") String phone);


}
