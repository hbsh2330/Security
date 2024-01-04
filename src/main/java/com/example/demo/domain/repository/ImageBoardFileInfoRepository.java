package com.example.demo.domain.repository;

import com.example.demo.domain.entity.ImageBoardFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageBoardFileInfoRepository extends JpaRepository<ImageBoardFileInfo, Long> {
    List<ImageBoardFileInfo> findByImageBoardId(Long imagBoardId);
}
