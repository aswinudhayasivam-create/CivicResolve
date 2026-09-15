package com.civicresolve.config;
import com.civicresolve.security.JwtFilter; import org.springframework.context.annotation.*; import org.springframework.security.config.annotation.web.builders.HttpSecurity; import org.springframework.security.config.http.SessionCreationPolicy; import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.security.web.SecurityFilterChain; import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; import org.springframework.web.cors.*; import java.util.*;
@Configuration public class SecurityConfig{
 @Bean PasswordEncoder encoder(){return new BCryptPasswordEncoder();}
 @Bean SecurityFilterChain filter(HttpSecurity h,JwtFilter jf)throws Exception{
  return h.csrf(c->c.disable()).cors(c->c.configurationSource(r->{var x=new CorsConfiguration();x.setAllowedOrigins(List.of("http://localhost:5173"));x.setAllowedMethods(List.of("*"));x.setAllowedHeaders(List.of("*"));return x;}))
   .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
   .authorizeHttpRequests(a->a.requestMatchers("/api/auth/**","/api/categories","/api/complaints/track/**").permitAll().requestMatchers("/api/admin/**").hasAnyRole("ADMIN","AUTHORITY").anyRequest().authenticated())
   .addFilterBefore(jf,UsernamePasswordAuthenticationFilter.class).build();
 }
}