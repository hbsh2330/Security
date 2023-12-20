package com.example.demo.config.auth.loginHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        System.out.println("[CustomAuthenticationFailureHandler] onAuthenticationFailure :" + exception);
        response.sendRedirect("/login?error=" + URLEncoder.encode(exception.getMessage(), "UTF-8"));//파라미터에 error 메세지를 뛰우기 위한코드
    }
}
