package com.example.productservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class RequestValidationFilter extends OncePerRequestFilter {
    private static final String secretKey = "bfc80827f07c171a3e1f0661a1abfd52ab1fec954283772e19c1a673efa58e41";


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Cookie cookie = WebUtils.getCookie(request, "accessToken");

        if(cookie != null){
            try {
                String token = cookie.getValue();
                Claims claims = Jwts.parser()
                        .verifyWith(getSignKey()).build()
                        .parseSignedClaims(token).getPayload();
                String userId = claims.getSubject();
                List<String> authorities = (List<String>) claims.get("authorities");

                // Convert authority strings to GrantedAuthority objects
                List<GrantedAuthority> authorityList = authorities.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                Authentication auth = new UsernamePasswordAuthenticationToken(userId,null,
                        authorityList);
                SecurityContextHolder.getContext().setAuthentication(auth);


            }catch (Exception e) {
                throw new BadCredentialsException("Invalid Token received!");
            }
        }
        filterChain.doFilter(request, response);
    }

    private SecretKey getSignKey() {
        byte[] key = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(key);
    }

}