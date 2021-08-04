package com.vuhien.application.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by VuHien96 on 02/08/2021 23:02
 */
@Component
public class JwtFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private static String HEADER = "Authorization";
    private static String BEARER = "Bearer";

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private CustomUserDetailService customUserDetailService;

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HEADER);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER)) {
            return authHeader.substring(7, authHeader.length());
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //Lấy token từ request
            String token = getToken(request);
            //Kiểm tra token có hợp lệ
            if (token != null && jwtProvider.validateJwtToken(token)) {
                //Lấy username trong token
                String username = jwtProvider.getUsernameFromToken(token);
                UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                //Set user
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception ex) {
            logger.error("Cannot set user authentication: {}", ex.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
