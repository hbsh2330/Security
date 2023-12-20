package com.example.demo.config;

import com.example.demo.config.auth.exceptionHandler.CustomaccessDeniedHandler;
import com.example.demo.config.auth.exceptionHandler.CustomauthenticationEntryPoint;
import com.example.demo.config.auth.jwt.JwtAuthorizationFilter;
import com.example.demo.config.auth.jwt.JwtProperties;
import com.example.demo.config.auth.jwt.JwtTokenProvider;
import com.example.demo.config.auth.loginHandler.CustomAuthenticationFailureHandler;
import com.example.demo.config.auth.loginHandler.CustomLoginSuccessHandler;
import com.example.demo.config.auth.loginHandler.Oauth2JwtLoginSuccessHandler;
import com.example.demo.config.auth.logoutHandler.CustomLogoutHandler;
import com.example.demo.config.auth.logoutHandler.CustomLogoutSuccessHandler;
import com.example.demo.domain.repository.UserRepository;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.Persistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class SecurityConfig {

    @Autowired
    private HikariDataSource dataSource;

    // JWT 주입
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        //CSRF 비활성화
//        http.csrf(
//                config -> config.disable()
//        );
        http.csrf(new Customizer<CsrfConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CsrfConfigurer<HttpSecurity> config) {
                config.disable();
            }
        });

        //요청 URL별 접근 제한
        http.authorizeHttpRequests(//요청 uri와 로그인한 계정의 권한이 로그인한 계정과 일치하는지? // 권한체크
                authorize->{
                    authorize.requestMatchers("/templates/**").permitAll(); //
                    authorize.requestMatchers("/js", "/css", "/images").permitAll(); //js를 시큐리티 코드가 권한 없이 모두 허용
                    authorize.requestMatchers("/", "login").permitAll(); // "/"와 "login"으로 들어온 녀석은 권한체크없이 프리패스
                    authorize.requestMatchers("/join").hasRole("ANONYMOUS"); // 지정을 안해줬을 때 기본값으로 ANONYMOUS을 스프링 시큐리티가 부여해준다.
                    authorize.requestMatchers("/user").hasRole("USER"); // user 에 들어가기 위해서 user로그인을 해야함 + hasRole("USER")을 가지고 있어야함
                    authorize.requestMatchers("/member").hasRole("MEMBER"); // member에 들어가기 위해서 member로그인을 해야함
                    authorize.requestMatchers("/admin").hasRole("ADMIN");
                    authorize.anyRequest().authenticated(); // 그외의 나머지 모두는 접근불가 접근할려면 요청을 받아야함
                }
        );
//        //로그인
//        http.formLogin(login->{
//            login.permitAll();
//            login.loginPage("/login");
//        });

        http.formLogin(new Customizer<FormLoginConfigurer<HttpSecurity>>() {
            @Override
            public void customize(FormLoginConfigurer<HttpSecurity> login) {
                login.permitAll();
                login.loginPage("/login");
                login.successHandler(customLoginSuccessHandler());
                login.failureHandler(new CustomAuthenticationFailureHandler());
            }
        });

        //로그아웃
        http.logout(
                (logout)->{
                    logout.permitAll();
                    logout.logoutUrl("/logout");
                    logout.addLogoutHandler(customLogoutHandler());
                    logout.logoutSuccessHandler(customLogoutSuccessHandler());
                    logout.deleteCookies("JSESSIONID", JwtProperties.COOKIE_NAME); // 쿠키 제거
                    logout.invalidateHttpSession(true); //세션 제거
                }
        );

        //예외처리
        http.exceptionHandling(
                ex-> {
                    ex.authenticationEntryPoint(new CustomauthenticationEntryPoint());
                    ex.accessDeniedHandler(new CustomaccessDeniedHandler());
                }
        );
        //RememberMe 서버가 꺼져도 자동으로 로그인 기억 로그인 기억하기 생각하면 쉬움
        http.rememberMe(
                rm->{
                    rm.key("rememberMeKey");
                    rm.rememberMeParameter("remember-me"); //로그인 파라미터로 받는 이름 //view의 네임과 일치해야함
                    rm.alwaysRemember(false); // false면 remember-me 가들어오면 true라고 설정하면 로그인할때마다 로그인상태를 저장함
                    rm.tokenValiditySeconds(3600); //토큰의 유효기간 설정 60*60 1시간
                    rm.tokenRepository(tokenRepository()); // 서버에 db에 특정 테이블을 형성하고 생성된 테이블에 사용자의 로그인 여부를 저장
                }
        );
        http.oauth2Login(oauth2 -> {
            oauth2.loginPage("/login");
            oauth2.successHandler(oauth2JwtLoginSuccessHandler());
        }
        );

        //SESSION INVALIDATE
        http.sessionManagement(
                httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
        );

        //JWT 주입
        http.addFilterBefore(
                new JwtAuthorizationFilter(userRepository, jwtTokenProvider),
                BasicAuthenticationFilter.class
        );

        return http.build();
    }


    //REMEMBER ME 처리 BEAN
    @Bean
    public PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
//        repo.setCreateTableOnStartup(true); // true로 하면 persistents 로그인 테이블을 생성
        repo.setDataSource(dataSource);
        return repo;
    }

    //CUSTOMLOGOUTSUCCESS BEAN

    @Bean
    public CustomLogoutHandler customLogoutHandler() {
        return new CustomLogoutHandler();
    }

    @Bean
    public CustomLogoutSuccessHandler customLogoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }
    // BCryptPasswordEncoder Bean 등록 - 패스워드 검증에 사용
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomLoginSuccessHandler customLoginSuccessHandler(){
        return new CustomLoginSuccessHandler();
    }

    @Bean
    public Oauth2JwtLoginSuccessHandler oauth2JwtLoginSuccessHandler(){
        return new Oauth2JwtLoginSuccessHandler();
    }
}
