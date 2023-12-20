package com.example.demo.config.auth.logoutHandler;

import com.example.demo.config.auth.PrincipalDetails;
import com.example.demo.config.auth.jwt.JwtProperties;
import com.example.demo.config.auth.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;

public class CustomLogoutHandler implements LogoutHandler {

    private RestTemplate restTemplate;

    public CustomLogoutHandler(){
        restTemplate = new RestTemplate();
    }
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    PersistentTokenRepository persistentTokenRepository;

    // naver
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        System.out.println("[CustomLogoutHandler] logout()");

        //JWT
            // cookie 에서 JWT token을 가져옵니다.
           String token = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(JwtProperties.COOKIE_NAME)).findFirst()
                    .map(cookie -> cookie.getValue())
                    .orElse(null);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        //-------------------------------------------------
        //REMEMBERME USER DELETE
        //-------------------------------------------------
        persistentTokenRepository.removeUserTokens(authentication.getName());
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String provider = principalDetails.getUserDto().getProvider();
        System.out.println("[CustomLogoutHandler] provider()"+provider);

        if (provider!=null&&provider.equals("kakao")){
            //AccessToken추출
            String accessToken = principalDetails.getAccessToken();
            //Request URL
            String url = "https://kapi.kakao.com/v1/user/logout";
            //Request Header
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/x-www-form-urlencoded");
            headers.add("Authorization", "Bearer "+accessToken);


            //Header + Parameter 단위 생성
            HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(headers);

            //Restamplate에 HttpEntity 등록
            ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            System.out.println("[CustomLogoutHandler] logout() resp" + resp);
            System.out.println("[CustomLogoutHandler] logout() resp.getBody" + resp.getBody());
        }else if (provider!=null&&provider.equals("naver")){ //네이버 개발자 센터 5.3.1 로그아웃 기능 구현
//            AccessToken추출
            String accessToken = principalDetails.getAccessToken();
            String url ="https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id="+naverClientId+"+&client_secret="+naverClientSecret+"&access_token="+accessToken+"&service_provider=NAVER"; //네이버 개발자 센터 5.3.1
            restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        }else if (provider!=null&&provider.equals("google")){
            //AccessToken 추출
            String accessToken = principalDetails.getAccessToken();
            //URL
            String url = "https://accounts.google.com/o/oauth2/revoke?token=" + accessToken;
            //Rest Request
            ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

            System.out.println("[CustomLogoutHandler] logout() : " + resp);
        }


        HttpSession session = request.getSession(false);// false세션이 없으면 새로 안만듬 defalt 세션이 없으면 새로만듬
        if (session!=null)
            session.invalidate();
    }
}

//    String accessToken = principalDetails.getAccessToken();
//            //Request URL
//            String url = "https://nid.naver.com/oauth2.0/token";
//            //Request Header
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Content-Type", "application/x-www-form-urlencoded");
//            headers.add("Authorization", "Bearer "+accessToken);
//
//            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//            params.add("client_id", naverClientId);
//            params.add("client_secret", naverClientSecret);
//            params.add("access_token", accessToken);
//            params.add("grant_type", "delete");
//            params.add("grant_type", "delete");
//
//            //Header + Parameter 단위 생성
//            HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(headers, params);
//
//            //Restamplate에 HttpEntity 등록
//            ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//            System.out.println("[CustomLogoutHandler] logout() resp" + resp);
//            System.out.println("[CustomLogoutHandler] logout() resp.getBody" + resp.getBody());

