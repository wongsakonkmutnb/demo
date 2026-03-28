package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // ปิด CSRF ชั่วคราวเพื่อให้ฟอร์มทำงานง่ายขึ้น
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/css/**", "/js/**").permitAll() // หน้าแรกทุกคนเข้าได้
                .requestMatchers("/admin/**").hasRole("ADMIN") // หน้าแอดมิน ต้องเป็น ADMIN เท่านั้น
                .anyRequest().authenticated() // หน้าอื่นๆ (ยืม, คืน, ประวัติ) ต้อง Login ก่อน
            )
            .formLogin(form -> form
                .defaultSuccessUrl("/", true) // ล็อกอินสำเร็จให้ไปหน้าแรก
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            );

        return http.build();
    }

    // กำหนดให้ใช้รหัสผ่านแบบข้อความธรรมดา (เพื่อความง่ายตอนเริ่มต้นโปรเจกต์)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); 
    }
}