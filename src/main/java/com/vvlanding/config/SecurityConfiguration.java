package com.vvlanding.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfiguration implements WebMvcConfigurer {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/opt/ImagesTomcat/**").addResourceLocations("file:/opt/ImagesTomcat/");
        registry.addResourceHandler("/product_images/**").addResourceLocations("file:product_images/");
        registry.addResourceHandler("/shop_logo/**").addResourceLocations("file:shop_logo/");
        registry.addResourceHandler("/member-avatars/**").addResourceLocations("file:member-avatars/");
        registry.addResourceHandler("/theme-image/**").addResourceLocations("file:theme-image/");
        registry.addResourceHandler("/theme-image64/**").addResourceLocations("file:theme-image64/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*");
    }


}