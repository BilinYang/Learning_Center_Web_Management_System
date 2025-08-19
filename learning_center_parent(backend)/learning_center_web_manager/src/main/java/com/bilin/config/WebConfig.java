package com.bilin.config;

import com.bilin.interceptor.LoginCheckInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration // '@Configuration' shows that the class is a configuration class, but the function of '@Configuration' is actually completely the same as '@Component'
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("Adding interceptor");
        // registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**").excludePathPatterns("/login"); // '/**' means to intercept ALL paths
        // the line below is equivalent to registry.addInterceptor(loginCheckInterceptor).excludePathPatterns("/login") since the default is to intercept everything
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login");
    }

}
