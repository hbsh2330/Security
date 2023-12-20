package com.example.demo.config.auth;

import com.example.demo.config.auth.provider.GoogleUserInfo;
import com.example.demo.config.auth.provider.KakaoUserInfo;
import com.example.demo.config.auth.provider.NaverUserInfo;
import com.example.demo.config.auth.provider.OAuth2UserInfo;
import com.example.demo.domain.dto.UserDto;

import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class PrincipalDetailsOAuth2Service extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("[PrincipalDetailsOAuth2Service] loadUser() userRequest :" + userRequest);
        System.out.println("[PrincipalDetailsOAuth2Service] loadUser() getClientRegistration :" + userRequest.getClientRegistration());
        System.out.println("[PrincipalDetailsOAuth2Service] loadUser() getAccessToken :" + userRequest.getAccessToken());
        System.out.println("[PrincipalDetailsOAuth2Service] loadUser() getAdditionalParameters :" + userRequest.getAdditionalParameters());
        System.out.println("[PrincipalDetailsOAuth2Service] loadUser() .getAccessToken().getTokenValue() :" + userRequest.getAccessToken().getTokenValue());
        System.out.println("[PrincipalDetailsOAuth2Service] loadUser() .getAccessToken().getTokenType() :" + userRequest.getAccessToken().getTokenType());
        System.out.println("[PrincipalDetailsOAuth2Service] loadUser() .getAccessToken().getScopes() :" + userRequest.getAccessToken().getScopes());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("[PrincipalDetailsOAuth2Service] loadUser() oAuth2User :" + oAuth2User);
        System.out.println("[PrincipalDetailsOAuth2Service] loadUser() oAuth2User.getAuthorities :" + oAuth2User.getAuthorities());
        System.out.println("[PrincipalDetailsOAuth2Service] loadUser() getAttributes() : " + oAuth2User.getAttributes());

        //OAuth Server Provider 구별
        String provider = userRequest.getClientRegistration().getRegistrationId();
        System.out.println("[PrincipalDetailsOAuth2Service] provider : " + provider);

        OAuth2UserInfo oAuth2UserInfo = null;
        if (provider != null && provider.equals("kakao")){
            String id = oAuth2User.getAttributes().get("id").toString();
            Map<String, Object> resp = (Map<String,Object>)oAuth2User.getAttributes().get("properties");
            KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(id, resp);
            System.out.println("[PrincipalDetailsOAuth2Service] loadUser() kakaoUserInfo" + kakaoUserInfo);
            oAuth2UserInfo = kakaoUserInfo;
        }else if (provider != null && provider.equals("naver")){
            Map<String, Object> resp = (Map<String, Object>)oAuth2User.getAttributes().get("response");
            String id = (String)resp.get("id");
            NaverUserInfo naverUserInfo = new NaverUserInfo(id, resp);
            System.out.println("[PrincipalDetailsOAuth2Service] loadUser() naverUserInfo" + naverUserInfo);
            oAuth2UserInfo = naverUserInfo;
        }else if (provider != null && provider.equals("google")){
            String id = (String ) oAuth2User.getAttributes().get("sub");
            GoogleUserInfo googleUserInfo = new GoogleUserInfo(id, oAuth2User.getAttributes());
            oAuth2UserInfo = googleUserInfo;
        }

        System.out.println("[PrincipalDetailsOAuth2Service] loadUser() oAuth2UserInfo : " + oAuth2UserInfo);
        //db 조회
        String username =oAuth2UserInfo.getProvider()+"_"+oAuth2UserInfo.getProviderId();
        String password = passwordEncoder.encode("1234");

        Optional<User> optional = userRepository.findById(username); // 계정을 찾아서
        UserDto dto = null;
        if (optional.isEmpty()){
            User user = User.builder()
                    .username(username)
                    .password(password)
                    .role("ROLE_USER")
                    .provider(oAuth2UserInfo.getProvider())
                    .providerId(oAuth2UserInfo.getProviderId())
                    .build();
            userRepository.save(user);
            dto = User.entityToDto(user);
            System.out.println("[PrincipalDetailsOAuth2Service] loadUser() " +oAuth2UserInfo.getProvider()+ "최초 로그인!");
        } else {
            User user = optional.get();
            dto = User.entityToDto(user);
            System.out.println("[PrincipalDetailsOAuth2Service] loadUser() " +oAuth2UserInfo.getProvider()+ "기존계정 로그인!");

        }

        //userRepository.findById(username);
        PrincipalDetails principalDetails = new PrincipalDetails();
        principalDetails.setAccessToken(userRequest.getAccessToken().getTokenValue());
        principalDetails.setAttributes(oAuth2UserInfo.getAttributes());
        principalDetails.setUserDto(dto);
        return principalDetails;
    }
}
