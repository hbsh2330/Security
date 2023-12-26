package com.example.demo.controller;

import com.example.demo.domain.dto.CertificationDto;
import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.service.UserService;
import com.example.demo.properties.EmailAuthPropertoes;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

import static java.rmi.server.LogStream.log;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;


    @GetMapping("/myinfo")
    public void user(Authentication authentication, Model model){ //의존성을 받아오는거
        UserController.log.info("GET /user/myinfo...Authentication" + authentication);
        UserController.log.info("username: " + authentication.getPrincipal());
        UserController.log.info("authorities :" + authentication.getAuthorities());
        UserController.log.info("details :" + authentication.getDetails());
        UserController.log.info("credentials :" + authentication.getCredentials());
        model.addAttribute("authentication", authentication);
    }

    @GetMapping("/join")
    public void join(){
        UserController.log.info("GET /join");

    }
    @PostMapping("/join")
    public String join_post(@Valid UserDto dto, BindingResult bindingResult, Model model){
        UserController.log.info("POST /join...dto" + dto);
        //파라미터 받기

        //입력값 검증(유효성 체크)
//        System.out.println(bindingResult);
        if (bindingResult.hasFieldErrors()){
            for (FieldError error : bindingResult.getFieldErrors()){
                log.info(error.getField() + " : " + error.getDefaultMessage());
                model.addAttribute(error.getField(), error.getDefaultMessage());
            }
            return "user/join";
        }
        //서비스 실행
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        boolean isJoin = userService.memberJoin(dto);
        //View로 속성전달
        if (isJoin)
            return "redirect:login?msg=MemberJoin Success!";
        else
            return "forward:join";
        // 예외처리
    }
    @GetMapping("/certification")
    public String certification(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserController.log.info("GET /user/certification ");
        if (request.getCookies() != null) {
            boolean isExisted = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("importAuth")).findFirst().isEmpty(); //쿠키들 중에 쿠키의 이름이 importAuth 첫번째 것의 찾아서 비어있는지 확인
            if (!isExisted) // 텅 비어있지 않으면
                response.sendRedirect("/user/join");
        }
        return "user/certification";
    }
    @GetMapping("/findId")
    public void findId(){
        UserController.log.info("GET /user/findId ");
    }
    @GetMapping("/findPw")
    public void findPw(){
        UserController.log.info("GET /user/findPw ");
    }

    @PostMapping(value = "/certification" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONObject> certification_post(@RequestBody CertificationDto params, HttpServletResponse response) throws IOException {
        UserController.log.info("Post /user/certification " + params);

        Cookie authCookie = new Cookie("importAuth", "true");
        authCookie.setMaxAge(60 * 60 * 5); // 쿠키의 지속 시간 5시간
        authCookie.setPath("/");
        response.addCookie(authCookie);

        JSONObject obj = new JSONObject();
        obj.put("success", true); // 요청 받은 값을 리턴
        return new ResponseEntity<JSONObject>(obj, HttpStatus.OK);
    }
    @GetMapping("/sendmail/{email}")
    @ResponseBody
    public ResponseEntity<JSONObject> sendmailFunc(@PathVariable("email") String email){

        //넣을 값 지정
        String code = EmailAuthPropertoes.planText;

        passwordEncoder.encode(code);
        //메일 메시지 만들기
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[WEB-Server]임시패스워드 발급");
        message.setText(passwordEncoder.encode(code));

        javaMailSender.send(message);
        UserController.log.info("GET /user/sendmail.." + email);

        return new ResponseEntity(new JSONObject().put("success", true) , HttpStatus.OK);
    }

    @GetMapping("/emailConfirm")
    @ResponseBody
    public JSONObject emailConfirmFunc(String emailCode){
        UserController.log.info("GET /user/emailConfirm .....code" + emailCode);

        boolean isAuth = passwordEncoder.matches(EmailAuthPropertoes.planText, emailCode);
        JSONObject object = new JSONObject();

        if (isAuth){
            object.put("success", true);
            object.put("message", "이메일 인증을 성공했습니다.");
            return object;
        }

        object.put("success", false);
        object.put("message", "이메일 인증에 실패했습니다.");
        return object;

    }

}
