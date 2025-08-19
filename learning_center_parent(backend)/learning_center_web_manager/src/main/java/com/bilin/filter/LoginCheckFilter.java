package com.bilin.filter;

import com.bilin.controller.exception.BusinessException;
import com.bilin.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
// @WebFilter("/*")
public class LoginCheckFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 0. Force type casting the ServletRequest object to a HttpServletRequest object
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        // 1. Get request url
        String uri = req.getRequestURI();
        log.info("uri: {}",uri);
        // 2. Determine whether the request is a login request. If so, let the request pass (pass to the next filter / continue the filter chain)
        if (uri.contains("login")) {
            filterChain.doFilter(servletRequest, servletResponse);
            // MUST end this request at this point (should NOT go on to next lines)
            return;
        }
        // 3. Get the token in the request header
        String token = req.getHeader("token");
        // 4. Determine if token exists. If not, return 401.
        if (token == null){
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.error("Token is null");
            // throw new BusinessException("You are not logged in. Please log in first.111");
            return;
        }

        // 5. Parse token. If parsing fails, return 401.
        try {
            Claims claims = JwtUtils.parseJwt(token);
            log.info("claims: {}", claims);
        } catch (Exception e) {
            resp.setStatus(401);
            log.error("Token parse failed: {}", e.getMessage());
            // throw new BusinessException("You are not logged in. Please log in first.");
            return;
        }

        // 6. Let the request pass
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
