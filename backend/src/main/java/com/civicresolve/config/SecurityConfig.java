package com.civicresolve.config;

import com.civicresolve.security.JwtFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import java.util.*;

@Configuration
public class SecurityConfig {
    @Value("${app.cors.origin:http://localhost:5173}") private String corsOrigin;
    @Bean PasswordEncoder encoder(){return new BCryptPasswordEncoder();}
    @Bean SecurityFilterChain filter(HttpSecurity h,JwtFilter jf)throws Exception{
        return h.csrf(c->c.disable()).cors(c->c.configurationSource(r->{var x=new CorsConfiguration();x.setAllowedOrigins(Arrays.stream(corsOrigin.split(",")).map(String::trim).filter(s->!s.isEmpty()).toList());x.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));x.setAllowedHeaders(List.of("*"));x.setAllowCredentials(false);return x;}))
            .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(e->e.authenticationEntryPoint((q,res,x)->res.sendError(401,"Authentication required")).accessDeniedHandler((q,res,x)->res.sendError(403,"Access denied")))
            .authorizeHttpRequests(a->a.requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                .requestMatchers("/api/auth/register","/api/auth/login","/api/categories","/api/public/**","/api/complaints/track/**").permitAll()
                .requestMatchers("/api/auth/me").hasAnyRole("CITIZEN","ADMIN","AUTHORITY")
                .requestMatchers("/api/account/**").hasRole("CITIZEN")
                .requestMatchers(HttpMethod.GET,"/api/complaints/mine").hasRole("CITIZEN")
                .requestMatchers(HttpMethod.POST,"/api/complaints").hasRole("CITIZEN")
                .requestMatchers("/api/admin/**").hasAnyRole("ADMIN","AUTHORITY")
                .anyRequest().authenticated())
            .addFilterBefore(jf,UsernamePasswordAuthenticationFilter.class).build();
    }
}
