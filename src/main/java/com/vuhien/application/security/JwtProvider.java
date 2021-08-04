package com.vuhien.application.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by VuHien96 on 02/08/2021 22:34
 */
@Component
public class JwtProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    //Tạo một key bí mật
    @Value("${jwt.secret}")
    private String secret;
    //Tạo thời hạn của token
    @Value("${jwt.duration}")
    private Integer duration;

    //Hàm tạo token
    public String createToken(Authentication authentication) {
        //Gán user hiện tại bằng với user của hệ thống
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();


        return Jwts.builder()
                .setSubject(customUserDetail.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + duration * 1000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    //Hàm lấy username từ token
    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    //Hàm kiểm tra token
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

}
