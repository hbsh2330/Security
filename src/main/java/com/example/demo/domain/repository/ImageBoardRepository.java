package com.example.demo.domain.repository;

import com.example.demo.domain.entity.ImageBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageBoardRepository extends JpaRepository<ImageBoard,Long> {

}
