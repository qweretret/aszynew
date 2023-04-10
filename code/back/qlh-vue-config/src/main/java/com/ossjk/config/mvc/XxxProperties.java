package com.ossjk.config.mvc;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@Slf4j
@ConfigurationProperties(prefix = "xxx")
@Data
public class XxxProperties {

		private String miniapp;
		private String miniapp2;

}
