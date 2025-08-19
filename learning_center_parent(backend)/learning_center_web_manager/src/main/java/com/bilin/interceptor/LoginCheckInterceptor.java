package com.bilin.interceptor;

import com.bilin.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. Get request url
        String uri = request.getRequestURI();
        log.info("uri: {}",uri);
        // 2. Get the token in the request header
        String token = request.getHeader("token");
        // 3. Determine if token exists. If not, return 401.
        if (token == null) {
            log.warn("Token is null");
            response.setStatus(HttpStatus.UNAUTHORIZED.value()); // HttpStatus.UNAUTHORIZED.value() is 401
            return false;
        }
        // 4. Parse token. If parsing fails, return 401.
        try{
            Claims claims = JwtUtils.parseJwt(token);
            log.info("claims: {}",claims);
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            // Do not let the request pass
            return false;
        }
        // 5. Let the request pass
        return true;
    }
}
