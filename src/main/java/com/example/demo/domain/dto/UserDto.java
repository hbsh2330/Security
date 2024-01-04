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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    @NotBlank(message="username을 입력하세요")
    private String username;
    @NotBlank(message="password 입력하세요")
    @Pattern(regexp="^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$",message="'숫자','문자','특수문자' 무조건 1개 이상, 비밀번호 '최소 8자에서 최대 16자'까지 허용됩니다" )
    private String password;

    //'숫자', '문자', '특수문자' 무조건 1개 이상, 비밀번호 '최소 8자에서 최대 16자'까지 허용
    //(특수문자는 정의된 특수문자만 사용 가능)

    @NotBlank(message="nickname은 반드시 입력해야합니다.")
    private String nickname;

    @NotBlank(message="repassword 입력하세요")
    @Pattern(regexp="^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$",message="'숫자','문자','특수문자' 무조건 1개 이상, 비밀번호 '최소 8자에서 최대 16자'까지 허용됩니다" )
    private String repassword;

    @NotBlank(message="연락처를 입력하세요")
    private String phone;
    
    @NotBlank(message="zipcode를 입력하세요")
    private String zipcode;

    @NotBlank(message="기본주소를 입력하세요")
    private String addr1;
    private String addr2;

    private String role;

    //OAUTH2
    private String provider;
    private String providerId;

//    //날짜
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    private LocalDateTime rdate;
}
