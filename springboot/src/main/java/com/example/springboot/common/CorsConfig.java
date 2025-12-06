package com.example.springboot.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


// 该类是设置跨域的问题
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        // 1. 创建 CORS 配置源
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // 2. 创建 CORS 配置
        CorsConfiguration config = new CorsConfiguration();

        // 3. 配置跨域设置
        config.setAllowCredentials(true); // 允许携带凭证（如 cookies）
        config.addAllowedOriginPattern("*"); // 使用 allowedOriginPattern 替代 allowedOrigin
        config.addAllowedHeader("*"); // 允许所有请求头
        config.addAllowedMethod("*"); // 允许所有请求方法
        config.setMaxAge(3600L); // 预检请求的缓存时间（秒）

        // 4. 对所有接口应用 CORS 配置
        source.registerCorsConfiguration("/**", config);

        // 5. 返回 CorsFilter
        return new CorsFilter(source);
    }
}