package com.example.demo.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank(message = "username을 입력하세요")
    private String username;
    @NotBlank(message = "password를 입력하세요")
    @Pattern(regexp = "/^(?=.*[a-zA-Z])(?=.*[0-9]).{8,25}$/", message = "영문 숫자 조합 8자리 이상문자를 입력하세요")
    private String password;
    @NotBlank(message = "password를 한번더 입력하세요")
    private String repassword;
    @NotBlank(message = "휴대폰 번호를 입력하세요")
    private String phone;
    @NotBlank(message = "zipcode를 입력하세요")
    private String zipcode;
    @NotBlank(message = "기본주소를 입력하세요")
    private String addr1;
    private String addr2;
    private String role;

    //날짜
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime date;

    //OAUTH2
    private String providerId;
    private String provider;
}
