package kr.co.mdi.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/image/**").permitAll() // 정적 리소스 허용
                .requestMatchers("/", "/index", "/login", "/register").permitAll() // 공개 페이지
                .anyRequest().authenticated() // 나머지는 인증 필요
            )
            .formLogin(login -> login
                .loginPage("/login")          // 커스텀 로그인 페이지
                .defaultSuccessUrl("/", true) // 로그인 성공 시 인덱스로 이동
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")         // 로그아웃 URL
                .logoutSuccessUrl("/")        // 로그아웃 성공 시 인덱스로 이동
                .invalidateHttpSession(true)  // 세션 무효화
                .deleteCookies("JSESSIONID")  // 쿠키 삭제
                .permitAll()
            );
        return http.build();
    }
}
