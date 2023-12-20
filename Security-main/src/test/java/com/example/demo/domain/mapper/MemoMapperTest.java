package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.MemoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemoMapperTest {

    @Autowired
    private MemoMapper mapper;

    @Test
    public void t1(){
        MemoDto dto = new MemoDto(1, "memo1");
        mapper.insert(dto);
        System.out.println(dto);
    }

    @Test
    public void t2(){
        MemoDto dto = mapper.FindByIdXML(2);
        System.out.println(dto);
    }
}