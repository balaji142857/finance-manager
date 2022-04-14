package com.kb.fm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

import com.kb.fm.config.ImportConfig;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(ImportConfig.class)
public class FinManApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinManApplication.class, args);
	}

}
