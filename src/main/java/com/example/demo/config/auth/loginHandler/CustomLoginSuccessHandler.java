package com.example.demo.config.auth.loginHandler;

import com.example.demo.config.auth.PrincipalDetails;
import com.example.demo.config.auth.jwt.JwtProperties;
import com.example.demo.config.auth.jwt.JwtTokenProvider;
import com.example.demo.config.auth.jwt.TokenInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;


public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler { // 인증 성공 핸들러

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("[CustomLoginSuccessHandler] onAuthenticationSuccess");

        //JWT 추가
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
//        String token = JwtUtils.createToken(principalDetails);

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        // 쿠키 생성
        Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, tokenInfo.getAccessToken());
        cookie.setMaxAge(JwtProperties.EXPIRATION_TIME); // 쿠키의 만료시간 설정
        cookie.setPath("/");
        response.addCookie(cookie);





        Collection<? extends GrantedAuthority> collection = authentication.getAuthorities();
        collection.forEach((role) -> {
            System.out.println("[CustomLoginSuccessHandler] onAuthenticationSuccess role" + role);
            String role_str = role.getAuthority();

            try {
                if (role_str.equals("ROLE_USER")) {
                    response.sendRedirect("/user");
                } else if (role_str.equals("ROLE_USER")) {
                    response.sendRedirect("/member");

                } else if (role_str.equals("ROLE_USER")) {
                    response.sendRedirect("/admin");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
