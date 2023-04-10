package com.ossjk.config.mvc;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@Slf4j
@ConfigurationProperties(prefix = "wx.miniapp")
@Data
public class WxminipappProperties {
              ///    wx.miniapp.configs
	private List<Config> configs;

	@Data
	public static class Config {

		private String appid;
		private String secret;
	}

}
