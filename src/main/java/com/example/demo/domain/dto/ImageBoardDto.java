package com.example.demo.domain.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data

public class ImageBoardDto {
    private Long id;
    private String seller;
    private String productname;
    private String category;
    private String brandname;
    private String price;
    private String itemdetals;
    private String amount;
    private String size;
    private MultipartFile[] files;
}
