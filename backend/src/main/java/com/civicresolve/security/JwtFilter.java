package com.civicresolve.security;
import jakarta.servlet.*; import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component; import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException; import java.util.List;

@Component public class JwtFilter extends OncePerRequestFilter{
 private final JwtService jwt; public JwtFilter(JwtService j){jwt=j;}
 protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{
  String h=req.getHeader("Authorization");
  if(h!=null&&h.startsWith("Bearer ")){
   try{
    var c=jwt.parse(h.substring(7)); String email=c.getSubject(); String role=c.get("role",String.class);
    if(email!=null&&!email.isBlank()&&List.of("CITIZEN","ADMIN","AUTHORITY").contains(role)){
     var a=new UsernamePasswordAuthenticationToken(email,null,List.of(new SimpleGrantedAuthority("ROLE_"+role)));
     SecurityContextHolder.getContext().setAuthentication(a);
    }
   }catch(Exception ignored){ SecurityContextHolder.clearContext(); }
  }
  chain.doFilter(req,res);
 }
}
