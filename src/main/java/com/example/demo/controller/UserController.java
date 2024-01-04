package com.example.demo.controller;

import com.example.demo.config.auth.PrincipalDetails;
import com.example.demo.config.auth.jwt.JwtProperties;
import com.example.demo.config.auth.jwt.JwtTokenProvider;
import com.example.demo.config.auth.jwt.TokenInfo;
import com.example.demo.domain.dto.CertificationDto;
import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.service.UserService;
import com.example.demo.properties.EmailAuthProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
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


    @InitBinder
    public void dataBinder(WebDataBinder dataBinder){
        log.info("databinder obj : " + dataBinder);
        //dataBinder.registerCustomEditor(String.class,new PhoneNumberEditor());
        dataBinder.registerCustomEditor(String.class,"phone" ,new PhoneNumberEditor());
    }



    @GetMapping(value = "/myinfo" )
    public void user(Authentication authentication , Model model){
        UserController.log.info("GET /user/myinfo...Authentication : " + authentication);
        UserController.log.info("username : " + authentication.getName());
        UserController.log.info("principal : " + authentication.getPrincipal());
        UserController.log.info("authorities : " + authentication.getAuthorities());
        UserController.log.info("details :  " +authentication.getDetails());
        UserController.log.info("credentials : " + authentication.getCredentials());

        model.addAttribute("authentication",authentication);

    }



    @GetMapping("/join")
    public void join(){
        UserController.log.info("GET /join");
    }
    @PostMapping("/join")
    public String join_post(@Valid UserDto dto, BindingResult bindingResult,Model model,HttpServletRequest request) throws Exception {
        UserController.log.info("POST /join...dto " + dto);
        //파라미터 받기

        //입력값 검증(유효성체크)
        //System.out.println(bindingResult);
        if(bindingResult.hasFieldErrors()){
            for(FieldError error :bindingResult.getFieldErrors()){
                log.info(error.getField() +" : " + error.getDefaultMessage());
                model.addAttribute(error.getField(),error.getDefaultMessage());
            }
            return "user/join";
        }

        //서비스 실행

        boolean isJoin =  userService.memberJoin(dto,model,request);
        //View로 속성등등 전달
        if(isJoin)
            return "redirect:login?msg=MemberJoin Success!";
        else
            return "user/join";
        //+a 예외처리

    }

    @GetMapping("/certification")
    public String certification(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserController.log.info("GET /user/certification");

        if(request.getCookies() !=null) {
            boolean isExisted = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("importAuth")).findFirst()
                    .isEmpty();
            if (!isExisted) {
                response.sendRedirect("/user/join");
                return null;
            }
        }
        return "user/certification";
    }

    @ExceptionHandler(Exception.class)
    public void AllExceptionFunc(Exception e){
        System.out.println(e);
    }

    @GetMapping("/findId")
    public void findId(){
        log.info("GET /user/findId");}


    @PostMapping(value = "/findId" )
    public @ResponseBody String findId_post(@RequestParam String nickname ,@RequestParam String phone) throws Exception {
        log.info("POST /user/findId "+nickname + " | " + phone);
        //유효성 체크(생략)
        //서비스 실행(nickname,phone 가 일치하는 username찾기.
        String username= userService.findUsername(nickname,phone);

        return username;

    }
    @PostMapping(value = "/findPw" )
    public @ResponseBody ResponseEntity<JSONObject> findPw_post(@RequestParam String username ,@RequestParam String nickname ,@RequestParam String phone) throws Exception {
        log.info("POST /user/findPw "+nickname + " | " + phone);
        //유효성 체크(생략)

        //서비스 실행(nickname,phone 가 일치하는 username찾기.
        boolean isSended = userService.findPassword(username,nickname,phone);

        JSONObject obj = new JSONObject();
        if(isSended)
            return new ResponseEntity(obj,HttpStatus.OK);

        return new ResponseEntity(obj,HttpStatus.INTERNAL_SERVER_ERROR);

    }
    @GetMapping("/findPw")
    public void findPw(){

        UserController.log.info("GET /user/findPw");

    }



    //---------------------
    @PostMapping(value = "/certification",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONObject>  certification_post(@RequestBody CertificationDto params, HttpServletResponse response) throws IOException {
        UserController.log.info("POST /user/certification.." + params);
        //쿠키로 본인인증 완료값을 전달!
        Cookie authCookie = new Cookie("importAuth","true");
        authCookie.setMaxAge(60*30); //30분동안 유지
        authCookie.setPath("/");
        response.addCookie(authCookie);
        JSONObject obj = new JSONObject();
        obj.put("success",true);

        return  new ResponseEntity<JSONObject>(obj, HttpStatus.OK);

    }
    @GetMapping("/sendMail/{email}")
    @ResponseBody
    public ResponseEntity<JSONObject> sendmailFunc(@PathVariable("email") String email){
        UserController.log.info("GET /user/sendMail.." + email);
        //넣을 값 지정
        String code = EmailAuthProperties.planText;

        passwordEncoder.encode(code);
        //메일 메시지 만들기
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[EMAIL AUTHENTICATION] CODE ");
        message.setText(passwordEncoder.encode(code));

        javaMailSender.send(message);


        return new ResponseEntity(new JSONObject().put("success", true) , HttpStatus.OK);
    }

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/emailConfirm")
    public @ResponseBody JSONObject emailConfirmFunc(String emailCode, String username , HttpServletResponse response ){
        UserController.log.info("GET /user/emailConfirm... code : " + emailCode + " username " + username);

        boolean isAuth= passwordEncoder.matches(EmailAuthProperties.planText,emailCode);
        JSONObject obj = new JSONObject();

        if(isAuth) {
            obj.put("success",true);
            obj.put("message","이메일 인증을 성공하셨습니다.");
            
            //JWT Token 에 이메일 인증완료 코드 JWT토큰으로 저장&전달
//            PrincipalDetails principalDetails =  (PrincipalDetails)authentication.getPrincipal();

            TokenInfo tokenInfo = jwtTokenProvider.generateToken("EmailAuth",username,true);
            // 쿠키 생성
            Cookie cookie = new Cookie("EmailAuth", tokenInfo.getAccessToken());
            cookie.setMaxAge(JwtProperties.EXPIRATION_TIME); // 쿠키의 만료시간 설정
            cookie.setPath("/");
            response.addCookie(cookie);


            return obj;
        }

        obj.put("success",false);
        obj.put("message","이메일 인증을 실패했습니다.");
        return obj;

    }
}



class PhoneNumberEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        System.out.println("PhoneNumberEditor's setAsText()..text : " + getValue());
        return (String)getValue();
    }
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        System.out.println("PhoneNumberEditor's setAsText()..text : " + text);
        String formattedText =  text.replaceAll("-","");
        setValue(formattedText);
    }
}

