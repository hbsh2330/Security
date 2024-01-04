package com.example.demo.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemoDto {
    private int id;
    private String text;
}
