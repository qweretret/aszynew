package com.ossjk.config.mvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Slf4j
@Configuration
@EnableConfigurationProperties(WxminipappProperties.class)
public class MvcConfig3 implements WebMvcConfigurer {


}
