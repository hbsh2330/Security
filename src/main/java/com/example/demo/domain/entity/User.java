package com.example.demo.domain.entity;

import com.example.demo.domain.dto.UserDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder // 생성자에 매개변수를 여러개 만들 필요없이(오버로딩)없이 빌더패턴으로 사용하여 생성자에 매개변수를 넣을 수 있다.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user")
public class User {
    @Id
    private String username;
    private String password;
    private String role;

    //OAUTH2
    private String providerId;
    private String provider;

    public static UserDto entityToDto(User user) { // 엔티티를 Dto로 바꿔주는 함수
        UserDto dto = UserDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .role((user.getRole()))
                .provider(user.getProvider())
                .providerId(user.getProviderId())
                .build();

        return dto;
    }
}
