package com.example.demo.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank(message = "username을 입력하세요")
    private String username;
    @NotBlank(message = "password를 입력하세요")
    private String password;
    @NotBlank(message = "repassword를 입력하세요")
    private String repassowrd;
    @NotBlank(message = "휴대폰 번호를 입력하세요")
    private String phone;
    @NotBlank(message = "zipcode를 입력하세요")
    private String zipcode;
    @NotBlank(message = "기본주소를 입력하세요")
    private String addr1;
    private String addr2;
    private String role;

    //OAUTH2
    private String providerId;
    private String provider;
}
